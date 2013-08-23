package info.tregmine;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ContextHandler;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;

import info.tregmine.api.TregminePlayer;
import info.tregmine.web.*;

public class WebServer implements Runnable
{
    public static class ChatMessage
    {
        private TregminePlayer sender;
        private String channel;
        private String text;

        public ChatMessage(TregminePlayer sender, String channel, String text)
        {
            this.sender = sender;
            this.channel = channel;
            this.text = text;
        }

        public TregminePlayer getSender() { return sender; }
        public String getChannel() { return channel; }
        public String getText() { return text; }
    }

    private static class DummyLogger implements org.eclipse.jetty.util.log.Logger
    {
        @Override
        public String getName() { return "DummyLogger"; }

        @Override
        public void warn(String msg, Object... args) {}

        @Override
        public void warn(Throwable thrown) {}

        @Override
        public void warn(String msg, Throwable thrown) {}

        @Override
        public void info(String msg, Object... args) {}

        @Override
        public void info(Throwable thrown) {}

        @Override
        public void info(String msg, Throwable thrown) {}

        @Override
        public boolean isDebugEnabled() {return false; }

        @Override
        public void setDebugEnabled(boolean enabled) {}

        @Override
        public void debug(String msg, Object... args) {}

        @Override
        public void debug(Throwable thrown) {}

        @Override
        public void debug(String msg, Throwable thrown) {}

        @Override
        public org.eclipse.jetty.util.log.Logger getLogger(String name)
        {
            return this;
        }

        @Override
        public void ignore(Throwable ignored) {}
    }

    private Server webServer;
    private WebHandler webHandler;
    private ChatHandler chatHandler;

    private Map<String, TregminePlayer> authTokens;
    private BlockingQueue<ChatMessage> messageQueue;

    private Thread thread = null;
    private boolean running = true;

    public WebServer(Tregmine tregmine)
    {
        thread = new Thread(this);

        authTokens = new HashMap<>();
        messageQueue = new LinkedBlockingQueue<>();

        PluginManager pluginMgm = tregmine.getServer().getPluginManager();

        try {
            FileConfiguration config = tregmine.getConfig();
            String apiKey = config.getString("api.signing-key", "");
            int apiPort = config.getInt("api.port", 9192);

            Tregmine.LOGGER.info("API Key: " + apiKey);
            Tregmine.LOGGER.info("API Port: " + apiPort);

            HandlerList handlers = new HandlerList();

            // Start chat handler and bind to /chat
            chatHandler = new ChatHandler(tregmine, pluginMgm);
            pluginMgm.registerEvents(chatHandler, tregmine);

            ContextHandler context = new ContextHandler();
            context.setContextPath("/chat");
            context.setHandler(chatHandler);
            handlers.addHandler(context);

            // Start web handler and bind to rest of paths
            webHandler = new WebHandler(tregmine, pluginMgm, apiKey);
            pluginMgm.registerEvents(webHandler, tregmine);

            webHandler.addAction(new AuthAction.Factory());
            webHandler.addAction(new PlayerKickAction.Factory());
            webHandler.addAction(new PlayerListAction.Factory());
            webHandler.addAction(new QueryLogAction.Factory());
            webHandler.addAction(new VersionAction.Factory());

            handlers.addHandler(webHandler);

            // Disable jetty logging
            org.eclipse.jetty.util.log.Log.setLog(new DummyLogger());

            // Start server at apiPort
            webServer = new Server(apiPort);
            webServer.setHandler(handlers);
        }
        catch (Exception e) {
            Tregmine.LOGGER.log(Level.WARNING, "Failed to start web server!", e);
        }
    }

    public Map<String, TregminePlayer> getAuthTokens()
    {
        return authTokens;
    }

    public ChatHandler getChatHandler()
    {
        return chatHandler;
    }

    public WebHandler getWebHandler()
    {
        return webHandler;
    }

    public void sendChatMessage(ChatMessage msg)
    {
        messageQueue.offer(msg);
    }

    public void start()
    {
        thread.start();
    }

    @Override
    public void run()
    {
        try {
            webServer.start();
            Tregmine.LOGGER.info("Web server started.");
        } catch (Exception e) {
            Tregmine.LOGGER.log(Level.WARNING, "Failed to start web server!", e);
        }

        while (running) {
            try {
                ChatMessage msg = messageQueue.take();
                chatHandler.broadcastToWeb(msg.getSender(),
                                           msg.getChannel(),
                                           msg.getText());
            }
            catch (InterruptedException e) {
            }
        }

        try {
            webServer.stop();
            webServer.join();
        } catch (Exception e) {
            Tregmine.LOGGER.log(Level.WARNING, "Failed to stop web server!", e);
        }
    }

    public void stop()
    {
        running = false;

        try {
            thread.interrupt();
            thread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

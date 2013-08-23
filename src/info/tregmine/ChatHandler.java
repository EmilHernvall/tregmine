package info.tregmine;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.Queue;
import java.util.Iterator;
import java.security.NoSuchAlgorithmException;
import java.security.InvalidKeyException;
import java.util.logging.Level;
import java.util.concurrent.Future;
import java.util.concurrent.ExecutionException;

import javax.crypto.spec.SecretKeySpec;
import javax.crypto.Mac;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.json.JSONException;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.websocket.server.WebSocketHandler;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;
import org.eclipse.jetty.websocket.api.RemoteEndpoint;
import org.eclipse.jetty.websocket.api.UpgradeRequest;
import org.eclipse.jetty.websocket.api.UpgradeResponse;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;
import org.eclipse.jetty.io.EofException;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import info.tregmine.api.TregminePlayer;

public class ChatHandler extends WebSocketHandler
    implements WebSocketCreator, Listener
{
    public static class WebChatEvent extends Event
    {
        private static final HandlerList handlers = new HandlerList();

        private String authToken;
        private String channel;
        private String text;

        public WebChatEvent(String authToken, String channel, String text)
        {
            this.authToken = authToken;
            this.channel = channel;
            this.text = text;
        }

        public String getAuthToken() { return authToken; }
        public String getChannel() { return channel; }
        public String getText() { return text; }

        @Override
        public HandlerList getHandlers()
        {
            return handlers;
        }

        public static HandlerList getHandlerList()
        {
            return handlers;
        }
    }

    public static class ChatSocket extends WebSocketAdapter
    {
        private ChatHandler handler;
        private PluginManager pluginMgr;
        private Session session;

        public ChatSocket(ChatHandler handler, PluginManager pluginMgr)
        {
            this.handler = handler;
            this.pluginMgr = pluginMgr;
        }

        @Override
        public Session getSession()
        {
            return session;
        }

        @Override
        public RemoteEndpoint getRemote()
        {
            return session.getRemote();
        }

        @Override
        public void onWebSocketConnect(Session sess)
        {
            this.session = sess;

            //sess.setIdleTimeout(Long.MAX_VALUE);
            handler.addSession(this);
        }

        @Override
        public void onWebSocketText(String message)
        {
            Session session = getSession();

            try {
                JSONObject obj = new JSONObject(message);
                String authToken = obj.getString("authToken");
                if (authToken == null) {
                    return;
                }
                String channel = obj.getString("channel");
                if (channel == null) {
                    return;
                }
                String text = obj.getString("text");
                if (text == null) {
                    return;
                }

                WebChatEvent event = new WebChatEvent(authToken, channel, text);
                pluginMgr.callEvent(event);
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onWebSocketClose(int statusCode, String reason)
        {
            handler.removeSession(this);
        }

        @Override
        public void onWebSocketError(Throwable e)
        {
            Tregmine.LOGGER.log(Level.WARNING, "Socket error", e);
            handler.removeSession(this);
        }
    }

    private Tregmine tregmine;
    private PluginManager pluginMgr;

    private Set<ChatSocket> sockets;

    public ChatHandler(Tregmine tregmine, PluginManager pluginMgr)
    {
        this.tregmine = tregmine;
        this.pluginMgr = pluginMgr;

        sockets = new HashSet<ChatSocket>();
    }

    private void addSession(ChatSocket session)
    {
        Tregmine.LOGGER.info("Web connected");
        sockets.add(session);
    }

    private void removeSession(ChatSocket session)
    {
        Tregmine.LOGGER.info("Web disconnected");
        sockets.remove(session);
    }

    private void disconnect(ChatSocket socket)
    {
        try {
            socket.getSession().disconnect();
        } catch (IOException e) { }
    }

    /**
     * Do not call directly! Calls are synchronized by
     * WebServer.sendChatMessage.
     **/
    public void broadcastToWeb(TregminePlayer sender, String channel, String text)
    {
        try {
            JSONObject obj = new JSONObject();
            obj.put("sender", sender.getRealName());
            obj.put("rank", sender.getRank().toString());
            obj.put("channel", channel);
            obj.put("text", text);

            String message = obj.toString();
            Iterator<ChatSocket> it = sockets.iterator();
            while (it.hasNext()) {
                ChatSocket socket = it.next();
                Session session = socket.getSession();
                if (!session.isOpen()) {
                    it.remove();
                    disconnect(socket);
                    continue;
                }

                session.getRemote().sendStringByFuture(message);
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Object createWebSocket(UpgradeRequest req, UpgradeResponse resp)
    {
        return new ChatSocket(this, pluginMgr);
    }

    @Override
    public void configure(WebSocketServletFactory factory)
    {
        factory.setCreator(this);
    }

    @EventHandler
    public void onWebChat(ChatHandler.WebChatEvent event)
    {
        WebServer server = tregmine.getWebServer();
        Map<String, TregminePlayer> authTokens = server.getAuthTokens();
        String authToken = event.getAuthToken();
        if (!authTokens.containsKey(authToken)) {
            Tregmine.LOGGER.info("Auth token " + authToken + " not found.");
            return;
        }
        TregminePlayer sender = authTokens.get(authToken);

        String channel = event.getChannel();
        String text = event.getText();

        for (TregminePlayer to : tregmine.getOnlinePlayers()) {
            if (to.getChatState() == TregminePlayer.ChatState.SETUP) {
                continue;
            }
            if (!channel.equalsIgnoreCase(to.getChatChannel())) {
                continue;
            }

            if ("global".equalsIgnoreCase(channel)) {
                to.sendMessage("(" + sender.getChatName()
                        + ChatColor.WHITE + ") " + ChatColor.WHITE + text);
            } else {
                to.sendMessage(channel + " (" + sender.getChatName()
                        + ChatColor.WHITE + ") " + ChatColor.WHITE + text);
            }
        }

        server.sendChatMessage(new WebServer.ChatMessage(sender, channel, text));

        Tregmine.LOGGER.info(channel + " <" + sender.getRealName() + "> " + text);
    }
}

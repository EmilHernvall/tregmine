package info.tregmine;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Level;

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
import info.tregmine.database.DAOException;
import info.tregmine.database.IContext;
import info.tregmine.database.ILogDAO;
import info.tregmine.database.IPlayerDAO;

public class ChatHandler extends WebSocketHandler
    implements WebSocketCreator, Listener
{
    public static class WebChatEvent extends Event
    {
        private static final HandlerList handlers = new HandlerList();

        private TregminePlayer player;
        private String channel;
        private String text;

        public WebChatEvent(TregminePlayer player, String channel, String text)
        {
            this.player = player;
            this.channel = channel;
            this.text = text;
        }

        public TregminePlayer getSender() { return player; }
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
        private Session session;
        private TregminePlayer player;

        public ChatSocket(ChatHandler handler)
        {
            this.handler = handler;
            this.player = null;
        }

        public void setPlayer(TregminePlayer v)
        {
            this.player = v;
        }

        public TregminePlayer getPlayer()
        {
            return player;
        }

        public void sendSystemMessage(String message)
        {
            try {
                JSONObject obj = new JSONObject();
                obj.put("action", "sysmsg");
                obj.put("text", message);

                getRemote().sendStringByFuture(obj.toString());
            }
            catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }

        public void sendMessage(JSONObject msg)
        {
            getRemote().sendStringByFuture(msg.toString());
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
            handler.dispatch(this, message);
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
    private Map<Integer, Date> kickedPlayers;

    public ChatHandler(Tregmine tregmine, PluginManager pluginMgr)
    {
        this.tregmine = tregmine;
        this.pluginMgr = pluginMgr;

        sockets = new HashSet<ChatSocket>();
        kickedPlayers = new HashMap<Integer, Date>();
    }

    public boolean isOnline(TregminePlayer player)
    {
        for (ChatSocket socket : sockets) {
            TregminePlayer current = socket.getPlayer();
            if (current == null) {
                continue;
            }
            if (current.getId() == player.getId()) {
                return true;
            }
        }

        return false;
    }

    public List<TregminePlayer> listPlayers()
    {
        List<TregminePlayer> players = new ArrayList<TregminePlayer>();
        for (ChatSocket socket : sockets) {
            TregminePlayer current = socket.getPlayer();
            if (current == null) {
                continue;
            }

            players.add(current);
        }

        return players;
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
     * WebServer.executeChatAction.
     **/
    public void kickPlayer(TregminePlayer sender,
                           TregminePlayer victim,
                           String message)
    {
        for (ChatSocket socket : sockets) {
            TregminePlayer current = socket.getPlayer();
            if (current == null) {
                continue;
            }
            if (current.getId() == victim.getId()) {
                socket.sendSystemMessage("You were kicked by " +
                        sender.getRealName() + ": " + message);

                disconnect(socket);
                removeSession(socket);
                kickedPlayers.put(victim.getId(), new Date());
            }
        }
    }

    /**
     * Do not call directly! Calls are synchronized by
     * WebServer.executeChatAction.
     **/
    public void broadcastToWeb(TregminePlayer sender, String channel, String text)
    {
        try {
            JSONObject obj = new JSONObject();
            obj.put("action", "msg");
            obj.put("sender", ChatColor.stripColor(sender.getChatName()));
            obj.put("rank", sender.getRank().toString());
            obj.put("channel", channel);
            obj.put("text", text);

            Iterator<ChatSocket> it = sockets.iterator();
            while (it.hasNext()) {
                ChatSocket socket = it.next();
                Session session = socket.getSession();
                if (!session.isOpen()) {
                    it.remove();
                    disconnect(socket);
                    continue;
                }

                socket.sendMessage(obj);
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void dispatch(ChatSocket socket, String message)
    {
        try {
            JSONObject obj = new JSONObject(message);
            if (!obj.has("action")) {
                return;
            }

            String action = obj.getString("action");

            if ("msg".equals(action)) {
                if (socket.getPlayer() == null) {
                    Tregmine.LOGGER.info("Player not set.");
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

                WebChatEvent event = new WebChatEvent(socket.getPlayer(),
                                                      channel,
                                                      text);
                pluginMgr.callEvent(event);
            }
            else if ("auth".equals(action)) {
                String authToken = obj.getString("authToken");
                if (authToken == null) {
                    return;
                }

                WebServer server = tregmine.getWebServer();
                Map<String, TregminePlayer> authTokens = server.getAuthTokens();
                if (!authTokens.containsKey(authToken)) {
                    Tregmine.LOGGER.info("Auth token " + authToken + " not found.");
                    socket.sendSystemMessage("Auth token not found.");
                    return;
                }

                TregminePlayer sender = authTokens.get(authToken);

                Date kickTime = kickedPlayers.get(sender.getId());
                if (kickTime != null) {
                    long time = (new Date().getTime() - kickTime.getTime())/1000L;
                    if (time < 600l) {
                        socket.sendSystemMessage(
                                "You are not allowed to reconnect yet.");
                        Tregmine.LOGGER.info(sender.getRealName() + " attempted to " +
                            "reconnect after being kicked before the allowed duration.");

                        disconnect(socket);
                        removeSession(socket);
                        return;
                    }
                }

                socket.setPlayer(sender);

                Tregmine.LOGGER.info(sender.getRealName() + " authed successfully.");
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Object createWebSocket(UpgradeRequest req, UpgradeResponse resp)
    {
        return new ChatSocket(this);
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
        TregminePlayer sender = event.getSender();

        String channel = event.getChannel();

        try (IContext ctx = tregmine.createContext()) {
            IPlayerDAO playerDAO = ctx.getPlayerDAO();
            for (TregminePlayer to : tregmine.getOnlinePlayers()) {
                if (to.getChatState() == TregminePlayer.ChatState.SETUP) {
                    continue;
                }
                if (!channel.equalsIgnoreCase(to.getChatChannel())) {
                    continue;
                }

                if (!sender.getRank().canNotBeIgnored()) {
                    if (playerDAO.doesIgnore(to, sender)) {
                        continue;
                    }
                }

                String text = event.getText();
                for (TregminePlayer online : tregmine.getOnlinePlayers()) {
                    if (text.contains(online.getName()) &&
                        !online.hasFlag(TregminePlayer.Flags.INVISIBLE)) {

                        text = text.replaceAll(online.getName(),
                                online.getChatName() + ChatColor.WHITE);
                    }
                }

                if ("global".equalsIgnoreCase(channel)) {
                    to.sendMessage("(" + sender.getChatName() +
                            ChatColor.WHITE + ") " + ChatColor.WHITE + text);
                } else {
                    to.sendMessage(channel + " (" + sender.getChatName() +
                            ChatColor.WHITE + ") " + ChatColor.WHITE + text);
                }
            }
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }

        server.executeChatAction(
                new WebServer.ChatMessage(sender, channel, event.getText()));

        Tregmine.LOGGER.info(channel +
                " (" + sender.getRealName() + ") " + event.getText());

        try (IContext ctx = tregmine.createContext()) {
            ILogDAO logDAO = ctx.getLogDAO();
            logDAO.insertChatMessage(sender, channel, event.getText());
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }
    }
}

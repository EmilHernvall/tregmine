package info.tregmine.bungee.chat;

import java.io.File;

public class ConfigFile {
    private static String configpath = File.separator+"plugins"+File.separator+"BungeeChat"+File.separator+"settings.yml";
    public Config c = new Config(configpath);
    public String host = c.getString("IRC Host", "irc.esper.net");
    public int port = c.getInt("Port", 6667);
    public String nick = c.getString("IRC Bot Nickname", "TregBot");
    public String pass = c.getString("IRC Bot Password", "Ask me for it");
    public String channel = c.getString("Channel", "#tregmine");
    public boolean serverChat = c.getBoolean("Cross Server Chat", true);
	public boolean ircChat = c.getBoolean("IRC Chat", true);
	public boolean ircShowConnectedServer = c.getBoolean("Show server that player is connected to in IRC", true);
	public boolean gameShowConnectedServer = c.getBoolean("Show server that player is connected to in game", true);
}
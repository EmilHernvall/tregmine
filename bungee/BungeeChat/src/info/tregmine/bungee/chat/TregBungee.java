package info.tregmine.bungee.chat;

import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;

import java.util.logging.Level;

public class TregBungee extends Plugin
implements Listener {

	private static TregBungee instance;
	public static ServerInfo hub;
	public final ConfigFile config = new ConfigFile();
	Configuration configuration;
	private PircBotX bot;

	@SuppressWarnings("unchecked")
	public void onEnable() {
		if (!config.pass.isEmpty()) {
			configuration = new Configuration.Builder()
			.setName(config.nick)
			.setLogin(config.nick)
			.setAutoNickChange(true)
			.setCapEnabled(true)
			.addListener(new IRCListener(this))
			.setServerHostname(config.host)
			.addAutoJoinChannel(config.channel)
			.setNickservPassword(config.pass)
			.setServerPort(config.port)
			.buildConfiguration();
		} else {
			configuration = new Configuration.Builder()
			.setName(config.nick)
			.setLogin(config.nick)
			.setAutoNickChange(true)
			.setCapEnabled(true)
			.addListener(new IRCListener(this))
			.setServerHostname(config.host)
			.addAutoJoinChannel(config.channel)
			.setServerPort(config.port)
			.buildConfiguration();
		}
		bot = new PircBotX(configuration);
		instance = this;

		this.getProxy().getScheduler().runAsync(this, new Runnable() {
			public void run() {
				try {
					bot.startBot();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		getProxy().getLogger().log(Level.INFO, "Chat: Enabled!");

		BungeeListener listener = new BungeeListener(this);
	}

	public void onDisable() {
		if (instance.getConfig().ircChat) {
			getProxy().getLogger().log(Level.INFO, "Chat: Disabled!");
		}
	}

	public static TregBungee getPlugin() {
		return instance;
	}

	public PircBotX getBot() {
		return bot;
	}

	public ConfigFile getConfig() {
		return config;
	}
}
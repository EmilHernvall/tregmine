package info.tregmine.api;

import org.bukkit.Sound;

/**
 * Represents a notification to send to a player
 * <p>
 * This is called at different times.
 * 
 * <p>
 * Ex: When a player receives a message, they receive Notification.MESSAGE
 * @author Robert Catron
 *
 */
public enum Notification {
	NONE(null),//Place holder for just a message
	BLESS(Sound.BLOCK_CHEST_CLOSE), //to keep it related to chests ect..
	COMMAND_FAIL(Sound.BLOCK_LEVER_CLICK),
	MESSAGE(Sound.ENTITY_PLAYER_LEVELUP),
	RANK_UP(Sound.BLOCK_PISTON_CONTRACT),
	RARE_DROP(Sound.ENTITY_ITEM_PICKUP),
	SUMMON(Sound.BLOCK_PORTAL_TRAVEL),
	WARP(Sound.ENTITY_ENDERMEN_TELEPORT),
	MAIL(Sound.BLOCK_NOTE_PLING),
	UNBLESS(Sound.BLOCK_CHEST_OPEN);
	
	private final Sound sound;
	
	private Notification(Sound sound)
	{
		this.sound = sound;
	}
	
	/**
	 * @return The sound of the notification
	 */
	public Sound getSound() {
		
		return sound;
	}
}

package info.tregmine.spleef;

import info.tregmine.spleef.ArenaManager.Team;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

public class PlayerData {

	private String player;
	private Team team;
	private ItemStack[] contents, armorContents;
	private Location loc;
	
	public PlayerData(String player, Team team, ItemStack[] contents, ItemStack[] armorContents, Location loc) {
		this.player = player;
		this.team = team;
		this.contents = contents;
		this.armorContents = armorContents;
		this.loc = loc;
	}
	
	public String getPlayerName() {
		return player;
	}
	
	public Team getTeam() {
		return team;
	}
	
	public ItemStack[] getContents() {
		return contents;
	}
	
	public ItemStack[] getArmorContents() {
		return armorContents;
	}
	
	public Location getLocation() {
		return loc;
	}
}
package info.tregmine.christmas.listeners;

import info.tregmine.christmas.ChristmasMain;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class TraderListener implements Listener {

	private ChristmasMain plugin;

	public TraderListener(ChristmasMain instance)
	{
		this.plugin = instance;
	}

	String merchantGood = ChatColor.DARK_GRAY + "<" + ChatColor.DARK_PURPLE + "Merchant" + ChatColor.DARK_GRAY + "> " + ChatColor.GRAY;
	String merchantBad = ChatColor.DARK_GRAY + "<" + ChatColor.DARK_PURPLE + "Merchant" + ChatColor.DARK_GRAY + "> " + ChatColor.RED;
	String bankerGood = ChatColor.DARK_GRAY + "<" + ChatColor.GOLD + "Banker" + ChatColor.DARK_GRAY + "> " + ChatColor.GRAY;
	String bankerBad = ChatColor.DARK_GRAY + "<" + ChatColor.GOLD + "Banker" + ChatColor.DARK_GRAY + "> "+ ChatColor.RED;

	public void addBossSpawn(Inventory inv){
		ItemStack itemstack = new ItemStack( Material.MONSTER_EGG, 1);
		ItemMeta meta = itemstack.getItemMeta();
		meta.setDisplayName("§bGrinch Spawn Egg");
		itemstack.setItemMeta(meta);

		inv.addItem(itemstack);
	}


	@EventHandler
	public void onOpenBank(PlayerInteractEntityEvent e) {

		if (e.getPlayer().getLocation().getWorld().getName() != "Christmas"){
			return;
		}
		if (e.getRightClicked().getType() != EntityType.SKELETON) {
			return;
		}
		Player p = e.getPlayer();

		Inventory inventory = Bukkit.createInventory(null, 9, "§4Christmas Banker");

		p.openInventory(inventory);
	}

	@EventHandler
	public void onOpenMerchant(PlayerInteractEntityEvent e) {
		int forSale = 0;

		if (e.getPlayer().getLocation().getWorld().getName() != "Christmas"){
			return;
		}
		if (e.getRightClicked().getType() != EntityType.ZOMBIE) {
			return;
		}
		Player p = e.getPlayer();

		Inventory inventory = Bukkit.createInventory(null, 9, "§4Christmas Merchant");
		if(plugin.getConfig().getInt("player." + p.getName() + ".tokens") >= plugin.getConfig().getInt("merchant" + ".grinchEggCost")){
			addBossSpawn(inventory);
		}
		ItemStack[] contents = inventory.getContents();
		for (ItemStack stack : contents) {
			if (stack == null){
				continue;
			}		
			if (stack.getItemMeta().getDisplayName().contains("Christmas Essence")) {
				p.getWorld().dropItemNaturally(p.getLocation(), stack);
				p.sendMessage(merchantBad + " Sorry, I aren't looking to buy Christmas Essence");
				continue;
			}
			forSale++;
		}
		if (forSale == 0){
			p.sendMessage(merchantBad + " I dont have anything to sell you at the moment");
		}else{
			p.openInventory(inventory);
		}
	}

	@EventHandler
	public void onCloseBankerInv(InventoryCloseEvent e)
	{
		Player p = (Player) e.getPlayer();
		Inventory inv = e.getInventory();

		if(e.getInventory().getName() != "§4Christmas Banker"){
			return;
		}

		int tokens = plugin.getConfig().getInt("player." + p.getName() + ".tokens");
		int added = 0;

		ItemStack[] contents = inv.getContents();
		for (ItemStack stack : contents) {
			if (stack == null){
				continue;
			}
			if (stack.getItemMeta().getDisplayName() == null){
				p.getWorld().dropItemNaturally(p.getLocation(), stack);
				p.sendMessage(bankerBad + " Sorry, I aren't accepting " + stack.getType().toString().toLowerCase());
				continue;
			}

			if (stack.getItemMeta().getDisplayName() == "§bGrinch Spawn Egg"){
				continue;
			}

			if (!stack.getItemMeta().getDisplayName().contains("Christmas Essence")) {
				p.getWorld().dropItemNaturally(p.getLocation(), stack);
				p.sendMessage(bankerBad + " Sorry, I aren't looking to buy " + stack.getType().toString().toLowerCase());
				continue;
			}
			added = (added + stack.getAmount());
		}

		int total = tokens + added;

		plugin.getConfig().set("player." + p.getName() + ".tokens", total);
		plugin.saveConfig();

		p.sendMessage(bankerGood + " I have added " + added + " tokens to your bank");
		p.sendMessage(bankerGood + " You now have a total of " + plugin.getConfig().getInt("player." + p.getName() + ".tokens") + " tokens!");
		
		if(plugin.getConfig().getInt("player." + p.getName() + ".tokens") > plugin.getConfig().getInt("merchant" + ".grinchEggCost")){
			p.sendMessage(ChatColor.AQUA + "You have enough tokens to buy a grinch spawn egg, speak to a merchant if you would like to purchase one.");
		}
	}

	@EventHandler
	public void onCloseMerchantInv(InventoryCloseEvent e)
	{
		Player p = (Player) e.getPlayer();
		Inventory inv = e.getInventory();

		if(e.getInventory().getName() != "§4Christmas Merchant"){
			return;
		}

		int tokens = plugin.getConfig().getInt("player." + p.getName() + ".tokens");
		int taken = 0;
		int bossEgg = 0;

		ItemStack[] contents = inv.getContents();
		for (ItemStack stack : contents) {
			if (stack == null){
				continue;
			}
			if (stack.getItemMeta().getDisplayName() == null){
				p.getWorld().dropItemNaturally(p.getLocation(), stack);
				p.sendMessage(merchantBad + " Sorry, I aren't looking to buy " + stack.getType().toString().toLowerCase());
				continue;
			}

			if (stack.getItemMeta().getDisplayName() == "§bGrinch Spawn Egg"){
				bossEgg++;
				continue;
			}

			if (stack != null) {
				p.getWorld().dropItemNaturally(p.getLocation(), stack);
				p.sendMessage(merchantBad + " Sorry, I aren't looking to buy " + stack.getType().toString().toLowerCase());
				continue;
			}
		}

		if (bossEgg == 0){
			taken = plugin.getConfig().getInt("merchant" + ".grinchEggCost");
		}

		int total = tokens - taken;

		plugin.getConfig().set("player." + p.getName() + ".tokens", total);
		plugin.saveConfig();

		p.sendMessage(merchantGood + " I have withdrawn " + taken + " tokens from your bank");
		p.sendMessage(merchantGood + " You now have a total of " + plugin.getConfig().getInt("player." + p.getName() + ".tokens") + " tokens!");
	}
}
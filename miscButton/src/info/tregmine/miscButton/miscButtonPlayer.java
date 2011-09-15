package info.tregmine.miscButton;




//import org.bukkit.ChatColor;
//import org.bukkit.entity.Player;
//import org.bukkit.event.block.Action;
//import org.bukkit.event.player.PlayerInteractEvent;
//import org.bukkit.Material;
import info.tregmine.currency.Wallet;

//import org.bukkit.Achievement;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
//import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
//import org.bukkit.block.Sign;
//import org.bukkit.entity.CreatureType;
import org.bukkit.entity.Player;
//import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.inventory.ItemStack;

public class miscButtonPlayer extends PlayerListener {
	private final miscButton plugin;
	private boolean state = true;
	public miscButtonPlayer(miscButton instance) {
		this.plugin = instance;
		plugin.getServer();
	}

	public void onPlayerInteract(PlayerInteractEvent event) {

		int button;
		try {
			button = info.tregmine.api.math.Checksum.block(event.getClickedBlock());
		} catch (Exception e) {
			button = 0;
		}

/*
		//Donate buttons 
		Block findSign;
		if (event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			findSign = event.getClickedBlock().getWorld().getBlockAt(event.getClickedBlock().getLocation().getBlockX(), event.getClickedBlock().getLocation().getBlockY()+1, event.getClickedBlock().getLocation().getBlockZ());

			if (findSign.getState() instanceof Sign ) {
				Sign sign = (Sign) findSign.getState();
				if (sign.getLine(0).toUpperCase().contains("DONATE")) {
					String toName = sign.getLine(3).trim();
					String fromName = event.getPlayer().getName();
					int amount;
					try {
						amount = Integer.parseInt(sign.getLine(1).trim());
					} catch (NumberFormatException e) {
						amount = 0;
						e.printStackTrace();
					} catch (IndexOutOfBoundsException e) {
						amount = 0;
						e.printStackTrace();
					}
					
					Wallet fromW = new Wallet(fromName);
					Wallet toW = new Wallet(toName);

					if(!this.plugin.time.containsKey(fromName)) {
						this.plugin.time.put(fromName, 0L);
					}
					
					
					long newTime = System.currentTimeMillis() - this.plugin.time.get(fromName);
					
					
					if(newTime < 3000) {
						event.getPlayer().sendMessage("Please waite a while to place a nother donation!");
						return;						
					}

//					event.getPlayer().sendMessage("\"" + toName + "\"");

					if (!fromW.exist()) {
						event.getPlayer().sendMessage("FROM Sorry there is no wallet to put it in!");
						return;
					}

					if (!toW.exist()) {
						event.getPlayer().sendMessage("Sorry there is no wallet to put it in!");
						return;
					}
					
					this.plugin.time.put(fromName, System.currentTimeMillis());
					if(fromW.take(amount)) {
						event.getPlayer().sendMessage(ChatColor.YELLOW + "You gave " + toName + " " + amount + " Tregs" );
						this.plugin.log.info(amount + ":DONATIONBUTTON "+ fromName + " => " + toName);
						Player tp = this.plugin.getServer().getPlayer(toName);
						if (tp != null) {
							tp.sendMessage(ChatColor.YELLOW +"You reviced a donaton of " + amount + " Tregs from " + fromName);
						}
						
						} else {
						event.getPlayer().sendMessage(ChatColor.RED + "Don't give more then you have!");
						this.plugin.log.info(amount + ":FAILD_DONATIONBUTTON_NOTREGS "+ fromName +  "=> " + toName);
					}
				}
			}
		}
*/

		// Gatway button at spawn
		if (button == 642446803) {
			event.getPlayer().teleport(this.plugin.getServer().getWorld("ChildsPlay").getSpawnLocation());
		}
		
		//Spleefarena button
		if (button == 1467819296) {
			Block sb1 = event.getClickedBlock().getWorld().getBlockAt(-1, 91, 1105);
			Block sb2 = event.getClickedBlock().getWorld().getBlockAt( 33, 91, 1133);

			Block ab1 = event.getClickedBlock().getWorld().getBlockAt(-1, 85, 1133);
			Block ab2 = event.getClickedBlock().getWorld().getBlockAt(33, 126, 1105);

			Block lb1 = event.getClickedBlock().getWorld().getBlockAt(-1, 85, 1133);
			Block lb2 = event.getClickedBlock().getWorld().getBlockAt(33, 88, 1105);


			fill(ab1,ab2, Material.AIR);
			fill(sb1,sb2, Material.SNOW_BLOCK);
			fill(lb1,lb2, Material.LAVA);
		}

		// (temporary)
		if (button == 388828234 && event.getPlayer().getName().matches("_Senna_")) {
			ItemStack item = new ItemStack(Material.COBBLESTONE, 64, (byte) 0);
			event.getPlayer().getInventory().addItem(item);
		}

		// (temporary)
		if (button == 781855244 && event.getPlayer().getName().matches("_Senna_")) {
			ItemStack item = new ItemStack(Material.DIRT, 64, (byte) 0);
			event.getPlayer().getInventory().addItem(item);
		}
				
		// (temporary)
		if (button == -425775511 && event.getPlayer().getName().matches("_Senna_")) {
			ItemStack item = new ItemStack(Material.SAND, 64, (byte) 0);
			event.getPlayer().getInventory().addItem(item);
		}
		

		// Whool at spawn mall
		if (button == 1091869270) {
			Wallet wallet = new Wallet(event.getPlayer().getName());
			long newbalance = wallet.balance()-10000;
			if (newbalance >= 0) {
				wallet.take(10000);
				ItemStack item = new ItemStack(Material.WOOL, 64, (byte) 0);
				event.getClickedBlock().getWorld().dropItem(event.getClickedBlock().getLocation(), item);
				event.getPlayer().sendMessage(ChatColor.YELLOW + "10,000 tregs taken from you");
			} else {
				event.getPlayer().sendMessage(ChatColor.RED + "You need 10,000 tregs");				
			}
		}

		
		// Fire at spawn mall
		if (button == -1247980981) {
			Wallet wallet = new Wallet(event.getPlayer().getName());
			long newbalance = wallet.balance()-5000;
			if (newbalance >= 0) {
				wallet.take(5000);
				ItemStack item = new ItemStack(Material.FIRE, 1, (byte) 0);
				event.getClickedBlock().getWorld().dropItem(event.getClickedBlock().getLocation(), item);
				event.getPlayer().sendMessage(ChatColor.YELLOW + "5000 tregs taken from you");
			} else {
				event.getPlayer().sendMessage(ChatColor.RED + "You need 5000 tregs");				
			}
		}

		// Slime at spawn mall
		if (button == 1825125364) {
			Wallet wallet = new Wallet(event.getPlayer().getName());
			long newbalance = wallet.balance()-400;
			if (newbalance >= 0) {
				wallet.take(400);
				ItemStack item = new ItemStack(Material.SLIME_BALL, 1, (byte) 0);
				event.getClickedBlock().getWorld().dropItem(event.getClickedBlock().getLocation(), item);
				event.getPlayer().sendMessage(ChatColor.YELLOW + "400 tregs taken from you");
			} else {
				event.getPlayer().sendMessage(ChatColor.RED + "You need 400 tregs");				
			}
		}

		
		// Slime at spawn mall
		if (button == -737085906) {
			Wallet wallet = new Wallet(event.getPlayer().getName());
			long newbalance = wallet.balance()-300;
			if (newbalance >= 0) {
				wallet.take(300);
				ItemStack item = new ItemStack(Material.BONE, 1, (byte) 0);
				event.getClickedBlock().getWorld().dropItem(event.getClickedBlock().getLocation(), item);
				event.getPlayer().sendMessage(ChatColor.YELLOW + "300 tregs taken from you");
			} else {
				event.getPlayer().sendMessage(ChatColor.RED + "You need 300 tregs");				
			}
		}

		
		// Paper sale at spawn
		if (button == 2094840697) {
			Wallet wallet = new Wallet(event.getPlayer().getName());
			long newbalance = wallet.balance()-500;
			if (newbalance >= 0) {
				wallet.take(500);
				ItemStack item = new ItemStack(Material.PAPER, 1, (byte) 0);
				event.getClickedBlock().getWorld().dropItem(event.getClickedBlock().getLocation(), item);
				event.getPlayer().sendMessage(ChatColor.YELLOW + "500 tregs taken from you");
			} else {
				event.getPlayer().sendMessage(ChatColor.RED + "You need 500 tregs");				
			}
		}

		// Compass sale at spawn
		if (button == 1869997378) {
			Wallet wallet = new Wallet(event.getPlayer().getName());
			long newbalance = wallet.balance()-500;
			if (newbalance >= 0) {
				wallet.take(500);
				ItemStack item = new ItemStack(Material.COMPASS, 1, (byte) 0);
				event.getClickedBlock().getWorld().dropItem(event.getClickedBlock().getLocation(), item);
				event.getPlayer().sendMessage(ChatColor.YELLOW + "500 tregs taken from you");
			} else {
				event.getPlayer().sendMessage(ChatColor.RED + "You need 500 tregs");				
			}
		}

		if (button == 156737597) {
			event.setCancelled(false);


			Wallet wallet = new Wallet(event.getPlayer().getName());
			long newbalance = wallet.balance()-15000;
			if (newbalance >= 0) {
				wallet.take(15000);
				for (Player p : this.plugin.getServer().getOnlinePlayers()){
					p.playEffect(p.getLocation(), Effect.DOOR_TOGGLE, (byte)0);
					p.sendMessage(ChatColor.YELLOW + "You got a bitch slap from "+ event.getPlayer().getName());
				}
				event.getPlayer().sendMessage(ChatColor.YELLOW + "15,000 tregs was taken from you");
			} else {
				event.getPlayer().sendMessage(ChatColor.RED + "You need 15,000 tregs");				
			}



			//			for (Achievement a :	Achievement.values()) {
			//				event.getPlayer().awardAchievement(a);
			//			}

			//			event.getPlayer().getWorld().strikeLightning(event.getPlayer().getLocation());
			//			event.getPlayer().getWorld().spawnCreature(event.getPlayer().getLocation(), CreatureType.COW);
		}


		if (button == -121887341) {
			Block top1 = event.getClickedBlock().getWorld().getBlockAt(614, 33, -105);
			Block top2 = event.getClickedBlock().getWorld().getBlockAt(614, 33, -102);

			Block air1 = event.getClickedBlock().getWorld().getBlockAt(614, 32, -105);
			Block air2 = event.getClickedBlock().getWorld().getBlockAt(614, 20, -102);

			fill(air1, air2, Material.AIR);
			fill(top1, top2, Material.AIR);

			if(state) {
				fill(top1, top2, Material.LAVA);
				state = false;
			} else {
				fill(top1, top2, Material.WATER);
				state = true;
			}
		}



	}



	public void fill(Block b1, Block b2, Material item) {
		double xMax = Math.max(b1.getLocation().getX(), b2.getLocation().getX());
		double xMin = Math.min(b1.getLocation().getX(), b2.getLocation().getX());

		double yMax = Math.max(b1.getLocation().getY(), b2.getLocation().getY());
		double yMin = Math.min(b1.getLocation().getY(), b2.getLocation().getY());

		double zMax = Math.max(b1.getLocation().getZ(), b2.getLocation().getZ());
		double zMin = Math.min(b1.getLocation().getZ(), b2.getLocation().getZ());


		for(double x=xMin; x<= xMax; x++ ) {
			for(double y=yMin; y<= yMax; y++ ) {
				for(double z=zMin; z<= zMax; z++ ) {
					Block block = b1.getWorld().getBlockAt((int)x, (int)y, (int)z);
					block.setType(item);
				}

			}

		}
	}

}

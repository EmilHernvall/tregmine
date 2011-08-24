package info.tregmine.anticheat;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPlaceEvent;


public class AntiCheatBlock extends BlockListener {
	private final AntiCheat plugin;

	public AntiCheatBlock(AntiCheat instance) {
		plugin = instance;
		plugin.getServer();
	}

	public void onBlockPlace (final BlockPlaceEvent event)	{

		if(event.getBlock().getType() == Material.PISTON_BASE || event.getBlock().getType() == Material.PISTON_STICKY_BASE) {
			final Block piston = event.getBlockPlaced();
			this.plugin.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin,new Runnable() {
				public void run() {
					//					plugin.getServer().broadcastMessage(event.getPlayer().getName() + " checking again - " + (int) piston.getData());

					if ((int)piston.getData() == 5) {
						int x = event.getBlock().getLocation().getBlockX();
						int y = event.getBlock().getLocation().getBlockY();
						int z = event.getBlock().getLocation().getBlockZ();

						Block standingLeft = piston.getWorld().getBlockAt(x+1, y, z+2);
						Block standingRight = piston.getWorld().getBlockAt(x+1, y, z-2);

						Block longStandingLeft = piston.getWorld().getBlockAt(x+2, y, z+1);
						Block longStandinRight = piston.getWorld().getBlockAt(x+2, y, z-1);

						if ((standingLeft.getType() == Material.PISTON_STICKY_BASE || standingLeft.getType() == Material.PISTON_BASE) && (int) standingLeft.getData() == 2) {
							plugin.getServer().broadcastMessage(event.getPlayer().getName() + " is trying to make a dublicator");
							piston.getWorld().strikeLightningEffect(piston.getLocation());
							piston.setType(Material.AIR);

							standingLeft.getWorld().strikeLightningEffect(standingLeft.getLocation());
							standingLeft.setType(Material.AIR);

						}

						if ((longStandingLeft.getType() == Material.PISTON_STICKY_BASE || longStandingLeft.getType() == Material.PISTON_BASE) && (int) longStandingLeft.getData() == 2) {
							plugin.getServer().broadcastMessage(event.getPlayer().getName() + " is trying to make a dublicator");
							piston.getWorld().strikeLightningEffect(piston.getLocation());
							piston.setType(Material.AIR);

							longStandingLeft.getWorld().strikeLightningEffect(standingLeft.getLocation());
							longStandingLeft.setType(Material.AIR);
						}

						if ((standingRight.getType() == Material.PISTON_STICKY_BASE || standingRight.getType() == Material.PISTON_BASE) && (int) standingRight.getData() == 3) {
							plugin.getServer().broadcastMessage(event.getPlayer().getName() + " is trying to make a dublicator");
							piston.getWorld().strikeLightningEffect(piston.getLocation());
							piston.setType(Material.AIR);

							standingRight.getWorld().strikeLightningEffect(standingRight.getLocation());
							standingRight.setType(Material.AIR);
						}

						if ((longStandinRight.getType() == Material.PISTON_STICKY_BASE || longStandinRight.getType() == Material.PISTON_BASE) && (int) longStandinRight.getData() == 3) {
							plugin.getServer().broadcastMessage(event.getPlayer().getName() + " is trying to make a dublicator");
							piston.getWorld().strikeLightningEffect(piston.getLocation());
							piston.setType(Material.AIR);

							longStandinRight.getWorld().strikeLightningEffect(standingRight.getLocation());
							longStandinRight.setType(Material.AIR);
						}

					}

					if ((int)piston.getData() == 2) {
						int x = event.getBlock().getLocation().getBlockX();
						int y = event.getBlock().getLocation().getBlockY();
						int z = event.getBlock().getLocation().getBlockZ();

						Block standingLeft = piston.getWorld().getBlockAt(x+2, y, z-1);
						Block standingRight = piston.getWorld().getBlockAt(x-2, y, z-1);

						Block longStandingLeft = piston.getWorld().getBlockAt(x+1, y, z-2);
						Block longStandinRight = piston.getWorld().getBlockAt(x-1, y, z-2);


						if ((standingLeft.getType() == Material.PISTON_STICKY_BASE || standingLeft.getType() == Material.PISTON_BASE) && (int) standingLeft.getData() == 4) {
							plugin.getServer().broadcastMessage(event.getPlayer().getName() + " is trying to make a dublicator");
							piston.getWorld().strikeLightningEffect(piston.getLocation());
							piston.setType(Material.AIR);

							standingLeft.getWorld().strikeLightningEffect(standingLeft.getLocation());
							standingLeft.setType(Material.AIR);

						}

						if ((longStandingLeft.getType() == Material.PISTON_STICKY_BASE || longStandingLeft.getType() == Material.PISTON_BASE) && (int) longStandingLeft.getData() == 4) {
							plugin.getServer().broadcastMessage(event.getPlayer().getName() + " is trying to make a dublicator");
							piston.getWorld().strikeLightningEffect(piston.getLocation());
							piston.setType(Material.AIR);

							longStandingLeft.getWorld().strikeLightningEffect(longStandingLeft.getLocation());
							longStandingLeft.setType(Material.AIR);

						}

						if ((standingRight.getType() == Material.PISTON_STICKY_BASE || standingRight.getType() == Material.PISTON_BASE) && (int) standingRight.getData() == 5) {
							plugin.getServer().broadcastMessage(event.getPlayer().getName() + " is trying to make a dublicator");
							piston.getWorld().strikeLightningEffect(piston.getLocation());
							piston.setType(Material.AIR);

							standingRight.getWorld().strikeLightningEffect(standingRight.getLocation());
							standingRight.setType(Material.AIR);
						}

						if ((longStandinRight.getType() == Material.PISTON_STICKY_BASE || longStandinRight.getType() == Material.PISTON_BASE) && (int) longStandinRight.getData() == 5) {
							plugin.getServer().broadcastMessage(event.getPlayer().getName() + " is trying to make a dublicator");
							piston.getWorld().strikeLightningEffect(piston.getLocation());
							piston.setType(Material.AIR);

							longStandinRight.getWorld().strikeLightningEffect(longStandinRight.getLocation());
							longStandinRight.setType(Material.AIR);
						}
					}
					
					if ((int)piston.getData() == 4) {
						int x = event.getBlock().getLocation().getBlockX();
						int y = event.getBlock().getLocation().getBlockY();
						int z = event.getBlock().getLocation().getBlockZ();

						Block standingLeft = piston.getWorld().getBlockAt(x-1, y, z+2);
						Block standingRight = piston.getWorld().getBlockAt(x-1, y, z-2);

						Block longStandingLeft = piston.getWorld().getBlockAt(x-2, y, z-1);
						Block longStandinRight = piston.getWorld().getBlockAt(x-2, y, z+1);
						
						if ((standingLeft.getType() == Material.PISTON_STICKY_BASE || standingLeft.getType() == Material.PISTON_BASE) && (int) standingLeft.getData() == 2) {
							plugin.getServer().broadcastMessage(event.getPlayer().getName() + " is trying to make a dublicator");
							piston.getWorld().strikeLightningEffect(piston.getLocation());
							piston.setType(Material.AIR);

							standingLeft.getWorld().strikeLightningEffect(standingLeft.getLocation());
							standingLeft.setType(Material.AIR);

						}

						if ((longStandingLeft.getType() == Material.PISTON_STICKY_BASE || longStandingLeft.getType() == Material.PISTON_BASE) && (int) longStandingLeft.getData() == 2) {
							plugin.getServer().broadcastMessage(event.getPlayer().getName() + " is trying to make a dublicator");
							piston.getWorld().strikeLightningEffect(piston.getLocation());
							piston.setType(Material.AIR);

							longStandingLeft.getWorld().strikeLightningEffect(standingLeft.getLocation());
							longStandingLeft.setType(Material.AIR);
						}

						if ((standingRight.getType() == Material.PISTON_STICKY_BASE || standingRight.getType() == Material.PISTON_BASE) && (int) standingRight.getData() == 3) {
							plugin.getServer().broadcastMessage(event.getPlayer().getName() + " is trying to make a dublicator");
							piston.getWorld().strikeLightningEffect(piston.getLocation());
							piston.setType(Material.AIR);

							standingRight.getWorld().strikeLightningEffect(standingRight.getLocation());
							standingRight.setType(Material.AIR);
						}

						if ((longStandinRight.getType() == Material.PISTON_STICKY_BASE || longStandinRight.getType() == Material.PISTON_BASE) && (int) longStandinRight.getData() == 3) {
							plugin.getServer().broadcastMessage(event.getPlayer().getName() + " is trying to make a dublicator");
							piston.getWorld().strikeLightningEffect(piston.getLocation());
							piston.setType(Material.AIR);

							longStandinRight.getWorld().strikeLightningEffect(standingRight.getLocation());
							longStandinRight.setType(Material.AIR);
						}
					}

					
					if ((int)piston.getData() == 3) {
						int x = event.getBlock().getLocation().getBlockX();
						int y = event.getBlock().getLocation().getBlockY();
						int z = event.getBlock().getLocation().getBlockZ();

						Block standingLeft = piston.getWorld().getBlockAt(x+2, y, z+1);
						Block standingRight = piston.getWorld().getBlockAt(x-2, y, z+1);

						Block longStandingLeft = piston.getWorld().getBlockAt(x+1, y, z+2);
						Block longStandinRight = piston.getWorld().getBlockAt(x-1, y, z+2);


						if ((standingLeft.getType() == Material.PISTON_STICKY_BASE || standingLeft.getType() == Material.PISTON_BASE) && (int) standingLeft.getData() == 4) {
							plugin.getServer().broadcastMessage(event.getPlayer().getName() + " is trying to make a dublicator");
							piston.getWorld().strikeLightningEffect(piston.getLocation());
							piston.setType(Material.AIR);

							standingLeft.getWorld().strikeLightningEffect(standingLeft.getLocation());
							standingLeft.setType(Material.AIR);

						}

						if ((longStandingLeft.getType() == Material.PISTON_STICKY_BASE || longStandingLeft.getType() == Material.PISTON_BASE) && (int) longStandingLeft.getData() == 4) {
							plugin.getServer().broadcastMessage(event.getPlayer().getName() + " is trying to make a dublicator");
							piston.getWorld().strikeLightningEffect(piston.getLocation());
							piston.setType(Material.AIR);

							longStandingLeft.getWorld().strikeLightningEffect(longStandingLeft.getLocation());
							longStandingLeft.setType(Material.AIR);

						}

						if ((standingRight.getType() == Material.PISTON_STICKY_BASE || standingRight.getType() == Material.PISTON_BASE) && (int) standingRight.getData() == 5) {
							plugin.getServer().broadcastMessage(event.getPlayer().getName() + " is trying to make a dublicator");
							piston.getWorld().strikeLightningEffect(piston.getLocation());
							piston.setType(Material.AIR);

							standingRight.getWorld().strikeLightningEffect(standingRight.getLocation());
							standingRight.setType(Material.AIR);
						}

						if ((longStandinRight.getType() == Material.PISTON_STICKY_BASE || longStandinRight.getType() == Material.PISTON_BASE) && (int) longStandinRight.getData() == 5) {
							plugin.getServer().broadcastMessage(event.getPlayer().getName() + " is trying to make a dublicator");
							piston.getWorld().strikeLightningEffect(piston.getLocation());
							piston.setType(Material.AIR);

							longStandinRight.getWorld().strikeLightningEffect(longStandinRight.getLocation());
							longStandinRight.setType(Material.AIR);
						}
					}
					
				}},20L);


		}



	}
}

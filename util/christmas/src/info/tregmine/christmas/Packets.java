package info.tregmine.christmas;

import java.lang.reflect.Field;
import java.util.HashMap;

import net.minecraft.server.v1_9_R1.DataWatcher;
import net.minecraft.server.v1_9_R1.EntityPlayer;
import net.minecraft.server.v1_9_R1.Packet;

import net.minecraft.server.v1_9_R1.PacketPlayInClientCommand;
import net.minecraft.server.v1_9_R1.PacketPlayOutSpawnEntityLiving;
import net.minecraft.server.v1_9_R1.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_9_R1.PacketPlayOutEntityMetadata;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftPlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class Packets {

	//Class not used at the moment due to 1.7.2 Dev Limitations.

	/* public static final int ENTITY_ID = 1234;

        private static HashMap<String, Boolean> hasHealthBar = new HashMap<String, Boolean>();

        public static void sendPacket(Player player, Packet packet){
                EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();

                entityPlayer.playerConnection.sendPacket(packet);
        }

		public static PacketPlayOutSpawnEntityLiving getMobPacket(String text, Location loc) {
        	PacketPlayOutSpawnEntityLiving mobPacket = new PacketPlayOutSpawnEntityLiving();

                mobPacket.a = (int) ENTITY_ID;
                mobPacket.b = (byte) EntityType.WITHER.getTypeId();
                mobPacket.c = (int) Math.floor(loc.getBlockX() * 32.0D);
                mobPacket.d = (int) Math.floor(loc.getBlockY() * 32.0D);
                mobPacket.e = (int) Math.floor(loc.getBlockZ() * 32.0D);
                mobPacket.f = (byte) 0;
                mobPacket.g = (byte) 0;
                mobPacket.h = (byte) 0;
                mobPacket.i = (short) 0;
                mobPacket.j = (short) 0;
                mobPacket.k = (short) 0;

                DataWatcher watcher = getWatcher(text, 300);

                try{
                        Field t = PacketPlayOutSpawnEntityLiving.class.getDeclaredField("t");

                        t.setAccessible(true);
                        t.set(mobPacket, watcher);
                } catch(Exception e){
                        e.printStackTrace();
                }

                return mobPacket;
        }

        public static PacketPlayOutEntityDestroy getDestroyEntityPacket(){
                PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy();

                packet.a = new int[]{ENTITY_ID};

                return packet;
        }

        public static PacketPlayOutEntityMetadata getMetadataPacket(DataWatcher watcher){
                PacketPlayOutEntityMetadata metaPacket = new PacketPlayOutEntityMetadata();

                metaPacket.a = (int) ENTITY_ID;

                try{
                        Field b = PacketPlayOutEntityMetadata.class.getDeclaredField("b");

                        b.setAccessible(true);
                        b.set(metaPacket, watcher.c());
                } catch(Exception e){
                        e.printStackTrace();
                }

                return metaPacket;
        }

        public static PacketPlayInClientCommand getRespawnPacket(){
                PacketPlayInClientCommand packet = new PacketPlayInClientCommand();

                packet.a = (int) 1;

                return packet;
        }

        public static DataWatcher getWatcher(String text, int health){
                DataWatcher watcher = new DataWatcher();

                watcher.a(0, (Byte) (byte) 0x20);
                watcher.a(6, (Float) (float) health);
                watcher.a(10, (String) text);
                watcher.a(11, (Byte) (byte) 1);

                return watcher;
        }


        public static void displayTextBar(String text, final Player player, Plugin main) {
                PacketPlayOutSpawnEntityLiving mobPacket = getMobPacket(text, player.getLocation());

                sendPacket(player, mobPacket);
                hasHealthBar.put(player.getName(), true);

                new BukkitRunnable(){
                        @Override
                        public void run(){
                                PacketPlayOutEntityDestroy destroyEntityPacket = getDestroyEntityPacket();

                                sendPacket(player, destroyEntityPacket);
                                hasHealthBar.put(player.getName(), false);
                        }
                }.runTaskLater(main, 400L);
        }
	 */
}
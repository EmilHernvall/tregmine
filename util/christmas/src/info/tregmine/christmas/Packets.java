package info.tregmine.christmas;
 
import java.lang.reflect.Field;
import java.util.HashMap;
 
import net.minecraft.server.v1_6_R3.DataWatcher;
import net.minecraft.server.v1_6_R3.EntityPlayer;
import net.minecraft.server.v1_6_R3.Packet;
import net.minecraft.server.v1_6_R3.Packet205ClientCommand;
import net.minecraft.server.v1_6_R3.Packet24MobSpawn;
import net.minecraft.server.v1_6_R3.Packet29DestroyEntity;
import net.minecraft.server.v1_6_R3.Packet40EntityMetadata;
 
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_6_R3.entity.CraftPlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
 
/*
 * Credit to ChaseChocolate for making this class free for anyone to use!
 */

public class Packets {
  public static final int ENTITY_ID = 1234;
       
        private static HashMap<String, Boolean> hasHealthBar = new HashMap<String, Boolean>();
       
        public static void sendPacket(Player player, Packet packet){
                EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
               
                entityPlayer.playerConnection.sendPacket(packet);
        }
       
        @SuppressWarnings("deprecation")
		public static Packet24MobSpawn getMobPacket(String text, Location loc) {
                Packet24MobSpawn mobPacket = new Packet24MobSpawn();
               
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
                        Field t = Packet24MobSpawn.class.getDeclaredField("t");
                       
                        t.setAccessible(true);
                        t.set(mobPacket, watcher);
                } catch(Exception e){
                        e.printStackTrace();
                }
               
                return mobPacket;
        }
       
        public static Packet29DestroyEntity getDestroyEntityPacket(){
                Packet29DestroyEntity packet = new Packet29DestroyEntity();
               
                packet.a = new int[]{ENTITY_ID};
               
                return packet;
        }
       
        public static Packet40EntityMetadata getMetadataPacket(DataWatcher watcher){
                Packet40EntityMetadata metaPacket = new Packet40EntityMetadata();
               
                metaPacket.a = (int) ENTITY_ID;
               
                try{
                        Field b = Packet40EntityMetadata.class.getDeclaredField("b");
                       
                        b.setAccessible(true);
                        b.set(metaPacket, watcher.c());
                } catch(Exception e){
                        e.printStackTrace();
                }
               
                return metaPacket;
        }
               
        public static Packet205ClientCommand getRespawnPacket(){
                Packet205ClientCommand packet = new Packet205ClientCommand();
               
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
                Packet24MobSpawn mobPacket = getMobPacket(text, player.getLocation());
               
                sendPacket(player, mobPacket);
                hasHealthBar.put(player.getName(), true);
               
                new BukkitRunnable(){
                        @Override
                        public void run(){
                                Packet29DestroyEntity destroyEntityPacket = getDestroyEntityPacket();
                               
                                sendPacket(player, destroyEntityPacket);
                                hasHealthBar.put(player.getName(), false);
                        }
                }.runTaskLater(main, 400L);
        }
}
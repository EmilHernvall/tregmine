package info.tregmine.api.lore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bukkit.Material;

import static org.bukkit.ChatColor.*;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class Lore
{

    public static enum Sword {
        BOOMSTICK(AQUA + "The Boomstick"),
        BROADSWORD(AQUA + "Broadsword"),
        CLAYMORE(AQUA + "Claymore"),
        CUTLASS(AQUA + "Cutlass"),
        DARK_RAGE(AQUA + "Dark Rage"),
        EXCALIBUR(GOLD + "Excalibur"),
        EPEE(AQUA + "Epee"),
        EQUALIZER(AQUA + "Equalizer"),
        GODSWORD(GOLD + "Godsword of " + God.getRandom()),
        GLAMDRING(AQUA + "Glamdring"),
        KATANA(AQUA + "Katana"),
        LONGSWORD(AQUA + "Longsword"),
        NIGHTFURY(AQUA + "Night Fury"),
        NIGHTSLICER(AQUA + "Night Slicer"),
        NIGHTMARE_SABER(AQUA + "Nightmare Sabre"),
        PAINMAKER(AQUA + "Pain Maker"),
        PERSUADER(AQUA + "The Persuader"),
        RAPIER(AQUA + "Rapier"),
        SAW(AQUA + "Saw"),
        SCIMITAR(AQUA + "Scimitar"),
        SHORTSWORD(AQUA + "Short sword"),
        STING(AQUA + "Sting"),
        TEAR_OF_LUCIFER(RED + "Tear of Lucifer"),
        VORPAL(RED + "Nether Vorpal");
        
        
        
        private final String name;
        private static Map<Integer, Sword> swords = Maps.newHashMap();
        
        static{
            for(Sword sword: values()){
                swords.put(sword.ordinal(), sword);
            }
        }
        
        private Sword(String name)
        {
            this.name = name;
        }

        @Override
        public String toString()
        {
            return name;
        }
        
        public static Sword getRandom()
        {
            return swords.get(new Random().nextInt(Sword.values().length - 1));
        }
    }

    public static enum Armor 
    {
        BASSINET(AQUA + "Bassinet"),
        BOOTS(AQUA + "Boots of Awe"),
        BRASSARD(AQUA + "Brassard"),
        CHESTPLATE(AQUA + "Chestplate of " + God.getRandom()),
        CUIRASS(AQUA + "Cuirass"),
        COIF(AQUA + "Coif"),
        COWL(AQUA + "Cowl"),
        FULLHELM(AQUA + "Fullhelm"),
        GREAVES(AQUA + "Greaves"),
        GUTBUKKIT(AQUA + "Gut Bukkit"),
        HELM_OF_NOTCH(AQUA + "Helm of Notch"),
        MOCCASINS(AQUA + "Moccasins"),
        PAULDRONS(AQUA + " Pauldrons of Valiance"),
        SABATONS(AQUA + "Sabatons"),
        SKULLSHIELD(AQUA + "Skull Shield"),
        STOMPERS(AQUA + "Stompers"),
        TASSETS(GOLD + "Tassets"),
        PLATELEGS(AQUA + "Platelegs");
        
        private final String name;
        private static HashMap<Integer, Armor> armors = Maps.newHashMap();
        private static HashMap<Armor, String> byPiece = Maps.newHashMap();
        
        static{
            for(Armor armor: values()){
                armors.put(armor.ordinal(), armor);
            }
            
            //Chest peice
            byPiece.put(CHESTPLATE, "CHESTPLATE");
            byPiece.put(CUIRASS, "CHESTPLATE");
            byPiece.put(GUTBUKKIT, "CHESTPLATE");
            
            //leggings
            byPiece.put(BRASSARD, "LEGGINGS");
            byPiece.put(TASSETS, "LEGGINGS");
            byPiece.put(PLATELEGS, "LEGGINGS");
            byPiece.put(GREAVES, "LEGGINGS");
            
            //helmet
            byPiece.put(COIF, "HELMET");
            byPiece.put(COWL, "HELMET");
            byPiece.put(BASSINET, "HELEMET");
            byPiece.put(FULLHELM, "HELMET");
            byPiece.put(HELM_OF_NOTCH, "HELMET");
            byPiece.put(SKULLSHIELD, "HELMET");

            //boots
            byPiece.put(BOOTS, "BOOTS");
            byPiece.put(MOCCASINS, "BOOTS");
            byPiece.put(SABATONS, "BOOTS");
            byPiece.put(STOMPERS, "BOOTS");
        }

        private Armor(String name)
        {
            this.name = name;
        }

        @Override
        public String toString()
        {
            return name;
        }
        
        public static Armor getRandomByType(Material mat)
        {
            String name = mat.name().split("_")[1];
            List<Armor> armors = Lists.newArrayList();
            for(Armor armor: values()){
                if(byPiece.get(armor).equalsIgnoreCase(name)){
                    armors.add(armor);
                }
            }
            return armors.get(new Random().nextInt(armors.size() -1));
        }
        
    }
    
    private static enum God{
        KNIPIL(DARK_RED + "knipil"),
        MEJJAD(DARK_RED + "mejjad"),
        EINAND(DARK_RED + "einand"),
        BLACKX(DARK_RED + "BlackX"),
        JAYT8(DARK_RED + "JAYT8"),
        GEORGEBOMBADIL(DARK_RED +"GeorgeBombadil"),
        SABERSAMUS(DARK_PURPLE + "Sabersamus"),
        NOTARO1997(DARK_PURPLE + "notaro1997"),
        LDIVERSE(RED + "lDiverse"),
        ICE347(DARK_PURPLE + "ice347");
        
        private final String name;
        private static HashMap<Integer, God> gods = Maps.newHashMap();
        
        static{
            for(God god: values()){
                gods.put(god.ordinal(), god);
            }
        }
        
        private God(String name)
        {
            this.name = name;
        }

        public static God getRandom()
        {
            return gods.get(new Random().nextInt(God.values().length - 1));
        }
        
        public String toString()
        {
            return name;
        }
    }
}

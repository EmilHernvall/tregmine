package info.tregmine.api;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;

public enum Rank
{
    UNVERIFIED(ChatColor.WHITE),
    TOURIST(ChatColor.WHITE),
    SETTLER(ChatColor.GREEN),
    RESIDENT(ChatColor.DARK_GREEN),
    DONATOR(ChatColor.GOLD),
    GUARDIAN(ChatColor.BLUE),
    CODER(ChatColor.DARK_PURPLE),
    BUILDER(ChatColor.YELLOW),
    JUNIOR_ADMIN(ChatColor.RED),
    SENIOR_ADMIN(ChatColor.DARK_RED);

    private ChatColor color;

    private Rank(ChatColor color)
    {
        this.color = color;
    }

    public boolean canUseCommands()
    {
        return this != UNVERIFIED;
    }

    public boolean canBuild()
    {
        return this == SETTLER ||
               this == RESIDENT ||
               this == DONATOR ||
               this == GUARDIAN ||
               this == BUILDER ||
               this == CODER ||
               this == JUNIOR_ADMIN ||
               this == SENIOR_ADMIN;
    }

    public boolean canBottleXP()
    {
        return this == DONATOR ||
               this == GUARDIAN ||
               this == BUILDER ||
               this == CODER ||
               this == JUNIOR_ADMIN ||
               this == SENIOR_ADMIN;
    }

    public boolean canPickup()
    {
        return this == SETTLER ||
               this == RESIDENT ||
               this == DONATOR ||
               this == GUARDIAN ||
               this == CODER ||
               this == BUILDER ||
               this == JUNIOR_ADMIN ||
               this == SENIOR_ADMIN;
    }

    public boolean canSetBiome()
    {
        return this == BUILDER ||
               this == JUNIOR_ADMIN ||
               this == SENIOR_ADMIN;
    }

    public boolean canRemItems()
    {
        return this == JUNIOR_ADMIN ||
               this == SENIOR_ADMIN;
    }

    public boolean canTeleport()
    {
        return this == SETTLER ||
               this == RESIDENT ||
               this == DONATOR ||
               this == GUARDIAN ||
               this == CODER ||
               this == BUILDER ||
               this == JUNIOR_ADMIN ||
               this == SENIOR_ADMIN;
    }

    public boolean canMentor()
    {
        return this == RESIDENT ||
               this == DONATOR ||
               this == GUARDIAN ||
               this == CODER ||
               this == BUILDER ||
               this == JUNIOR_ADMIN ||
               this == SENIOR_ADMIN;
    }

    public boolean canSetTime()
    {
        return this == DONATOR ||
               this == GUARDIAN ||
               this == CODER ||
               this == BUILDER ||
               this == JUNIOR_ADMIN ||
               this == SENIOR_ADMIN;
    }
    
    public boolean canForceOpenChests()
    {
        return  this == JUNIOR_ADMIN ||
                this == SENIOR_ADMIN;
    }

    public boolean canSetWeather()
    {
        return this == DONATOR ||
               this == GUARDIAN ||
               this == CODER ||
               this == BUILDER ||
               this == JUNIOR_ADMIN ||
               this == SENIOR_ADMIN;
    }

    public boolean canSetQuitMessage()
    {
        return this == DONATOR ||
               this == GUARDIAN ||
               this == CODER ||
               this == BUILDER ||
               this == JUNIOR_ADMIN ||
               this == SENIOR_ADMIN;
    }

    public boolean canUseCompass()
    {
        return this == DONATOR ||
               this == GUARDIAN ||
               this == CODER ||
               this == BUILDER ||
               this == JUNIOR_ADMIN ||
               this == SENIOR_ADMIN;
    }

    public boolean canFly()
    {
        return this == DONATOR ||
               this == GUARDIAN ||
               this == CODER ||
               this == BUILDER ||
               this == JUNIOR_ADMIN ||
               this == SENIOR_ADMIN;
    }

    public boolean canSaveHome()
    {
        return this == SETTLER ||
               this == RESIDENT ||
               this == DONATOR ||
               this == GUARDIAN ||
               this == CODER ||
               this == BUILDER ||
               this == JUNIOR_ADMIN ||
               this == SENIOR_ADMIN;
    }

    public boolean canShieldTeleports()
    {
        return this == DONATOR ||
               this == GUARDIAN ||
               this == CODER ||
               this == BUILDER ||
               this == JUNIOR_ADMIN ||
               this == SENIOR_ADMIN;
    }

    public boolean mustUseKeyword()
    {
        return this == GUARDIAN ||
               this == CODER ||
               this == BUILDER ||
               this == JUNIOR_ADMIN ||
               this == SENIOR_ADMIN;
    }


    public boolean canVisitHomes()
    {
        return this == GUARDIAN ||
               this == CODER ||
               this == JUNIOR_ADMIN ||
               this == SENIOR_ADMIN;
    }

    public boolean canReport()
    {
        return this == GUARDIAN ||
               this == CODER ||
               this == JUNIOR_ADMIN ||
               this == SENIOR_ADMIN;
    }

    public boolean canSeeAliases()
    {
        return this == GUARDIAN ||
               this == CODER ||
               this == JUNIOR_ADMIN ||
               this == SENIOR_ADMIN;
    }

    public boolean canViewPlayersBadge()
    {
        return this == JUNIOR_ADMIN ||
               this == SENIOR_ADMIN;
    }

    public boolean canNotBeIgnored()
    {
        return this == GUARDIAN ||
               this == JUNIOR_ADMIN ||
               this == SENIOR_ADMIN;
    }

    public boolean canViewIgnored()
    {
        return this == GUARDIAN ||
               this == JUNIOR_ADMIN ||
               this == SENIOR_ADMIN;
    }
    
    public boolean canBypassWorld()
    {
        return this == JUNIOR_ADMIN ||
                this == SENIOR_ADMIN;
    }

    public boolean canWarn()
    {
        return this == GUARDIAN ||
               this == JUNIOR_ADMIN ||
               this == SENIOR_ADMIN;
    }

    public boolean arePickupsLogged()
    {
        return this == SETTLER ||
               this == RESIDENT ||
               this == DONATOR ||
               this == CODER ||
               this == GUARDIAN ||
               this == BUILDER;
    }

    public boolean canKick()
    {
        return this == GUARDIAN ||
               this == JUNIOR_ADMIN ||
               this == SENIOR_ADMIN;
    }

    public boolean canBan()
    {
        return this == GUARDIAN ||
               this == JUNIOR_ADMIN ||
               this == SENIOR_ADMIN;
    }

    public boolean canBless()
    {
        return this == GUARDIAN ||
               this == JUNIOR_ADMIN ||
               this == SENIOR_ADMIN;
    }

    public boolean canUseCreative()
    {
        return this == BUILDER ||
               this == JUNIOR_ADMIN ||
               this == SENIOR_ADMIN;
    }

    public boolean canFill()
    {
        return this == BUILDER ||
               this == JUNIOR_ADMIN ||
               this == SENIOR_ADMIN;
    }

    public boolean canBrush()
    {
        return this == BUILDER ||
               this == JUNIOR_ADMIN ||
               this == SENIOR_ADMIN;
    }

    public boolean canTeleportBetweenWorlds()
    {
        return this == BUILDER ||
               this == CODER ||
               this == JUNIOR_ADMIN ||
               this == SENIOR_ADMIN;
    }

    public boolean canInspectInventories()
    {
        return this == JUNIOR_ADMIN ||
               this == SENIOR_ADMIN;
    }

    public boolean canSetOthersQuitMessage()
    {
        return this == JUNIOR_ADMIN ||
               this == SENIOR_ADMIN;
    }

    public boolean canChangeName()
    {
        return this == JUNIOR_ADMIN ||
               this == SENIOR_ADMIN;
    }
    
    public boolean canChangeJackpot()
    {
        return this == JUNIOR_ADMIN ||
               this == SENIOR_ADMIN;
    }

    public boolean canSpawnMobs()
    {
        return this == BUILDER ||
               this == JUNIOR_ADMIN ||
               this == SENIOR_ADMIN;
    }

    public boolean canSpawnItems()
    {
        return this == JUNIOR_ADMIN ||
               this == SENIOR_ADMIN;
    }

    public boolean canCreateWarps()
    {
        return this == JUNIOR_ADMIN ||
               this == SENIOR_ADMIN;
    }

    public boolean canNuke()
    {
        return this == JUNIOR_ADMIN ||
               this == SENIOR_ADMIN;
    }

    public boolean canBeGod()
    {
        return this == JUNIOR_ADMIN ||
               this == SENIOR_ADMIN;
    }

    public boolean canSendPeopleToOtherWorlds()
    {
        return this == CODER ||
               this == JUNIOR_ADMIN ||
               this == SENIOR_ADMIN;
    }

    public boolean canChooseLottery()
    {
        return this == JUNIOR_ADMIN ||
               this == SENIOR_ADMIN ||
               this == GUARDIAN ||
               this == CODER;
    }

    public boolean canGetPlayerHead()
    {
        return this == JUNIOR_ADMIN ||
                this == SENIOR_ADMIN ||
                this == BUILDER;
    }

    public boolean canSetSpawners()
    {
        return this == BUILDER ||
               this == JUNIOR_ADMIN ||
               this == SENIOR_ADMIN;
    }

    public boolean canGetTrueTab()
    {
        return this == JUNIOR_ADMIN ||
               this == SENIOR_ADMIN;
    }

    public boolean canSummon()
    {
        return this == CODER ||
               this == JUNIOR_ADMIN ||
               this == SENIOR_ADMIN;
    }

    public boolean canDoHiddenTeleport()
    {
        return this == JUNIOR_ADMIN ||
               this == SENIOR_ADMIN;
    }

    public boolean canOverrideTeleportShield()
    {
        return this == JUNIOR_ADMIN ||
               this == SENIOR_ADMIN;
    }

    public boolean canOverrideForceShield()
    {
        return this == JUNIOR_ADMIN ||
               this == SENIOR_ADMIN;
    }

    public boolean canTeleportToPlayers()
    {
        return this == JUNIOR_ADMIN ||
               this == SENIOR_ADMIN;
    }

    public boolean canVanish()
    {
        return this == JUNIOR_ADMIN ||
               this == SENIOR_ADMIN;
    }

    public boolean canSeeHiddenInfo()
    {
        return this == CODER ||
               this == JUNIOR_ADMIN ||
               this == SENIOR_ADMIN;
    }

    public boolean canModifyZones()
    {
        return this == JUNIOR_ADMIN ||
               this == SENIOR_ADMIN;
    }

    public boolean canUseEnhancedCompass()
    {
        return this == GUARDIAN ||
               this == CODER ||
               this == BUILDER ||
               this == JUNIOR_ADMIN ||
               this == SENIOR_ADMIN;
    }

    public boolean canPlaceBannedBlocks()
    {
        return this == JUNIOR_ADMIN ||
               this == SENIOR_ADMIN;
    }

    public boolean canBreakBannedBlocks()
    {
        return this == JUNIOR_ADMIN ||
                this == SENIOR_ADMIN;
    }

    public boolean canSpawnTools()
    {
        return this == JUNIOR_ADMIN ||
               this == SENIOR_ADMIN;
    }

    public boolean canCraftTools()
    {
        return this == RESIDENT ||
                this == DONATOR ||
                this == GUARDIAN ||
                this == CODER ||
                this == BUILDER ||
                this == JUNIOR_ADMIN ||
                this == SENIOR_ADMIN;
    }

    public boolean canUseTools()
    {
        return this == RESIDENT ||
                this == DONATOR ||
                this == GUARDIAN ||
                this == CODER ||
                this == BUILDER ||
                this == JUNIOR_ADMIN ||
                this == SENIOR_ADMIN;
    }

    public boolean canRepairTools()
    {
        return this == RESIDENT ||
                this == DONATOR ||
                this == GUARDIAN ||
                this == CODER ||
                this == BUILDER ||
                this == JUNIOR_ADMIN ||
                this == SENIOR_ADMIN;
    }
    
    public boolean canChannelView()
    {
        return this == JUNIOR_ADMIN ||
               this == SENIOR_ADMIN;
    }

    public boolean canGetChunkInfo()
    {
        return this == JUNIOR_ADMIN ||
               this == SENIOR_ADMIN;
    }

    public int getTeleportTimeout()
    {
        if (this == CODER ||
            this == BUILDER ||
            this == JUNIOR_ADMIN ||
            this == SENIOR_ADMIN) {

            return 0;
        } else if (this == GUARDIAN) {
            return 20 * 1;
        } else if (this == DONATOR) {
            return 20 * 1;
        } else {
            return 20 * 5;
        }
    }

    public int getTeleportDistanceLimit()
    {
        if (this == CODER ||
            this == BUILDER ||
            this == JUNIOR_ADMIN ||
            this == SENIOR_ADMIN) {

            return Integer.MAX_VALUE;
        } else if (this == GUARDIAN) {
            return Integer.MAX_VALUE;
        } else if (this == DONATOR) {
            return 10000;
        } else {
            return 100;
        }
    }

    public int getHomeLimit()
    {
        if (this == CODER ||
            this == BUILDER ||
            this == JUNIOR_ADMIN ||
            this == SENIOR_ADMIN) {

            return Integer.MAX_VALUE;
        } else if (this == GUARDIAN) {
            return 20;
        } else if (this == DONATOR) {
            return 5;
        } else if (this == RESIDENT || this == SETTLER) {
            return 1;
        } else {
            return 0;
        }
    }

    public int getBlessCost(Block block)
    {
        if (this == JUNIOR_ADMIN ||
            this == SENIOR_ADMIN) {

            return 0;
        }

        switch (block.getType()) {
        case CHEST:
            return 25000;
        case WOOD_DOOR:
        case WOODEN_DOOR:
            return 2000;
        default:
            return 4000;
        }
    }

    public int getTradeDistance(TregminePlayer player)
    {
        if (this == JUNIOR_ADMIN ||
            this == SENIOR_ADMIN) {
            return 1000;
        } else if (this == GUARDIAN) {
            return 1000;
        } else if (this == CODER ||
                   this == BUILDER) {
            return 1000;
        } else if (this == DONATOR) {
            return 200;
        } else if (player.hasBadge(Badge.MERCHANT)) {
            return 500;
        } else {
            return 100;
        }
    }
    
    public double getPickupDistance()
    {
        if (this == JUNIOR_ADMIN || this == SENIOR_ADMIN) {
            return 5;
        } else if (this == GUARDIAN || this == CODER || this == BUILDER) {
            return 4;
        } else if (this == DONATOR) {
            return 3;
        } else if (this == RESIDENT) {
            return 1.5;
        } else {
            return 1;
        }
    }
    
    public boolean canEditBanks()
    {
        return (this == JUNIOR_ADMIN ||
                this == SENIOR_ADMIN
                /* || this == CODER */ ); //Possibly? :P
                
    }
    
    public boolean canCheckBlocks()
    {
        return (this == JUNIOR_ADMIN || this == SENIOR_ADMIN);
    }

    public ChatColor getColor()
    {
        return color;
    }

    public static Rank fromString(String value)
    {
        for (Rank rank : Rank.values())  {
            if (value.equalsIgnoreCase(rank.toString())) {
                return rank;
            }
        }

        return null;
    }

}

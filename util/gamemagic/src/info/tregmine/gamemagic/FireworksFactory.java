package info.tregmine.gamemagic;

//import java.util.Arrays;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;

public class FireworksFactory
{
    private FireworkEffect.Type type = FireworkEffect.Type.BALL;

    private Color[] colors = new Color[19];
    private Color fadeTo = null;

    private int colorCounter = 0;
    private int duration = 1;

    public FireworksFactory()
    {
    }

    public void addColor(Color _color)
    {
        if (colorCounter > 0) {
            for (int cc = 1; cc <= colorCounter; cc++) {
                if (colors[cc].equals(_color)) {
                    return;
                }
            }
        }
        colorCounter++;
        colors[colorCounter] = _color;
    }

    public String effectToString(FireworkEffect.Type type)
    {
        if (FireworkEffect.Type.BALL.equals(type)) {
            return "ball";
        }
        else if (FireworkEffect.Type.BALL_LARGE.equals(type)) {
            return "large ball";
        }
        else if (FireworkEffect.Type.STAR.equals(type)) {
            return "star";
        }
        else if (FireworkEffect.Type.CREEPER.equals(type)) {
            return "creeper";
        }

        return "UNKOWN";
    }

    public String colorToString(Color c)
    {
        return c.toString().toLowerCase();
    }

    public void duration(int _duration)
    {
        this.duration = _duration;
    }

    public String[] hasColorAsString()
    {

        String [] colorNames = new String[colorCounter];

        for (int i = 0; i >= colorCounter;i++) {
            colorNames[i] = this.colorToString(colors[i]);
        }
        return colorNames;
    }

    public void fadeTo(Color color)
    {
        this.fadeTo = color;
    }

    public void addType(FireworkEffect.Type type)
    {
        this.type = type;
    }

    public void shot(Location location)
    {
        Firework firework = (Firework)location.getWorld().spawnEntity(location, EntityType.FIREWORK);
        FireworkEffect.Builder effect = FireworkEffect.builder();

        for (int cc = 1; cc <= colorCounter; cc++) {
            effect.withColor(colors[cc]);
        }

        FireworkMeta meta = firework.getFireworkMeta();
        effect.with(type);
        if (this.fadeTo != null) {
            effect.withFade(fadeTo);
        }

        meta.setPower(this.duration);
        meta.addEffect(effect.build());
        firework.setFireworkMeta(meta);
    }

    public ItemStack getAsStack(int _stackSize)
    {
        ItemStack item = new ItemStack(Material.FIREWORK, _stackSize);
        FireworkEffect.Builder effect = FireworkEffect.builder();

        String colorString = "";

        for (int cc = 1; cc <= colorCounter; cc++) {
            effect.withColor(colors[cc]);
            colorString = colorString + " " + this.colorToString(colors[cc]);
        }

        FireworkMeta meta = (FireworkMeta) item.getItemMeta();
        meta.setDisplayName("Firework: " + colorString + " " + this.effectToString(this.type)  );
        effect.with(type);

        if (this.fadeTo != null) {
            effect.withFade(fadeTo);
        }

        meta.setPower(this.duration);
        meta.addEffect(effect.build());
        item.setItemMeta(meta);

        return item;
    }
}

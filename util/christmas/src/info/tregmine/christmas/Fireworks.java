package info.tregmine.christmas;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;

public class Fireworks {

	public static void Fireworks(Player p){
		Firework rfw = (Firework) p.getWorld()
				.spawnEntity(p.getLocation(),EntityType.FIREWORK);
		FireworkMeta rfwm = rfw.getFireworkMeta();
		FireworkEffect.Type rtype = FireworkEffect.Type.BALL_LARGE;
		FireworkEffect reffect = (FireworkEffect.builder().trail(false)
				.withColor(Color.RED).flicker(false).with(rtype).build());
		rfwm.addEffect(reffect);
		rfwm.setPower(0);
		rfw.setFireworkMeta(rfwm);

		Firework gfw = (Firework) p.getWorld()
				.spawnEntity(p.getLocation(),EntityType.FIREWORK);
		FireworkMeta gfwm = gfw.getFireworkMeta();
		FireworkEffect.Type gtype = FireworkEffect.Type.BALL_LARGE;
		FireworkEffect geffect = (FireworkEffect.builder().trail(false)
				.withColor(Color.LIME).flicker(false).with(gtype).build());
		gfwm.addEffect(geffect);
		gfwm.setPower(0);
		gfw.setFireworkMeta(gfwm);
	}
}

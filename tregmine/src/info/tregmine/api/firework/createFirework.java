package info.tregmine.api.firework;

//import java.util.Arrays;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;

public class createFirework {

	private FireworkEffect.Type type = FireworkEffect.Type.BALL;


	private Color[] colors = new Color[19];
	private Color fadeTo = null;

	private int colorCounter = 0;
	private int duration = 1;



	public void addColor(Color _color) {

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

	public String effectToString(FireworkEffect.Type _type) {

		if (FireworkEffect.Type.BALL.equals(_type)) {
			return "ball";
		}
		
		if (FireworkEffect.Type.BALL_LARGE.equals(_type)) {
			return "large ball";
		}
		
		if (FireworkEffect.Type.STAR.equals(_type)) {
			return "star";
		}
		
		if (FireworkEffect.Type.CREEPER.equals(_type)) {
			return "creeper";
		}
		
		return "UNKOWN";
	}

	public String colorToString(Color c) {

		if (c.equals(Color.WHITE)) {
			return "white";
		}

		if (c.equals(Color.BLACK)) {
			return "black";
		}

		if (c.equals(Color.BLUE)) {
			return "blue";
		}

		if (c.equals(Color.FUCHSIA)) {
			return "fuchsia";
		}

		if (c.equals(Color.GRAY)) {
			return "gray";
		}

		if (c.equals(Color.GREEN)) {
			return "green";
		}

		if (c.equals(Color.LIME)) {
			return "lime";
		}

		if (c.equals(Color.MAROON)) {
			return "maroon";
		}

		if (c.equals(Color.NAVY)) {
			return "navy";
		}

		if (c.equals(Color.OLIVE)) {
			return "olive";
		}

		if (c.equals(Color.ORANGE)) {
			return "orange";
		}

		if (c.equals(Color.PURPLE)) {
			return "purple";
		}

		if (c.equals(Color.AQUA)) {
			return "aqua";
		}

		if (c.equals(Color.RED)) {
			return "red";
		}

		if (c.equals(Color.SILVER)) {
			return "silver";
		}

		if (c.equals(Color.TEAL)) {
			return "teal";
		}

		if (c.equals(Color.YELLOW)) {
			return "yellow";
		}

		return c.toString();

	}

	public void duration(int _duration) {
		this.duration = _duration;
	}

	public String[] hasColorAsString() {

		String [] colorNames = new String[colorCounter];

		for (int i = 0; i >= colorCounter;i++) {
			colorNames[i] = this.colorToString(colors[i]);
		}
		return colorNames;
	}

	public void fadeTo(Color _color) {
		this.fadeTo = _color;
	}
	
	public void addType(FireworkEffect.Type _type) {
		this.type = _type;
	}

	public void shot(Location _location){
		
        Firework firework = (Firework) _location.getWorld().spawnEntity(_location, EntityType.FIREWORK);
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

	
	public ItemStack getAsStack(int _stackSize){
		ItemStack item = new ItemStack(Material.FIREWORK, _stackSize);
		FireworkEffect.Builder effect = FireworkEffect.builder();

		String colorString = "";

		//		Arrays.sort(colors);

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

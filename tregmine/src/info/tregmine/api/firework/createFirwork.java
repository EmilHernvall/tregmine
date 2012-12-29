package info.tregmine.api.firework;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;

public class createFirwork {

	private FireworkEffect.Type type = FireworkEffect.Type.BALL;


	private Color[] colors = new Color[19];
	public int colorCounter = 0;



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

	public String[] hasColorAsString() {

		String [] colorNames = new String[colorCounter];

		for (int i = 0; i >= colorCounter;i++) {
			colorNames[i] = this.colorToString(colors[i]);
		}
		return colorNames;
	}

	public void addType(FireworkEffect.Type _type) {
		this.type = _type;
	}

	public ItemStack getAsStack(int _stackSize){
		ItemStack item = new ItemStack(Material.FIREWORK, _stackSize);
		FireworkEffect.Builder effect = FireworkEffect.builder();

		
		for (int cc = 1; cc <= colorCounter; cc++) {
			effect.withColor(colors[cc]);
		}
		
		FireworkMeta meta = (FireworkMeta) item.getItemMeta();
		meta.setDisplayName("Firework: " + effect.toString() );
		meta.addEffect(effect.build());
		item.setItemMeta(meta);

		return item;
	}

}

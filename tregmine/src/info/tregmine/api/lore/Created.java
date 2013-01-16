package info.tregmine.api.lore;

import org.bukkit.ChatColor;

	public enum Created {

		MINED  (ChatColor.GREEN + "MINED"),
		SPAWNED (ChatColor.RED + "SPAWNED"),
		FILLED (ChatColor.RED + "FILLED"),
		CREATIVE (ChatColor.YELLOW + "CREATIVE");

		private String colorString = null;

		Created (String _text){
			this.colorString = _text;
		};
		
		public String toColorString() {
			return colorString;
		}
		
}

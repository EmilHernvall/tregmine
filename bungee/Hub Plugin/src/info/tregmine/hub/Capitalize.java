package info.tregmine.hub;

public class Capitalize {
	
	/*
	 * This class probably isn't really necessary, but yeah.
	 */
	
	public String capitalize(String input){
		String output = input.substring(0, 1).toUpperCase() + input.substring(1);
		return output;
	}
}

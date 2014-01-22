package info.tregmine.portals;

public class Capitalize {
	
	/*
	 * This class probably isn't really necessary, but yeah.
	 * 
	 * Example use "hello" --> "Hello"
	 */
	
	public static String string(String original){
	    if(original.length() == 0)
	        return original;
	    return original.substring(0, 1).toUpperCase() + original.substring(1);
	}
}

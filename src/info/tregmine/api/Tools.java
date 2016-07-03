package info.tregmine.api;

public class Tools {
	public Tools(){
		
	}
	
	public String reverseColorCodes(String a){
    	String b = a.replace("§", "#");
    	return b;
    }
}

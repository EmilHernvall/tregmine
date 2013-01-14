package info.tregmine.api.url;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Url {

	public static String getURL(String _text){
		String regex = "\\b(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";

		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(_text);

		String g = null;

		while (m.find()) {
			g = m.group(0);
		}


		return g;
	}



}

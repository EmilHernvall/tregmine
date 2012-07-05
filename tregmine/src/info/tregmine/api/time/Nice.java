package info.tregmine.api.time;

public class Nice {


	public static String easytime(long oldTime, long newTime){
		long diffSeconds = (newTime - oldTime) / 1000;
		long diffMinutes = (newTime - oldTime) / 1000;
		long diffHouer = (newTime - oldTime) / 1000;
		long diffDays = (newTime - oldTime) / 1000;
		
		// Less then 59 seconds
		if (diffSeconds < 59) {
			return diffSeconds + " seconds ago";
		}

		// Less then 59 minutes
		if (diffSeconds < 60*59) {
			return diffSeconds + " minutes ago";
		}
		
		// Less then 59 minutes
		if (diffSeconds < 60*60*24) {
			return diffSeconds + " day ago";
		}

		if (diffSeconds < 60*60*25) {
			return diffSeconds + " day ago";
		}
		
		
		return null;
	}
}

package application;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class Utilities {
	
	
	
	public static String getCurrentISODate() {
		return ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ISO_DATE_TIME);
	}

	public static String getDateFromString(String date) {
		ZoneId id = ZoneId.systemDefault();
		String localTime = ZonedDateTime.parse(date).withZoneSameInstant(id).toString();
		String [] parti = localTime.split("T");
		return parti [0];
	}
	
	public static String getHourFromString(String date) {
		ZoneId id = ZoneId.systemDefault();
		String localTime = ZonedDateTime.parse(date).withZoneSameInstant(id).toString();
		String [] parti = localTime.split("T");
		return parti [1].split("\\.") [0];
	}
	
	public static String getHourFromStringTrimmed(String date) {
		String hour = getHourFromString(date);
		String [] split = hour.split(":");
		return split [0] + ":" + split [1];
	}

}

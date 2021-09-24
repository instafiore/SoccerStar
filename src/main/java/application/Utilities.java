package application;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Utilities {
	
	private String pathFont  = "/application/view/fonts/AzeretMono-Italic-VariableFont_wght.ttf" ;
	
	private static Utilities instance = null ;

	public static Utilities getInstance() {
		if(instance == null )
			instance = new Utilities() ;
		return instance;
	}
	
	public String getPathFont() {
		return pathFont;
	}
	
	private Utilities() {	}
	  
	
	public static final String RULEPASSWORDNOTRESPECTED = "Password isn't valid!" ;
	public static final String RULEEMAILNOTRESPECTED = "Email isn't valid!" ;
	public static final String RULEUSERNAMNOTRESPECTED = "Username isn't valid" ;
	public static final String RULESDIDTRESPECTED = "The validity rules are not respected";
	
	private static final String REGEXUSERNAME = "[a-zA-Z].*" ; 
	private static final String REGEXPASSWORD = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@\\.?#$_%^&+=!])(?=\\S+$).*"; 
	private static final String REGEXEMAIL = "((.+@gmail\\.com)|(.+@servizimicrosoft.unical.it))" ; 

	
    private static Pattern patternUsername = Pattern.compile(REGEXUSERNAME);
    private static Pattern patternPassword = Pattern.compile(REGEXPASSWORD);
    private static Pattern patternEmail = Pattern.compile(REGEXEMAIL);
	
    
    public static byte[] getByteArrayFromFile(File file) {
    	
    	if(file == null)
    		return null ;
    	
    	ByteArrayOutputStream bos = null ;
    	
    	try {
    		FileInputStream fis = new FileInputStream(file);
    		byte[] buffer = new byte[1024] ;
    		bos = new ByteArrayOutputStream() ;
    		for(int len ; (len = fis.read(buffer)) != -1; )
    			bos.write(buffer, 0, len);
    		fis.close();
    	}catch ( Exception e) {
			return null ;
		}
    	return bos != null ? bos.toByteArray() : null ;
    }
    
    
    public static Image getImageFromByteArray(byte[] bytes,double width , double height) {
    	
    	return new Image(new ByteArrayInputStream(bytes),width,height,true,true);
    	
    }
    		
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
	
	
    
    public static boolean rulesRespected(String username , String password , String email) {
    	return patternUsername.matcher(username).matches() && patternPassword.matcher(password).matches() && patternEmail.matcher(email).matches() ;
    }
    
    public static boolean ruleUsernameRespected(String username) {
    	if(username.length() < 6 && username.length() > 18)
    		return false ;
    	return patternUsername.matcher(username).matches();
    }
    
    public static boolean rulePasswordRespected(String password) {
    	
    	if(password.length()<6 || password.length()>20)
    		return false ;
    	
    	for(int i = 0 ; i < password.length() ; ++i)
    		if(password.charAt(i) == '&')
    			return false ;
    	
    	return patternPassword.matcher(password).matches();
    }
    
    public static boolean ruleEmailRespected(String email) {
    	return patternEmail.matcher(email).matches();
    }
    
    public static int getPriceField(String field) {
    	
    	if(field.equals(Settings.FIELD1))
    		return Settings.PRICEFIELD1;
    	else if(field.equals(Settings.FIELD2))
    		return Settings.PRICEFIELD2;
    	else if(field.equals(Settings.FIELD3))
    		return Settings.PRICEFIELD3;
    	return 0 ;
    }

    public static int getRewardField(String field) {
    	
    	if(field.equals(Settings.FIELD1))
    		return Settings.REWARDFIELD1;
    	else if(field.equals(Settings.FIELD2))
    		return Settings.REWARDFIELD2;
    	else if(field.equals(Settings.FIELD3))
    		return Settings.REWARDFIELD3;
    	return 0 ;
    }
    
}

package application.net.server;


import java.sql.SQLException;
import java.util.HashMap;
import java.util.Properties;
import java.util.Random;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;



public class Mail {

	private static final String SENDER = "soccerstartsf@gmail.com";
	private static final String PASSWORD = "L6wlpfe287752_";
	private static final String MESSAGESENT = "Message sent to -> ";
	private static Mail instance = null ;
	private static final int LENGTHCODE = 6 ;
	
	private HashMap<String, String> codes ;
	
	public static Mail getInstance() {
		if(instance == null)
			instance = new Mail();
		return instance;
	}
	
	private Mail() {
		codes = new HashMap<String,String>();
	}
	
	public void send(String receiver) {
		
		String subject = "Recovery password Soccer Star";
		String text ;
		Properties properties = new Properties();
		
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.starttls.enable", "true");
		properties.put("mail.smtp.host", "smtp.gmail.com");
		properties.put("mail.smtp.port", "587");
		
		Session session = Session.getInstance(properties, new Authenticator() {
			
			@Override
			protected  PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(SENDER, PASSWORD);
			}
		});
		
		String code = generateCode() ;
		
		text = "Hey "+receiver+"\n"
				+ "Code: "+ code + "\n"
				+ "This is the authentication code, entering this code you can reset your password!\n"
				+ "Best regards , Soccer Star.";
		
		String emailReceiver = "" ;
		try {
			emailReceiver = Database.getInstance().getAccount(receiver).getEmail() ;
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		Message message = prepareMessage(session,subject ,emailReceiver,text);
		
		try {
			Transport.send(message);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		
		codes.put(receiver, code);
		
		System.out.println("[MAIL] "+MESSAGESENT+receiver);
	}

	private Message prepareMessage(Session session,String subject ,String receiver,String text) {
		
		Message message = new MimeMessage(session);
		try {
			message.setFrom(new InternetAddress(SENDER));
			message.setRecipient(Message.RecipientType.TO, new InternetAddress(receiver));
			message.setSubject(subject);
			message.setText(text);
			return message ;
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	private String generateCode() {
		Random random = new Random() ;
		String code = "" ;
		for(int i = 0 ; i < LENGTHCODE ; ++i)
			code += ""+ random.nextInt(10);
		
		return code ;
	}
	
	public boolean checkCode(String username,String code) {
		return code.equals(codes.get(username));
	}
}

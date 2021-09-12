package application.net.server;


import java.util.Properties;

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
	
	public static Mail getInstance() {
		if(instance == null)
			instance = new Mail();
		return instance;
	}
	
	private Mail() {
		
	}
	
	public void send(String receiver ,String subject , String text) {
		
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
		
		
		Message message = prepareMessage(session,subject ,receiver,text);
		
		try {
			Transport.send(message);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
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
	
}

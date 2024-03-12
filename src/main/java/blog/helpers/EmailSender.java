package blog.helpers;

import java.util.Properties;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

public class EmailSender {
	
	
	private final String user="rishav3330";
	private final String password="qsltqpkbmpwvpjjv";
	
	public boolean sendEmail(String to,String from,String subject,String text) throws AddressException, MessagingException {
		boolean flag=false;
		
		Properties properties=new Properties();
		
		properties.put("mail.smtp.auth",true);
		properties.put("mail.smtp.starttls.enable",true);
		properties.put("mail.smtp.port","587");
		properties.put("mail.smtp.host","smtp.gmail.com");
		
		
	Session session=Session.getInstance(properties, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(user,password);
				
			}
		});
		
		
			Message message=new MimeMessage(session);
		
			message.setFrom(new InternetAddress(from));
			message.setRecipient(Message.RecipientType.TO,new InternetAddress(to));
			message.setSubject(subject);
			message.setText(text);
			Transport.send(message);
			flag=true;
			
		
		
		
		
		
		return flag;
	}

}

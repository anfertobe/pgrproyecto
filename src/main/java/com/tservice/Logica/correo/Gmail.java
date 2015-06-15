/**
 * 
 */
package com.tservice.Logica.correo;

import java.awt.AlphaComposite;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.JTextField;
import org.springframework.stereotype.Service;

/**
 * @author andres
 *
 */
@Service
public class Gmail implements Sender {
	
	private static final String SMTP_HOST_NAME = "smtp.gmail.com";
	private static final int SMTP_HOST_PORT = 465;

	@Override
	public boolean sender(String mensaje, String subject, String Destinatario) throws MessagingException {

		Properties props = new Properties();
		boolean conect = false;
		props.put("mail.transport.protocol", "smtps");
		props.put("mail.smtps.host", SMTP_HOST_NAME);
		props.put("mail.smtps.auth", "true");

		Session mailSession = Session.getDefaultInstance(props);
		mailSession.setDebug(true);
		Transport transport;
			transport = mailSession.getTransport();
			MimeMessage message = new MimeMessage(mailSession);
			message.setSubject(subject);
			message.setContent(mensaje, "text/plain");

			message.addRecipient(Message.RecipientType.TO,
					new InternetAddress(Destinatario));

			transport.connect(SMTP_HOST_NAME, SMTP_HOST_PORT,
					"tservicecosw2015@gmail.com", "t-service-cosw-20151");
			conect=transport.isConnected();
			transport.sendMessage(message,
					message.getRecipients(Message.RecipientType.TO));
			transport.close();
			return conect;
	}
		
}

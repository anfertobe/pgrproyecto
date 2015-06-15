/**
 * 
 */
package com.tservice.Logica.correo;

import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.swing.JTextField;

/**
 * @author andres
 *
 */
public interface Sender {


	public boolean sender(String mensaje, String recipients, String subject) throws NoSuchProviderException, MessagingException;
	
	
}

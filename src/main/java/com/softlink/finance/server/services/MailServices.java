/**
 * Copyright 2011 Google
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.softlink.finance.server.services;
/**
 * This class details about the process for sending mail.
 * 
 * @author
 */

import java.io.IOException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.softlink.finance.client.request.MailServicesRequest;
/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class MailServices extends RemoteServiceServlet 
 	implements MailServicesRequest{
	
	//mail id from which the mail has to be sent
	private static String fromAddress;
	/**
	   * Method defines the way to send a mail
	   * 
	   * @param fromAddress : mail id from which the mail has to be sent
	   * @param toAddress : the address to which mail needs to be sent
	   * @param subject : subject of the mail
	   * @param msgBody : mail content
	   * @throws IOException
	   */
	// Send the Mail
	@Override
	public void sendmail(String Address, String toAddress, String subject,
			String msgBody) throws IOException {
		
		fromAddress = Address;
		Properties props = new Properties();
	    Session session = Session.getDefaultInstance(props, null);
	    
	    try {
	    	Message msg = new MimeMessage(session);
	        msg.setFrom(new InternetAddress(fromAddress));
	        msg.addRecipient(Message.RecipientType.TO, new InternetAddress(toAddress));
	        msg.setSubject(subject);
	        msg.setText(msgBody);
	        Transport.send(msg);
	    } 
	    catch (AddressException addressException) {} 
	    catch (MessagingException messageException) {}
		
	}

}

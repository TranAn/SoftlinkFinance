package com.softlink.finance.services;

import java.io.IOException;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>MailServices</code>.
 */
public interface MailServicesAsync {
	void sendmail(String fromAddress, String toAddress, String subject, 
			String msgBody, AsyncCallback<Void> callback) throws IOException ;
}

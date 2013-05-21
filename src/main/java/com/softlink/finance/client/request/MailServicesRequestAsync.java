package com.softlink.finance.client.request;

import java.io.IOException;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>MailServicesRequest</code>.
 */
public interface MailServicesRequestAsync {
	void sendmail(String fromAddress, String toAddress, String subject, 
			String msgBody, AsyncCallback<Void> callback) throws IOException ;
}

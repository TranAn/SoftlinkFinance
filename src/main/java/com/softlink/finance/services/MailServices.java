package com.softlink.finance.services;

import java.io.IOException;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("mailservice")
public interface MailServices extends RemoteService {
	void sendmail(String fromAddress, String toAddress, 
			String subject, String msgBody) throws IOException ;
}
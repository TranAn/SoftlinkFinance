package com.softlink.financehome.client.request;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("userservice_rq")
public interface UserServicesRequest extends RemoteService {
	String getUserEmail();
	String setLogin(String callbackURL);
	String setLogout(String callbackURL);
	boolean isUserLoggedIn();
}

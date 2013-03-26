package com.softlink.financehome.services;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("userservice")
public interface UserServices extends RemoteService {
	String getUserName();
	String setLogin(String callbackURL);
	String setLogout(String callbackURL);
	boolean isUserLoggedIn();
}

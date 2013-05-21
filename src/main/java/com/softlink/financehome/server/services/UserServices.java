package com.softlink.financehome.server.services;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.softlink.financehome.client.request.UserServicesRequest;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class UserServices extends RemoteServiceServlet 
	implements UserServicesRequest{
	
	private final static UserService userService = 
			UserServiceFactory.getUserService();

	@Override
	public String getUserEmail() {
	    User user = userService.getCurrentUser();
	    return user.getEmail();
	}
	
	@Override
	public String setLogin(String callbackURL) {
		return userService.createLoginURL(callbackURL);
	}
	
	@Override
	public String setLogout(String callbackURL) {
		return userService.createLogoutURL(callbackURL);
	}

	@Override
	public boolean isUserLoggedIn() {
		return userService.isUserLoggedIn();
	}
}

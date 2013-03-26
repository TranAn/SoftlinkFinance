package com.softlink.financeuser.server;


import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.softlink.financeuser.services.UserServices;
/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class UserServicesImpl extends RemoteServiceServlet 
	implements UserServices {
	
	private final static UserService userService = 
			UserServiceFactory.getUserService();

	public String getUserName() {
	    User user = userService.getCurrentUser();
	    return user.getNickname();
	}

	public String setLogin(String callbackURL) {
		return userService.createLoginURL(callbackURL);
	}

	public String setLogout(String callbackURL) {
		return userService.createLogoutURL(callbackURL);
	}

	@Override
	public boolean isUserLoggedIn() {
		return userService.isUserLoggedIn();
	}
}

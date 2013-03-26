package com.softlink.finance.services;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>UserServices</code>.
 */
public interface UserServicesAsync {
	void getUserName(AsyncCallback<String> callback);
	void setLogin(String callbackURL, AsyncCallback<String> callback);
	void setLogout(String callbackURL,AsyncCallback<String> callback);
	void isUserLoggedIn(AsyncCallback<Boolean> callback);
}

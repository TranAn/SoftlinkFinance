package com.softlink.finance.client.request;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>UserServicesRequest</code>.
 */
public interface UserServicesRequestAsync {
	void getUserEmail(AsyncCallback<String> callback);
	void setLogin(String callbackURL, AsyncCallback<String> callback);
	void setLogout(String callbackURL,AsyncCallback<String> callback);
	void isUserLoggedIn(AsyncCallback<Boolean> callback);
}

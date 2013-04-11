package com.softlink.financeuser.datastore;


import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.softlink.financedatastore.client.SeriUser;

/**
 * <b>DataStore-Servlet (name: seriuserImpl)</b>
 * <p> manage user in <b>Module finance</b>
 * <p> - Entity Kind: <i><b>finance-user</b></i>
 */
public interface SeriUserObjAsync {
	
	
	/**
	 * Add a user(user have type SeriUser in Datastore)
	 * @param user element to be added
	 */
	void insert(SeriUser user, AsyncCallback<Void> callback);
	
	
	/**
	 * Delete a user (user have type SeriUser in Datastore)
	 * @param user element to be deleted
	 */
	void delete(SeriUser user, AsyncCallback<Void> callback);
	
	
	/**
	 * List all user in <b>Module finance</b> (user have type SeriUser in Datastore)
	 * <p>Return result is all user in <b>Module finance</b>
	 */
	void list_user(AsyncCallback<List<SeriUser>> callback);
	
	
	/**
	 * 
	 * Load user's data from memcache
	 * @return <b>true</b> if user have new data
	 * , <b>false</b> if contrary
	 */
	void loadUserFromMemcache(AsyncCallback<Boolean> callback);
	
	
	/**
	 * Change user's count to 0 when read memcache completed
	 */
	void onLoadComplete(AsyncCallback<Void> callback);
}

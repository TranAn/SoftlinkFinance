package com.softlink.financeuser.client.request;


import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.softlink.datastore.model.FinanceUser;

/**
 * <b>DataStore-Servlet (name: seriuserImpl)</b>
 * <p> manage user in <b>Module finance</b>
 * <p> - Entity Kind: <i><b>finance-user</b></i>
 */
public interface FinanceUserRequestAsync {
	
	
	/**
	 * Add a user(user have type FinanceUser in Datastore)
	 * @param user element to be added
	 */
	void insert(FinanceUser user, AsyncCallback<Void> callback);
	
	
	/**
	 * Delete a user (user have type FinanceUser in Datastore)
	 * @param user element to be deleted
	 */
	void delete(FinanceUser user, AsyncCallback<Void> callback);
	
	
	/**
	 * List all user in <b>Module finance</b> (user have type FinanceUser in Datastore)
	 * <p>Return result is all user in <b>Module finance</b>
	 */
	void list_user(AsyncCallback<List<FinanceUser>> callback);
	
}

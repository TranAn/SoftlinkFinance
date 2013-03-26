package com.softlink.financeuser.datastore;


import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.softlink.financedatastore.client.SeriUser;

/**
 * The async counterpart of <code>FinancialRequirements</code>.
 */
public interface SeriUserObjAsync {
	void insert(SeriUser user, AsyncCallback<Void> callback);
	void delete(SeriUser fr, AsyncCallback<Void> callback);
	void list_user(AsyncCallback<List<SeriUser>> callback);
	
}

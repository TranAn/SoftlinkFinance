package com.softlink.finance.datastore;


import java.util.Date;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.softlink.financedatastore.client.FinanceRequirements;
import com.softlink.financedatastore.client.SeriUser;


/**
 * The async counterpart of <code>FinanceRequirements</code>.
 */
public interface FinanceRequirementsObjAsync {
	void insert(FinanceRequirements fr, AsyncCallback<Void> callback);
	void delete(FinanceRequirements fr, AsyncCallback<Void> callback);
	void approveRequest(FinanceRequirements fr, String status, Date update_time, String comment, AsyncCallback<Void> callback);
	void list_fr(AsyncCallback<List<FinanceRequirements>> callback);
	void list_newfr(FinanceRequirements lastfr, AsyncCallback<List<FinanceRequirements>>callback);
	void list_draftfr(AsyncCallback<List<FinanceRequirements>> callback);
	void list_newdraftfr(FinanceRequirements lastfr, AsyncCallback<List<FinanceRequirements>> callback);
	void list_trashfr(AsyncCallback<List<FinanceRequirements>> callback);
	void list_newtrashfr(FinanceRequirements lastfr, AsyncCallback<List<FinanceRequirements>> callback);
	void find(Long req_id, AsyncCallback<List<FinanceRequirements>> callback);
	void checkUserAdminRole(AsyncCallback<Boolean> callback);
	void list_manager(AsyncCallback<List<SeriUser>> callback);
	void list_newRequestPerUser(AsyncCallback<List<FinanceRequirements>> callback);
	void updateUserLog(Date update_time, AsyncCallback<Void> callback);
}

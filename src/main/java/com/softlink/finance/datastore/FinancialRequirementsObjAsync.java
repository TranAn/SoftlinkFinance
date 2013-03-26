package com.softlink.finance.datastore;


import java.util.Date;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.softlink.financedatastore.client.FinancialRequirements;
import com.softlink.financedatastore.client.SeriUser;


/**
 * The async counterpart of <code>FinancialRequirements</code>.
 */
public interface FinancialRequirementsObjAsync {
	void insert(FinancialRequirements fr, AsyncCallback<Void> callback);
	void delete(FinancialRequirements fr, AsyncCallback<Void> callback);
	void approveRequest(FinancialRequirements fr, String status, Date update_time, String comment, AsyncCallback<Void> callback);
	void list_fr(AsyncCallback<List<FinancialRequirements>> callback);
	void list_newfr(FinancialRequirements lastfr, AsyncCallback<List<FinancialRequirements>>callback);
	void list_draftfr(AsyncCallback<List<FinancialRequirements>> callback);
	void list_newdraftfr(FinancialRequirements lastfr, AsyncCallback<List<FinancialRequirements>> callback);
	void list_trashfr(AsyncCallback<List<FinancialRequirements>> callback);
	void list_newtrashfr(FinancialRequirements lastfr, AsyncCallback<List<FinancialRequirements>> callback);
	void find(Long req_id, AsyncCallback<List<FinancialRequirements>> callback);
	void checkUserAdminRole(AsyncCallback<Boolean> callback);
	void list_manager(AsyncCallback<List<SeriUser>> callback);
	void list_newRequestPerUser(AsyncCallback<List<FinancialRequirements>> callback);
	void updateUserLog(Date update_time, AsyncCallback<Void> callback);
}

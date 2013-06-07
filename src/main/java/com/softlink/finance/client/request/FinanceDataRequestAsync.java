package com.softlink.finance.client.request;

import java.util.Date;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.softlink.datastore.model.FinanceData;
import com.softlink.datastore.model.FinanceUser;

/**
 * <b>Datastore-Servlet (name: financialrequirementsImpl)</b>
 * <p> manage data in <b>Module finance</b>
 * <p> - Entity Kind: <i><b>finance-data</b></i>
 * <p> - Entity Kind relate: <i><b>finance-user</b></i>
 */
public interface FinanceDataRequestAsync {
	
	
	/**
	 * Check current user is manager or not, user in<b>Module finance</b>
	 * <p>Return result = true if user is manager
	 * <p>Return result = false if contrary
	 */
	void isManagementUser(AsyncCallback<Boolean> callback);


	/**
	 * List all user-manager in <b>Module finance</b> (user have type FinanceUser in Datastore)
	 * <p>Return result is all user-manager in <b>Module finance</b>
	 */
	void list_manager(AsyncCallback<List<FinanceUser>> callback);


	/**
	 * Add a request (request have type FinanceData in Datastore)
	 * @param fr element to be added
	 */
	void insert(FinanceData data, AsyncCallback<Void> callback);
	
	
	/**
	 * Delete a request (request have type FinanceData in Datastore)
	 * @param fr element to be deleted
	 */
	void delete(FinanceData data, AsyncCallback<Void> callback);
	
	
	/**
	 * Update a request (request have type FinanceData in Datastore)
	 * @param fr element to be updated
	 * @param status new status
	 * @param update_time new update_time
	 * @param comment more comment
	 */
	void approveRequest(FinanceData data, String status, Date update_time, String comment, AsyncCallback<Void> callback);
	
	
	/**
	 * Find request by id (request have type FinanceData in Datastore)
	 * <p>Return result is request have id = req_id
	 * @param req_id id of request want to find
	 */
	void find(String dataID, AsyncCallback<FinanceData> callback);


	/**
	 * List all request (request have type FinanceData in Datastore)
	 * <p>Return result is all request have status <b>PENDING, APPROVED, DENIED</b> in Datastore
	 */
	void list_fr(AsyncCallback<List<FinanceData>> callback);
	
	
	/**
	 * List new request (request have type FinanceData in Datastore)
	 * <p>Return result is new update-requests have status <b>PENDING, APPROVED, DENIED</b> which client does not load yet
	 */
	void list_newfr(FinanceData lastfr, AsyncCallback<List<FinanceData>>callback);
	
	
	/**
	 * List all request (request have type FinanceData in Datastore)
	 * <p>Return result is all request have status <b>DRAFT</b> in Datastore
	 */
	void list_draftfr(AsyncCallback<List<FinanceData>> callback);
	
	
	/**
	 * List new request (request have type FinanceData in Datastore)
	 * <p>Return result is new update-requests have status <b>DRAFT</b> which client does not load yet
	 */
	void list_newdraftfr(FinanceData lastfr, AsyncCallback<List<FinanceData>> callback);
	
	
	/**
	 * List All request (request have type FinanceData in Datastore)
	 * <p>Return result is all request have status <b>DELETED</b> trong Datastore
	 */
	void list_trashfr(AsyncCallback<List<FinanceData>> callback);
	
	
	/**
	 * List new request (request have type FinanceData in Datastore)
	 * <p>Return result is new update-requests have status <b>DELETED</b> which client does not load yet
	 */
	void list_newtrashfr(FinanceData lastfr, AsyncCallback<List<FinanceData>> callback);
	
	/**
	 * Get A List of updateData which it's elements have id in updateDataList
	 * @param updateDataIDList
	 * @param callback
	 */
	void loadUpdateDataList(List<String> updateDataIDList, AsyncCallback<List<FinanceData>> callback);
	
	/**
	 * Load <b>Memcache</b>
	 * <p>Return description and path of updateData to DataStore
	 */
	void loadMemcache(AsyncCallback<List<String>> callback);
	
	/**
	 * Flush data in Memcache in current user
	 * @param EPath
	 * @param callback
	 */
	void flushData(String EPath, AsyncCallback<Void> callback);
	
	/**
	 * Get current user login
	 * @param callback
	 */
	void getCurrentUserName(AsyncCallback<String> callback);
	
}

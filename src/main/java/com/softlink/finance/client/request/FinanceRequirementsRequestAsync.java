package com.softlink.finance.client.request;

import java.util.Date;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.softlink.finance.shared.FinanceRequirements;
import com.softlink.finance.shared.SeriUser;

/**
 * <b>Datastore-Servlet (name: financialrequirementsImpl)</b>
 * <p> manage data in <b>Module finance</b>
 * <p> - Entity Kind: <i><b>finance-data</b></i>
 * <p> - Entity Kind relate: <i><b>finance-user</b></i>
 */
public interface FinanceRequirementsRequestAsync {
	
	
	/**
	 * Add a request (request have type FinanceRequirements in Datastore)
	 * @param fr element to be added
	 */
	void insert(FinanceRequirements fr, AsyncCallback<Void> callback);
	
	
	/**
	 * Delete a request (request have type FinanceRequirements in Datastore)
	 * @param fr element to be deleted
	 */
	void delete(FinanceRequirements fr, AsyncCallback<Void> callback);
	
	
	/**
	 * Update a request (request have type FinanceRequirements in Datastore)
	 * @param fr element to be updated
	 * @param status new status
	 * @param update_time new update_time
	 * @param comment more comment
	 */
	void approveRequest(FinanceRequirements fr, String status, Date update_time, String comment, AsyncCallback<Void> callback);
	
	
	/**
	 * List all request (request have type FinanceRequirements in Datastore)
	 * <p>Return result is all request have status <b>PENDING, APPROVED, DENIED</b> in Datastore
	 */
	void list_fr(AsyncCallback<List<FinanceRequirements>> callback);
	
	
	/**
	 * List new request (request have type FinanceRequirements in Datastore)
	 * <p>Return result is new update-requests have status <b>PENDING, APPROVED, DENIED</b> which client does not load yet
	 */
	void list_newfr(FinanceRequirements lastfr, AsyncCallback<List<FinanceRequirements>>callback);
	
	
	/**
	 * List all request (request have type FinanceRequirements in Datastore)
	 * <p>Return result is all request have status <b>DRAFT</b> in Datastore
	 */
	void list_draftfr(AsyncCallback<List<FinanceRequirements>> callback);
	
	
	/**
	 * List new request (request have type FinanceRequirements in Datastore)
	 * <p>Return result is new update-requests have status <b>DRAFT</b> which client does not load yet
	 */
	void list_newdraftfr(FinanceRequirements lastfr, AsyncCallback<List<FinanceRequirements>> callback);
	
	
	/**
	 * List All request (request have type FinanceRequirements in Datastore)
	 * <p>Return result is all request have status <b>DELETED</b> trong Datastore
	 */
	void list_trashfr(AsyncCallback<List<FinanceRequirements>> callback);
	
	
	/**
	 * List new request (request have type FinanceRequirements in Datastore)
	 * <p>Return result is new update-requests have status <b>DELETED</b> which client does not load yet
	 */
	void list_newtrashfr(FinanceRequirements lastfr, AsyncCallback<List<FinanceRequirements>> callback);
	
	
	/**
	 * Find request by id (request have type FinanceRequirements in Datastore)
	 * <p>Return result is request have id = req_id
	 * @param req_id id of request want to find
	 */
	void find(String req_id, AsyncCallback<FinanceRequirements> callback);
	
	
	/**
	 * Check user signin is admin or not, user in<b>Module finance</b>
	 * <p>Return result = true if user is admin
	 * <p>Return result = false if contrary
	 */
	void checkUserAdminRole(AsyncCallback<Boolean> callback);
	
	
	/**
	 * List all user-manager in <b>Module finance</b> (user have type SeriUser in Datastore)
	 * <p>Return result is all user-manager in <b>Module finance</b>
	 */
	void list_manager(AsyncCallback<List<SeriUser>> callback);
	
	
	/**
	 * List all request belong to specify user in <b>Module finance</b> (request have type FinanceRequirements, user have type SeriUser in Datastore)
	 * <p>Return result is all request if user have role <b>manager</b> in <b>Module finance</b>
	 * <p>Return result is requests belong to each user if user have role <b>user</b> in <b>Module finance</b>
	 */
	void list_newRequestPerUser(AsyncCallback<List<FinanceRequirements>> callback);
	
	
	/**
	 * Update time-work(userlog) for user
	 * @param update_time new userlog
	 */
	void updateUserLog(Date update_time, AsyncCallback<Void> callback);
}

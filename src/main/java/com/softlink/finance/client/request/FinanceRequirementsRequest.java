package com.softlink.finance.client.request;

import java.util.Date;
import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.softlink.finance.shared.FinanceRequirements;
import com.softlink.finance.shared.SeriUser;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("financerequirements_rq")
public interface FinanceRequirementsRequest extends RemoteService {
	void insert(FinanceRequirements fr);
	void delete(FinanceRequirements fr);
	void approveRequest(FinanceRequirements fr, String status, Date update_time, String comment);
	List<FinanceRequirements> list_fr();
	List<FinanceRequirements> list_newfr(FinanceRequirements lastfr);
	List<FinanceRequirements> list_draftfr();
	List<FinanceRequirements> list_newdraftfr(FinanceRequirements lastfr);
	List<FinanceRequirements> list_trashfr();
	List<FinanceRequirements> list_newtrashfr(FinanceRequirements lastfr);
	FinanceRequirements find(String req_id);
	boolean checkUserAdminRole(); 
	List<SeriUser> list_manager();
	List<FinanceRequirements> list_newRequestPerUser();
	void updateUserLog(Date update_time);
}
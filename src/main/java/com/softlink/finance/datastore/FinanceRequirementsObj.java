package com.softlink.finance.datastore;


import java.util.Date;
import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.softlink.financedatastore.client.FinanceRequirements;
import com.softlink.financedatastore.client.SeriUser;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("financialrequirementsImpl")
public interface FinanceRequirementsObj extends RemoteService {
	void insert(FinanceRequirements fr);
	void delete(FinanceRequirements fr);
	void approveRequest(FinanceRequirements fr, String status, Date update_time, String comment);
	List<FinanceRequirements> list_fr();
	List<FinanceRequirements> list_newfr(FinanceRequirements lastfr);
	List<FinanceRequirements> list_draftfr();
	List<FinanceRequirements> list_newdraftfr(FinanceRequirements lastfr);
	List<FinanceRequirements> list_trashfr();
	List<FinanceRequirements> list_newtrashfr(FinanceRequirements lastfr);
	List<FinanceRequirements> find(Long req_id);
	boolean checkUserAdminRole(); 
	List<SeriUser> list_manager();
	List<FinanceRequirements> list_newRequestPerUser();
	void updateUserLog(Date update_time);
}
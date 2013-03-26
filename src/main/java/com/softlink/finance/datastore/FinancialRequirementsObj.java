package com.softlink.finance.datastore;


import java.util.Date;
import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.softlink.financedatastore.client.FinancialRequirements;
import com.softlink.financedatastore.client.SeriUser;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("financialrequirementsImpl")
public interface FinancialRequirementsObj extends RemoteService {
	void insert(FinancialRequirements fr);
	void delete(FinancialRequirements fr);
	void approveRequest(FinancialRequirements fr, String status, Date update_time, String comment);
	List<FinancialRequirements> list_fr();
	List<FinancialRequirements> list_newfr(FinancialRequirements lastfr);
	List<FinancialRequirements> list_draftfr();
	List<FinancialRequirements> list_newdraftfr(FinancialRequirements lastfr);
	List<FinancialRequirements> list_trashfr();
	List<FinancialRequirements> list_newtrashfr(FinancialRequirements lastfr);
	List<FinancialRequirements> find(Long req_id);
	boolean checkUserAdminRole(); 
	List<SeriUser> list_manager();
	List<FinancialRequirements> list_newRequestPerUser();
	void updateUserLog(Date update_time);
}
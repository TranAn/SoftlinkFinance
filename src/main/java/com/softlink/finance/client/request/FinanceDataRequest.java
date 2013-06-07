package com.softlink.finance.client.request;

import java.util.Date;
import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.softlink.datastore.model.FinanceData;
import com.softlink.datastore.model.FinanceUser;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("financerequirements_rq")
public interface FinanceDataRequest extends RemoteService {
	//Basic Operation
	boolean isManagementUser(); 
	void insert(FinanceData data);
	void delete(FinanceData data);
	void approveRequest(FinanceData data, String status, Date update_time, String comment);
	FinanceData find(String dataID);
	
	//Query Operation
	String getCurrentUserName();
	List<FinanceUser> list_manager();
	List<FinanceData> list_fr();
	List<FinanceData> list_newfr(FinanceData lastfr);
	List<FinanceData> list_draftfr();
	List<FinanceData> list_newdraftfr(FinanceData lastfr);
	List<FinanceData> list_trashfr();
	List<FinanceData> list_newtrashfr(FinanceData lastfr);
	
	//Notify Operation
	List<String> loadMemcache();
	List<FinanceData> loadUpdateDataList(List<String> updateDataIDList);
	void flushData(String EPath);
}
package com.softlink.financeuser.client.request;


import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.softlink.datastore.model.FinanceUser;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("seriuser_rq")
public interface FinanceUserRequest extends RemoteService{
	void insert(FinanceUser user);
	void delete(FinanceUser user);
	List<FinanceUser> list_user();
}

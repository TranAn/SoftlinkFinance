package com.softlink.financeuser.client.request;


import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.softlink.financeuser.shared.SeriUser;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("seriuser_rq")
public interface SeriUserRequest extends RemoteService{
	void insert(SeriUser user);
	void delete(SeriUser user);
	List<SeriUser> list_user();
	boolean loadUserFromMemcache();
	void onLoadComplete();
}

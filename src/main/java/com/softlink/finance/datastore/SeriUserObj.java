package com.softlink.finance.datastore;


import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.softlink.financedatastore.client.SeriUser;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("seriuserImpl")
public interface SeriUserObj extends RemoteService{
	void insert(SeriUser user);
	void delete(SeriUser user);
	List<SeriUser> list_user();
	boolean loadUserFromMemcache();
	void onLoadComplete();
}

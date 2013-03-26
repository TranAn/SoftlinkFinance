package com.softlink.financeuser.server;

import java.util.List;


import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Query;
import com.softlink.financedatastore.client.SeriUser;
import com.softlink.financeuser.datastore.SeriUserObj;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class SeriUserObjImpl extends RemoteServiceServlet implements
	SeriUserObj{

	public void insert(SeriUser user) {
		Objectify ofy = ObjectifyService.begin();
		ofy.put(user);
		assert user.userid != null;
	}

	public void delete(SeriUser user) {
		Objectify ofy = ObjectifyService.begin();
		ofy.delete(user);
	}

	public List<SeriUser> list_user() {
		Objectify ofy = ObjectifyService.begin();
		Query<SeriUser> q = ofy.query(SeriUser.class);
		return q.list();
	}

}

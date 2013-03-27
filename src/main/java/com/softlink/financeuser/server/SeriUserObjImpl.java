package com.softlink.financeuser.server;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import static com.googlecode.objectify.ObjectifyService.ofy;

import com.softlink.financedatastore.client.SeriUser;
import com.softlink.financeuser.datastore.SeriUserObj;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class SeriUserObjImpl extends RemoteServiceServlet implements
	SeriUserObj{

	public void insert(SeriUser user) {
		ofy().save().entity(user);
		assert user.userid != null;
	}

	public void delete(SeriUser user) {
		ofy().delete().entity(user);
	}

	public List<SeriUser> list_user() {
		List<SeriUser> q = ofy().load().type(SeriUser.class).list();
		List<SeriUser> l = new ArrayList<SeriUser>();
		l.addAll(q);
		return l;
	}

}

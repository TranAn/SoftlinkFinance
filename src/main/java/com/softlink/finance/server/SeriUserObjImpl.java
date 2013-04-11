package com.softlink.finance.server;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.softlink.financedatastore.client.SeriUser;
import com.softlink.finance.datastore.SeriUserObj;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class SeriUserObjImpl extends RemoteServiceServlet implements
	SeriUserObj{
	
	private static final Logger log = Logger.getLogger(SeriUserObjImpl.class.getName());
	
	private static MemcacheService memcache = MemcacheServiceFactory.getMemcacheService();
	
	private final static UserService userService = 
			UserServiceFactory.getUserService();

	public void insert(SeriUser user) {
		ofy().save().entity(user);
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

	public boolean loadUserFromMemcache() {
		String username = userService.getCurrentUser().getNickname();
			if(username.contains("@")==false)
				username = username+"@gmail.com";
		if(memcache.contains(username)) {
			log.info("Get Key: "+username+" in memcache");
			SeriUser user = (SeriUser)memcache.get(username);
			if(user==null)
				return false;
			if(user.getCount()!=0)
				return true;
			else
				return false;	
		}
		log.info("Key: "+username+" not exist in memcache");
		return false;
	}

	public void onLoadComplete() {
		String username = userService.getCurrentUser().getNickname();
		if(username.contains("@")==false)
			username = username+"@gmail.com";
		SeriUser user = ofy().load().type(SeriUser.class).id(username).get();
		if(user!=null) {
			user.setCount(0);
			ofy().save().entity(user);
			memcache.delete(username);
			log.info("Key: "+username+" has been flushed");
		}
	}

}

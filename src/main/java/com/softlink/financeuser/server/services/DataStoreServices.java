package com.softlink.financeuser.server.services;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.googlecode.objectify.ObjectifyService;
import com.softlink.financeuser.client.request.SeriUserRequest;
import com.softlink.financeuser.shared.SeriUser;

@SuppressWarnings("serial")
public class DataStoreServices extends RemoteServiceServlet
	implements SeriUserRequest {
	
	public DataStoreServices() {
		ObjectifyService.register(SeriUser.class);
	}
	
	private static final Logger log = Logger
			.getLogger(DataStoreServices.class.getName());
	private static MemcacheService memcache = MemcacheServiceFactory
			.getMemcacheService();
	private final static UserService userService = 
			UserServiceFactory.getUserService();
	
	//Implement SeriUserRequest Interface------------------------------
	@Override
	public void insert(SeriUser user) {
		ofy().save().entity(user);
	}

	@Override
	public void delete(SeriUser user) {
		ofy().delete().entity(user);
	}
	
	@Override
	public List<SeriUser> list_user() {
		List<SeriUser> q = ofy().load().type(SeriUser.class).list();
		List<SeriUser> l = new ArrayList<SeriUser>();
		l.addAll(q);
		return l;
	}

	@Override
	public boolean loadUserFromMemcache() {
		String username = userService.getCurrentUser().getEmail();
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
//		log.info("Key: "+username+" not exist in memcache");
		return false;
	}

	@Override
	public void onLoadComplete() {
		String username = userService.getCurrentUser().getEmail();
		SeriUser user = ofy().load().type(SeriUser.class).id(username).get();
		if(user!=null) {
			user.setCount(0);
			ofy().save().entity(user);
			memcache.delete(username);
			log.info("Key: "+username+" has been flushed");
		}
	}
}
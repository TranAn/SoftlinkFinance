package com.softlink.financeuser.server.services;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.softlink.datastore.model.MemcacheData;
import com.softlink.datastore.model.FinanceUser;
import com.softlink.financeuser.client.request.FinanceUserRequest;

@SuppressWarnings("serial")
public class DataStoreServices extends RemoteServiceServlet
	implements FinanceUserRequest {
	
//	public DataStoreServices() {
//		ObjectifyService.register(FinanceUser.class);
//		ObjectifyService.register(MemcacheData.class);
//	}
	
	private static final Logger log = Logger.getLogger(DataStoreServices.class.getName());
	
	//Implement FinanceUserRequest Interface------------------------------
	@Override
	public void insert(FinanceUser user) {
		ofy().save().entity(user);
		
		//Build Memcached
		MemcacheData memcached_data = new MemcacheData();
		memcached_data.setUsername(user.getUsername());
		List<String> cachedlist = new ArrayList<String>();
		memcached_data.setCachedlist(cachedlist);
		ofy().save().entity(memcached_data);
		log.info(user.getUsername()+ " has been cached");
	}

	@Override
	public void delete(FinanceUser user) {
		ofy().delete().entity(user);
		
		//Destroy Memcached
		MemcacheData memcached_data = ofy().load().type(MemcacheData.class)
				.id(user.getUsername()).get();
		if(memcached_data!=null) {
			ofy().delete().entity(memcached_data);
			log.info(user.getUsername()+ " has been flush");
		}
	}
	
	@Override
	public List<FinanceUser> list_user() {
		List<FinanceUser> q = ofy().load().type(FinanceUser.class).list();
		List<FinanceUser> l = new ArrayList<FinanceUser>();
		l.addAll(q);
		return l;
	}
	
}
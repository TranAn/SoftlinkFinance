package com.softlink.finance.server.services;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.softlink.finance.client.request.FinanceRequirementsRequest;
import com.softlink.finance.client.request.SeriUserRequest;
import com.softlink.finance.shared.FinanceRequirements;
import com.softlink.finance.shared.SeriUser;

@SuppressWarnings("serial")
public class DataStoreServices extends RemoteServiceServlet implements
FinanceRequirementsRequest, SeriUserRequest {
	
	private static final Logger log = Logger
			.getLogger(DataStoreServices.class.getName());
	private static MemcacheService memcache = MemcacheServiceFactory
			.getMemcacheService();
	private final static UserService userService = 
			UserServiceFactory.getUserService();
	
	//Procedure-------------------------------------------------------------------
	private String getUserName() {
		return userService.getCurrentUser().getEmail();
	}
	
	private SeriUser findUser() {
		List<SeriUser> q = ofy().load().type(SeriUser.class)
				.filter("username", getUserName())
				.list();
		List<SeriUser> l = new ArrayList<SeriUser>();
		l.addAll(q);
		if(l.isEmpty())
			return null;
		else
			return l.get(0);
	}
	
	//Implement FinanceRequirementsRequest Interface------------------------------
	@Override
	public void insert(FinanceRequirements fr) {
		ofy().save().entity(fr);
		if(fr.getStatus().equals("DRAFT")==false) {
			List<SeriUser> list_user = list_manager();
			for(SeriUser user:list_user) {
				user.setCount(user.getCount()+1);
				ofy().save().entity(user);
				memcache.put(user.getUsername(), user);
				
				log.info("Key: "+user.getUsername()+" has been put into memcache");
			}
		}
	}
	
	@Override
	public void delete(FinanceRequirements fr) {
		ofy().delete().entity(fr);
	}

	@Override
	public void approveRequest(FinanceRequirements fr, String status, Date update_time, String comment) {
		if(checkUserAdminRole()||fr.getStatus().equals("DRAFT")) {
			fr.setStatus(status);
			fr.setUpdate_time(update_time);
			if((status.equals("APPROVED")||status.equals("DENIED"))&&fr.getReq_time()==null)
				fr.setReq_time(update_time);
			if(fr.getComment().isEmpty())
				fr.setComment(comment);
			else
				fr.setComment(fr.getComment()+comment);
			ofy().save().entity(fr);
			SeriUser reporter = ofy().load().type(SeriUser.class)
					.filter("username", fr.getReporter()).first().get();
			List<SeriUser> list_user = list_manager();
			list_user.add(reporter);
			for(SeriUser user:list_user) {
				user.setCount(user.getCount()+1);
				ofy().save().entity(user);
				memcache.put(user.getUsername(), user);
				
				log.info("Key: "+user.getUsername()+" has been put into memcache");
			}
		}
	}

	@Override
	public FinanceRequirements find(String req_id) {
		Long id = Long.valueOf(req_id);
		FinanceRequirements q = ofy().load().type(FinanceRequirements.class).id(id).get();
		return q;
	}

	@Override
	public List<FinanceRequirements> list_fr() {
		List<String> kindfr = new ArrayList<String>();
		kindfr.add("PENDING");
		kindfr.add("APPROVED");
		kindfr.add("DENIED");
		if(checkUserAdminRole()) {
			List<FinanceRequirements> q = ofy().load().type(FinanceRequirements.class)
					.filter("status in", kindfr)
					.order("update_time")
					.list();
			List<FinanceRequirements> l = new ArrayList<FinanceRequirements>();
			l.addAll(q);
			return l;
		} else {
			List<FinanceRequirements> q = ofy().load().type(FinanceRequirements.class)
					.filter("reporter", getUserName())
					.filter("status in", kindfr)
					.order("update_time")
					.list();
			List<FinanceRequirements> l = new ArrayList<FinanceRequirements>();
			l.addAll(q);
			return l;
		}
	}

	@Override
	public List<FinanceRequirements> list_newfr(FinanceRequirements lastfr) {
		if(checkUserAdminRole()) {
			List<FinanceRequirements> q = ofy().load().type(FinanceRequirements.class)
					.filter("update_time >", lastfr.getUpdate_time())
					.list();
			List<FinanceRequirements> l = new ArrayList<FinanceRequirements>();
			l.addAll(q);
			return l;
		} else {
			List<FinanceRequirements> q = ofy().load().type(FinanceRequirements.class)
					.filter("reporter", getUserName())
					.filter("update_time >", lastfr.getUpdate_time())
					.list();
			List<FinanceRequirements> l = new ArrayList<FinanceRequirements>();
			l.addAll(q);
			return l;
		}
	}

	@Override
	public List<FinanceRequirements> list_draftfr() {
		List<FinanceRequirements> q = ofy().load().type(FinanceRequirements.class)
				.filter("status", "DRAFT")
				.filter("reporter", getUserName())
				.order("update_time")
				.list();
		List<FinanceRequirements> l = new ArrayList<FinanceRequirements>();
		l.addAll(q);
		return l;
	}

	@Override
	public List<FinanceRequirements> list_newdraftfr(
			FinanceRequirements lastfr) {
		List<FinanceRequirements> q = ofy().load().type(FinanceRequirements.class)
				.filter("status", "DRAFT")
				.filter("reporter", getUserName())
				.filter("update_time >", lastfr.getUpdate_time())
				.list();
		List<FinanceRequirements> l = new ArrayList<FinanceRequirements>();
		l.addAll(q);
		return l;
	}

	@Override
	public List<FinanceRequirements> list_trashfr() {
		if(checkUserAdminRole()) {
			List<FinanceRequirements> q = ofy().load().type(FinanceRequirements.class)
					.filter("status", "DELETED")
					.order("update_time")
					.list();
			List<FinanceRequirements> l = new ArrayList<FinanceRequirements>();
			l.addAll(q);
			return l;
		} else {
			List<FinanceRequirements> q = ofy().load().type(FinanceRequirements.class)
					.filter("reporter", getUserName())
					.filter("status", "DELETED")
					.order("update_time")
					.list();
			List<FinanceRequirements> l = new ArrayList<FinanceRequirements>();
			l.addAll(q);
			return l;
		}
	}

	@Override
	public List<FinanceRequirements> list_newtrashfr(
			FinanceRequirements lastfr) {
		if(checkUserAdminRole()) {
			List<FinanceRequirements> q = ofy().load().type(FinanceRequirements.class)
					.filter("status", "DELETED")
					.filter("update_time >", lastfr.getUpdate_time())
					.list();
			List<FinanceRequirements> l = new ArrayList<FinanceRequirements>();
			l.addAll(q);
			return l;
		} else {
			List<FinanceRequirements> q = ofy().load().type(FinanceRequirements.class)
					.filter("reporter", getUserName())
					.filter("status", "DELETED")
					.filter("update_time >", lastfr.getUpdate_time())
					.list();
			List<FinanceRequirements> l = new ArrayList<FinanceRequirements>();
			l.addAll(q);
			return l;
		}
	}

	@Override
	public List<FinanceRequirements> list_newRequestPerUser() {
		List<String> kindfr = new ArrayList<String>();
		kindfr.add("PENDING");
		kindfr.add("APPROVED");
		kindfr.add("DENIED");
		List<SeriUser> q = ofy().load().type(SeriUser.class)
				.filter("username", getUserName())
				.list();
		List<SeriUser> l = new ArrayList<SeriUser>();
		l.addAll(q);
		if(l.isEmpty())
			return null;
		else {
			if(checkUserAdminRole()) {
				List<FinanceRequirements> q1 = ofy().load().type(FinanceRequirements.class)
						.filter("status in", kindfr)
						.filter("update_time >", q.get(0).getTimework())
						.order("update_time")
						.list();
				List<FinanceRequirements> l1 = new ArrayList<FinanceRequirements>();
				l1.addAll(q1);
				List<FinanceRequirements> q2 = ofy().load().type(FinanceRequirements.class)
						.filter("reporter", getUserName())
						.filter("status", "DRAFT")
						.filter("update_time >", q.get(0).getTimework())
						.order("update_time")
						.list();
				List<FinanceRequirements> l2 = new ArrayList<FinanceRequirements>();
				l2.addAll(q2);
				List<FinanceRequirements> l3 = new ArrayList<FinanceRequirements>();
				if(l1.isEmpty()==false&&l2.isEmpty()==false) {
					if(l1.get(0).getUpdate_time().after(l2.get(0).getUpdate_time())){
						for(FinanceRequirements fr: l2)
							l3.add(fr);
						for(FinanceRequirements fr: l1)
							l3.add(fr);
					} else {
						for(FinanceRequirements fr: l1)
							l3.add(fr);
						for(FinanceRequirements fr: l2)
							l3.add(fr);
					}
				}
				if(l2.isEmpty())
					for(FinanceRequirements fr: l1)
						l3.add(fr);
				if(l1.isEmpty())
					for(FinanceRequirements fr: l2)
						l3.add(fr);
				return l3;
			} else {
				kindfr.add("DRAFT");
				List<FinanceRequirements> q1 = ofy().load().type(FinanceRequirements.class)
						.filter("status in", kindfr)
						.filter("reporter", getUserName())
						.filter("update_time >", q.get(0).getTimework())
						.order("update_time")
						.list();
				List<FinanceRequirements> l3 = new ArrayList<FinanceRequirements>();
				l3.addAll(q1);
				return l3;
			}
		}
	}

	@Override
	public boolean checkUserAdminRole() {
		List<SeriUser> q = ofy().load().type(SeriUser.class)
				.filter("username", getUserName())
				.list();
		List<SeriUser> l = new ArrayList<SeriUser>();
		l.addAll(q);
		if(l.isEmpty())
			return false;
		else {
			if(l.get(0).getRole().equals("Officer"))
				return true;
			else
				return false;
		}
	}
	
	@Override
	public List<SeriUser> list_manager() {
		List<SeriUser> q = ofy().load().type(SeriUser.class)
				.filter("role", "Officer")
				.list();
		List<SeriUser> l = new ArrayList<SeriUser>();
		l.addAll(q);
		return l;
	}
	
	@Override
	public void updateUserLog(Date update_time) {
		SeriUser user = findUser();
		user.setTimework(update_time);
		ofy().save().entity(user);
	}
	
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
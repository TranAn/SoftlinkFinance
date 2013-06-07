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
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Result;
import com.softlink.datastore.model.MemcacheData;
import com.softlink.datastore.model.FinanceData;
import com.softlink.datastore.model.FinanceUser;
import com.softlink.finance.client.request.FinanceDataRequest;

@SuppressWarnings("serial")
public class DataStoreServices extends RemoteServiceServlet implements
		FinanceDataRequest {

	private static final Logger log = Logger.getLogger(DataStoreServices.class
			.getName());
	private static MemcacheService memcached = MemcacheServiceFactory
			.getMemcacheService();
	private final static UserService userService = UserServiceFactory
			.getUserService();

	/*
	 * ---Procedure---
	 */
	private void cachedData(List<FinanceUser> user, FinanceData data) {
		String delimiter = "/";
		String sender = getCurrentUserName();
		String identifyEPath = "1"+ delimiter + data.getRequest_id();
		String newEPath = "1"+ delimiter + data.getRequest_id() + delimiter + data.getVersion()
				+ delimiter + sender + delimiter + data.getDescription();
		for (FinanceUser u : user) {
			MemcacheData memcached_data = ofy().load().type(MemcacheData.class)
					.id(u.getUsername()).get();
			if (memcached_data != null) {
				List<String> cachedlist = memcached_data.getCachedlist();
				if(cachedlist == null)
					cachedlist = new ArrayList<String>();
				
				for (int i = 0; i < cachedlist.size(); i++) {
					String epath = cachedlist.get(i);
					String[] epaths = epath.split(delimiter);
					String classname = epaths[0];
					String id = epaths[1];
					String childEpath = classname + delimiter + id;
					if (childEpath.equals(identifyEPath))
						cachedlist.remove(i);
				}

				cachedlist.add(newEPath);
				memcached_data.setCachedlist(cachedlist);

				ofy().save().entity(memcached_data);
				memcached.put(memcached_data.getUsername(),
						memcached_data.getCachedlist());
			}
		}
		log.info("Element-Path: " + newEPath + " has been cached");
	}

	@Override
	public void flushData(String identifyEPath) {
		String delimiter = "/";
		MemcacheData memcached_data = ofy().load().type(MemcacheData.class)
				.id(getCurrentUserName()).get();
		if (memcached_data != null) {
			List<String> cachedlist = memcached_data.getCachedlist();
			if (cachedlist != null) {
				for (int i = 0; i < cachedlist.size(); i++) {
					String epath = cachedlist.get(i);
					String[] epaths = epath.split(delimiter);
					String classname = epaths[0];
					String id = epaths[1];
					String childEpath = classname + delimiter + id;
					if (childEpath.equals(identifyEPath))
						cachedlist.remove(i);
				}
			}
			
			memcached_data.setCachedlist(cachedlist);
			ofy().save().entity(memcached_data);
			memcached.put(memcached_data.getUsername(),
					memcached_data.getCachedlist());
		}

		log.info("Element-Path: " + identifyEPath + " has been flush from "
				+ getCurrentUserName());
	}

	private FinanceUser findUser(String username) {
		FinanceUser q = ofy().load().type(FinanceUser.class).id(username).get();
		return q;
	}

	/*
	 * ---Constructor---
	 */
	// public DataStoreServices() {
	// ObjectifyService.register(FinanceData.class);
	// ObjectifyService.register(FinanceUser.class);
	// ObjectifyService.register(MemcacheData.class);
	// }

	/*
	 * ---Implement FinanceDataRequest Interface---
	 */
	@Override
	public boolean isManagementUser() {
		FinanceUser q = ofy().load().type(FinanceUser.class)
				.id(getCurrentUserName()).get();
		if (q == null)
			return false;
		else {
			if (q.getRole().equals("Officer"))
				return true;
			else
				return false;
		}
	}

	@Override
	public String getCurrentUserName() {
		return userService.getCurrentUser().getEmail();
	}

	@Override
	public List<FinanceUser> list_manager() {
		List<FinanceUser> q = ofy().load().type(FinanceUser.class)
				.filter("role", "Officer").list();
		List<FinanceUser> l = new ArrayList<FinanceUser>();
		l.addAll(q);
		return l;
	}

	@Override
	public void insert(FinanceData fr) {
		Result<Key<FinanceData>> result = ofy().save().entity(fr);
		Key<FinanceData> resultKey = result.now();

		FinanceData data = ofy().load().key(resultKey).get();
		if (fr.getStatus().equals("DRAFT") == false) {
			List<FinanceUser> user = list_manager();
			if (!user.contains(findUser(data.getReporter())))
				user.add(findUser(data.getReporter()));
			cachedData(user, data);
		} else {
			List<FinanceUser> user = new ArrayList<FinanceUser>();
			user.add(findUser(data.getReporter()));
			cachedData(user, data);
		}
	}

	// This function will remove request permanent.
	@Override
	public void delete(FinanceData fr) {
		ofy().delete().entity(fr);

		// FinanceData data = fr;
		// data.setVersion(data.getVersion()+1);
		// data.setDescription("DELETED");
		// List<FinanceUser> user = list_manager();
		// if(!user.contains(findUser(data.getReporter())))
		// user.add(findUser(data.getReporter()));
		// cachedData(user,data);
	}

	// This function will update request to APPROVED or DENIED or DRAFT or
	// DELETED.
	@Override
	public void approveRequest(FinanceData fr, String status, Date update_time,
			String comment) {
		if (isManagementUser() || fr.getStatus().equals("DRAFT")) {
			fr.setStatus(status);
			fr.setUpdate_time(update_time);
			if ((status.equals("APPROVED") || status.equals("DENIED"))
					&& fr.getReq_time() == null)
				fr.setReq_time(update_time);
			if (fr.getComment().isEmpty())
				fr.setComment(comment);
			else
				fr.setComment(fr.getComment() + comment);
			fr.setVersion(fr.getVersion() + 1);
			ofy().save().entity(fr);

			FinanceData data = fr;
			List<FinanceUser> user = list_manager();
			if (!user.contains(findUser(data.getReporter())))
				user.add(findUser(data.getReporter()));
			cachedData(user, data);
		}
	}

	@Override
	public FinanceData find(String data_id) {
		Long id;
		try {
			id = Long.valueOf(data_id);
			FinanceData q = ofy().load().type(FinanceData.class).id(id).get();
			return q;
		} catch (Exception e) {
			log.info("Error: id is invalid!");
			return null;
		}
	}

	@Override
	public List<FinanceData> list_fr() {
		List<String> kindfr = new ArrayList<String>();
		kindfr.add("PENDING");
		kindfr.add("APPROVED");
		kindfr.add("DENIED");
		if (isManagementUser()) {
			List<FinanceData> q = ofy().load().type(FinanceData.class)
					.filter("status in", kindfr).order("update_time").list();
			List<FinanceData> l = new ArrayList<FinanceData>();
			l.addAll(q);
			return l;
		} else {
			List<FinanceData> q = ofy().load().type(FinanceData.class)
					.filter("reporter", getCurrentUserName())
					.filter("status in", kindfr).order("update_time").list();
			List<FinanceData> l = new ArrayList<FinanceData>();
			l.addAll(q);
			return l;
		}
	}

	@Override
	public List<FinanceData> list_draftfr() {
		List<FinanceData> q = ofy().load().type(FinanceData.class)
				.filter("status", "DRAFT")
				.filter("reporter", getCurrentUserName()).order("update_time")
				.list();
		List<FinanceData> l = new ArrayList<FinanceData>();
		l.addAll(q);
		return l;
	}

	@Override
	public List<FinanceData> list_trashfr() {
		if (isManagementUser()) {
			List<FinanceData> q = ofy().load().type(FinanceData.class)
					.filter("status", "DELETED").order("update_time").list();
			List<FinanceData> l = new ArrayList<FinanceData>();
			l.addAll(q);
			return l;
		} else {
			List<FinanceData> q = ofy().load().type(FinanceData.class)
					.filter("reporter", getCurrentUserName())
					.filter("status", "DELETED").order("update_time").list();
			List<FinanceData> l = new ArrayList<FinanceData>();
			l.addAll(q);
			return l;
		}
	}

	@Override
	public List<FinanceData> list_newfr(FinanceData lastfr) {
		if (isManagementUser()) {
			List<FinanceData> q = ofy().load().type(FinanceData.class)
					.filter("update_time >", lastfr.getUpdate_time()).list();
			List<FinanceData> l = new ArrayList<FinanceData>();
			l.addAll(q);
			return l;
		} else {
			List<FinanceData> q = ofy().load().type(FinanceData.class)
					.filter("reporter", getCurrentUserName())
					.filter("update_time >", lastfr.getUpdate_time()).list();
			List<FinanceData> l = new ArrayList<FinanceData>();
			l.addAll(q);
			return l;
		}
	}

	@Override
	public List<FinanceData> list_newdraftfr(FinanceData lastfr) {
		List<FinanceData> q = ofy().load().type(FinanceData.class)
				.filter("status", "DRAFT")
				.filter("reporter", getCurrentUserName())
				.filter("update_time >", lastfr.getUpdate_time()).list();
		List<FinanceData> l = new ArrayList<FinanceData>();
		l.addAll(q);
		return l;
	}

	@Override
	public List<FinanceData> list_newtrashfr(FinanceData lastfr) {
		if (isManagementUser()) {
			List<FinanceData> q = ofy().load().type(FinanceData.class)
					.filter("status", "DELETED")
					.filter("update_time >", lastfr.getUpdate_time()).list();
			List<FinanceData> l = new ArrayList<FinanceData>();
			l.addAll(q);
			return l;
		} else {
			List<FinanceData> q = ofy().load().type(FinanceData.class)
					.filter("reporter", getCurrentUserName())
					.filter("status", "DELETED")
					.filter("update_time >", lastfr.getUpdate_time()).list();
			List<FinanceData> l = new ArrayList<FinanceData>();
			l.addAll(q);
			return l;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> loadMemcache() {
		// TODO Auto-generated method stub
		if (memcached.contains(getCurrentUserName())) {
			List<String> cachedlist = (List<String>) memcached
					.get(getCurrentUserName());
			return cachedlist;
		} else {
			MemcacheData memcached_data = ofy().load().type(MemcacheData.class)
					.id(getCurrentUserName()).get();
			if (memcached_data == null) {
				log.info("Synchrony notification encounter problem! May be user is not in system");
				return null;
			}
			
			List<String> cachedlist = memcached_data.getCachedlist();
			if(cachedlist == null || cachedlist.isEmpty()) {
				memcached.put(memcached_data.getUsername(),"");
				return null;
			}
			
			memcached.put(memcached_data.getUsername(),
					memcached_data.getCachedlist());
			return cachedlist;
		}
	}

	@Override
	public List<FinanceData> loadUpdateDataList(List<String> updateDataIDList) {
		// TODO Auto-generated method stub
		List<FinanceData> updateList = new ArrayList<FinanceData>();
		for (String updateDataID : updateDataIDList) {
			Long id;
			try {
				id = Long.valueOf(updateDataID);
				FinanceData updateData = ofy().load().type(FinanceData.class)
						.id(id).get();
				updateList.add(updateData);
			} catch (Exception e) {
				log.info("Error: update data id is invalid!");
				return null;
			}
		}
		return updateList;
	}

}
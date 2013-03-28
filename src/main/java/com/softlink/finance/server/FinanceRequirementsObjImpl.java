package com.softlink.finance.server;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.softlink.finance.datastore.FinanceRequirementsObj;
import com.softlink.financedatastore.client.FinanceRequirements;
import com.softlink.financedatastore.client.SeriUser;

import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class FinanceRequirementsObjImpl extends RemoteServiceServlet implements
		FinanceRequirementsObj {
	
	private final static UserService userService = 
			UserServiceFactory.getUserService();

	public void insert(FinanceRequirements fr) {
		ofy().save().entity(fr);
		assert fr.request_id != null;
	}
	
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
		}
	}

	public void delete(FinanceRequirements fr) {
		ofy().delete().entity(fr);
	}

	public List<FinanceRequirements> find(Long req_id) {
		List<FinanceRequirements> q = ofy().load().type(FinanceRequirements.class)
				.filter("request_id", req_id)
				.list();
		List<FinanceRequirements> l = new ArrayList<FinanceRequirements>();
		l.addAll(q);
		return l;
	}

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
	
	public List<SeriUser> list_manager() {
		List<SeriUser> q = ofy().load().type(SeriUser.class)
				.filter("role", "Officer")
				.list();
		List<SeriUser> l = new ArrayList<SeriUser>();
		l.addAll(q);
		return l;
	}
	
	public void updateUserLog(Date update_time) {
		SeriUser user = findUser();
		user.setTimework(update_time);
		ofy().save().entity(user);
	}
	
	//Procedure----------------------------------------------------------
	private String getUserName() {
		String username = userService.getCurrentUser().getNickname();
		if(username.contains("@"))
			return username;
		return username+"@gmail.com";
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
	
}

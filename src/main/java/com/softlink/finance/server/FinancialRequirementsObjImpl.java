package com.softlink.finance.server;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Query;
import com.softlink.finance.datastore.FinancialRequirementsObj;
import com.softlink.financedatastore.client.FinancialRequirements;
import com.softlink.financedatastore.client.SeriUser;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class FinancialRequirementsObjImpl extends RemoteServiceServlet implements
		FinancialRequirementsObj {
	
	private final static UserService userService = 
			UserServiceFactory.getUserService();

	public void insert(FinancialRequirements fr) {
		Objectify ofy = ObjectifyService.begin();
		ofy.put(fr);
		assert fr.request_id != null;
	}
	
	public void approveRequest(FinancialRequirements fr, String status, Date update_time, String comment) {
		if(checkUserAdminRole()||fr.getStatus().equals("DRAFT")) {
			Objectify ofy = ObjectifyService.begin();
			fr.setStatus(status);
			fr.setUpdate_time(update_time);
			if((status.equals("APPROVED")||status.equals("DENIED"))&&fr.getReq_time()==null)
				fr.setReq_time(update_time);
			if(fr.getComment().isEmpty())
				fr.setComment(comment);
			else
				fr.setComment(fr.getComment()+comment);
			ofy.put(fr);
		}
	}

	public void delete(FinancialRequirements fr) {
		Objectify ofy = ObjectifyService.begin();
		ofy.delete(fr);
	}

	public List<FinancialRequirements> find(Long req_id) {
		Objectify ofy = ObjectifyService.begin();
		Query<FinancialRequirements> q = ofy.query(FinancialRequirements.class)
				.filter("request_id", req_id);
		return q.list();
	}

	public List<FinancialRequirements> list_fr() {
		Objectify ofy = ObjectifyService.begin();
		List<String> kindfr = new ArrayList<String>();
		kindfr.add("PENDING");
		kindfr.add("APPROVED");
		kindfr.add("DENIED");
		if(checkUserAdminRole()) {
			Query<FinancialRequirements> q = ofy.query(FinancialRequirements.class)
					.filter("status in", kindfr)
					.order("update_time");
			return q.list();
		} else {
			Query<FinancialRequirements> q = ofy.query(FinancialRequirements.class)
					.filter("reporter", getUserName())
					.filter("status in", kindfr)
					.order("update_time");
			return q.list();
		}
	}

	public List<FinancialRequirements> list_newfr(FinancialRequirements lastfr) {
		Objectify ofy = ObjectifyService.begin();
		if(checkUserAdminRole()) {
			Query<FinancialRequirements> q = ofy.query(FinancialRequirements.class)
					.filter("update_time >", lastfr.getUpdate_time());
			return q.list();
		} else {
			Query<FinancialRequirements> q = ofy.query(FinancialRequirements.class)
					.filter("reporter", getUserName())
					.filter("update_time >", lastfr.getUpdate_time());
			return q.list();
		}
	}

	public List<FinancialRequirements> list_draftfr() {
		Objectify ofy = ObjectifyService.begin();
		Query<FinancialRequirements> q = ofy.query(FinancialRequirements.class)
				.filter("status", "DRAFT")
				.filter("reporter", getUserName())
				.order("update_time");
		return q.list();
	}

	public List<FinancialRequirements> list_newdraftfr(
			FinancialRequirements lastfr) {
		Objectify ofy = ObjectifyService.begin();
		Query<FinancialRequirements> q = ofy.query(FinancialRequirements.class)
				.filter("status", "DRAFT")
				.filter("reporter", getUserName())
				.filter("update_time >", lastfr.getUpdate_time());
		return q.list();
	}

	public List<FinancialRequirements> list_trashfr() {
		Objectify ofy = ObjectifyService.begin();
		if(checkUserAdminRole()) {
			Query<FinancialRequirements> q = ofy.query(FinancialRequirements.class)
					.filter("status", "DELETED")
					.order("update_time");
			return q.list();
		} else {
			Query<FinancialRequirements> q = ofy.query(FinancialRequirements.class)
					.filter("reporter", getUserName())
					.filter("status", "DELETED")
					.order("update_time");
			return q.list();
		}
	}

	public List<FinancialRequirements> list_newtrashfr(
			FinancialRequirements lastfr) {
		Objectify ofy = ObjectifyService.begin();
		if(checkUserAdminRole()) {
			Query<FinancialRequirements> q = ofy.query(FinancialRequirements.class)
					.filter("status", "DELETED")
					.filter("update_time >", lastfr.getUpdate_time());
			return q.list();
		} else {
			Query<FinancialRequirements> q = ofy.query(FinancialRequirements.class)
					.filter("reporter", getUserName())
					.filter("status", "DELETED")
					.filter("update_time >", lastfr.getUpdate_time());
			return q.list();
		}
	}

	public List<FinancialRequirements> list_newRequestPerUser() {
		Objectify ofy = ObjectifyService.begin();
		List<String> kindfr = new ArrayList<String>();
		kindfr.add("PENDING");
		kindfr.add("APPROVED");
		kindfr.add("DENIED");
		Query<SeriUser> q = ofy.query(SeriUser.class)
				.filter("username", getUserName());
		List<SeriUser> user = q.list();
		if(user.isEmpty())
			return null;
		else {
			if(checkUserAdminRole()) {
				Query<FinancialRequirements> q1 = ofy.query(FinancialRequirements.class)
						.filter("status in", kindfr)
						.filter("update_time >", user.get(0).getTimework())
						.order("update_time");
				Query<FinancialRequirements> q2 = ofy.query(FinancialRequirements.class)
						.filter("reporter", getUserName())
						.filter("status", "DRAFT")
						.filter("update_time >", user.get(0).getTimework())
						.order("update_time");
				List<FinancialRequirements> l1 = q1.list();
				List<FinancialRequirements> l2 = q2.list();
				List<FinancialRequirements> l = new ArrayList<FinancialRequirements>();
				if(l1.isEmpty()==false&&l2.isEmpty()==false) {
					if(l1.get(0).getUpdate_time().after(l2.get(0).getUpdate_time())){
						for(FinancialRequirements fr: l2)
							l.add(fr);
						for(FinancialRequirements fr: l1)
							l.add(fr);
					} else {
						for(FinancialRequirements fr: l1)
							l.add(fr);
						for(FinancialRequirements fr: l2)
							l.add(fr);
					}
				}
				if(l2.isEmpty())
					for(FinancialRequirements fr: l1)
						l.add(fr);
				if(l1.isEmpty())
					for(FinancialRequirements fr: l2)
						l.add(fr);
				return l;
			} else {
				kindfr.add("DRAFT");
				Query<FinancialRequirements> q1 = ofy.query(FinancialRequirements.class)
						.filter("status in", kindfr)
						.filter("reporter", getUserName())
						.filter("update_time >", user.get(0).getTimework())
						.order("update_time");
				return q1.list();
			}
		}
	}

	public boolean checkUserAdminRole() {
		Objectify ofy = ObjectifyService.begin();
		Query<SeriUser> q = ofy.query(SeriUser.class)
				.filter("username", getUserName());
		List<SeriUser> user = q.list();
		if(user.isEmpty())
			return false;
		else {
			if(user.get(0).getRole().equals("Officer"))
				return true;
			else
				return false;
		}
	}
	
	public List<SeriUser> list_manager() {
		Objectify ofy = ObjectifyService.begin();
		Query<SeriUser> q = ofy.query(SeriUser.class)
				.filter("role", "Officer");
		return q.list();
	}
	
	public void updateUserLog(Date update_time) {
		Objectify ofy = ObjectifyService.begin();
		SeriUser user = findUser();
		user.setTimework(update_time);
		ofy.put(user);
	}
	
	//Procedure----------------------------------------------------------
	private String getUserName() {
		String username = userService.getCurrentUser().getNickname();
		if(username.contains("@"))
			return username;
		return username+"@gmail.com";
	}
	
	private SeriUser findUser() {
		Objectify ofy = ObjectifyService.begin();
		Query<SeriUser> q = ofy.query(SeriUser.class)
				.filter("username", getUserName());
		List<SeriUser> user = q.list();
		if(user.isEmpty())
			return null;
		else
			return user.get(0);
	}
	
	
}

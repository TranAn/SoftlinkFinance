package com.softlink.financedatastore.client;

import java.util.Date;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Ignore;
import com.googlecode.objectify.annotation.Index;

@Entity(name="finance-user")
@Index
public class SeriUser implements IsSerializable{
	
	public SeriUser(Long userid, String username, String role, Date timework) {
		super();
		this.userid = userid;
		this.username = username;
		this.role = role;
		this.timework = timework;
	}
	/**
	 * 
	 */
	@Id 
	public Long userid;
	private String username;
	private String role;
	private Date timework;
	@Ignore String doNotPersist;

	public SeriUser() {
	 	super();	
	}

	public Long getUserid() {
		return userid;
	}

	public void setUserid(Long userid) {
		this.userid = userid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public Date getTimework() {
		return timework;
	}

	public void setTimework(Date timework) {
		this.timework = timework;
	}

}

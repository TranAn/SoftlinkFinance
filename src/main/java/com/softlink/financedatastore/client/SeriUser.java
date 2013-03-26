package com.softlink.financedatastore.client;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Id;
import javax.persistence.Transient;

public class SeriUser implements Serializable{
	
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
	private static final long serialVersionUID = 1L;
	@Id 
	public Long userid;
	private String username;
	private String role;
	private Date timework;
	@Transient String doNotPersist;

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

package com.softlink.datastore.model;

import java.io.Serializable;
import java.util.Date;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity(name="finance-user")
@Index
public class FinanceUser implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@Id private String username;
	private String role;
	private Date timework;

	public FinanceUser() {
	 	super();	
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

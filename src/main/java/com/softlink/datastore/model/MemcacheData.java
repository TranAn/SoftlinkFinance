package com.softlink.datastore.model;

import java.io.Serializable;
import java.util.List;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity(name="memcache-data")
@Index
public class MemcacheData implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@Id private String username;
	private List<String> cachedlist;
	
	public MemcacheData() {
		super();
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public List<String> getCachedlist() {
		return cachedlist;
	}

	public void setCachedlist(List<String> cachedlist) {
		this.cachedlist = cachedlist;
	}
		
}


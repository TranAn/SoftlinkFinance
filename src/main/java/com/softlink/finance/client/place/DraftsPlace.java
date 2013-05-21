package com.softlink.finance.client.place;

import com.google.gwt.place.shared.Place;

public class DraftsPlace extends Place{
	
	private String activity_token;
	
	public DraftsPlace(String activity_token) {
		this.activity_token = activity_token;
	}

	public String getActivityToken(){
		return activity_token;
	}
	
	public void setActivityToken(String activiti_token){
		this.activity_token = activiti_token;
	}

}

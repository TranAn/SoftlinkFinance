package com.softlink.finance.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.softlink.datastore.model.FinanceData;

public class LocalStorage {
	
	private String currentUser;
	private boolean isManager = false;
	private List<String> cachedlist = new ArrayList<String>();
	private Map<String,Boolean> notifyList = new HashMap<String,Boolean>();
	private Map<String,FinanceData> updateDataList = new HashMap<String,FinanceData>();
	
	public LocalStorage() {}

	//Get Set Method
	public String getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(String currentUser) {
		this.currentUser = currentUser;
	}

	public boolean isManager() {
		return isManager;
	}

	public void setManager(boolean isManager) {
		this.isManager = isManager;
	}
	
	public List<String> getCachedlist() {
		return cachedlist;
	}

	public void setCachedlist(List<String> cachedlist) {
		this.cachedlist.clear();
		this.cachedlist.addAll(cachedlist);
	}
	
	public Map<String, Boolean> getNotifyList() {
		return notifyList;
	}
	
	public void flushNotifyList(String key) {
		notifyList.remove(key);
	}

	public void putToNotifyList(String id, boolean marknotify) {
		notifyList.put(id, marknotify);
	}
	
	public Map<String, FinanceData> getUpdateDataList() {
		return updateDataList;
	}
	
	public void setUpdateDataList(Map<String, FinanceData> updateDataList) {
		this.updateDataList.clear();
		this.updateDataList.putAll(updateDataList);
	}
	
	public void putToUpdateDataList(FinanceData data) {
		String key = String.valueOf(data.getRequest_id());
		updateDataList.put(key,data);
	}
	
	public void flushUpdateDataList(FinanceData data) {
		String key = String.valueOf(data.getRequest_id());
		updateDataList.remove(key);
	}
	
	public void flushUpdateDataListByKey(String key) {
		updateDataList.remove(key);
	}
	
}

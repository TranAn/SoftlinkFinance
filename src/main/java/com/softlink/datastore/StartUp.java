package com.softlink.datastore;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.googlecode.objectify.ObjectifyService;
import com.softlink.datastore.model.*;

@SuppressWarnings("serial")
public class StartUp extends RemoteServiceServlet {
	
	public StartUp() {
		ObjectifyService.register(FinanceData.class);
		ObjectifyService.register(FinanceUser.class);
		ObjectifyService.register(MemcacheData.class);
	}

}

package com.softlink.financedatastore.server;


import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.googlecode.objectify.ObjectifyService;
import com.softlink.financedatastore.client.FinanceRequirements;
import com.softlink.financedatastore.client.SeriUser;
import com.softlink.financedatastore.client.StartUpServlet;

@SuppressWarnings("serial")
public class StartUpServletImpl extends RemoteServiceServlet implements
	StartUpServlet{
	
	public StartUpServletImpl() {
		ObjectifyService.register(FinanceRequirements.class);
		ObjectifyService.register(SeriUser.class);
	}

}

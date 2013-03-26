package com.softlink.financedatastore.server;


import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.googlecode.objectify.ObjectifyService;
import com.softlink.financedatastore.client.FinancialRequirements;
import com.softlink.financedatastore.client.SeriUser;
import com.softlink.financedatastore.client.StartUpServlet;

@SuppressWarnings("serial")
public class StartUpServletImpl extends RemoteServiceServlet implements
	StartUpServlet{
	
	public StartUpServletImpl() {
		ObjectifyService.register(FinancialRequirements.class);
		ObjectifyService.register(SeriUser.class);
	}

}

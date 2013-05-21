package com.softlink.finance.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.googlecode.objectify.ObjectifyService;
import com.softlink.finance.shared.FinanceRequirements;
import com.softlink.finance.shared.SeriUser;

@SuppressWarnings("serial")
public class StartUpServlet extends RemoteServiceServlet {
	
	public StartUpServlet() {
		ObjectifyService.register(FinanceRequirements.class);
		ObjectifyService.register(SeriUser.class);
	}

}

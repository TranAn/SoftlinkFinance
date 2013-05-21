package com.softlink.finance.client.activity;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.softlink.finance.client.ClientFactory;
import com.softlink.finance.client.events.InDraftsPlaceEvent;
import com.softlink.finance.client.events.InParentPlaceEvent;
import com.softlink.finance.client.place.DraftsPlace;
import com.softlink.finance.client.view.DraftsView;

public class DraftsActivity extends AbstractActivity{
	
	private ClientFactory clientFactory;
	private String token;

	public DraftsActivity(DraftsPlace place, ClientFactory clientFactory) {
		this.token = place.getActivityToken();
		this.clientFactory = clientFactory;
	}
	
	@Override
	public void start(AcceptsOneWidget containerView, EventBus eventBus) {
		DraftsView draftsView = clientFactory.getDraftsView();
		eventBus.fireEvent(new InDraftsPlaceEvent());
		eventBus.fireEvent(new InParentPlaceEvent());
		containerView.setWidget(draftsView.asWidget());
	}
	
	@Override
	public String mayStop() {
		return null;
	}

}

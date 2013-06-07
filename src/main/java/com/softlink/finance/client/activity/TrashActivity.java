package com.softlink.finance.client.activity;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.softlink.finance.client.ClientFactory;
import com.softlink.finance.client.events.InParentPlaceEvent;
import com.softlink.finance.client.events.InTrashPlaceEvent;
import com.softlink.finance.client.place.TrashPlace;
import com.softlink.finance.client.view.TrashView;

public class TrashActivity extends AbstractActivity {
	
	private ClientFactory clientFactory;
	private String token;

	public TrashActivity(TrashPlace place, ClientFactory clientFactory) {
		this.token = place.getActivityToken();
		this.clientFactory = clientFactory;
	}
	
	@Override
	public void start(AcceptsOneWidget containerView, EventBus eventBus) {
		TrashView trashView = clientFactory.getTrashView();
		
		eventBus.fireEvent(new InTrashPlaceEvent());
		eventBus.fireEvent(new InParentPlaceEvent());
		
		containerView.setWidget(trashView.asWidget());
		
	}
	
	@Override
	public String mayStop() {
		return null;
	}
}

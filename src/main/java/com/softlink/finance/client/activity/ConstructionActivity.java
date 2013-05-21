package com.softlink.finance.client.activity;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.softlink.finance.client.ClientFactory;
import com.softlink.finance.client.events.InConstructionPlaceEvent;
import com.softlink.finance.client.events.InParentPlaceEvent;
import com.softlink.finance.client.place.ConstructionPlace;
import com.softlink.finance.client.view.UnderConstructionView;

public class ConstructionActivity extends AbstractActivity{

	private ClientFactory clientFactory;
	
	public ConstructionActivity(ConstructionPlace place, ClientFactory clientFactory) {
		this.clientFactory = clientFactory;
	}
	
	@Override
	public void start(AcceptsOneWidget containerView, EventBus eventBus) {
		UnderConstructionView underConstructionView = clientFactory.getUnderConstructionView();
		containerView.setWidget(underConstructionView.asWidget());
		eventBus.fireEvent(new InConstructionPlaceEvent());
		eventBus.fireEvent(new InParentPlaceEvent());
		Window.alert("INFO: Area Under Construction");
	}

}

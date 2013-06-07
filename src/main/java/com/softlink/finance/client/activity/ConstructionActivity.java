package com.softlink.finance.client.activity;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.softlink.finance.client.ClientFactory;
import com.softlink.finance.client.events.InConstructionPlaceEvent;
import com.softlink.finance.client.events.InParentPlaceEvent;
import com.softlink.finance.client.place.ConstructionPlace;
import com.softlink.finance.client.view.UnderConstructionView;

public class ConstructionActivity extends AbstractActivity
	implements UnderConstructionView.Presenter{

	private ClientFactory clientFactory;
	private Place previousPlace;
	
	/*
	 * ---Constructor---
	 */
	public ConstructionActivity(ConstructionPlace place, ClientFactory clientFactory) {
		this.clientFactory = clientFactory;
		previousPlace = place.getPreviousPlace();
	}
	
	/*
	 * Automatic invoke when activity is start.
	 */
	@Override
	public void start(AcceptsOneWidget containerView, EventBus eventBus) {
		UnderConstructionView underConstructionView = clientFactory.getUnderConstructionView();
		underConstructionView.setPresenter(this);
		underConstructionView.startCountDown();
		
		eventBus.fireEvent(new InConstructionPlaceEvent());
		eventBus.fireEvent(new InParentPlaceEvent());
		
		containerView.setWidget(underConstructionView.asWidget());
	}
	
	/*
	 * Automatic invoke when activity is stop.
	 */
	@Override
	public String mayStop() {
		return null;
	}

	/*
	 * ---Presenter Listener---
	 */
	@Override
	public void goToPreviousPlace() {
		// TODO Auto-generated method stub
		Place currentPlace = clientFactory.getPlaceController().getWhere();
		if(currentPlace instanceof ConstructionPlace)
			clientFactory.getPlaceController().goTo(previousPlace);
	}

}

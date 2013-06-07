package com.softlink.finance.client;

import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.place.shared.PlaceHistoryHandler;
import com.google.gwt.user.client.ui.RootPanel;
import com.softlink.finance.client.mvp.AppActivityMapper;
import com.softlink.finance.client.mvp.AppPlaceHistoryMapper;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Finance implements EntryPoint {
	
	private Place defaultPlace = null;

	@Override
	public void onModuleLoad() {
		// Create ClientFactory using deferred binding so we can replace with different
		// impls in gwt.xml
		ClientFactory clientFactory = GWT.create(ClientFactory.class);
		EventBus eventBus = clientFactory.getEventBus();
		PlaceController placeController = clientFactory.getPlaceController();
		FinanceView financeView = clientFactory.getFinanceView();

		// Start ActivityManager for the main widget with our ActivityMapper
		ActivityMapper activityMapper = new AppActivityMapper(clientFactory);
		ActivityManager activityManager = new ActivityManager(activityMapper, eventBus);
		activityManager.setDisplay(financeView.getContainerView());

		// Start PlaceHistoryHandler with our PlaceHistoryMapper
		AppPlaceHistoryMapper historyMapper= GWT.create(AppPlaceHistoryMapper.class);
		PlaceHistoryHandler historyHandler = new PlaceHistoryHandler(historyMapper);
		historyHandler.register(placeController, eventBus, defaultPlace);
		
		// Goes to place represented on URL or default place
		historyHandler.handleCurrentHistory();
		
		// Add MainView to the Root
		RootPanel rootPanel = RootPanel.get();
		rootPanel.setSize("98.5%", "100%");
		rootPanel.add(financeView,0,0);
		
		// Set default Place
		financeView.setDefaultPlace();
	}
}

package com.softlink.finance.client;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.softlink.finance.client.view.DraftsView;
import com.softlink.finance.client.view.FinanceRequirementView;
import com.softlink.finance.client.view.RequestDetailView;
import com.softlink.finance.client.view.TrashView;
import com.softlink.finance.client.view.UnderConstructionView;

public interface ClientFactory
{
	/**
	 * Get the {@link EventBus}
	 * @return the event bus used through the app
	 */
	EventBus getEventBus();

	/**
	 * Get the {@link PlaceController}
	 * @return the place controller
	 */
	PlaceController getPlaceController();
	
	/**
	 * Get the {@link LocalStorage}
	 * @return the LocalStorage of app
	 */
	LocalStorage getLocalStorage();
	
	/**
	 * MainView, it has been add to RootPanel
	 * @return {@link FinanceView}
	 */
	FinanceView getFinanceView();
	
	/*
	 * CoreView, it has been add to ContainerView in MainView
	 * Desktop, Mobile, Tablet always have it
	 */
	FinanceRequirementView getFinanceRequirementView();
	DraftsView getDraftsView();
	TrashView getTrashView();
	RequestDetailView getRequestDetailView();
	UnderConstructionView getUnderConstructionView();
}

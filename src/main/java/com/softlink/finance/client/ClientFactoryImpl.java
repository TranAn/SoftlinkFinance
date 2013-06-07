package com.softlink.finance.client;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.place.shared.PlaceController;
import com.softlink.finance.client.view.DraftsView;
import com.softlink.finance.client.view.FinanceRequirementView;
import com.softlink.finance.client.view.RequestDetailView;
import com.softlink.finance.client.view.TrashView;
import com.softlink.finance.client.view.UnderConstructionView;
import com.softlink.finance.client.view.desktop.DesktopDraftsView;
import com.softlink.finance.client.view.desktop.DesktopFinanceRequirementView;
import com.softlink.finance.client.view.desktop.DesktopRequestDetailView;
import com.softlink.finance.client.view.desktop.DesktopTrashView;
import com.softlink.finance.client.view.desktop.DesktopUnderConstructionView;
import com.softlink.finance.client.view.desktop.FinanceViewDesktop;

/**
 * Use ClientFactoryImpl by default for Desktop
 */
public class ClientFactoryImpl implements ClientFactory
{
	private static final EventBus eventBus = new SimpleEventBus();
	private static final PlaceController placeController = new PlaceController(eventBus);
	private static final LocalStorage localStorage = new LocalStorage();
	
	private final FinanceView financeView = new FinanceViewDesktop(this);
	
	private static final DraftsView draftsView = new DesktopDraftsView();
	private static final FinanceRequirementView financeRequirementView = new DesktopFinanceRequirementView();
	private static final RequestDetailView  requestDetailView = new DesktopRequestDetailView();
	private static final TrashView trashView = new DesktopTrashView();
	private static final UnderConstructionView underConstructionView = new DesktopUnderConstructionView();

	public ClientFactoryImpl() {}
	
	@Override
	public EventBus getEventBus()
	{
		return eventBus;
	}

	@Override
	public PlaceController getPlaceController()
	{
		return placeController;
	}
	
	@Override
	public LocalStorage getLocalStorage()
	{
		return localStorage;
	}

	@Override
	public FinanceView getFinanceView() {
		// TODO Auto-generated method stub
		return financeView;
	}

	@Override
	public FinanceRequirementView getFinanceRequirementView() {
		// TODO Auto-generated method stub
		return financeRequirementView;
	}

	@Override
	public DraftsView getDraftsView() {
		// TODO Auto-generated method stub
		return draftsView;
	}

	@Override
	public TrashView getTrashView() {
		// TODO Auto-generated method stub
		return trashView;
	}

	@Override
	public RequestDetailView getRequestDetailView() {
		// TODO Auto-generated method stub
		return requestDetailView;
	}

	@Override
	public UnderConstructionView getUnderConstructionView() {
		// TODO Auto-generated method stub
		return underConstructionView;
	}

}

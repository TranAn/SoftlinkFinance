package com.softlink.finance.client.activity;

import java.util.List;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.softlink.finance.client.ClientFactory;
import com.softlink.finance.client.events.InFinanceRequirementPlaceEvent;
import com.softlink.finance.client.events.InParentPlaceEvent;
import com.softlink.finance.client.events.LoadFailEvent;
import com.softlink.finance.client.events.LoadSuccessEvent;
import com.softlink.finance.client.events.LoadingEvent;
import com.softlink.finance.client.events.RefreshingEvent;
import com.softlink.finance.client.events.SendBackRequestEvent;
import com.softlink.finance.client.place.FinanceRequirementPlace;
import com.softlink.finance.client.request.FinanceRequirementsRequest;
import com.softlink.finance.client.request.FinanceRequirementsRequestAsync;
import com.softlink.finance.client.view.FinanceRequirementView;
import com.softlink.finance.client.view.RequestDetailView;
import com.softlink.finance.shared.FinanceRequirements;

public class FinanceRequirementActivity extends AbstractActivity 
	implements FinanceRequirementView.Presenter{
	
	private ClientFactory clientFactory;
	private EventBus eventBus;
	private final static FinanceRequirementsRequestAsync financeRequirementsRequest = 
			GWT.create(FinanceRequirementsRequest.class);

	/*
	 * ---Presenter Procedure---
	 */
	private void getNewData(final FinanceRequirementView financeRequirementView){
		eventBus.fireEvent(new LoadingEvent());
		financeRequirementsRequest.list_fr(
	    		 new AsyncCallback<List<FinanceRequirements>>() {
	    	 public void onFailure(Throwable caught) {
	    		 eventBus.fireEvent(new LoadFailEvent());
			 }
			 public void onSuccess(List<FinanceRequirements> result) {
				 eventBus.fireEvent(new LoadSuccessEvent());
				 financeRequirementView.setNewData(result);
			 }
		});
	}

	private void getUpdateData(final FinanceRequirementView financeRequirementView) {
		eventBus.fireEvent(new RefreshingEvent());
		financeRequirementsRequest.list_newfr(financeRequirementView.getLastData(),
	    		 new AsyncCallback<List<FinanceRequirements>>() {
	    	 public void onFailure(Throwable caught) {
	    		eventBus.fireEvent(new LoadFailEvent());
			 }
			 public void onSuccess(List<FinanceRequirements> result) {
				 eventBus.fireEvent(new LoadSuccessEvent());
				 financeRequirementView.setUpdateData(result);
	  		 }
		 });
	}

	/*
	 * ---Constructor---
	 */
	public FinanceRequirementActivity(FinanceRequirementPlace place, ClientFactory clientFactory) {
		this.clientFactory = clientFactory;
		this.eventBus = clientFactory.getEventBus();
	}
	
	/*
	 * Automatic invoke when activity is start.
	 */
	@Override
	public void start(AcceptsOneWidget containerView, EventBus eventBus) {
		FinanceRequirementView financeRequirementView = clientFactory.getFinanceRequirementView();
		financeRequirementView.setPresenter(this);
		eventBus.fireEvent(new InFinanceRequirementPlaceEvent());
		eventBus.fireEvent(new InParentPlaceEvent());
		containerView.setWidget(financeRequirementView.asWidget());
		
		if(financeRequirementView.getDataCount()==0)
			getNewData(financeRequirementView);
		else
			getUpdateData(financeRequirementView);
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
	/**
	 * Go to RequestDetailView and sent selected item to it.
	 */
	@Override
	public void goTo(Place place, FinanceRequirements selected_fr) {
		// TODO Auto-generated method stub
		clientFactory.getPlaceController().goTo(place);
	}
	
	public void setHandlerEventBus() {
		eventBus.addHandler(SendBackRequestEvent.TYPE, new SendBackRequestEvent.Handler() {
			@Override
			public void onSendBackRequest(SendBackRequestEvent event) {
				// TODO Auto-generated method stub
				
			}
		});
	}
}

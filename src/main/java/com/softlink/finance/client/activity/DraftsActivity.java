package com.softlink.finance.client.activity;

import java.util.List;
import java.util.logging.Logger;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.softlink.datastore.model.FinanceData;
import com.softlink.finance.client.ClientFactory;
import com.softlink.finance.client.events.InDraftsPlaceEvent;
import com.softlink.finance.client.events.LoadFailEvent;
import com.softlink.finance.client.events.LoadSuccessEvent;
import com.softlink.finance.client.events.LoadingEvent;
import com.softlink.finance.client.events.RefreshingEvent;
import com.softlink.finance.client.place.DraftsPlace;
import com.softlink.finance.client.request.FinanceDataRequest;
import com.softlink.finance.client.request.FinanceDataRequestAsync;
import com.softlink.finance.client.view.DraftsView;

public class DraftsActivity extends AbstractActivity
	implements DraftsView.Presenter{
	
	private ClientFactory clientFactory;
	private EventBus eventBus;
	private String token;
	private final static FinanceDataRequestAsync financeRequirementsRequest = 
			GWT.create(FinanceDataRequest.class);
	private Logger logger = Logger.getLogger(DraftsActivity.class.getName());
	
	/*
	 * ---Presenter Procedure---
	 */
	private void getNewData(final DraftsView draftsView){
		eventBus.fireEvent(new LoadingEvent());
		financeRequirementsRequest.list_draftfr(
	    		 new AsyncCallback<List<FinanceData>>() {
	    	 public void onFailure(Throwable caught) {
	    		 eventBus.fireEvent(new LoadFailEvent());
			 }
			 public void onSuccess(List<FinanceData> result) {
				 eventBus.fireEvent(new LoadSuccessEvent());
				 draftsView.setNewData(result);
			 }
		});
	}

	private void getUpdateData(final DraftsView draftsView) {
		eventBus.fireEvent(new RefreshingEvent());
		financeRequirementsRequest.list_newdraftfr(draftsView.getLastData(),
	    		 new AsyncCallback<List<FinanceData>>() {
	    	 public void onFailure(Throwable caught) {
	    		eventBus.fireEvent(new LoadFailEvent());
			 }
			 public void onSuccess(List<FinanceData> result) {
				 eventBus.fireEvent(new LoadSuccessEvent());
				 draftsView.setUpdateData(result);
	  		 }
		 });
	}

	/*
	 * ---Constructor---
	 */
	public DraftsActivity(DraftsPlace place, ClientFactory clientFactory) {
		this.token = place.getActivityToken();
		this.clientFactory = clientFactory;
		this.eventBus = clientFactory.getEventBus();
	}
	
	/*
	 * Automatic invoke when activity is start.
	 */
	@Override
	public void start(AcceptsOneWidget containerView, EventBus eventBus) {
		DraftsView draftsView = clientFactory.getDraftsView();
		draftsView.setPresenter(this);
		
		eventBus.fireEvent(new InDraftsPlaceEvent());
//		eventBus.fireEvent(new InParentPlaceEvent());
		
		containerView.setWidget(draftsView.asWidget());
		
		if(draftsView.getDataCount()==0)
			getNewData(draftsView);
		else
			getUpdateData(draftsView);
	}
	
	/*
	 * Automatic invoke when activity is stop.
	 */
	@Override
	public String mayStop() {
		return null;
	}

}

package com.softlink.finance.client.activity;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.softlink.datastore.model.FinanceData;
import com.softlink.finance.client.ClientFactory;
import com.softlink.finance.client.events.InFinanceRequirementPlaceEvent;
import com.softlink.finance.client.events.InRequestDetailViewEvent;
import com.softlink.finance.client.events.LoadFailEvent;
import com.softlink.finance.client.events.LoadSuccessEvent;
import com.softlink.finance.client.events.LoadingEvent;
import com.softlink.finance.client.events.RefreshingEvent;
import com.softlink.finance.client.place.FinanceRequirementPlace;
import com.softlink.finance.client.request.FinanceDataRequest;
import com.softlink.finance.client.request.FinanceDataRequestAsync;
import com.softlink.finance.client.view.FinanceRequirementView;

public class FinanceRequirementActivity extends AbstractActivity implements
		FinanceRequirementView.Presenter {

	private ClientFactory clientFactory;
	private EventBus eventBus;
	private final static FinanceDataRequestAsync financeRequirementsRequest = GWT
			.create(FinanceDataRequest.class);
	private Logger logger = Logger.getLogger(FinanceRequirementActivity.class
			.getName());

	/*
	 * ---Presenter Procedure---
	 */
	private void getNewData(final FinanceRequirementView financeRequirementView) {
		eventBus.fireEvent(new LoadingEvent());
		financeRequirementsRequest
				.list_fr(new AsyncCallback<List<FinanceData>>() {
					public void onFailure(Throwable caught) {
						eventBus.fireEvent(new LoadFailEvent());
					}

					public void onSuccess(List<FinanceData> result) {
						eventBus.fireEvent(new LoadSuccessEvent());
						if(financeRequirementView.getDataCount()==0)
							financeRequirementView.setNewData(result);
					}
				});
	}

	private void getUpdateData(
			final FinanceRequirementView financeRequirementView) {
		eventBus.fireEvent(new RefreshingEvent());
		Collection<FinanceData> updateDataList = clientFactory
				.getLocalStorage().getUpdateDataList().values();
		Map<String,Boolean> notifyList = clientFactory
				.getLocalStorage().getNotifyList();
		String currentUser = clientFactory.getLocalStorage().getCurrentUser();
		financeRequirementView.setUpdateData(updateDataList,notifyList,currentUser);
	}

	/*
	 * ---Constructor---
	 */
	public FinanceRequirementActivity(FinanceRequirementPlace place,
			ClientFactory clientFactory) {
		this.clientFactory = clientFactory;
		this.eventBus = clientFactory.getEventBus();
	}

	/*
	 * Automatic invoke when activity is start.
	 */
	@Override
	public void start(AcceptsOneWidget containerView, EventBus eventBus) {
		FinanceRequirementView financeRequirementView = clientFactory
				.getFinanceRequirementView();
		financeRequirementView.setPresenter(this);

		eventBus.fireEvent(new InFinanceRequirementPlaceEvent());
		// eventBus.fireEvent(new InParentPlaceEvent());

		containerView.setWidget(financeRequirementView.asWidget());

		if (financeRequirementView.getDataCount() == 0)
			getNewData(financeRequirementView);
		else
			getUpdateData(financeRequirementView);
	}

	/*
	 * Automatic invoke when activity is stop.
	 */
	@Override
	public String mayStop() {
		FinanceRequirementView financeRequirementView = clientFactory
				.getFinanceRequirementView();
		financeRequirementView.setPresenter(null);
		return null;
	}

	/*
	 * ---Presenter Listener---
	 */
	/**
	 * Go to RequestDetailView and sent selected item to it.
	 */
	@Override
	public void goTo(Place place) {
		// TODO Auto-generated method stub
		clientFactory.getPlaceController().goTo(place);
		if (place instanceof FinanceRequirementPlace) {
			FinanceRequirementPlace currentPlace = (FinanceRequirementPlace) place;
			if (currentPlace.getActivityToken() != null)
				eventBus.fireEvent(new InRequestDetailViewEvent());
		}
	}

	@Override
	public void onRefreshComplete() {
		// TODO Auto-generated method stub
		eventBus.fireEvent(new LoadSuccessEvent());
	}

	@Override
	public void flushData(FinanceData data) {
		// TODO Auto-generated method stub
		String id = String.valueOf(data.getRequest_id());
		String delimiter = "/";
		clientFactory.getLocalStorage().flushNotifyList(id);
		clientFactory.getLocalStorage().flushUpdateDataListByKey(id);
		String identifyEpath = "1"+ delimiter + id;
		financeRequirementsRequest.flushData(identifyEpath, new AsyncCallback<Void>() {
			@Override
			public void onFailure(Throwable caught) {}
			@Override
			public void onSuccess(Void result) {}
		});
	}

	@Override
	public void onUpdate() {
		// TODO Auto-generated method stub
		FinanceRequirementView financeRequirementView = clientFactory
				.getFinanceRequirementView();
		getUpdateData(financeRequirementView);
	}

}

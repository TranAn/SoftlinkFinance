package com.softlink.finance.client.activity;

import java.util.Date;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.softlink.finance.client.ClientFactory;
import com.softlink.finance.client.events.ApprovedRequestEvent;
import com.softlink.finance.client.events.DeniedRequestEvent;
import com.softlink.finance.client.events.InFinanceRequirementPlaceEvent;
import com.softlink.finance.client.events.InSubPlaceEvent;
import com.softlink.finance.client.events.LoadFailEvent;
import com.softlink.finance.client.events.SendBackRequestEvent;
import com.softlink.finance.client.place.FinanceRequirementPlace;
import com.softlink.finance.client.place.TrashPlace;
import com.softlink.finance.client.request.FinanceRequirementsRequest;
import com.softlink.finance.client.request.FinanceRequirementsRequestAsync;
import com.softlink.finance.client.request.UserServicesRequest;
import com.softlink.finance.client.request.UserServicesRequestAsync;
import com.softlink.finance.client.view.RequestDetailView;
import com.softlink.finance.shared.FinanceRequirements;

public class RequestDetailActivity extends AbstractActivity
	implements RequestDetailView.Presenter{
	
	private ClientFactory clientFactory;
	private EventBus eventBus;
	private Place currentPlace;
	private String token;
	private final static FinanceRequirementsRequestAsync financeRequirementsRequest = 
			GWT.create(FinanceRequirementsRequest.class);
	private final static UserServicesRequestAsync userservices = 
			  GWT.create(UserServicesRequest.class);

	/*
	 * ---Presenter Procedure---
	 */
	private void getData(final RequestDetailView requestDetailView) {
		financeRequirementsRequest.find(token, new AsyncCallback<FinanceRequirements>() {
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				clientFactory.getPlaceController().goTo(new FinanceRequirementPlace(null));
				eventBus.fireEvent(new LoadFailEvent());
			}
			@Override
			public void onSuccess(FinanceRequirements result) {
				// TODO Auto-generated method stub
				requestDetailView.setData(result);
			}
		});
	}
	
	private void checkUserRole(final RequestDetailView requestDetailView) {
		financeRequirementsRequest.checkUserAdminRole(new AsyncCallback<Boolean>(){
			@Override
			public void onFailure(Throwable caught) {}
			@Override
			public void onSuccess(Boolean result) {
				requestDetailView.setUserRole(result);
			}
	    });
	}
	
	/*
	 * ---Constructor---
	 */
	public RequestDetailActivity(FinanceRequirementPlace place, ClientFactory clientFactory) {
		this.token = place.getActivityToken();
		this.currentPlace = (Place) place;
		this.clientFactory = clientFactory;
		this.eventBus = clientFactory.getEventBus();
	}
	
	public RequestDetailActivity(TrashPlace place, ClientFactory clientFactory) {
		this.token = place.getActivityToken();
		this.currentPlace = (Place) place;
		this.clientFactory = clientFactory;
		this.eventBus = clientFactory.getEventBus();
	}
	
	@Override
	public void start(AcceptsOneWidget containerView, EventBus eventBus) {
		if(currentPlace instanceof FinanceRequirementPlace) {
			RequestDetailView requestDetailView = clientFactory.getRequestDetailView();
			requestDetailView.setPresenter(this);
			eventBus.fireEvent(new InFinanceRequirementPlaceEvent());
			eventBus.fireEvent(new InSubPlaceEvent());
			checkUserRole(requestDetailView);
			getData(requestDetailView);
			containerView.setWidget(requestDetailView.asWidget());
		}
		if(currentPlace instanceof TrashPlace)
			Window.alert("In DesktopTrashView Place");
	}
	
	@Override
	public String mayStop() {
		return null;
	}

	/*
	 * ---Presenter Listener---
	 */
	@Override
	public void goTo(Place place) {
		// TODO Auto-generated method stub
		clientFactory.getPlaceController().goTo(place);
	}

	@Override
	public void onApprovedRequest(final String comment) {
		// TODO Auto-generated method stub
		final RequestDetailView requestDetailView = clientFactory.getRequestDetailView();
		final FinanceRequirements fr = requestDetailView.getData();
		final Date date = new Date(System.currentTimeMillis());
		if(comment.equals("")==false){
			userservices.getUserEmail(new AsyncCallback<String>() {
				public void onFailure(Throwable caught) {}
				public void onSuccess(String result) {
					String cm = result.split("@")[0]+":"+comment+"\r\n \r\n";
					financeRequirementsRequest.approveRequest(fr, "APPROVED", date, cm, 
							new AsyncCallback<Void>() {
						@Override
						public void onFailure(Throwable caught) {
							eventBus.fireEvent(new LoadFailEvent());
						}
						@Override
						public void onSuccess(Void result) {			
							getData(requestDetailView);
							eventBus.fireEvent(new ApprovedRequestEvent());
						}
					});
				}
			});	
		} else {
			financeRequirementsRequest.approveRequest(fr, "APPROVED", date, comment, 
					new AsyncCallback<Void>() {
				@Override
				public void onFailure(Throwable caught) {
					eventBus.fireEvent(new LoadFailEvent());
				}
				@Override
				public void onSuccess(Void result) {
					getData(requestDetailView);
					eventBus.fireEvent(new ApprovedRequestEvent());
				}
			});
		}
	}

	@Override
	public void onDeniedRequest(final String comment) {
		// TODO Auto-generated method stub
		final RequestDetailView requestDetailView = clientFactory.getRequestDetailView();
		final FinanceRequirements fr = requestDetailView.getData();
		final Date date = new Date(System.currentTimeMillis());
		if(comment.equals("")==false){
			userservices.getUserEmail(new AsyncCallback<String>() {
				public void onFailure(Throwable caught) {}
				public void onSuccess(String result) {
					String cm = result.split("@")[0]+":"+comment+"\r\n \r\n";
					financeRequirementsRequest.approveRequest(fr, "DENIED", date, cm, 
							new AsyncCallback<Void>() {
						@Override
						public void onFailure(Throwable caught) {
							eventBus.fireEvent(new LoadFailEvent());
						}
						@Override
						public void onSuccess(Void result) {
							getData(requestDetailView);
							eventBus.fireEvent(new DeniedRequestEvent());
						}
					});
				}
			});	
		} else {
			financeRequirementsRequest.approveRequest(fr, "DENIED", date, comment, 
					new AsyncCallback<Void>() {
				@Override
				public void onFailure(Throwable caught) {
					eventBus.fireEvent(new LoadFailEvent());
				}
				@Override
				public void onSuccess(Void result) {
					getData(requestDetailView);
					eventBus.fireEvent(new DeniedRequestEvent());
				}
			});
		}
	}

	@Override
	public void onSendBackRequest(final String comment) {
		// TODO Auto-generated method stub
		final RequestDetailView requestDetailView = clientFactory.getRequestDetailView();
		final FinanceRequirements fr = requestDetailView.getData();
		final Date date = new Date(System.currentTimeMillis());
		if(comment.equals("")==false){
			userservices.getUserEmail(new AsyncCallback<String>() {
				public void onFailure(Throwable caught) {}
				public void onSuccess(String result) {
					String cm = result.split("@")[0]+":"+comment+"\r\n \r\n";
					financeRequirementsRequest.approveRequest(fr, "DRAFT", date, cm, 
							new AsyncCallback<Void>() {
						@Override
						public void onFailure(Throwable caught) {
							eventBus.fireEvent(new LoadFailEvent());
						}
						@Override
						public void onSuccess(Void result) {
							eventBus.fireEvent(new SendBackRequestEvent(fr));
							clientFactory.getPlaceController().goTo(new FinanceRequirementPlace("null"));
						}
					});
				}
			});	
		} else {
			financeRequirementsRequest.approveRequest(fr, "DRAFT", date, comment, 
					new AsyncCallback<Void>() {
				@Override
				public void onFailure(Throwable caught) {
					eventBus.fireEvent(new LoadFailEvent());
				}
				@Override
				public void onSuccess(Void result) {
					eventBus.fireEvent(new SendBackRequestEvent(fr));
					clientFactory.getPlaceController().goTo(new FinanceRequirementPlace("null"));
				}
			});
		}
	}

}

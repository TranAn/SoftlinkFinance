package com.softlink.finance.client.activity;

import java.util.Date;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.softlink.datastore.model.FinanceData;
import com.softlink.finance.client.ClientFactory;
import com.softlink.finance.client.events.ApprovedRequestEvent;
import com.softlink.finance.client.events.BadRequestEvent;
import com.softlink.finance.client.events.DeniedRequestEvent;
import com.softlink.finance.client.events.InFinanceRequirementPlaceEvent;
import com.softlink.finance.client.events.LoadFailEvent;
import com.softlink.finance.client.events.SendBackRequestEvent;
import com.softlink.finance.client.events.TransmitNotificationEvent;
import com.softlink.finance.client.place.FinanceRequirementPlace;
import com.softlink.finance.client.place.TrashPlace;
import com.softlink.finance.client.request.FinanceDataRequest;
import com.softlink.finance.client.request.FinanceDataRequestAsync;
import com.softlink.finance.client.request.UserServicesRequest;
import com.softlink.finance.client.request.UserServicesRequestAsync;
import com.softlink.finance.client.view.RequestDetailView;

public class RequestDetailActivity extends AbstractActivity
	implements RequestDetailView.Presenter{
	
	private ClientFactory clientFactory;
	private EventBus eventBus;
	private Place currentPlace;
	private String token;
	private final static FinanceDataRequestAsync financeRequirementsRequest = 
			GWT.create(FinanceDataRequest.class);
	private final static UserServicesRequestAsync userservices = 
			  GWT.create(UserServicesRequest.class);

	/*
	 * ---Presenter Procedure---
	 */
	private void getData(final RequestDetailView requestDetailView) {
		financeRequirementsRequest.find(token, new AsyncCallback<FinanceData>() {
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				clientFactory.getPlaceController().goTo(new FinanceRequirementPlace(null));
				eventBus.fireEvent(new LoadFailEvent());
			}
			@Override
			public void onSuccess(FinanceData result) {
				// TODO Auto-generated method stub
				if(result!=null&&!result.getStatus().equals("DRAFT"))
					requestDetailView.setData(result);
				else {
					eventBus.fireEvent(new BadRequestEvent());
					Timer timer = new Timer() {
					      public void run() {
					    	  if(currentPlace instanceof FinanceRequirementPlace)
									clientFactory.getPlaceController().goTo(new FinanceRequirementPlace(null));
					    	  if(currentPlace instanceof TrashPlace)
									clientFactory.getPlaceController().goTo(new TrashPlace(null));
					      }
					};
					timer.schedule(1000);
					
				}
			}
		});
	}
	
	private void checkUserRole(final RequestDetailView requestDetailView) {
		boolean isManager = clientFactory.getLocalStorage().isManager();
		requestDetailView.setUserRole(isManager);
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
	
	/*
	 * Automatic invoke when activity is start.
	 */
	@Override
	public void start(AcceptsOneWidget containerView, EventBus eventBus) {
		if(currentPlace instanceof FinanceRequirementPlace) {
			RequestDetailView requestDetailView = clientFactory.getRequestDetailView();
			requestDetailView.setPresenter(this);
			
			eventBus.fireEvent(new InFinanceRequirementPlaceEvent());
			//eventBus.fireEvent(new InSubPlaceEvent());
			
			checkUserRole(requestDetailView);
			getData(requestDetailView);
			containerView.setWidget(requestDetailView.asWidget());
			
			String id = token;
			if(clientFactory.getLocalStorage().getUpdateDataList().containsKey(id)) {
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
				eventBus.fireEvent(new TransmitNotificationEvent());
			}
		}
		
		if(currentPlace instanceof TrashPlace)
			Window.alert("In DesktopTrashView Place");
	}
	
	/*
	 * Automatic invoke when activity is start.
	 */
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
		final FinanceData fr = requestDetailView.getData();
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
		final FinanceData fr = requestDetailView.getData();
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
		final FinanceData fr = requestDetailView.getData();
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
							eventBus.fireEvent(new SendBackRequestEvent());
							Timer timer = new Timer() {
							      public void run() {
							    	  clientFactory.getPlaceController().goTo(new FinanceRequirementPlace(null));
							      }
							};
							timer.schedule(1000);
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
					eventBus.fireEvent(new SendBackRequestEvent());
					Timer timer = new Timer() {
					      public void run() {
					    	  clientFactory.getPlaceController().goTo(new FinanceRequirementPlace(null));
					      }
					};
					timer.schedule(1000);
				}
			});
		}
	}

}

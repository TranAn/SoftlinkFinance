package com.softlink.finance.client.view.desktop;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DeckLayoutPanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.softlink.datastore.model.FinanceData;
import com.softlink.finance.client.ClientFactory;
import com.softlink.finance.client.FinanceView;
import com.softlink.finance.client.events.ApprovedRequestEvent;
import com.softlink.finance.client.events.BadRequestEvent;
import com.softlink.finance.client.events.CreateRequestFailEvent;
import com.softlink.finance.client.events.CreateRequestSuccessEvent;
import com.softlink.finance.client.events.DeniedRequestEvent;
import com.softlink.finance.client.events.InConstructionPlaceEvent;
import com.softlink.finance.client.events.InDraftsPlaceEvent;
import com.softlink.finance.client.events.InFinanceRequirementPlaceEvent;
import com.softlink.finance.client.events.InParentPlaceEvent;
import com.softlink.finance.client.events.InRequestDetailViewEvent;
import com.softlink.finance.client.events.InSubPlaceEvent;
import com.softlink.finance.client.events.InTrashPlaceEvent;
import com.softlink.finance.client.events.LoadFailEvent;
import com.softlink.finance.client.events.LoadSuccessEvent;
import com.softlink.finance.client.events.LoadingEvent;
import com.softlink.finance.client.events.RefreshingEvent;
import com.softlink.finance.client.events.SendBackRequestEvent;
import com.softlink.finance.client.events.TransmitNotificationEvent;
import com.softlink.finance.client.place.FinanceRequirementPlace;
import com.softlink.finance.client.request.FinanceDataRequest;
import com.softlink.finance.client.request.FinanceDataRequestAsync;

/**
 * Implement MainView for desktop version
 */
public class FinanceViewDesktop extends Composite implements FinanceView {

	interface DesktopFinanceViewUiBinder extends
			UiBinder<Widget, FinanceViewDesktop> {
	}

	private static DesktopFinanceViewUiBinder uiBinder = GWT
			.create(DesktopFinanceViewUiBinder.class);
	private final static FinanceDataRequestAsync financeRequirementsRequest = GWT
			.create(FinanceDataRequest.class);
	private Logger logger = Logger
			.getLogger(FinanceViewDesktop.class.getName());

	private ClientFactory clientFactory;
	private EventBus eventBus;

	private DesktopWelcomeView desktopWelcomeView = new DesktopWelcomeView();
	private DesktopRequestDialogView dlgRequest = new DesktopRequestDialogView();

	private boolean startupcomplete = false;

	@UiField
	DesktopTopPanelView topPanel;
	@UiField
	DesktopToolBarPanelView toolbarPanel;
	@UiField
	DesktopTabPanelView tabPanel;
	@UiField
	SplitLayoutPanel splitPanel;
	@UiField
	DeckLayoutPanel ContainerView;

	/*
	 * ---Procedure---
	 */
	/**
	 * Check the data.id exists or not in updateDataList
	 * 
	 * @param id
	 * @return <b>true</b> if id is new</br> <b>false</b> if id exists
	 */
	private boolean IDCheck(String id) {
		if (clientFactory.getLocalStorage().getUpdateDataList().containsKey(id))
			return false;
		return true;
	}

	/**
	 * Check the version of data.id is newest or not
	 * 
	 * @param id
	 * @param version
	 * @return <b>true</b> if version is new</br> <b>false</b> if updateDataList
	 *         have this version of the data
	 */
	private boolean VersionCheck(String id, String version) {
		FinanceData updateData = clientFactory.getLocalStorage()
				.getUpdateDataList().get(id);
		if (version.equals(String.valueOf(updateData.getVersion())))
			return false;
		return true;
	}

	private void loadMemcache() {
		financeRequirementsRequest
				.loadMemcache(new AsyncCallback<List<String>>() {
					@Override
					public void onFailure(Throwable caught) {
						startupcomplete = true;
					}

					@Override
					public void onSuccess(List<String> result) {
						// TODO Auto-generated method stub
						if (result == null || result.isEmpty())
							startupcomplete = true;
						else {
							clientFactory.getLocalStorage().setCachedlist(result);
							setUpdateDataList();
						}
					}
				});
	}
	
	private void markNotify(String dataID, String sender) {
		if(clientFactory.getLocalStorage().getCurrentUser().equals(sender)) {
			if(clientFactory.getLocalStorage().getNotifyList().containsKey(dataID)) 
				clientFactory.getLocalStorage().flushNotifyList(dataID);
			clientFactory.getLocalStorage().putToNotifyList(dataID, false);
		} else {
			if(clientFactory.getLocalStorage().getNotifyList().containsKey(dataID)) 
				clientFactory.getLocalStorage().flushNotifyList(dataID);
			clientFactory.getLocalStorage().putToNotifyList(dataID, true);
		}
	}

	private void setUpdateDataList() {
		// setup updateDataIDList
		List<String> cachedlist = clientFactory.getLocalStorage()
				.getCachedlist();
		List<String> updateDataIDList = new ArrayList<String>();
		String delimiter = "/";
		for (String epath : cachedlist) {
			String[] epaths = epath.split(delimiter);
			String classname = epaths[0];
			String id = epaths[1];
			String version = epaths[2];
			String sender = epaths[3];
			if (classname.equals("1")) {
				if (clientFactory.getLocalStorage().getUpdateDataList().isEmpty()) {
					updateDataIDList.add(id);
					markNotify(id,sender);
				}
				else {
					if (IDCheck(id)) {
						updateDataIDList.add(id);
						markNotify(id,sender);
					}
					else if (VersionCheck(id, version)) {
						updateDataIDList.add(id);
						markNotify(id,sender);
						clientFactory.getLocalStorage().flushUpdateDataListByKey(id);
					}
				}
			}
		}
		// Request updateDataList through updateDataIDList. If updateDataIDList
		// isEmpty,
		// the MainView doesn't have transmit notification to the other layout.
		if (!updateDataIDList.isEmpty()) {
			requestNewData(updateDataIDList);
		} else
			startupcomplete = true;
	}

	private void requestNewData(List<String> updateDataIDList) {
		financeRequirementsRequest.loadUpdateDataList(updateDataIDList,
				new AsyncCallback<List<FinanceData>>() {
					@Override
					public void onFailure(Throwable caught) {
						startupcomplete = true;
					}

					@Override
					public void onSuccess(List<FinanceData> result) {
						// TODO Auto-generated method stub
						if (!result.isEmpty()) {
							for (FinanceData updateData : result) {
								clientFactory.getLocalStorage().putToUpdateDataList(updateData);
							}
							startupcomplete = true;
							eventBus.fireEvent(new TransmitNotificationEvent());
						}
					}
				});
	}

	private void Polling() {
		Timer elapsedTimer = new Timer() {
			public void run() {
				loadMemcache();
			}
		};
		elapsedTimer.scheduleRepeating(10000);
	}
	
	private void startupLoading() {
		// store current user name
		financeRequirementsRequest.getCurrentUserName(new AsyncCallback<String>() {
			@Override
			public void onFailure(Throwable caught) {}
			@Override
			public void onSuccess(String result) {
				clientFactory.getLocalStorage().setCurrentUser(result);
			}
		});
		// store role of user
		financeRequirementsRequest.isManagementUser(new AsyncCallback<Boolean>() {
			@Override
			public void onFailure(Throwable caught) {}
			@Override
			public void onSuccess(Boolean result) {
				clientFactory.getLocalStorage().setManager(result);
			}
		});
		financeRequirementsRequest.list_fr(new AsyncCallback<List<FinanceData>>() {
			public void onFailure(Throwable caught) {}
			public void onSuccess(List<FinanceData> result) {
				if(clientFactory.getFinanceRequirementView().getDataCount()==0)
				clientFactory.getFinanceRequirementView().setNewData(result);
			}
		});
	}

	/*
	 * Constructor
	 */
	public FinanceViewDesktop(ClientFactory clientFactory) {
		initWidget(uiBinder.createAndBindUi(this));
		this.clientFactory = clientFactory;
		this.eventBus = clientFactory.getEventBus();

		setHandlerEventBus();

		topPanel.setController(clientFactory);
		toolbarPanel.setController(clientFactory);
		tabPanel.setController(clientFactory);
		dlgRequest.setController(clientFactory);

		splitPanel.setWidgetMinSize(tabPanel, 222);
		topPanel.setLogoutState();
		toolbarPanel.setDesktopRequestDialogView(dlgRequest);

		ContainerView.add(desktopWelcomeView);
		ContainerView.add(clientFactory.getFinanceRequirementView());
		ContainerView.add(clientFactory.getDraftsView());
		ContainerView.add(clientFactory.getTrashView());
		ContainerView.add(clientFactory.getRequestDetailView());
		ContainerView.add(clientFactory.getUnderConstructionView());
		ContainerView.setAnimationVertical(true);
		ContainerView.setAnimationDuration(600);

		startupLoading();
		Polling();
	}
	
	private void waitToRefresh() {
		Timer timer = new Timer() {
		      public void run() {
		    	  loadMemcache();
		      }
		};
		timer.schedule(1000);
	}

	/*
	 * ---EventBus Handler---
	 */
	public void setHandlerEventBus() {
		eventBus.addHandler(ApprovedRequestEvent.TYPE,
				new ApprovedRequestEvent.Handler() {
					@Override
					public void onApprovedRequest(ApprovedRequestEvent event) {
						// TODO Auto-generated method stub
						toolbarPanel.setVisibleNotice();
						toolbarPanel.setTextNotice("Request has been approved!");
						waitToRefresh();
					}
				});
		eventBus.addHandler(CreateRequestFailEvent.TYPE,
				new CreateRequestFailEvent.Handler() {
					@Override
					public void onCreateRequestFail(CreateRequestFailEvent event) {
						// TODO Auto-generated method stub
						toolbarPanel.setVisibleNotice();
						toolbarPanel.setTextNotice("Send request failure!");
					}
				});
		eventBus.addHandler(CreateRequestSuccessEvent.TYPE,
				new CreateRequestSuccessEvent.Handler() {
					@Override
					public void onCreateRequestSuccess(
							CreateRequestSuccessEvent event) {
						// TODO Auto-generated method stub
						toolbarPanel.setVisibleNotice();
						toolbarPanel.setTextNotice("Request has been send!");
						waitToRefresh();
					}
				});
		eventBus.addHandler(DeniedRequestEvent.TYPE,
				new DeniedRequestEvent.Handler() {
					@Override
					public void onDeniedRequest(DeniedRequestEvent event) {
						// TODO Auto-generated method stub
						toolbarPanel.setVisibleNotice();
						toolbarPanel.setTextNotice("Request has been denied!");
						waitToRefresh();
					}
				});
		eventBus.addHandler(InConstructionPlaceEvent.TYPE,
				new InConstructionPlaceEvent.Handler() {
					@Override
					public void inConstructionPlace(
							InConstructionPlaceEvent event) {
						// TODO Auto-generated method stub
						toolbarPanel.setDefault();
					}
				});
		eventBus.addHandler(InDraftsPlaceEvent.TYPE,
				new InDraftsPlaceEvent.Handler() {
					@Override
					public void inDraftsPlace(InDraftsPlaceEvent event) {
						// TODO Auto-generated method stub
						toolbarPanel.setDraftsToolBar();
						tabPanel.setDraftStyle();
					}
				});
		eventBus.addHandler(InFinanceRequirementPlaceEvent.TYPE,
				new InFinanceRequirementPlaceEvent.Handler() {
					@Override
					public void inFinanceRequirementPlace(
							InFinanceRequirementPlaceEvent event) {
						// TODO Auto-generated method stub
						toolbarPanel.setFinanceRequirementToolBar();
						tabPanel.setFinancialRequirementStyle();
					}
				});
		eventBus.addHandler(InRequestDetailViewEvent.TYPE,
				new InRequestDetailViewEvent.Handler() {
					@Override
					public void inRequestDetailView(
							InRequestDetailViewEvent event) {
						// TODO Auto-generated method stub
						toolbarPanel.setRequestDetailToolBar();
					}
				});
		eventBus.addHandler(InTrashPlaceEvent.TYPE,
				new InTrashPlaceEvent.Handler() {
					@Override
					public void inTrashPlace(InTrashPlaceEvent event) {
						// TODO Auto-generated method stub
						toolbarPanel.setTrashToolBar();
						tabPanel.setTrashStyle();
					}
				});
		eventBus.addHandler(LoadFailEvent.TYPE, new LoadFailEvent.Handler() {
			@Override
			public void onLoadFail(LoadFailEvent event) {
				// TODO Auto-generated method stub
				toolbarPanel.setVisibleNotice();
				toolbarPanel.setTextNotice("Load failure!");
			}
		});
		eventBus.addHandler(LoadingEvent.TYPE, new LoadingEvent.Handler() {
			@Override
			public void onLoading(LoadingEvent event) {
				// TODO Auto-generated method stub
				toolbarPanel.setVisibleNotice();
				toolbarPanel.setTextNotice("Loading...");
			}
		});
		eventBus.addHandler(LoadSuccessEvent.TYPE,
				new LoadSuccessEvent.Handler() {
					@Override
					public void onLoadSuccess(LoadSuccessEvent event) {
						// TODO Auto-generated method stub
						toolbarPanel.setHideNotice();
					}
				});
		eventBus.addHandler(RefreshingEvent.TYPE,
				new RefreshingEvent.Handler() {
					@Override
					public void onRefreshing(RefreshingEvent event) {
						// TODO Auto-generated method stub
						toolbarPanel.setVisibleNotice();
						toolbarPanel.setTextNotice("Loading...");
					}
				});
		eventBus.addHandler(SendBackRequestEvent.TYPE,
				new SendBackRequestEvent.Handler() {
					@Override
					public void onSendBackRequest(SendBackRequestEvent event) {
						// TODO Auto-generated method stub
						toolbarPanel.setVisibleNotice();
						toolbarPanel.setTextNotice("Request has been send back to requester!");
						waitToRefresh();
					}
				});
		eventBus.addHandler(InSubPlaceEvent.TYPE,
				new InSubPlaceEvent.Handler() {
					@Override
					public void inSubPlace(InSubPlaceEvent event) {
						// TODO Auto-generated method stub
						ContainerView.setAnimationVertical(false);
					}
				});
		eventBus.addHandler(InParentPlaceEvent.TYPE,
				new InParentPlaceEvent.Handler() {
					@Override
					public void inParentPlace(InParentPlaceEvent event) {
						// TODO Auto-generated method stub
						ContainerView.setAnimationVertical(true);
					}
				});
		eventBus.addHandler(BadRequestEvent.TYPE,
				new BadRequestEvent.Handler() {
					@Override
					public void onBadRequest(BadRequestEvent event) {
						// TODO Auto-generated method stub
						toolbarPanel.setVisibleNotice();
						toolbarPanel.setTextNotice("Your request is no longer exist!");
					}
				});
		eventBus.addHandler(TransmitNotificationEvent.TYPE,
				new TransmitNotificationEvent.Handler() {
					@Override
					public void onTransmitNotification(
							TransmitNotificationEvent event) {
						// TODO Auto-generated method stub
						topPanel.setNotifyStyle();
						tabPanel.setNotifyStyle();
						clientFactory.getFinanceRequirementView().onUpdate();
					}
				});
	}

	/*
	 * --- Implement View Interface---
	 */
	@Override
	public DeckLayoutPanel getContainerView() {
		// TODO Auto-generated method stub
		return ContainerView;
	}

	@Override
	public void setDefaultPlace() {
		// TODO Auto-generated method stub
		loadMemcache();
		if (clientFactory.getPlaceController().getWhere() == null) {
			ContainerView.setAnimationDuration(0);
			ContainerView.setWidget(desktopWelcomeView);
			toolbarPanel.setDisable();
			tabPanel.setDisable();
			Timer timer = new Timer() {
				public void run() {
					if (startupcomplete) {
						if (clientFactory.getPlaceController().getWhere() == null) {
							ContainerView.setAnimationDuration(600);
							toolbarPanel.setEnable();
							tabPanel.setEnable();
							clientFactory.getPlaceController().goTo(
									new FinanceRequirementPlace(null));
						} else {
							ContainerView.setAnimationDuration(600);
							toolbarPanel.setEnable();
							tabPanel.setEnable();
						}
					}
				}
			};
			timer.scheduleRepeating(1000);
		}
	}

}

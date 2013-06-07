/*
 * Copyright 2007 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.softlink.finance.client.view.desktop;

import java.util.Collection;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DeckLayoutPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.softlink.datastore.model.FinanceData;
import com.softlink.finance.client.ClientFactory;
import com.softlink.finance.client.request.UserServicesRequest;
import com.softlink.finance.client.request.UserServicesRequestAsync;

/**
 * The top panel, which contains the 'welcome' message and various links.
 */
public class DesktopTopPanelView extends Composite {

	interface Binder extends UiBinder<Widget, DesktopTopPanelView> {
	}

	private static final Binder binder = GWT.create(Binder.class);

	private final static UserServicesRequestAsync userservices = GWT
			.create(UserServicesRequest.class);

	private ClientFactory clientFactory;
	private EventBus eventBus;

	private DesktopSearchTextBoxView desktopSearchTextBoxView = new DesktopSearchTextBoxView();
	private SimplePanel blankPanel = new SimplePanel();
	private boolean searchOn = false;
	String thisURL = Window.Location.getPath();

	@UiField
	Anchor signLink;
	@UiField
	Anchor aboutLink;
	@UiField
	Label userlabel;
	@UiField
	MenuItem homeMenu;
	@UiField
	MenuItem searchMenu;
	@UiField
	MenuItem userguideMenu;
	@UiField
	MenuItem financeMenu;
	@UiField
	MenuItem taskmanagerMenu;
	@UiField
	MenuItem supportMenu;
	@UiField
	Image notificationbtn;
	@UiField
	DeckLayoutPanel searchtxb;
	@UiField
	Image userCenterbtn;
	@UiField
	Label notifyLabel;
	@UiField Image notifyflag;

	/*
	 * ---Procedure---
	 */
	public void setController(ClientFactory clientFactory) {
		this.eventBus = clientFactory.getEventBus();
		this.clientFactory = clientFactory;
	}

	public void setUpMenu() {
		final String hostname = Window.Location.getHostName();
		final String port = Window.Location.getPort();

		Command homeMenuCommand = new Command() {
			public void execute() {
				Window.Location.assign("/");
			}
		};
		homeMenu.setCommand(homeMenuCommand);

		Command financeMenuCommand = new Command() {
			public void execute() {
				String redirectURL = "http://" + hostname + ":" + port
						+ "/a/finance/";
				Window.Location.assign(redirectURL);
			}
		};
		financeMenu.setCommand(financeMenuCommand);

		Command searchMenuCommand = new Command() {
			public void execute() {
				if (!searchOn) {
					searchOn = true;
					searchtxb.setWidget(desktopSearchTextBoxView);
					desktopSearchTextBoxView.setFocus();
				} else {
					searchOn = false;
					searchtxb.setWidget(blankPanel);
				}
			}
		};
		searchMenu.setCommand(searchMenuCommand);
	}
	
	public void setNotifyStyle() {
		// check new record
		int newNotify = 0;
		Collection<FinanceData> updateList = clientFactory.getLocalStorage()
				.getUpdateDataList().values();
		Map<String, Boolean> notifyList = clientFactory.getLocalStorage()
				.getNotifyList();
		for (FinanceData updateData : updateList) {
			if (!updateData.getStatus().equals("DRAFT")
					&& !updateData.getStatus().equals("DELETED")) {
				String id = String.valueOf(updateData.getRequest_id());
				if(notifyList.containsKey(id))
					if(notifyList.get(id))
						newNotify++;
			}
		}
		// set style
		if(newNotify==0) {
			notifyflag.setVisible(false);
			notifyLabel.setVisible(false);
		}
		else {
			notifyflag.setVisible(true);
			notifyLabel.setVisible(true);
			notifyLabel.setText(String.valueOf(newNotify));
		}
			
	}

	/*
	 * ---Constructor---
	 */
	public DesktopTopPanelView() {
		initWidget(binder.createAndBindUi(this));

		desktopSearchTextBoxView
				.setListener(new DesktopSearchTextBoxView.Listener() {
					@Override
					public void hideSearchTextBox() {
						// TODO Auto-generated method stub
						searchtxb.setWidget(blankPanel);
						Timer timer = new Timer() {
							public void run() {
								searchOn = false;
							}
						};
						timer.schedule(500);
					}
				});

		searchtxb.add(desktopSearchTextBoxView);
		searchtxb.add(blankPanel);
		searchtxb.setWidget(blankPanel);

		setUpMenu();
	}

	/*
	 * ---Request---
	 */
	public void setLoginState() {
		userlabel.setStyleName("loginlabel");
		userlabel.setText("You are not sign in!");
		signLink.setText("Sign In");
		userservices.setLogin(thisURL, new AsyncCallback<String>() {
			public void onFailure(Throwable caught) {
			}

			public void onSuccess(String result) {
				signLink.setHref(result);
			}
		});
	}

	public void setLogoutState() {
		userlabel.setStyleName("logoutlabel");
		userservices.getUserEmail(new AsyncCallback<String>() {
			public void onFailure(Throwable caught) {
			}

			public void onSuccess(String result) {
				userlabel.setText(result);
			}
		});
		signLink.setText("Sign Out");
		userservices.setLogout("/", new AsyncCallback<String>() {
			public void onFailure(Throwable caught) {
			}

			public void onSuccess(String result) {
				signLink.setHref(result);
			}
		});
	}

	/*
	 * ---Event Handler---
	 */
	@UiHandler("aboutLink")
	void onAboutClicked(ClickEvent event) {
		// When the 'About' item is selected, show the DesktopAboutDialogView.
		// Note that showing a dialog box does not block -- execution continues
		// normally, and the dialog fires an event when it is closed.
		DesktopAboutDialogView dlg = new DesktopAboutDialogView();
		dlg.show();
		dlg.center();
	}

	@UiHandler("userCenterbtn")
	void onUserCenterbtnClick(ClickEvent event) {
		final String hostname = Window.Location.getHostName();
		final String port = Window.Location.getPort();
		String redirectURL = "http://" + hostname + ":" + port
				+ "/a/finance/user/";
		Window.Location.assign(redirectURL);
	}
}

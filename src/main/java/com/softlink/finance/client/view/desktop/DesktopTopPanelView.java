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

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.Widget;
import com.softlink.finance.client.request.UserServicesRequest;
import com.softlink.finance.client.request.UserServicesRequestAsync;

/**
 * The top panel, which contains the 'welcome' message and various links.
 */
public class DesktopTopPanelView extends Composite {

	  interface Binder extends UiBinder<Widget, DesktopTopPanelView> { }
	  private static final Binder binder = GWT.create(Binder.class);
	  
	  private final static UserServicesRequestAsync userservices = 
			  GWT.create(UserServicesRequest.class);
	  private EventBus eventBus;
	  String thisURL = Window.Location.getPath();
	  
	  @UiField Anchor signLink;
	  @UiField Anchor aboutLink;
	  @UiField Label userlabel;
	  @UiField MenuItem homeMenu;
	  @UiField MenuItem financialMenu;
	  @UiField MenuItem userMenu;
  
	   	/*
	   	 * Constructor---
	   	 */
	 	@SuppressWarnings("deprecation")
	  	public DesktopTopPanelView() {
	  		initWidget(binder.createAndBindUi(this));
	  		setUpMenu();
	 	}
 	
	 	/*
	 	 * Procedure---
	 	 */
	 	public void setEventBus(EventBus eventBus) {
	 		this.eventBus = eventBus;
	 	}
	 	
	 	public void setUpMenu() {
	 		final String hostname = Window.Location.getHostName();
	 	    final String port = Window.Location.getPort();
	 	    
	 		Command homeMenuCommand = new Command() {
		        public void execute() {
		        	Window.Location.replace("/");
		        }
		    };
		    homeMenu.setCommand(homeMenuCommand);
		    
		    Command financialMenuCommand = new Command() {
		        public void execute() {
		        	String replaceUrl = "http://"+hostname+":"+port+"/a/finance/";
		        	Window.Location.replace(replaceUrl);
		        }
		    };
		    financialMenu.setCommand(financialMenuCommand);
		    
		    Command userMenuCommand = new Command() {
		        public void execute() {
		        	String replaceUrl = "http://"+hostname+":"+port+"/a/finance/user/";
		        	Window.Location.replace(replaceUrl);
		        }
		    };
		    userMenu.setCommand(userMenuCommand);
	 	}
 	
	  /*
	   * Request---
	   */
	  public void setLoginState() {
		  userlabel.setStyleName("loginlabel");
		  userlabel.setText("You are not sign in!");
		  signLink.setText("Sign In");
		  userservices.setLogin(thisURL, new AsyncCallback<String>() {
				public void onFailure(Throwable caught) {} 
				public void onSuccess(String result) {
					signLink.setHref(result);
				}
		  });
	  }
	  
	  public void setLogoutState() {
		  userlabel.setStyleName("logoutlabel");
		  userservices.getUserEmail(new AsyncCallback<String>() {
				public void onFailure(Throwable caught) {}
				public void onSuccess(String result) {
					userlabel.setText("Welcome, "+result);
				}
		  });
		  signLink.setText("Sign Out");
		  userservices.setLogout("/", new AsyncCallback<String>() {
				public void onFailure(Throwable caught) {} 
				public void onSuccess(String result) {
					signLink.setHref(result);
				}
		  });
	  }
  
	  /*
	   * Event Handler---
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
}

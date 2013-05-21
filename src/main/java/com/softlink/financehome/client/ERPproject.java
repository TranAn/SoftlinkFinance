package com.softlink.financehome.client;


import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.softlink.financehome.client.request.UserServicesRequest;
import com.softlink.financehome.client.request.UserServicesRequestAsync;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class ERPproject implements EntryPoint {
	
	private final static UserServicesRequestAsync userservices = 
			  GWT.create(UserServicesRequest.class);
	String thisURL = Window.Location.getPath();
	
	public void onModuleLoad() {
		RootPanel rootPanel = RootPanel.get();
		
		AbsolutePanel absolutePanel = new AbsolutePanel();
		rootPanel.add(absolutePanel,0,0);
		absolutePanel.setSize("100%", "99%");
		
		AbsolutePanel absolutePanel_3 = new AbsolutePanel();
		absolutePanel_3.setStyleName("footer");
		absolutePanel.add(absolutePanel_3, 0, 360);
		absolutePanel_3.setSize("100%", "100%%");
		
		AbsolutePanel absolutePanel_2 = new AbsolutePanel();
		absolutePanel_2.setStyleName("header");
		absolutePanel.add(absolutePanel_2, 0, 0);
		absolutePanel_2.setSize("100%", "300px");
		
		AbsolutePanel absolutePanel_1 = new AbsolutePanel();
		absolutePanel.add(absolutePanel_1, 300, 120);
		absolutePanel_1.setStyleName("background");
		absolutePanel_1.setSize("738px", "421px");
		
		Label lblNewLabel = new Label("ITPRO-ERP");
		lblNewLabel.setStyleName("title");
		absolutePanel_1.add(lblNewLabel, 269, 33);
		
		final Anchor signIn = new Anchor("Sign In");
		signIn.setStyleName("anchor");
		absolutePanel_1.add(signIn, 320, 133);
		
		final Anchor support = new Anchor("Support");
		support.setStyleName("anchor");
		absolutePanel_1.add(support, 316, 200);
		
		final Anchor about = new Anchor("About");
		about.setStyleName("anchor");
		absolutePanel_1.add(about, 326, 267);
		
		final Anchor signOut = new Anchor("Sign Out");
		signOut.setVisible(false);
		signOut.setStyleName("anchor");
		absolutePanel_1.add(signOut, 320, 334);
		
		final Label labelModules = new Label("Modules");
		labelModules.setVisible(false);
		labelModules.setStyleName("module");
		absolutePanel_1.add(labelModules, 316, 117);
		
		final Button financialButton = new Button();
		financialButton.setVisible(false);
		financialButton.setStyleName("sendButton");
		financialButton.setText("Finance");
		absolutePanel_1.add(financialButton, 241, 166);
		financialButton.setSize("111px", "79px");
		
		final Button userButton = new Button();
		userButton.setVisible(false);
		userButton.setText("User");
		userButton.setStyleName("sendButton");
		absolutePanel_1.add(userButton, 370, 166);
		userButton.setSize("111px", "79px");
		
		userservices.isUserLoggedIn(new AsyncCallback<Boolean>() {
			public void onFailure(Throwable caught) {}
			public void onSuccess(Boolean result) {
				if(result==true){
					signIn.setVisible(false);
					support.setVisible(false);
					about.setVisible(false);
					labelModules.setVisible(true);
					financialButton.setVisible(true);
					userButton.setVisible(true);
					signOut.setVisible(true);
					userservices.setLogout(thisURL, new AsyncCallback<String>() {
						public void onFailure(Throwable caught) {} 
						public void onSuccess(String result) {
							signOut.setHref(result);
						}
					});
				}
				if(result==false){
					labelModules.setVisible(false);
					financialButton.setVisible(false);
					userButton.setVisible(false);
					signOut.setVisible(false);
					signIn.setVisible(true);
					support.setVisible(true);
					about.setVisible(true);
					userservices.setLogin(thisURL, new AsyncCallback<String>() {
						public void onFailure(Throwable caught) {} 
						public void onSuccess(String result) {
							signIn.setHref(result);
						}
					});
				}
			}
	    });
		
		financialButton.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				Window.Location.replace("a/finance/");
			}
		});
		
		userButton.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				Window.Location.replace("a/finance/user/");
			}
		});
		
		about.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				AboutDialog dlg = new AboutDialog();
			    dlg.show();
			    dlg.center();
			}
		});
	}
}

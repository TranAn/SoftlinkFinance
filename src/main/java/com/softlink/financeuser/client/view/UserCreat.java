package com.softlink.financeuser.client.view;

import java.util.Date;


import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.Label;
import com.softlink.financeuser.client.request.*;
import com.softlink.datastore.model.FinanceUser;

public class UserCreat extends Composite {

	interface Binder extends UiBinder<Widget, UserCreat> { }
	private static final Binder binder = GWT.create(Binder.class);
	private final FinanceUserRequestAsync SeriUserObj = GWT
			  .create(FinanceUserRequest.class);
	private Timer elapsedTimer = null;
	
	@UiField TextBox username;
	@UiField ListBox role;
	@UiField Button creatuserButton;
	@UiField Label infolabel;
	@UiField Button refreshButton;
	
	public UserCreat() {
		initWidget(binder.createAndBindUi(this));
		role.addItem("Officer");
		role.addItem("Member");
		infolabel.setVisible(false);
		elapsedTimer = new Timer () {
			 public void run() {
				infolabel.setVisible(false);
		  	 }
		};
		elapsedTimer.scheduleRepeating(6000);
	}
	
	@UiHandler("creatuserButton")
	void onCreatuserButtonClick(ClickEvent event) {
		FinanceUser user = new FinanceUser();
		user.setUsername(username.getText());
		user.setRole(role.getValue(role.getSelectedIndex()));
		Date date = new Date(2000);
		user.setTimework(date);
		SeriUserObj.insert(user, new AsyncCallback<Void>(){
			@Override
			public void onFailure(Throwable caught) {
				infolabel.setText("creat fail, connection has been interupt!");
				infolabel.setVisible(true);
			}
			@Override
			public void onSuccess(Void result) {
				infolabel.setText("New user has been created!");
				infolabel.setVisible(true);
			}
		});
	}
	
	@UiHandler("refreshButton")
	void onRefreshButtonClick(ClickEvent event) {
		username.setText("");
		infolabel.setVisible(false);
	}
}

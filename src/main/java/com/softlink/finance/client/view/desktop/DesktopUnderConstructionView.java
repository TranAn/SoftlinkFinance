package com.softlink.finance.client.view.desktop;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.softlink.finance.client.view.UnderConstructionView;

public class DesktopUnderConstructionView extends Composite 
	implements UnderConstructionView{

	interface Binder extends UiBinder<Widget, DesktopUnderConstructionView> { }
	private static final Binder binder = GWT.create(Binder.class);

	private Presenter listener;
	private Timer elapsedTimer = null;
	private int countDown;
	
	@UiField Anchor goBackbtn;
	@UiField Label countDownLabel;
	
	/*
	 * ---Constructor---
	 */
	public DesktopUnderConstructionView() {
		initWidget(binder.createAndBindUi(this));
	}

	/*
	 * ---Event Handler---
	 */
	@UiHandler("goBackbtn")
	void onGoBackbtnClick(ClickEvent event) {
		elapsedTimer.cancel();
		listener.goToPreviousPlace();
	}

	/*
	 * ---Implement View Interface---
	 */
	@Override
	public void setPresenter(Presenter listener) {
		// TODO Auto-generated method stub
		this.listener = listener;
	}


	@Override
	public void startCountDown() {
		// TODO Auto-generated method stub
		countDown = 3;
		countDownLabel.setText(String.valueOf(countDown));
		elapsedTimer = new Timer () {
			 public void run() {
				 countDownLabel.setText(String.valueOf(countDown));
				 countDown--;
				 if(countDown==-1) {
					 elapsedTimer.cancel();
					 listener.goToPreviousPlace();
				 } 
			 }
		 };
		 elapsedTimer.scheduleRepeating(1000);
	}
}

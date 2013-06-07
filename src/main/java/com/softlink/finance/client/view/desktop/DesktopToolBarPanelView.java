package com.softlink.finance.client.view.desktop;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.softlink.finance.client.ClientFactory;

public class DesktopToolBarPanelView extends Composite{

	interface Binder extends UiBinder<Widget, DesktopToolBarPanelView> { }
	private static final Binder binder = GWT.create(Binder.class);
	
	private ClientFactory clientFactory;
	private EventBus eventBus;
	
	private DesktopRequestDialogView dlg;
	private Timer elapsedTimer = null;
	
	@UiField Button request;
	@UiField Label noticelabel;
	@UiField Button backbutton;
	@UiField AbsolutePanel toolbar;
	@UiField Label defaulttitle;
	@UiField Button markbutton;
	@UiField Button refreshbutton;
	@UiField Button printbutton;
	@UiField Button settingbutton;
	@UiField Button deletebutton;
	@UiField Label datelabel;
	
	/*
	 * ---Procedure---
	 */
	public void setDisable() {
		request.setEnabled(false);
	}
	
	public void setEnable() {
		request.setEnabled(true);
	}

	/*
	 * ---Constructor---
	 */
	public DesktopToolBarPanelView() {
		initWidget(binder.createAndBindUi(this));
		noticelabel.setVisible(false);
		setDefault();
		final Date date = new Date();
		elapsedTimer = new Timer () {
			 public void run() {
				date.setTime(System.currentTimeMillis());
				datelabel.setText(date.toString());
			 }
		 };
		 elapsedTimer.scheduleRepeating(1000);
	}
	
	/*
	 * ---Procedure---
	 */
	public void setController(ClientFactory clientFactory){
		this.eventBus = clientFactory.getEventBus();
		this.clientFactory = clientFactory;
	}
	
	public void setDesktopRequestDialogView(DesktopRequestDialogView dlg) {
		this.dlg = dlg;
	}
	
	public void setTextNotice(String notice) {
		noticelabel.setText(notice);
	}
	
	public void setVisibleNotice() {
		noticelabel.setVisible(true);
	}
	
	public void setHideNotice() {
		noticelabel.setVisible(false);
	}
	
	public void setDefault() {
		noticelabel.setVisible(false);
		removeAllTool();
		toolbar.add(defaulttitle,230,30);
	}
	
	public void setRequestDetailToolBar() {
		removeAllTool();
		toolbar.add(backbutton,230,30);
		toolbar.add(refreshbutton,310,30);
		toolbar.add(printbutton,390,30);
		toolbar.add(deletebutton,470,30);
		deletebutton.setTitle("Move To Trash");
	}
	
	public void setFinanceRequirementToolBar() {
		removeAllTool();
		toolbar.add(markbutton,230,30);
		toolbar.add(refreshbutton,310,30);
		toolbar.add(printbutton,390,30);
		toolbar.add(settingbutton,470,30);
	}
	
	public void setDraftsToolBar() {
		removeAllTool();
		toolbar.add(markbutton,230,30);
		toolbar.add(refreshbutton,310,30);
		toolbar.add(settingbutton,390,30);
	}
	
	public void setTrashToolBar() {
		removeAllTool();
		toolbar.add(markbutton,230,30);
		toolbar.add(deletebutton,310,30);
		refreshbutton.setTitle("Restore");
		toolbar.add(refreshbutton,390,30);
		toolbar.add(settingbutton,470,30);
		deletebutton.setTitle("Remove Request");
	}
	
	public void setTrashDetailToolBar() {
		removeAllTool();
		toolbar.add(backbutton,230,30);
		toolbar.add(deletebutton,310,30);
		refreshbutton.setTitle("Restore");
		toolbar.add(refreshbutton,390,30);
	}
	
	public void removeAllTool() {
		toolbar.remove(defaulttitle);
		toolbar.remove(markbutton);
		toolbar.remove(backbutton);
		toolbar.remove(refreshbutton);
		toolbar.remove(printbutton);
		toolbar.remove(settingbutton);
		toolbar.remove(deletebutton);
		refreshbutton.setTitle("Refresh");
	}

	/*
	 * ---Event Handler---
	 */
	@UiHandler("request")
	void onRequestClick(ClickEvent event) {
		noticelabel.setText("");
		noticelabel.setVisible(false);
		dlg.refresh();
		dlg.setPopupPosition(Window.getClientWidth()-775,Window.getClientHeight()-450);
		dlg.show();
	}

	@UiHandler("backbutton")
	void onBackButtonClick(ClickEvent event) {
//		if(state==5)
//			if(FinancialRequirement_listener!=null)
//				FinancialRequirement_listener.onBackButtonClick();
//		if(state==8)
//			if(Trash_listener!=null)
//				Trash_listener.onBackButtonClick();
	}
	
	@UiHandler("refreshbutton")
	void onRefreshButtonClick(ClickEvent event) {
//		if(state==5)
//			if(FinancialRequirement_listener!=null)
//				FinancialRequirement_listener.onRefreshButtonClick();
//		if(state==7)
//			if(Draft_listener!=null)
//				Draft_listener.onRefreshButtonClick();
//		if(state==8)
//			if(Trash_listener!=null)
//				Trash_listener.onRefreshButtonClick();
	}
	
	@UiHandler("deletebutton")
	void onDeleteButtonClick(ClickEvent event) {
//		if(state==5)
//			if(FinancialRequirement_listener!=null)
//				FinancialRequirement_listener.onDeleteButtonClick();
//		if(state==8)
//			if(Trash_listener!=null)
//				Trash_listener.onDeleteButtonClick();
	}

}

package com.softlink.finance.widget;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
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

public class ToolBarPanel extends Composite{

	interface Binder extends UiBinder<Widget, ToolBarPanel> { }
	private static final Binder binder = GWT.create(Binder.class);
	private RequestDialog dlg = new RequestDialog();
	private Timer elapsedTimer = null;
	private int state = 0;
	
	private FinancialRequirement_Listener FinancialRequirement_listener;
	public interface FinancialRequirement_Listener {
		void onBackButtonClick();
		void onRefreshButtonClick();
		void onDeleteButtonClick();
	}
	public void setFinancialRequirement_Listener(FinancialRequirement_Listener listener) {
		this.FinancialRequirement_listener = listener;
	}
	
	private Draft_Listener Draft_listener;
	public interface Draft_Listener {
		void onRefreshButtonClick();
	}
	public void setDraft_Listener(Draft_Listener listener) {
		this.Draft_listener = listener;
	}
	
	private Trash_Listener Trash_listener;
	public interface Trash_Listener {
		void onBackButtonClick();
		void onDeleteButtonClick();
		void onRefreshButtonClick();
	}
	public void setTrash_Listener(Trash_Listener listener) {
		this.Trash_listener = listener;
	}
	
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

	public ToolBarPanel() {
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

	@UiHandler("request")
	void onRequestClick(ClickEvent event) {
		noticelabel.setText("");
		noticelabel.setVisible(false);
		dlg.refresh();
		dlg.setPopupPosition(Window.getClientWidth()-385,Window.getClientHeight()-620);
		dlg.show();
		dlg.setListener(new RequestDialog.Listener() {
			public void onSendSuccess() {
				noticelabel.setVisible(true);
				noticelabel.setText("Request has been sent!");
			}
			public void onSendFailure() {
				noticelabel.setVisible(true);
				noticelabel.setText("Request send failure, maybe the connection has been interrupt!");
			}
			@Override
			public void onDraftSuccess() {
				noticelabel.setVisible(true);
				noticelabel.setText("Request has been saved to Drafts!");
			}
		});
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
	
	public void setRequestDetailToolBar1() {
		removeAllTool();
		toolbar.add(backbutton,230,18);
		toolbar.add(refreshbutton,310,18);
		toolbar.add(printbutton,390,18);
	}
	
	public void setRequestDetailToolBar2() {
		removeAllTool();
		toolbar.add(backbutton,230,18);
		toolbar.add(refreshbutton,310,18);
		toolbar.add(printbutton,390,18);
		toolbar.add(deletebutton,470,18);
		deletebutton.setTitle("Move To Trash");
	}
	
	public void setFinancialRequirementToolBar() {
		removeAllTool();
		toolbar.add(markbutton,230,18);
		toolbar.add(refreshbutton,310,18);
		toolbar.add(printbutton,390,18);
		toolbar.add(settingbutton,470,18);
		state = 5;
	}
	
	public void setDraftsToolBar() {
		removeAllTool();
		toolbar.add(markbutton,230,18);
		toolbar.add(refreshbutton,310,18);
		toolbar.add(settingbutton,390,18);
		state = 7;
	}
	
	public void setTrashToolBar() {
		removeAllTool();
		toolbar.add(markbutton,230,18);
		toolbar.add(deletebutton,310,18);
		refreshbutton.setTitle("Restore");
		toolbar.add(refreshbutton,390,18);
		toolbar.add(settingbutton,470,18);
		deletebutton.setTitle("Remove Request");
		state = 8;
	}
	
	public void setTrashDetailToolBar() {
		removeAllTool();
		toolbar.add(backbutton,230,18);
		toolbar.add(deletebutton,310,18);
		refreshbutton.setTitle("Restore");
		toolbar.add(refreshbutton,390,18);
	}
	
	private void removeAllTool() {
		toolbar.remove(defaulttitle);
		toolbar.remove(markbutton);
		toolbar.remove(backbutton);
		toolbar.remove(refreshbutton);
		toolbar.remove(printbutton);
		toolbar.remove(settingbutton);
		toolbar.remove(deletebutton);
		refreshbutton.setTitle("Refresh");
	}

	//Event Handler-----------------------------------------
	@UiHandler("backbutton")
	void onBackButtonClick(ClickEvent event) {
		if(state==5)
			if(FinancialRequirement_listener!=null)
				FinancialRequirement_listener.onBackButtonClick();
		if(state==8)
			if(Trash_listener!=null)
				Trash_listener.onBackButtonClick();
	}
	
	@UiHandler("refreshbutton")
	void onRefreshButtonClick(ClickEvent event) {
		if(state==5)
			if(FinancialRequirement_listener!=null)
				FinancialRequirement_listener.onRefreshButtonClick();
		if(state==7)
			if(Draft_listener!=null)
				Draft_listener.onRefreshButtonClick();
		if(state==8)
			if(Trash_listener!=null)
				Trash_listener.onRefreshButtonClick();
	}
	
	@UiHandler("deletebutton")
	void onDeleteButtonClick(ClickEvent event) {
		if(state==5)
			if(FinancialRequirement_listener!=null)
				FinancialRequirement_listener.onDeleteButtonClick();
		if(state==8)
			if(Trash_listener!=null)
				Trash_listener.onDeleteButtonClick();
	}
	
}

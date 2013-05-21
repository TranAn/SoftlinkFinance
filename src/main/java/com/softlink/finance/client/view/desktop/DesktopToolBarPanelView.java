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
import com.softlink.finance.client.events.ApprovedRequestEvent;
import com.softlink.finance.client.events.CreateRequestFailEvent;
import com.softlink.finance.client.events.CreateRequestSuccessEvent;
import com.softlink.finance.client.events.DeniedRequestEvent;
import com.softlink.finance.client.events.InConstructionPlaceEvent;
import com.softlink.finance.client.events.InDraftsPlaceEvent;
import com.softlink.finance.client.events.InFinanceRequirementPlaceEvent;
import com.softlink.finance.client.events.InTrashPlaceEvent;
import com.softlink.finance.client.events.LoadFailEvent;
import com.softlink.finance.client.events.LoadSuccessEvent;
import com.softlink.finance.client.events.LoadingEvent;
import com.softlink.finance.client.events.RefreshingEvent;
import com.softlink.finance.client.events.SendBackRequestEvent;

public class DesktopToolBarPanelView extends Composite{

	interface Binder extends UiBinder<Widget, DesktopToolBarPanelView> { }
	private static final Binder binder = GWT.create(Binder.class);
	
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
	public void setEventBus(EventBus eventBus) {
		this.eventBus = eventBus;
		setHandlerEventBus();
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
		deletebutton.setTitle("Move To DesktopTrashView");
	}
	
	public void setFinanceRequirementToolBar() {
		removeAllTool();
		toolbar.add(markbutton,230,18);
		toolbar.add(refreshbutton,310,18);
		toolbar.add(printbutton,390,18);
		toolbar.add(settingbutton,470,18);
	}
	
	public void setDraftsToolBar() {
		removeAllTool();
		toolbar.add(markbutton,230,18);
		toolbar.add(refreshbutton,310,18);
		toolbar.add(settingbutton,390,18);
	}
	
	public void setTrashToolBar() {
		removeAllTool();
		toolbar.add(markbutton,230,18);
		toolbar.add(deletebutton,310,18);
		refreshbutton.setTitle("Restore");
		toolbar.add(refreshbutton,390,18);
		toolbar.add(settingbutton,470,18);
		deletebutton.setTitle("Remove Request");
	}
	
	public void setTrashDetailToolBar() {
		removeAllTool();
		toolbar.add(backbutton,230,18);
		toolbar.add(deletebutton,310,18);
		refreshbutton.setTitle("Restore");
		toolbar.add(refreshbutton,390,18);
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
	
	/*
	 * ---EventBus Handler---
	 */
	void setHandlerEventBus() {
		eventBus.addHandler(CreateRequestFailEvent.TYPE, new CreateRequestFailEvent.Handler() {
			@Override
			public void onCreateRequestFail(CreateRequestFailEvent event) {
				// TODO Auto-generated method stub
				noticelabel.setVisible(true);
				noticelabel.setText("Sent Request Failure!");
			}
		});
		eventBus.addHandler(CreateRequestSuccessEvent.TYPE, new CreateRequestSuccessEvent.Handler() {
			@Override
			public void onCreateRequestSuccess(CreateRequestSuccessEvent event) {
				// TODO Auto-generated method stub
				noticelabel.setVisible(true);
				noticelabel.setText("Request has been sent!");
			}
		});
		eventBus.addHandler(LoadingEvent.TYPE, new LoadingEvent.Handler() {
			@Override
			public void onLoading(LoadingEvent event) {
				noticelabel.setVisible(true);
				noticelabel.setText("Loading...");
			}
		});
		eventBus.addHandler(LoadFailEvent.TYPE, new LoadFailEvent.Handler() {
			@Override
			public void onLoadFail(LoadFailEvent event) {
				// TODO Auto-generated method stub
				noticelabel.setVisible(true);
				noticelabel.setText("Load Failure!");
			}
		});
		eventBus.addHandler(LoadSuccessEvent.TYPE, new LoadSuccessEvent.Handler() {
			@Override
			public void onLoadSuccess(LoadSuccessEvent event) {
				noticelabel.setVisible(false);
			}
		});
		eventBus.addHandler(InDraftsPlaceEvent.TYPE, new InDraftsPlaceEvent.Handler() {
			@Override
			public void inDraftsPlace(InDraftsPlaceEvent event) {
				// TODO Auto-generated method stub
				setDraftsToolBar();
			}
		});
		eventBus.addHandler(InFinanceRequirementPlaceEvent.TYPE, new InFinanceRequirementPlaceEvent.Handler() {
			@Override
			public void inFinanceRequirementPlace(
					InFinanceRequirementPlaceEvent event) {
				// TODO Auto-generated method stub
				setFinanceRequirementToolBar();
			}
		});
		eventBus.addHandler(InTrashPlaceEvent.TYPE, new InTrashPlaceEvent.Handler() {
			@Override
			public void inTrashPlace(InTrashPlaceEvent event) {
				// TODO Auto-generated method stub
				setTrashToolBar();
			}
		});
		eventBus.addHandler(InConstructionPlaceEvent.TYPE, new InConstructionPlaceEvent.Handler() {
			@Override
			public void inConstructionPlace(InConstructionPlaceEvent event) {
				// TODO Auto-generated method stub
				setDefault();
			}
		});
		eventBus.addHandler(RefreshingEvent.TYPE, new RefreshingEvent.Handler() {
			@Override
			public void onRefreshing(RefreshingEvent event) {
				// TODO Auto-generated method stub
				noticelabel.setVisible(true);
				noticelabel.setText("Refreshing...");
			}
		});
		eventBus.addHandler(ApprovedRequestEvent.TYPE, new ApprovedRequestEvent.Handler() {		
			@Override
			public void onApprovedRequest(ApprovedRequestEvent event) {
				// TODO Auto-generated method stub
				noticelabel.setVisible(true);
				noticelabel.setText("Request has been approved!");
			}
		});
		eventBus.addHandler(DeniedRequestEvent.TYPE, new DeniedRequestEvent.Handler() {		
			@Override
			public void onDeniedRequest(DeniedRequestEvent event) {
				// TODO Auto-generated method stub
				noticelabel.setVisible(true);
				noticelabel.setText("Request has been denied!");
			}
		});
		eventBus.addHandler(SendBackRequestEvent.TYPE, new SendBackRequestEvent.Handler() {		
			@Override
			public void onSendBackRequest(SendBackRequestEvent event) {
				// TODO Auto-generated method stub
				noticelabel.setVisible(true);
				noticelabel.setText("Request has been send back to requester!");
			}
		});
	}
}

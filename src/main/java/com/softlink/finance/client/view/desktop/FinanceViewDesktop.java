package com.softlink.finance.client.view.desktop;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DeckLayoutPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.softlink.finance.client.ClientFactory;
import com.softlink.finance.client.FinanceView;
import com.softlink.finance.client.events.InParentPlaceEvent;
import com.softlink.finance.client.events.InSubPlaceEvent;

public class FinanceViewDesktop extends Composite implements FinanceView {
	
	interface DesktopFinanceViewUiBinder extends UiBinder<Widget, FinanceViewDesktop> {}

	private static DesktopFinanceViewUiBinder uiBinder = GWT
			.create(DesktopFinanceViewUiBinder.class);

	//private ClientFactory clientFactory;
	private EventBus eventBus;
	private boolean firstContentWidget = false;
	private DesktopRequestDialogView dlgRequest = new DesktopRequestDialogView();

	@UiField DesktopTopPanelView topPanel;
	@UiField DesktopToolBarPanelView toolbarPanel;
	@UiField DesktopTabPanelView tabPanel;
	@UiField SplitLayoutPanel splitPanel;
	@UiField DeckLayoutPanel ContainerView;

	/*
	 * Constructor
	 */
	public FinanceViewDesktop(ClientFactory clientFactory) {
		initWidget(uiBinder.createAndBindUi(this));
		this.eventBus = clientFactory.getEventBus();
		setHandlerEventBus();
		
		topPanel.setEventBus(clientFactory.getEventBus());
		toolbarPanel.setEventBus(clientFactory.getEventBus());
		tabPanel.setEventBus(clientFactory.getEventBus());
		dlgRequest.setEventBus(clientFactory.getEventBus());
		
		topPanel.setLogoutState();
		toolbarPanel.setDesktopRequestDialogView(dlgRequest);
		
		ContainerView.add(clientFactory.getFinanceRequirementView());
		ContainerView.add(clientFactory.getDraftsView());
		ContainerView.add(clientFactory.getTrashView());
		ContainerView.add(clientFactory.getRequestDetailView());
		ContainerView.add(clientFactory.getUnderConstructionView());
		ContainerView.setAnimationVertical(true);
		ContainerView.setAnimationDuration(600);
		
		setWidget(clientFactory.getFinanceRequirementView());
	}

	/*
	 * ---Procedure---
	 */
	public void setWidget(IsWidget content) {
		ContainerView.setWidget(content);
	
		// Do not animate the first time we show a widget.
		if (firstContentWidget) {
			firstContentWidget = false;
			ContainerView.animate(0);
		}
	}

	/*
	 * --- Implement View Interface---
	 */
	@Override
	public DeckLayoutPanel getContainerView() {
		// TODO Auto-generated method stub
		return ContainerView;
	}
	
	/*
	 * ---EventBus Handler---
	 */
	public void setHandlerEventBus() {
		eventBus.addHandler(InSubPlaceEvent.TYPE, new InSubPlaceEvent.Handler() {
			@Override
			public void inSubPlace(InSubPlaceEvent event) {
				// TODO Auto-generated method stub
				ContainerView.setAnimationVertical(false);
			}
		});
		eventBus.addHandler(InParentPlaceEvent.TYPE, new InParentPlaceEvent.Handler() {
			@Override
			public void inParentPlace(InParentPlaceEvent event) {
				// TODO Auto-generated method stub
				ContainerView.setAnimationVertical(true);
			}
		});
	}

}

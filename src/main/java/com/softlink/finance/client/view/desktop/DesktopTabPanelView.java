/*
 * Copyright 2008 Google Inc.
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
import com.google.gwt.place.shared.Place;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.StackLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.softlink.datastore.model.FinanceData;
import com.softlink.finance.client.ClientFactory;
import com.softlink.finance.client.place.ConstructionPlace;
import com.softlink.finance.client.place.DraftsPlace;
import com.softlink.finance.client.place.FinanceRequirementPlace;
import com.softlink.finance.client.place.TrashPlace;

/**
 * A composite that contains the shortcut stack panel on the left side. The
 * mailbox tree and shortcut lists don't actually do anything, but serve to show
 * how you can construct an interface using
 * {@link com.google.gwt.user.client.ui.StackPanel},
 * {@link com.google.gwt.user.client.ui.Tree}, and other custom widgets.
 */
public class DesktopTabPanelView extends Composite {

	interface Binder extends UiBinder<Widget, DesktopTabPanelView> {
	}

	private static final Binder binder = GWT.create(Binder.class);

	private ClientFactory clientFactory;
	private EventBus eventBus;

	@UiField
	Anchor statistics;
	@UiField
	Anchor staticexpenses;
	@UiField
	Anchor financialrequirement;
	@UiField
	StackLayoutPanel stackpanel;
	@UiField
	Anchor drafts;
	@UiField
	Anchor trash;
	@UiField
	Anchor future;

	/*
	 * ---Procedure---
	 */
	public void setDisable() {
		statistics.setEnabled(false);
		staticexpenses.setEnabled(false);
		financialrequirement.setEnabled(false);
		drafts.setEnabled(false);
		trash.setEnabled(false);
		future.setEnabled(false);
	}

	public void setEnable() {
		statistics.setEnabled(true);
		staticexpenses.setEnabled(true);
		financialrequirement.setEnabled(true);
		drafts.setEnabled(true);
		trash.setEnabled(true);
		future.setEnabled(true);
	}

	/**
	 * Constructs a new clean Tab Panel.
	 * 
	 * @param images
	 *            a bundle that provides the images for this widget
	 */
	public DesktopTabPanelView() {
		initWidget(binder.createAndBindUi(this));
		RemoveStyle();
	}

	/*
	 * ---Procedure---
	 */
	public void setController(ClientFactory clientFactory) {
		this.eventBus = clientFactory.getEventBus();
		this.clientFactory = clientFactory;
	}

	public void setNotifyStyle() {
		// check new record
		int newFinanceRequirement = 0;
		int newDrafts = 0;
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
							newFinanceRequirement++;
			}
			if (updateData.getStatus().equals("DRAFT")
					&& updateData.getReporter().equals(
							clientFactory.getLocalStorage().getCurrentUser()))
				newDrafts++;
		}
		// set style
		if (newFinanceRequirement == 0)
			financialrequirement.setText("Finance Requirement");
		if (newFinanceRequirement != 0)
			financialrequirement.setText("Finance Requirement ("
					+ newFinanceRequirement + ")");
		if (newDrafts == 0)
			drafts.setText("Drafts");
		if (newDrafts != 0)
			drafts.setText("Drafts (" + newDrafts + ")");
	}

	public void setStatisticStyle() {
		RemoveStyle();
		statistics.setStyleName("Hyperlink");
	}

	public void setStaticExpensesStyle() {
		RemoveStyle();
		staticexpenses.setStyleName("Hyperlink");
	}

	public void setFinancialRequirementStyle() {
		RemoveStyle();
		financialrequirement.setStyleName("Hyperlink");
	}

	public void setDraftStyle() {
		RemoveStyle();
		drafts.setStyleName("Hyperlink");
	}

	public void setTrashStyle() {
		RemoveStyle();
		trash.setStyleName("Hyperlink");
	}

	public void setFutureStyle() {
		RemoveStyle();
		future.setStyleName("Hyperlink");
	}

	public void openStack(int index) {
		stackpanel.showWidget(index);
	}

	public void RemoveStyle() {
		statistics.removeStyleName("Hyperlink");
		staticexpenses.removeStyleName("Hyperlink");
		financialrequirement.removeStyleName("Hyperlink");
		future.removeStyleName("Hyperlink");
		drafts.removeStyleName("Hyperlink");
		trash.removeStyleName("Hyperlink");
	}

	/*
	 * ---Event Handler---
	 */
	@UiHandler("financialrequirement")
	void onFinancialRequirementClick(ClickEvent event) {
		openStack(0);
		setFinancialRequirementStyle();
		clientFactory.getPlaceController().goTo(
				new FinanceRequirementPlace(null));
	}

	@UiHandler("drafts")
	void onDraftsClick(ClickEvent event) {
		openStack(0);
		setDraftStyle();
		clientFactory.getPlaceController().goTo(new DraftsPlace(null));
	}

	@UiHandler("trash")
	void onTrashClick(ClickEvent event) {
		openStack(0);
		setTrashStyle();
		clientFactory.getPlaceController().goTo(new TrashPlace(null));
	}

	@UiHandler("future")
	void onFutureClick(ClickEvent event) {
		openStack(0);
		setFutureStyle();
		Place previousPlace = clientFactory.getPlaceController().getWhere();
		clientFactory.getPlaceController().goTo(
				new ConstructionPlace(previousPlace));
	}

	@UiHandler("staticexpenses")
	void onStaticexpensesClick(ClickEvent event) {
		openStack(0);
		setStaticExpensesStyle();
		Place previousPlace = clientFactory.getPlaceController().getWhere();
		clientFactory.getPlaceController().goTo(
				new ConstructionPlace(previousPlace));
	}

	@UiHandler("statistics")
	void onStatisticsClick(ClickEvent event) {
		openStack(1);
		setStatisticStyle();
		Place previousPlace = clientFactory.getPlaceController().getWhere();
		clientFactory.getPlaceController().goTo(
				new ConstructionPlace(previousPlace));
	}

}

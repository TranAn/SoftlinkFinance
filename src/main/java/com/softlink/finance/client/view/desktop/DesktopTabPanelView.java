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

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.StackLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.softlink.finance.client.events.InConstructionPlaceEvent;
import com.softlink.finance.client.events.InDraftsPlaceEvent;
import com.softlink.finance.client.events.InFinanceRequirementPlaceEvent;
import com.softlink.finance.client.events.InTrashPlaceEvent;

/**
 * A composite that contains the shortcut stack panel on the left side. The
 * mailbox tree and shortcut lists don't actually do anything, but serve to show
 * how you can construct an interface using
 * {@link com.google.gwt.user.client.ui.StackPanel},
 * {@link com.google.gwt.user.client.ui.Tree}, and other custom widgets.
 */
public class DesktopTabPanelView extends Composite {

  interface Binder extends UiBinder<Widget, DesktopTabPanelView> { }
  private static final Binder binder = GWT.create(Binder.class);
  
  private EventBus eventBus;
  
  @UiField Hyperlink statistics;
  @UiField Hyperlink staticexpenses;
  @UiField Hyperlink financialrequirement;
  @UiField StackLayoutPanel stackpanel;
  @UiField Hyperlink drafts;
  @UiField Hyperlink trash;
  @UiField Hyperlink future;
  
  /**
   * Constructs a new clean Tab Panel.
   * 
   * @param images a bundle that provides the images for this widget
   */
  public DesktopTabPanelView() {
    initWidget(binder.createAndBindUi(this));
    RemoveStyle();
  }
  
  /*
   * ---Procedure---
   */
  public void setEventBus(EventBus eventBus){
	  this.eventBus = eventBus;
	  setHandlerEventBus();
  }
  
  public void setNotifyStyle(int newFinancialRequirement, int newDraft) {
	  if(newFinancialRequirement==0)
		  financialrequirement.setText("Finance Requirement");
	  if(newFinancialRequirement!=0)
		  financialrequirement.setText("Finance Requirement ("+newFinancialRequirement+")");
	  if(newDraft==0)
		  drafts.setText("DesktopDraftsView");
	  if(newDraft!=0)
		  drafts.setText("DesktopDraftsView ("+newDraft+")");
  }
  
  public void setStatisticStyle() {
	  RemoveStyle();
	  statistics.setStyleName("gwt-Hyperlink");
  }
  
  public void setStaticExpensesStyle() {
	  RemoveStyle();
	  staticexpenses.setStyleName("gwt-Hyperlink");
  }
  
  public void setFinancialRequirementStyle() {
	  RemoveStyle();
	  financialrequirement.setStyleName("gwt-Hyperlink");
  }
  
  public void setDraftStyle() {
	  RemoveStyle();
	  drafts.setStyleName("gwt-Hyperlink");
  }
  
  public void setTrashStyle() {
	  RemoveStyle();
	  trash.setStyleName("gwt-Hyperlink");
  }
  
  public void setFutureStyle() {
	  RemoveStyle();
	  future.setStyleName("gwt-Hyperlink");
  }
  
  public void openStack(int index) {
	  stackpanel.showWidget(index);
  }
  
  public void RemoveStyle() {
	  statistics.removeStyleName("gwt-Hyperlink");
	  staticexpenses.removeStyleName("gwt-Hyperlink");
	  financialrequirement.removeStyleName("gwt-Hyperlink");
	  future.removeStyleName("gwt-Hyperlink");
	  drafts.removeStyleName("gwt-Hyperlink");
	  trash.removeStyleName("gwt-Hyperlink");
  }
  
  @UiHandler("financialrequirement")
  void onFinancialRequirementClick(ClickEvent event) {
	 openStack(0);
	 setFinancialRequirementStyle();
	 eventBus.fireEvent(new InFinanceRequirementPlaceEvent());
  }
  
  @UiHandler("drafts")
  void onDraftsClick(ClickEvent event) {
	 openStack(0);
	 setDraftStyle();
	 eventBus.fireEvent(new InDraftsPlaceEvent());
  }
  
  @UiHandler("trash")
  void onTrashClick(ClickEvent event) {
	  openStack(0);
	  setTrashStyle();
	  eventBus.fireEvent(new InTrashPlaceEvent());
  }
  
  @UiHandler("future")
  void onFutureClick(ClickEvent event) {
	  openStack(0);
	  setFutureStyle();
	  eventBus.fireEvent(new InConstructionPlaceEvent());
  }
  
  @UiHandler("staticexpenses")
  void onStaticexpensesClick(ClickEvent event) {
	  openStack(0);
	  setStaticExpensesStyle();
	  eventBus.fireEvent(new InConstructionPlaceEvent());
  }
  
  @UiHandler("statistics")
  void onStatisticsClick(ClickEvent event) {
	  openStack(1);
	  setStatisticStyle();
	  eventBus.fireEvent(new InConstructionPlaceEvent());
  }
  
  /*
   * ---EventBus Handler---
   */
  void setHandlerEventBus(){
	  eventBus.addHandler(InDraftsPlaceEvent.TYPE, new InDraftsPlaceEvent.Handler() {
		@Override
		public void inDraftsPlace(InDraftsPlaceEvent event) {
			// TODO Auto-generated method stub
			setDraftStyle();
		}
	  });
	  eventBus.addHandler(InFinanceRequirementPlaceEvent.TYPE, new InFinanceRequirementPlaceEvent.Handler() {
		@Override
		public void inFinanceRequirementPlace(
				InFinanceRequirementPlaceEvent event) {
			// TODO Auto-generated method stub
			setFinancialRequirementStyle();
		}
	  });
	  eventBus.addHandler(InTrashPlaceEvent.TYPE, new InTrashPlaceEvent.Handler() {
		@Override
		public void inTrashPlace(InTrashPlaceEvent event) {
			// TODO Auto-generated method stub
			setTrashStyle();
		}
	  });
  }
}

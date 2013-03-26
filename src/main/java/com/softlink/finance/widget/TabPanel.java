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
package com.softlink.finance.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.StackLayoutPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Widget;

/**
 * A composite that contains the shortcut stack panel on the left side. The
 * mailbox tree and shortcut lists don't actually do anything, but serve to show
 * how you can construct an interface using
 * {@link com.google.gwt.user.client.ui.StackPanel},
 * {@link com.google.gwt.user.client.ui.Tree}, and other custom widgets.
 */
public class TabPanel extends ResizeComposite {

  interface Binder extends UiBinder<Widget, TabPanel> { }
  private static final Binder binder = GWT.create(Binder.class);
  
  private Listener listener;
  public interface Listener {
	  void onFinancialRequirementSelected();
	  void onDraftsSelected();
	  void onTrashSelected();
	  void onFutureSelected();
  }
  public void setListener(Listener listener) {
	  this.listener = listener;
  }
  
  @UiField Hyperlink statistics;
  @UiField Hyperlink staticincome;
  @UiField Hyperlink staticexpenses;
  @UiField Hyperlink financialrequirement;
  @UiField Hyperlink arisingincome;
  @UiField StackLayoutPanel stackpanel;
  @UiField Hyperlink drafts;
  @UiField Hyperlink trash;
  @UiField Hyperlink future;
  
  //Procedure-----------------------------------------------
  public void setNotifyStyle(int newFinancialRequirement, int newDraft) {
	  if(newFinancialRequirement==0)
		  financialrequirement.setText("Financial Requirement");
	  if(newFinancialRequirement!=0)
		  financialrequirement.setText("Financial Requirement ("+newFinancialRequirement+")");
	  if(newDraft==0)
		  drafts.setText("Drafts");
	  if(newDraft!=0)
		  drafts.setText("Drafts ("+newDraft+")");
  }

  
  /**
   * Constructs a new shortcuts widget using the specified images.
   * 
   * @param images a bundle that provides the images for this widget
   */
  public TabPanel() {
    initWidget(binder.createAndBindUi(this));
    RemoveStyle();
  }
  
  public void setStatisticStyle() {
	  RemoveStyle();
	  statistics.setStyleName("gwt-Hyperlink");
  }
  
  public void setStaticIncomeStyle() {
	  RemoveStyle();
	  staticincome.setStyleName("gwt-Hyperlink");
  }
  
  public void setArisingIncomeStyle() {
	  RemoveStyle();
	  arisingincome.setStyleName("gwt-Hyperlink");
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
	  staticincome.removeStyleName("gwt-Hyperlink");
	  arisingincome.removeStyleName("gwt-Hyperlink");
	  staticexpenses.removeStyleName("gwt-Hyperlink");
	  financialrequirement.removeStyleName("gwt-Hyperlink");
	  future.removeStyleName("gwt-Hyperlink");
	  drafts.removeStyleName("gwt-Hyperlink");
	  trash.removeStyleName("gwt-Hyperlink");
  }
  
  @UiHandler("financialrequirement")
  void onFinancialRequirementClick(ClickEvent event) {
	  if(listener!=null)
		  listener.onFinancialRequirementSelected();
  }
  
  @UiHandler("drafts")
  void onDraftsClick(ClickEvent event) {
	  if(listener!=null)
		  listener.onDraftsSelected();
  }
  
  @UiHandler("trash")
  void onTrashClick(ClickEvent event) {
	  if(listener!=null)
		  listener.onTrashSelected();
  }
  
  @UiHandler("future")
  void onFutureClick(ClickEvent event) {
	  if(listener!=null)
		  listener.onFutureSelected();
  }
  
}

/*
 * Copyright 2007 Google Inc.
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
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DateLabel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.softlink.finance.datastore.FinancialRequirementsObj;
import com.softlink.finance.datastore.FinancialRequirementsObjAsync;
import com.softlink.financedatastore.client.FinancialRequirements;

/**
 * A composite for displaying the details of an email message.
 */
public class RequestDetail extends Composite {

  interface Binder extends UiBinder<Widget, RequestDetail> { }
  private static final Binder binder = GWT.create(Binder.class);
  private final static FinancialRequirementsObjAsync FinancialRequirementsObj = 
			GWT.create(FinancialRequirementsObj.class);
  private FinancialRequirements item;
  private boolean isAdmin;
  private String savecomment="";
  @UiField Button approve;
  @UiField Button deny;
  @UiField Button moreinfo;
  @UiField Label account;
  @UiField Label description;
  @UiField Label tags;
  @UiField Label currency;
  @UiField Label real_amount;
  @UiField Label tax_amount;
  @UiField Label cus_id;
  @UiField Label reference;
  @UiField Hyperlink document;
  @UiField Label assets_id;
  @UiField TextBox reporter;
  @UiField TextBox requester;
  @UiField TextBox manager;
  @UiField DateLabel init_time;
  @UiField DateLabel update_time;
  @UiField DateLabel req_time;
  @UiField Label status;
  @UiField TextBox comment;
  @UiField AbsolutePanel header;
  @UiField TextArea commentbox;
  @UiField Label commentlabel;
  
  private Listener listener;
	
  public interface Listener {
	  void onApproveClick(FinancialRequirements item, String comment);
	  void onDenyClick(FinancialRequirements item, String comment);
	  void onMoreInfoClick(FinancialRequirements item, String comment);
	  void onUpdateUserLog();
  }
	
  public void setListener(Listener listener) {
	  this.listener = listener;
  }
  
  public RequestDetail() {
    initWidget(binder.createAndBindUi(this));
    FinancialRequirementsObj.checkUserAdminRole(new AsyncCallback<Boolean>(){
		@Override
		public void onFailure(Throwable caught) {}
		@Override
		public void onSuccess(Boolean result) {
			if(result==true)
				isAdmin=true;
			else
				isAdmin=false;
		}
    });
  }
  
  public void refresh() {
	  savecomment="";
  }

  public void setItem(final FinancialRequirements item) {
	  this.item = item;
	  reporter.setText(item.getReporter());
	  manager.setText(item.getManager());
	  requester.setText(item.getRequester());
	  description.setText(item.getDescription());
	  tags.setText(item.getTags());
	  account.setText(item.getAccount());
	  currency.setText(item.getCurrency());
	  real_amount.setText(String.valueOf(item.getReal_amount()));
	  tax_amount.setText(String.valueOf(item.getTax_amount()));
	  cus_id.setText(String.valueOf(item.getCus_id()));
	  assets_id.setText(String.valueOf(item.getAssets_id()));
	  document.setText(item.getDocument());
	  reference.setText(item.getRefference());
	  commentbox.setText(item.getComment());
	  init_time.setValue(item.getInit_time());
	  update_time.setValue(item.getUpdate_time());
	  req_time.setValue(item.getReq_time());
	  status.setText(item.getStatus());
	  if(isAdmin){
		  comment.setVisible(true);
		  commentlabel.setVisible(true);
		  approve.setVisible(true);
		  deny.setVisible(true);
		  moreinfo.setVisible(true);
		  commentbox.setHeight("123px");
	  } else {
		  approve.setVisible(false);
		  deny.setVisible(false);
		  moreinfo.setVisible(false);
		  comment.setVisible(false);
		  commentlabel.setVisible(false);
		  commentbox.setHeight("175px");
	  }
	  if(item.getStatus().equals("PENDING")){
		  header.setStyleName("header");
		  status.setStyleName("moreinfostatus");
	  }
	  if(item.getStatus().equals("APPROVED")){
		  header.setStyleName("approveheader");
		  status.setStyleName("approvestatus");
	  }
	  if(item.getStatus().equals("DENIED")){
		  header.setStyleName("denyheader");
		  status.setStyleName("denystatus");
	  }
	  if(item.getStatus().equals("DELETED")){
		  header.setStyleName("denyheader");
		  status.setStyleName("denystatus");
		  approve.setVisible(false);
		  deny.setVisible(false);
		  moreinfo.setVisible(false);
		  comment.setVisible(false);
		  commentlabel.setVisible(false);
		  commentbox.setHeight("175px");
	  }
	  comment.setText("");
  }
  
  @UiHandler("approve")
  void onApproveClick(ClickEvent event) {
	  if(Window.confirm("Are you sure to want to approve this request?"))
		  if(listener!=null) {
			  listener.onUpdateUserLog();
			  if(savecomment.equals("")==false)
				  listener.onApproveClick(item, savecomment);
			  else 
				  listener.onApproveClick(item, comment.getText());
		  }
  }
  
  @UiHandler("deny")
  void onDenyClick(ClickEvent event) {
	  if(Window.confirm("Are you sure to want to deny this request?"))
		  if(listener!=null) {
			  listener.onUpdateUserLog();
			  if(savecomment.equals("")==false)
				  listener.onDenyClick(item, savecomment);
			  else
				  listener.onDenyClick(item, comment.getText());
		  }
  }
  
  @UiHandler("comment")
  void onCommentKeyUp(KeyUpEvent event) {
	  if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
		  savecomment = savecomment + " " + comment.getText();
		  commentbox.setText(commentbox.getText()+"me:"+comment.getText()+"\r\n");
		  comment.setText("");
		  comment.setFocus(true);
	  }
  }
  
  @UiHandler("moreinfo")
  void onMoreinfoClick(ClickEvent event) {
	  if(Window.confirm("Are you sure to want to send back this request to the requester?"))
		  if(listener!=null) {
			  listener.onUpdateUserLog();
			  if(savecomment.equals("")==false)
				  listener.onMoreInfoClick(item, savecomment);
			  else
				  listener.onMoreInfoClick(item, comment.getText());
		  }
  }
  
}


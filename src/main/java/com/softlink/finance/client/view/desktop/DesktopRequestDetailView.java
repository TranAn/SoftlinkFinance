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
package com.softlink.finance.client.view.desktop;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DateLabel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.softlink.finance.client.view.RequestDetailView;
import com.softlink.datastore.model.FinanceData;

/**
 * A composite for displaying the details of an email message.
 */
public class DesktopRequestDetailView extends Composite 
	implements RequestDetailView{

	  interface Binder extends UiBinder<Widget, DesktopRequestDetailView> { }
	  private static final Binder binder = GWT.create(Binder.class);
	  
	  private FinanceData item;
	  private boolean isAdmin;
	  private String savecomment="";
	  private Presenter listener;
	  
	  @UiField Button approve;
	  @UiField Button deny;
	  @UiField Button moreinfo;
	  @UiField Label account;
	  @UiField Label description;
	  @UiField Label tags;
	  @UiField Label currency;
	  @UiField Label real_amount;
	  @UiField Label bill_amount;
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
	  
	  public DesktopRequestDetailView() {
	    initWidget(binder.createAndBindUi(this));
	  }
	  
	  /*
	   * ---Procedure---
	   */
	  public void refresh() {
		  savecomment="";
	  }
	
	  public void setItem(FinanceData item) {
		  reporter.setText(item.getReporter());
		  manager.setText(item.getManager());
		  requester.setText(item.getRequester());
		  description.setText(item.getDescription());
		  tags.setText(item.getTags());
		  account.setText(item.getAccount());
		  currency.setText(item.getCurrency());
		  real_amount.setText(String.valueOf(item.getReal_amount()));
		  bill_amount.setText(String.valueOf(item.getBill_amount()));
		  cus_id.setText(String.valueOf(item.getCus_id()));
		  assets_id.setText(String.valueOf(item.getAssets_id()));
		  document.setText(item.getDocument());
		  reference.setText(item.getRefference());
		  commentbox.setText(item.getComment());
		  init_time.setValue(item.getInit_time());
		  update_time.setValue(item.getUpdate_time());
		  req_time.setValue(item.getReq_time());
		  status.setText(item.getStatus());
		  
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
	  
	  /*
	   * ---Event Handler---
	   */
	  @UiHandler("approve")
	  void onApproveClick(ClickEvent event) {
		  if(Window.confirm("Are you sure to want to approve this request?"))
			  if(listener!=null) {
//				  listener.onUpdateUserLog();
				  if(savecomment.equals("")==false)
					  listener.onApprovedRequest(savecomment);
				  else 
					  listener.onApprovedRequest(comment.getText());
			  }
	  }
	  
	  @UiHandler("deny")
	  void onDenyClick(ClickEvent event) {
		  if(Window.confirm("Are you sure to want to deny this request?"))
			  if(listener!=null) {
//				  listener.onUpdateUserLog();
				  if(savecomment.equals("")==false)
					  listener.onDeniedRequest(savecomment);
				  else
					  listener.onDeniedRequest(comment.getText());
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
//				  listener.onUpdateUserLog();
				  if(savecomment.equals("")==false)
					  listener.onSendBackRequest(savecomment);
				  else
					  listener.onSendBackRequest(comment.getText());
			  }
	  }
	

	/*
	 * ---Implement View Interface---
	 */
	@Override
	public void setData(FinanceData fr) {
		// TODO Auto-generated method stub
		item = fr;
		setItem(item);
	}

	@Override
	public void setPresenter(Presenter listener) {
		// TODO Auto-generated method stub
		this.listener = listener;
	}

	@Override
	public void setUserRole(boolean isAdmin) {
		// TODO Auto-generated method stub
		this.isAdmin = isAdmin;
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
	}

	@Override
	public FinanceData getData() {
		// TODO Auto-generated method stub
		return item;
	}
  
}


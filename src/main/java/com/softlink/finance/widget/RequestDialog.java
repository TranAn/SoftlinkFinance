package com.softlink.finance.widget;


import java.io.IOException;
import java.util.Date;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.softlink.finance.datastore.FinanceRequirementsObj;
import com.softlink.finance.datastore.FinanceRequirementsObjAsync;
import com.softlink.finance.services.MailServices;
import com.softlink.finance.services.MailServicesAsync;
import com.softlink.finance.services.UserServices;
import com.softlink.finance.services.UserServicesAsync;
import com.softlink.financedatastore.client.FinanceRequirements;
import com.softlink.financedatastore.client.SeriUser;

public class RequestDialog extends DialogBox{

	interface Binder extends UiBinder<Widget, RequestDialog> { }
	private static final Binder binder = GWT.create(Binder.class);
	private final static UserServicesAsync userservices = 
			  GWT.create(UserServices.class);
	private final FinanceRequirementsObjAsync FinancialRequirementsObj = GWT
			  .create(FinanceRequirementsObj.class);
	private final MailServicesAsync MailServices = GWT
			  .create(MailServices.class);
	
	public interface Listener {
		void onSendSuccess();
		void onSendFailure();
		void onDraftSuccess();
	}
	private Listener listener;
	  
	public void setListener(Listener listener) {
		this.listener = listener;
	}
	
	@UiField Button closebutton;
	@UiField Label reporter;
	@UiField TextBox requester;
	@UiField ListBox manager;
	@UiField ListBox account;
	@UiField ListBox currency;
	@UiField IntegerBox real_amount;
	@UiField IntegerBox tax_amount;
	@UiField TextArea tags;
	@UiField ListBox cus_id;
	@UiField ListBox assets_id;
	@UiField TextArea reference;
	@UiField Button sendrequest;
	@UiField Image refresh;
	@UiField Button draft;
	@UiField Label tagslabel;
	@UiField Label requesterlabel;
	@UiField Image image;
	@UiField TextArea description;
	@UiField Label descriptionlabel;
	@UiField TextArea document;

	public RequestDialog() {
		// Use this opportunity to set the dialog's caption.
	    setText("Make Request");
		setWidget(binder.createAndBindUi(this));
		
		setAnimationEnabled(true);
	    setGlassEnabled(true);
	    
	    userservices.isUserLoggedIn(new AsyncCallback<Boolean>() {
			public void onFailure(Throwable caught) {}
			public void onSuccess(Boolean result) {
				if(result==true){
					userservices.getUserName(new AsyncCallback<String>() {
						public void onFailure(Throwable caught) {}
						public void onSuccess(String result) {
							if(checkUsername(result))
								reporter.setText(result);
							else
								reporter.setText(result+"@gmail.com");
						}
				    });
				}
			}
	    });
	    
	    //initdialog
	    FinancialRequirementsObj.list_manager(new AsyncCallback<List<SeriUser>>() {
			public void onFailure(Throwable caught) {}
			public void onSuccess(List<SeriUser> result) {
				if(result.isEmpty())
					 manager.addItem("");
				else
					for(SeriUser user: result)
						 manager.addItem(user.getUsername());
			}
	    	
	    });
	    account.addItem("Company's Funds");
	    account.addItem("Bank's Account");
	    currency.addItem("USD");
	    currency.addItem("VND");
	    cus_id.addItem("null");
	    assets_id.addItem("null");
	}
	
	@Override
	protected void onPreviewNativeEvent(NativePreviewEvent preview) {
		super.onPreviewNativeEvent(preview);

		NativeEvent evt = preview.getNativeEvent();
		if (evt.getType().equals("keydown")) {
			// Use the popup's key preview hooks to close the dialog when either
			// enter or escape is pressed.
			switch (evt.getKeyCode()) {
			case KeyCodes.KEY_ESCAPE:
				hide();
				break;
			}
		}
	}
	
	boolean checkUsername(String username) {
		for(int i=0;i<=username.length();i++)
			if(username.charAt(i)=='@')
				return true;
		return false;
	}
	
	boolean VerifyField(){
		boolean check = true;
		  if(requester.getText().equals("")||requester.getText().equals("requester")) {
			  check = false;
			  requesterlabel.setStyleName("serverResponseLabelError");
		  } else {
			  requesterlabel.removeStyleName("serverResponseLabelError");
		  }
		  if(description.getText().equals("")||description.getText().equals("request for?")) {
			  check = false;
			  descriptionlabel.setStyleName("serverResponseLabelError");
		  }else {
			  descriptionlabel.removeStyleName("serverResponseLabelError");
		  }
		return check;	
	}
	
	public void refresh() {
		requester.setText("requester");
		requester.setStyleName("textbox");
		real_amount.setText("0");
		real_amount.setStyleName("textbox");
		tax_amount.setText("0");
		tax_amount.setStyleName("textbox");
		tags.setText("relate with project?");
		tags.setStyleName("textbox");
		document.setText("relate with documents?");
		document.setStyleName("textbox");
		reference.setText("relate with someone?");
		reference.setStyleName("textbox");
		description.setText("request for?");
		description.setStyleName("textbox");
		manager.setItemSelected(0, true);
		account.setItemSelected(0, true);
		currency.setItemSelected(0, true);
		cus_id.setItemSelected(0, true);
		assets_id.setItemSelected(0, true);
		requesterlabel.removeStyleName("serverResponseLabelError");
		descriptionlabel.removeStyleName("serverResponseLabelError");
	}
	
	//Event Handler-------------------------------
	
	@UiHandler("closebutton")
	  void onSignOutClicked(ClickEvent event) {
	    hide();
	}
	
	@UiHandler("refresh")
	void onRefreshClick(ClickEvent event) {
		refresh();
	}
	
	@UiHandler("requester")
	void onRequesterMouseDown(MouseDownEvent event) {
		if(requester.getText().equals("requester")) {
			requester.setText("");
			requester.removeStyleName("textbox");
		}
	}
	
	@UiHandler("real_amount")
	void onReal_amountMouseDown(MouseDownEvent event) {
		if(real_amount.getText().equals("0")){
			real_amount.setText("");
			real_amount.removeStyleName("textbox");
		}
	}
	
	@UiHandler("tax_amount")
	void onTax_amountMouseDown(MouseDownEvent event) {
		if(tax_amount.getText().equals("0")){
			tax_amount.setText("");
			tax_amount.removeStyleName("textbox");
		}
	}
	
	@UiHandler("tags")
	void onTagsMouseDown(MouseDownEvent event) {
		if(tags.getText().equals("relate with project?")){
			tags.setText("");
			tags.removeStyleName("textbox");
		}
		tags.setHeight("59px");
	}
	
	@UiHandler("reference")
	void onReferenceMouseDown(MouseDownEvent event) {
		if(reference.getText().equals("relate with someone?")){
			reference.setText("");
			reference.removeStyleName("textbox");
		}
		reference.setHeight("56px");
	}
	
	@UiHandler("sendrequest")
	void onSendrequestClick(ClickEvent event) {
		if(VerifyField()) {
			FinanceRequirements fr = new FinanceRequirements();
			fr.setReporter(reporter.getText());
			fr.setRequester(requester.getText());
			fr.setManager(manager.getValue(manager.getSelectedIndex()));
			fr.setAccount(account.getValue(account.getSelectedIndex()));
			fr.setCurrency(currency.getValue(currency.getSelectedIndex()));
			fr.setReal_amount(real_amount.getValue());
			fr.setTax_amount(tax_amount.getValue());
			fr.setDescription(description.getText());
			if(cus_id.getValue(cus_id.getSelectedIndex()).equals("null")==false)
				fr.setCus_id(Long.valueOf(cus_id.getValue(cus_id.getSelectedIndex())));
			if(assets_id.getValue(assets_id.getSelectedIndex()).equals("null")==false)
				fr.setAssets_id(Long.valueOf(assets_id.getValue(assets_id.getSelectedIndex())));
			if(document.getText().equals("relate with documents?"))
				fr.setDocument("");
			else
				fr.setDocument(document.getText());
			if(reference.getText().equals("relate with someone?"))
				fr.setRefference("");
			else
				fr.setRefference(reference.getText());
			if(tags.getText().equals("relate with project?"))
				fr.setTags("");
			else
				fr.setTags(tags.getText());
			Date date = new Date(System.currentTimeMillis());
			fr.setInit_time(date);
			fr.setUpdate_time(date);
			fr.setStatus("PENDING");
			fr.setComment("");
			final String fromAddress = reporter.getText();
			final String toAddress = manager.getValue(manager.getSelectedIndex());
			final String subject = "ITPRO-New Request # real_amount: "+real_amount.getText()+fr.getCurrency()+", tax_amount: "+tax_amount.getText()+fr.getCurrency()+" # for "+description.getText();
			final String url = Window.Location.getHost()+"/financial/#Finance%20Requirement";
			final String msgBody = "see Detail: "
					  +"\r\n "+url;
			FinancialRequirementsObj.insert(fr, new AsyncCallback<Void>() {
				  public void onFailure(Throwable caught) {
					  if(listener!=null)
						listener.onSendFailure();	
				  }
				  public void onSuccess(Void result) {
					  if(listener!=null)
						listener.onSendSuccess();
					  try {
							MailServices.sendmail(fromAddress, toAddress, subject, msgBody,
								  new AsyncCallback<Void>(){
									public void onFailure(Throwable caught) {}
									public void onSuccess(Void result) {}
							});
					  } catch (IOException e) {
						  e.printStackTrace();
					  }
				  }
			});
			hide();
		}
	}
	
	@UiHandler("real_amount")
	void onReal_amountMouseOut(MouseOutEvent event) {
		if (real_amount.getText().equals(""))
			real_amount.setText("0");
	}
	
	@UiHandler("tax_amount")
	void onTax_amountMouseOut(MouseOutEvent event) {
		if (tax_amount.getText().equals(""))
			tax_amount.setText("0");
	}
	
	@UiHandler("tags")
	void onTagsMouseOut(MouseOutEvent event) {
		tags.setHeight("30px");
	}
	
	@UiHandler("reference")
	void onReferenceMouseOut(MouseOutEvent event) {
		reference.setHeight("30px");
	}
	
	@UiHandler("refresh")
	void onRefreshMouseOut(MouseOutEvent event) {
		refresh.setVisible(false);
		image.setVisible(true);
	}
	
	@UiHandler("image")
	void onImageMouseMove(MouseMoveEvent event) {
		image.setVisible(false);
		refresh.setVisible(true);
	}
	
	@UiHandler("draft")
	void onDraftClick(ClickEvent event) {
		FinanceRequirements fr = new FinanceRequirements();
		fr.setReporter(reporter.getText());
		fr.setRequester(requester.getText());
		fr.setManager(manager.getValue(manager.getSelectedIndex()));
		fr.setAccount(account.getValue(account.getSelectedIndex()));
		fr.setCurrency(currency.getValue(currency.getSelectedIndex()));
		fr.setReal_amount(real_amount.getValue());
		fr.setTax_amount(tax_amount.getValue());
		fr.setDescription(description.getText());
		if(cus_id.getValue(cus_id.getSelectedIndex()).equals("null")==false)
			fr.setCus_id(Long.valueOf(cus_id.getValue(cus_id.getSelectedIndex())));
		if(assets_id.getValue(assets_id.getSelectedIndex()).equals("null")==false)
			fr.setAssets_id(Long.valueOf(assets_id.getValue(assets_id.getSelectedIndex())));
		if(document.getText().equals("relate with documents?"))
			fr.setDocument("");
		else
			fr.setDocument(document.getText());
		if(reference.getText().equals("relate with someone?"))
			fr.setRefference("");
		else
			fr.setRefference(reference.getText());
		if(tags.getText().equals("relate with project?"))
			fr.setTags("");
		else
			fr.setTags(tags.getText());
		Date date = new Date(System.currentTimeMillis());
		fr.setInit_time(date);
		fr.setUpdate_time(date);
		fr.setStatus("DRAFT");
		fr.setComment("");
		FinancialRequirementsObj.insert(fr, new AsyncCallback<Void>() {
			  public void onFailure(Throwable caught) {
				  if(listener!=null)
					listener.onSendFailure();	
			  }
			  public void onSuccess(Void result) {
				  if(listener!=null)
					listener.onDraftSuccess();
			  }
		  });
		hide();
	}
	
	@UiHandler("description")
	void onDescriptionMouseDown(MouseDownEvent event) {
		if(description.getText().equals("request for?")){
			description.setText("");
			description.removeStyleName("textbox");
		}
		description.setHeight("69px");
	}
	
	@UiHandler("description")
	void onDescriptionMouseOut(MouseOutEvent event) {
		description.setHeight("18px");
	}
	
	@UiHandler("document")
	void onDocumentMouseDown(MouseDownEvent event) {
		if(document.getText().equals("relate with documents?")){
			document.setText("");
			document.removeStyleName("textbox");
		}
		document.setHeight("59px");
	}
	
	@UiHandler("document")
	void onDocumentMouseOut(MouseOutEvent event) {
		document.setHeight("27px");
	}
}

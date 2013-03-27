package com.softlink.finance.widget;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.DateCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.BrowserEvents;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.RowStyles;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.LongBox;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.CellPreviewEvent;
import com.google.gwt.view.client.CellPreviewEvent.Handler;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionModel;
import com.softlink.finance.datastore.FinanceRequirementsObj;
import com.softlink.finance.datastore.FinanceRequirementsObjAsync;
import com.softlink.finance.services.MailServices;
import com.softlink.finance.services.MailServicesAsync;
import com.softlink.financedatastore.client.FinanceRequirements;

public class Drafts extends Composite {
	
	interface Binder extends UiBinder<Widget, Drafts> {}
	private static final Binder binder = GWT.create(Binder.class);
	
	private final static FinanceRequirementsObjAsync FinancialRequirementsObj = 
			GWT.create(FinanceRequirementsObj.class);
	private final static MailServicesAsync MailServices = GWT
			  .create(MailServices.class);
	private ToolBarPanel toolbar;
	@UiField(provided=true) CellTable<FinanceRequirements> draftcellTable = new 
			CellTable<FinanceRequirements>();
	@UiField(provided=true) SimplePager pager;
	private List<FinanceRequirements> list_fr = new ArrayList<FinanceRequirements>();
	private List<FinanceRequirements> list_selectedfr = new ArrayList<FinanceRequirements>();
	private FinanceRequirements selected_fr;
	private Timer elapsedTimer = null;
	private AbsolutePanel panel = new AbsolutePanel();
	private Label label = new Label("<Folder is empty>");
	private Listener listener;
	
	public interface Listener {
		void onUpdateUserLog();
	}
	public void setListener(Listener listener) {
		this.listener = listener;
	}
	
	private Date UserLog;
	
	@UiField DockLayoutPanel docklayoutpanel;
	@UiField Button send;
	@UiField Button delete;
	@UiField Label req_id;
	@UiField TextBox requester;
	@UiField TextBox account;
	@UiField TextBox manager;
	@UiField TextBox currency;
	@UiField IntegerBox tax_amount;
	@UiField IntegerBox real_amount;
	@UiField TextArea description;
	@UiField LongBox cus_id;
	@UiField LongBox assets_id;
	@UiField TextArea reference;
	@UiField TextArea commentbox;
	@UiField AbsolutePanel composebox;
	@UiField AbsolutePanel composeheader;
	@UiField Image expandbutton;
	@UiField Image collapsebutton;
	@UiField Label requesterlabel;
	@UiField Label descriptionlabel;
	@UiField TextBox comment;
	@UiField TextArea tags;
	@UiField TextArea document;
	@UiField ScrollPanel scrollpanel;
	
	//Inner Function-----------------------------------------------
	public void setNotifyStyle(final int newDraft, Date updateUserLog) {
		UserLog = updateUserLog;
		 draftcellTable.setRowStyles(new RowStyles<FinanceRequirements>() {
			public String getStyleNames(FinanceRequirements row, int rowIndex) {
				if((row.getComment().length()>0)&&(rowIndex+1>newDraft))
					return "moreinfoRowStyle";
				if((row.getComment().length()>0)&&(rowIndex+1<=newDraft))
					return "moreinfoBoldRowStyle";
				if((row.getComment().length()<=0)&&(rowIndex+1<=newDraft))
					return "normalBoldRowStyle";
				return null;
			}
		});
		draftcellTable.redraw();
	}
	
	public void setDocSize(String width, String height) {
		docklayoutpanel.setWidth(width);
		docklayoutpanel.setHeight(height);
	}
	
	public void refresh() {
		if(list_fr.isEmpty()) {
			toolbar.setVisibleNotice();
			toolbar.setTextNotice("Loading...");
			getData();
		} else {
			toolbar.setVisibleNotice();
			toolbar.setTextNotice("Refresh...");
			getNewData();
		}
	}
	
	public void stop() {
		if(elapsedTimer!=null)
			elapsedTimer.cancel();
		elapsedTimer = null;
	}
	
	public void start() {
		warmUpRequest();
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
	
	//Periodically Time--------------------------------------------
	private void warmUpRequest() {
		elapsedTimer = new Timer () {
			 public void run() {
				//RPC call to get new data
				 if(list_fr.isEmpty())
					 getData();
				 else {
			  		 getNewData();
				 }
		  	 }
		 };
		 elapsedTimer.scheduleRepeating(10000);
		 // ... The elapsed timer has started ...
	}
	
	//RPC call-----------------------------------------------------
	
	private void getData(){
		 FinancialRequirementsObj.list_draftfr(
	    		 new AsyncCallback<List<FinanceRequirements>>() {
	    	 public void onFailure(Throwable caught) {
	    		 toolbar.setVisibleNotice();
				 toolbar.setTextNotice("Load failure, the connetion has been interupt!");
			 }
			 public void onSuccess(List<FinanceRequirements> result) { 
				 toolbar.setHideNotice();
				 if(result.isEmpty()){
					 scrollpanel.remove(draftcellTable);
					 scrollpanel.add(panel);
				 } else {
					 for(int i=result.size()-1;i>=0;i--)
						 list_fr.add(result.get(i));
					 scrollpanel.remove(panel);
					 scrollpanel.add(draftcellTable);
				 }
			 }
		 });
	}
	
	private void getNewData() {
		FinancialRequirementsObj.list_newdraftfr(list_fr.get(0),
 	    		 new AsyncCallback<List<FinanceRequirements>>() {
 	    	 public void onFailure(Throwable caught) {
 	    		toolbar.setVisibleNotice();
				toolbar.setTextNotice("Load failure, the connetion has been interupt!");
 			 }
			 public void onSuccess(List<FinanceRequirements> result) {
				 toolbar.setHideNotice();
	  			 for(FinanceRequirements fr: result)   				
	  				 list_fr.add(0,fr);
	  		 }
 		 });
	}

	public Drafts(final ToolBarPanel toolbar) {
		 // Do not refresh the headers and footers every time the data is updated.
	    draftcellTable.setAutoHeaderRefreshDisabled(true);
	    draftcellTable.setAutoFooterRefreshDisabled(true);

		// Create a Pager to control the table.
		SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
		pager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
		pager.setDisplay(draftcellTable);
		
		initWidget(binder.createAndBindUi(this));
		this.toolbar = toolbar;
		docklayoutpanel.remove(composebox);
		
		final SelectionModel<FinanceRequirements> selectionModel = new 
				MultiSelectionModel<FinanceRequirements>();
		draftcellTable.setSelectionModel(selectionModel,
		        DefaultSelectionEventManager.<FinanceRequirements> createCheckboxManager());
		
		final Column<FinanceRequirements, Boolean> checkColumn = 
				new Column<FinanceRequirements, Boolean>(
		        new CheckboxCell(true, false)) {
		      @Override
		      public Boolean getValue(FinanceRequirements object) { 
		    	  return selectionModel.isSelected(object);
			  }
		    };
		draftcellTable.addColumn(checkColumn, SafeHtmlUtils.fromSafeConstant("<br/>"));
		draftcellTable.setColumnWidth(checkColumn,"5%");
		
		final TextColumn<FinanceRequirements> RequestIDColumn = new 
				TextColumn<FinanceRequirements>() {
			@Override
		    public String getValue(FinanceRequirements object) {
				return String.valueOf(object.getRequest_id());
		    }
		};
		draftcellTable.addColumn(RequestIDColumn, "Request ID");
		draftcellTable.setColumnWidth(RequestIDColumn,"10%");
		
		final TextColumn<FinanceRequirements> ReporterColumn = new 
				TextColumn<FinanceRequirements>() {
			@Override
			public String getValue(FinanceRequirements object) {
				return object.getReporter();
			}
		};
		draftcellTable.addColumn(ReporterColumn, "Reporter");
		draftcellTable.setColumnWidth(ReporterColumn,"15%");
		
		final TextColumn<FinanceRequirements> DescriptionColumn = new 
				TextColumn<FinanceRequirements>() {
			@Override
			public String getValue(FinanceRequirements object) {
				if (object.getDescription().length()<=51)
					return object.getDescription();
				return object.getDescription().substring(0, 51)+" . . .";
			}
		};
		draftcellTable.addColumn(DescriptionColumn, "Description");
		draftcellTable.setColumnWidth(DescriptionColumn,"35%");
		
		final Column<FinanceRequirements, Date> Update_TimeColumn = new 
		    		Column<FinanceRequirements, Date>(new DateCell()) {
		      @Override
		      public Date getValue(FinanceRequirements object) {
		        return object.getUpdate_time();
		      }
		    };
	    Update_TimeColumn.setSortable(true);
	    draftcellTable.addColumn(Update_TimeColumn, "Update_Time");
	    draftcellTable.setColumnWidth(Update_TimeColumn,"25%");
		    
	    final TextColumn<FinanceRequirements> StatusColumn = new 
	    		TextColumn<FinanceRequirements>() {
			@Override
			public String getValue(FinanceRequirements object) {
				return object.getStatus();
			}
		};
		draftcellTable.addColumn(StatusColumn, "Status");
		draftcellTable.setColumnWidth(StatusColumn,"10%");
			
		draftcellTable.addCellPreviewHandler(new Handler<FinanceRequirements>(){
			public void onCellPreview(
					CellPreviewEvent<FinanceRequirements> event) {
				if (BrowserEvents.CLICK.equals(event.getNativeEvent().getType())) {
					selected_fr = event.getValue();
					if(selected_fr.getUpdate_time().equals(UserLog))
						FinancialRequirementsObj.updateUserLog(selected_fr.getUpdate_time(), 
								new AsyncCallback<Void> () {
									public void onFailure(Throwable caught) {}
									public void onSuccess(Void result) {
										if(listener!=null)
											listener.onUpdateUserLog();
									}	
						});
					if(event.getColumn()!=0){
						if(draftcellTable.getPageSize()!=5) {
							draftcellTable.setPageStart(Integer.valueOf(list_fr.indexOf(selected_fr)/5)*5);
							draftcellTable.setKeyboardSelectedRow(Integer.valueOf(list_fr.indexOf(selected_fr)%5));
						}
						expandbutton.setVisible(false);
						collapsebutton.setVisible(true);
						draftcellTable.setPageSize(5);
						docklayoutpanel.remove(composebox);
						docklayoutpanel.remove(composeheader);
						docklayoutpanel.addSouth(composebox, 17);
						docklayoutpanel.addSouth(composeheader, 3);
						FinanceRequirements requestselected = event.getValue();
						req_id.setText(String.valueOf(requestselected.getRequest_id()));
						requester.setText(requestselected.getRequester());
						manager.setText(requestselected.getManager());
						account.setText(requestselected.getAccount());
						currency.setText(requestselected.getCurrency());
						real_amount.setValue(requestselected.getReal_amount());
						tax_amount.setValue(requestselected.getTax_amount());
						description.setText(requestselected.getDescription());
						cus_id.setValue(requestselected.getCus_id());
						if(requestselected.getCus_id()==null)
							cus_id.setText("null");
						assets_id.setValue(requestselected.getAssets_id());
						if(requestselected.getAssets_id()==null)
							assets_id.setText("null");
						tags.setText(requestselected.getTags());
						document.setText(requestselected.getDocument());
						reference.setText(requestselected.getRefference());
						commentbox.setText(requestselected.getComment());
						comment.setText("");
				    }
					else {
						if(checkColumn.getValue(selected_fr)==true)
							list_selectedfr.add(selected_fr);
						else
							list_selectedfr.remove(selected_fr);
					}
				}
			}
		});
		    
	    draftcellTable.setRowStyles(new RowStyles<FinanceRequirements>() {
			public String getStyleNames(FinanceRequirements row, int rowIndex) {
				if(row.getComment().length()>1)
					return "moreinfoRowStyle";
				return null;
			}
		});
		
		// Create a data provider.
	    final ListDataProvider<FinanceRequirements> dataProvider = new 
			 ListDataProvider<FinanceRequirements>();
		 
		ListHandler<FinanceRequirements> columnSortHandler = 
		    		new ListHandler<FinanceRequirements>(list_fr);
		columnSortHandler.setComparator(Update_TimeColumn, new Comparator<FinanceRequirements>() {
		      @Override
		      public int compare(FinanceRequirements o1, FinanceRequirements o2) {		    	  
		    	  return o1.getUpdate_time().compareTo(o2.getUpdate_time());	    	     	  
		      }
		});
	    draftcellTable.addColumnSortHandler(columnSortHandler);
		 
		 // Create a new timer to perform an update periodically
		 
		dataProvider.addDataDisplay(draftcellTable);
 		list_fr = dataProvider.getList();
 		
 		toolbar.setVisibleNotice();
		toolbar.setTextNotice("Loading...");
		panel.setSize("100%", "60px");
		panel.add(label,500,10);
 		getData();
 		
 		toolbar.setDraft_Listener(new ToolBarPanel.Draft_Listener() {
			@Override
			public void onRefreshButtonClick() {
				list_fr.removeAll(list_fr);
				list_selectedfr.removeAll(list_selectedfr);
				selected_fr = null;
				expandbutton.setVisible(true);
				collapsebutton.setVisible(false);
				docklayoutpanel.remove(composebox);
				docklayoutpanel.remove(composeheader);
				docklayoutpanel.addSouth(composeheader, 3);
				draftcellTable.setPageStart(0);
				draftcellTable.setPageSize(13);
				toolbar.setVisibleNotice();
				toolbar.setTextNotice("Loading...");
				getData();
			}
		});
	}
	
	//Handler Event----------------------------------------------
	@UiHandler("expandbutton")
	void onExpandClick(ClickEvent event) {
		expandbutton.setVisible(false);
		collapsebutton.setVisible(true);
		docklayoutpanel.remove(composebox);
		docklayoutpanel.remove(composeheader);
		docklayoutpanel.addSouth(composebox, 17);
		docklayoutpanel.addSouth(composeheader, 3);
		draftcellTable.setPageSize(5);
		requesterlabel.removeStyleName("serverResponseLabelError");
		descriptionlabel.removeStyleName("serverResponseLabelError");
		comment.setText("");
		if(selected_fr!=null) {
			draftcellTable.setPageStart(Integer.valueOf(list_fr.indexOf(selected_fr)/5)*5);
			draftcellTable.setKeyboardSelectedRow(Integer.valueOf(list_fr.indexOf(selected_fr)%5));
		}
		else {
			req_id.setText("None");
			requester.setText("");
			manager.setText("");
			account.setText("");
			currency.setText("");
			real_amount.setText("");
			tax_amount.setText("");
			description.setText("");
			cus_id.setText("null");
			assets_id.setText("null");
			document.setText("");
			reference.setText("");
			tags.setText("");
			commentbox.setText("");
		}
	}
	
	@UiHandler("collapsebutton")
	void onCollapsebuttonClick(ClickEvent event) {
		expandbutton.setVisible(true);
		collapsebutton.setVisible(false);
		docklayoutpanel.remove(composebox);
		docklayoutpanel.remove(composeheader);
		docklayoutpanel.addSouth(composeheader, 3);
		draftcellTable.setPageStart(0);
		draftcellTable.setPageSize(13);
	}
	
	@UiHandler("send")
	void onSendClick(ClickEvent event) {
		if(VerifyField()&&draftcellTable.getPageSize()==5) {
			if(Window.confirm("Do you want to send this request?")) {
				FinanceRequirements fr = new FinanceRequirements();
				fr.setRequest_id(selected_fr.getRequest_id());
				fr.setReporter(selected_fr.getReporter());
				fr.setRequester(requester.getText());
				fr.setManager(manager.getText());
				fr.setAccount(account.getText());
				fr.setCurrency(currency.getText());
				fr.setReal_amount(real_amount.getValue());
				fr.setTax_amount(tax_amount.getValue());
				fr.setDescription(description.getText());
				if(cus_id.getText().equals("null")==false)
					fr.setCus_id(cus_id.getValue());
				if(assets_id.getText().equals("null")==false)
					fr.setAssets_id(assets_id.getValue());
				fr.setTags(tags.getText());
				fr.setDocument(document.getText());
				fr.setRefference(reference.getText());
				Date date = new Date(System.currentTimeMillis());
				fr.setInit_time(selected_fr.getInit_time());
				fr.setUpdate_time(date);
				fr.setStatus("DRAFT");
				fr.setComment(selected_fr.getComment());
				final String fromAddress = selected_fr.getReporter();
				final String toAddress = manager.getText();
				final String subject = "ITPRO-New Request # real_amount: "+real_amount.getText()+selected_fr.getCurrency()+", tax_amount: "+tax_amount.getText()+selected_fr.getCurrency()+" # for "+description.getText();
				final String url = Window.Location.getHost()+"/financial/#Finance%20Requirement";
				final String msgBody = "see Detail: "
						  +"\r\n "+url;
				String cm;
				if(comment.getText().equals("")==false)
					cm = selected_fr.getReporter().split("@")[0]+":"+comment.getText()+"\r\n \r\n";
				else
					cm = "";
				FinancialRequirementsObj.approveRequest(fr, "PENDING", date, cm, new AsyncCallback<Void>() {
					  public void onFailure(Throwable caught) {
						  toolbar.setVisibleNotice();
						  toolbar.setTextNotice("Request send failure, maybe the connection has been interrupt!");
					  }
					  public void onSuccess(Void result) {
						  toolbar.setVisibleNotice();
						  toolbar.setTextNotice("Request has been sent!");
						  list_fr.remove(selected_fr);
						  try {
							MailServices.sendmail(fromAddress, toAddress, subject, msgBody,
								  new AsyncCallback<Void>(){
									public void onFailure(Throwable caught) {}
									public void onSuccess(Void result) {}
							});
						  } catch (IOException e) {
							  e.printStackTrace();
						  }
						  expandbutton.setVisible(true);
						  collapsebutton.setVisible(false);
						  docklayoutpanel.remove(composebox);
						  docklayoutpanel.remove(composeheader);
						  docklayoutpanel.addSouth(composeheader, 3);
						  draftcellTable.setPageStart(0);
						  draftcellTable.setPageSize(13);
						  selected_fr=null;
						  if(listener!=null)
								listener.onUpdateUserLog();
					  }
				  });
				
			}
		}
	}
	
	@UiHandler("commentbox")
	void onCommentboxClick(MouseMoveEvent event) {
		composebox.remove(commentbox);
		commentbox.setSize("284px", "120px");
		composebox.add(commentbox,818,41);
	}
	
	@UiHandler("commentbox")
	void onCommentboxMouseOut(MouseOutEvent event) {
		composebox.remove(commentbox);
		commentbox.setSize("137px", "120px");
		composebox.add(commentbox,965,41);
	}
	
	@UiHandler("delete")
	void onDeleteClick(ClickEvent event) {
		if(selected_fr!=null && draftcellTable.getPageSize()==5) {
			if(Window.confirm("Do you want to remove this request?")) {
				FinancialRequirementsObj.delete(selected_fr, new AsyncCallback<Void>() {
					public void onFailure(Throwable caught) {
						toolbar.setVisibleNotice();
						toolbar.setTextNotice("Request send failure, the connection has been interrupt!");
					}
					public void onSuccess(Void result) {
						toolbar.setVisibleNotice();
						toolbar.setTextNotice("Request has been remove!");
						list_selectedfr.remove(selected_fr);
						list_fr.remove(selected_fr);
						expandbutton.setVisible(true);
						collapsebutton.setVisible(false);
						docklayoutpanel.remove(composebox);
						docklayoutpanel.remove(composeheader);
						docklayoutpanel.addSouth(composeheader, 3);
						draftcellTable.setPageStart(0);
						draftcellTable.setPageSize(13);
						selected_fr=null;
						if(listener!=null)
							listener.onUpdateUserLog();
					}
				});
			}
		}
		if(draftcellTable.getPageSize()==13 && list_selectedfr.isEmpty()==false) {
			if(Window.confirm("Do you want to remove all selected request?")) {
				for(final FinanceRequirements fr: list_selectedfr) {
					FinancialRequirementsObj.delete(fr, new AsyncCallback<Void>() {
						public void onFailure(Throwable caught) {
							toolbar.setVisibleNotice();
							toolbar.setTextNotice("Request send failure, the connection has been interrupt!");
						}
						public void onSuccess(Void result) {
							toolbar.setVisibleNotice();
							toolbar.setTextNotice("All request has been remove!");
							if(fr.equals(selected_fr)) {
								selected_fr=null;
							}
							list_fr.remove(fr);
							list_selectedfr.remove(fr);
						}
					});
				}
			}
		}
	}
}

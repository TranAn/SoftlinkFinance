package com.softlink.finance.client.view.desktop;

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
import com.google.gwt.user.client.Window;
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
import com.softlink.finance.client.view.DraftsView;
import com.softlink.datastore.model.FinanceData;

public class DesktopDraftsView extends Composite 
	implements DraftsView{
	
	interface Binder extends UiBinder<Widget, DesktopDraftsView> {}
	private static final Binder binder = GWT.create(Binder.class);

	private List<FinanceData> list_fr = new ArrayList<FinanceData>();
	private List<FinanceData> list_selectedfr = new ArrayList<FinanceData>();
	private FinanceData selected_fr;
	private AbsolutePanel panel = new AbsolutePanel();
	private Label label = new Label("<Folder is empty>");
	private int newDraft;
	private Date UserLog;
	private Presenter listener;
	
	@UiField(provided=true) CellTable<FinanceData> draftcellTable = new 
			CellTable<FinanceData>();
	@UiField(provided=true) SimplePager pager;
	@UiField DockLayoutPanel docklayoutpanel;
	@UiField Button send;
	@UiField Button delete;
	@UiField Label req_id;
	@UiField TextBox requester;
	@UiField TextBox account;
	@UiField TextBox manager;
	@UiField TextBox currency;
	@UiField IntegerBox bill_amount;
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
	
	/*
	 * ---Constructor---
	 */
	public DesktopDraftsView() {
		 // Do not refresh the headers and footers every time the data is updated.
	    draftcellTable.setAutoHeaderRefreshDisabled(true);
	    draftcellTable.setAutoFooterRefreshDisabled(true);

		// Create a Pager to control the table.
		SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
		pager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
		pager.setDisplay(draftcellTable);
		
		initWidget(binder.createAndBindUi(this));
	
		docklayoutpanel.remove(composebox);
		
		final SelectionModel<FinanceData> selectionModel = new 
				MultiSelectionModel<FinanceData>();
		draftcellTable.setSelectionModel(selectionModel,
		        DefaultSelectionEventManager.<FinanceData> createCheckboxManager());
		
		ListHandler<FinanceData> columnSortHandler = 
	    		new ListHandler<FinanceData>(list_fr);
		
		final Column<FinanceData, Boolean> checkColumn = 
				new Column<FinanceData, Boolean>(
		        new CheckboxCell(true, false)) {
		      @Override
		      public Boolean getValue(FinanceData object) { 
		    	  return selectionModel.isSelected(object);
			  }
		    };
		draftcellTable.addColumn(checkColumn, SafeHtmlUtils.fromSafeConstant("<br/>"));
		draftcellTable.setColumnWidth(checkColumn,"10px");
		
		final TextColumn<FinanceData> RequestIDColumn = new 
				TextColumn<FinanceData>() {
			@Override
		    public String getValue(FinanceData object) {
				return String.valueOf(object.getRequest_id());
		    }
		};
		draftcellTable.addColumn(RequestIDColumn, "Request ID");
		draftcellTable.setColumnWidth(RequestIDColumn,"10%");
		
		final TextColumn<FinanceData> ReporterColumn = new 
				TextColumn<FinanceData>() {
			@Override
			public String getValue(FinanceData object) {
				return object.getReporter();
			}
		};
		draftcellTable.addColumn(ReporterColumn, "Reporter");
		draftcellTable.setColumnWidth(ReporterColumn,"15%");
		
		final TextColumn<FinanceData> DescriptionColumn = new 
				TextColumn<FinanceData>() {
			@Override
			public String getValue(FinanceData object) {
				if (object.getDescription().length()<=51)
					return object.getDescription();
				return object.getDescription().substring(0, 51)+" . . .";
			}
		};
		draftcellTable.addColumn(DescriptionColumn, "Description");
		draftcellTable.setColumnWidth(DescriptionColumn,"35%");
		
		final Column<FinanceData, Date> Update_TimeColumn = new 
		    		Column<FinanceData, Date>(new DateCell()) {
		      @Override
		      public Date getValue(FinanceData object) {
		        return object.getUpdate_time();
		      }
		    };
	    Update_TimeColumn.setSortable(true);
	    draftcellTable.addColumn(Update_TimeColumn, "Update_Time");
	    draftcellTable.setColumnWidth(Update_TimeColumn,"25%");
	    columnSortHandler.setComparator(Update_TimeColumn, new Comparator<FinanceData>() {
		      @Override
		      public int compare(FinanceData o1, FinanceData o2) {		    	  
		    	  return o1.getUpdate_time().compareTo(o2.getUpdate_time());	    	     	  
		      }
		});
	    draftcellTable.addColumnSortHandler(columnSortHandler);
		    
	    final TextColumn<FinanceData> StatusColumn = new 
	    		TextColumn<FinanceData>() {
			@Override
			public String getValue(FinanceData object) {
				return object.getStatus();
			}
		};
		draftcellTable.addColumn(StatusColumn, "Status");
		draftcellTable.setColumnWidth(StatusColumn,"10%");
			
		draftcellTable.addCellPreviewHandler(new Handler<FinanceData>(){
			public void onCellPreview(
					CellPreviewEvent<FinanceData> event) {
				if (BrowserEvents.CLICK.equals(event.getNativeEvent().getType())) {
					selected_fr = event.getValue();
//					if(selected_fr.getUpdate_time().equals(UserLog))
//						FinancialRequirementsObj.updateUserLog(selected_fr.getUpdate_time(), 
//								new AsyncCallback<Void> () {
//									public void onFailure(Throwable caught) {}
//									public void onSuccess(Void result) {
//										if(listener!=null)
//											listener.onUpdateUserLog();
//									}	
//						});
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
						FinanceData requestselected = event.getValue();
						req_id.setText(String.valueOf(requestselected.getRequest_id()));
						requester.setText(requestselected.getRequester());
						manager.setText(requestselected.getManager());
						account.setText(requestselected.getAccount());
						currency.setText(requestselected.getCurrency());
						real_amount.setValue(requestselected.getReal_amount());
						bill_amount.setValue(requestselected.getBill_amount());
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
		    
	    draftcellTable.setRowStyles(new RowStyles<FinanceData>() {
			public String getStyleNames(FinanceData row, int rowIndex) {
				if(row.getComment().length()>1)
					return "moreinfoRowStyle";
				return null;
			}
		});
		
		// Create a data provider.
	    final ListDataProvider<FinanceData> dataProvider = new 
			 ListDataProvider<FinanceData>();
		dataProvider.addDataDisplay(draftcellTable);
 		list_fr = dataProvider.getList();
		panel.setSize("100%", "60px");
		panel.add(label,500,10);
	}
	
	/*
	 * Procedure---
	 */
	public void setNotifyStyle(final int newDraft, Date updateUserLog) {
//		UserLog = updateUserLog;
//		this.newDraft = newDraft;
//		 if(list_fr.isEmpty())
//			 getData();
//		 else {
//	  		 getNewData();
//		 }
	}
	
	public void setDocSize(String width, String height) {
		docklayoutpanel.setWidth(width);
		docklayoutpanel.setHeight(height);
	}
	
	public void refresh() {
//		if(list_fr.isEmpty()) {
//			toolbar.setVisibleNotice();
//			toolbar.setTextNotice("Loading...");
//			getData();
//		} else {
//			toolbar.setVisibleNotice();
//			toolbar.setTextNotice("Refresh...");
//			getNewData();
//		}
	}
	
	public void redrawTable() {
		draftcellTable.setRowStyles(new RowStyles<FinanceData>() {
			public String getStyleNames(FinanceData row, int rowIndex) {
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
	
	public boolean VerifyField(){
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
	
	
	/*
	 * Event Handler---
	 */
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
			bill_amount.setText("");
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
				FinanceData fr = new FinanceData();
				fr.setRequest_id(selected_fr.getRequest_id());
				fr.setReporter(selected_fr.getReporter());
				fr.setRequester(requester.getText());
				fr.setManager(manager.getText());
				fr.setAccount(account.getText());
				fr.setCurrency(currency.getText());
				fr.setReal_amount(real_amount.getValue());
				fr.setBill_amount(bill_amount.getValue());
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
				final String subject = "ITPRO-New Request # real_amount: "+real_amount.getText()+selected_fr.getCurrency()+", tax_amount: "+bill_amount.getText()+selected_fr.getCurrency()+" # for "+description.getText();
				final String url = Window.Location.getHost()+"/financial/#Finance%20Requirement";
				final String msgBody = "see Detail: "
						  +"\r\n "+url;
				String cm;
				if(comment.getText().equals("")==false)
					cm = selected_fr.getReporter().split("@")[0]+":"+comment.getText()+"\r\n \r\n";
				else
					cm = "";
//				FinancialRequirementsObj.approveRequest(fr, "PENDING", date, cm, new AsyncCallback<Void>() {
//					  public void onFailure(Throwable caught) {
//						  toolbar.setVisibleNotice();
//						  toolbar.setTextNotice("Request send failure, maybe the connection has been interrupt!");
//					  }
//					  public void onSuccess(Void result) {
//						  toolbar.setVisibleNotice();
//						  toolbar.setTextNotice("Request has been sent!");
//						  list_fr.remove(selected_fr);
//						  try {
//							MailServices.sendmail(fromAddress, toAddress, subject, msgBody,
//								  new AsyncCallback<Void>(){
//									public void onFailure(Throwable caught) {}
//									public void onSuccess(Void result) {}
//							});
//						  } catch (IOException e) {
//							  e.printStackTrace();
//						  }
//						  expandbutton.setVisible(true);
//						  collapsebutton.setVisible(false);
//						  docklayoutpanel.remove(composebox);
//						  docklayoutpanel.remove(composeheader);
//						  docklayoutpanel.addSouth(composeheader, 3);
//						  draftcellTable.setPageStart(0);
//						  draftcellTable.setPageSize(13);
//						  selected_fr=null;
//						  if(listener!=null)
//								listener.onUpdateUserLog();
//					  }
//				  });
				
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
//				FinancialRequirementsObj.delete(selected_fr, new AsyncCallback<Void>() {
//					public void onFailure(Throwable caught) {
//						toolbar.setVisibleNotice();
//						toolbar.setTextNotice("Request send failure, the connection has been interrupt!");
//					}
//					public void onSuccess(Void result) {
//						toolbar.setVisibleNotice();
//						toolbar.setTextNotice("Request has been remove!");
//						list_selectedfr.remove(selected_fr);
//						list_fr.remove(selected_fr);
//						expandbutton.setVisible(true);
//						collapsebutton.setVisible(false);
//						docklayoutpanel.remove(composebox);
//						docklayoutpanel.remove(composeheader);
//						docklayoutpanel.addSouth(composeheader, 3);
//						draftcellTable.setPageStart(0);
//						draftcellTable.setPageSize(13);
//						selected_fr=null;
//						if(listener!=null)
//							listener.onUpdateUserLog();
//					}
//				});
			}
		}
		if(draftcellTable.getPageSize()==13 && list_selectedfr.isEmpty()==false) {
			if(Window.confirm("Do you want to remove all selected request?")) {
				for(final FinanceData fr: list_selectedfr) {
//					FinancialRequirementsObj.delete(fr, new AsyncCallback<Void>() {
//						public void onFailure(Throwable caught) {
//							toolbar.setVisibleNotice();
//							toolbar.setTextNotice("Request send failure, the connection has been interrupt!");
//						}
//						public void onSuccess(Void result) {
//							toolbar.setVisibleNotice();
//							toolbar.setTextNotice("All request has been remove!");
//							if(fr.equals(selected_fr)) {
//								selected_fr=null;
//							}
//							list_fr.remove(fr);
//							list_selectedfr.remove(fr);
//						}
//					});
				}
			}
		}
	}

	/*
	 * ---Implement View Interface---
	 */
	@Override
	public void setPresenter(Presenter listener) {
		// TODO Auto-generated method stub
		this.listener = listener;
	}

	@Override
	public void setNewData(List<FinanceData> list_fr) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		 if(list_fr.isEmpty()) {
			 scrollpanel.remove(draftcellTable);
			 scrollpanel.add(panel);
		 } else {
			 for(int i=list_fr.size()-1;i>=0;i--)
				 this.list_fr.add(list_fr.get(i));
			 scrollpanel.remove(panel);
			 scrollpanel.add(draftcellTable);
		 }
	}

	@Override
	public void setUpdateData(List<FinanceData> list_fr) {
		// TODO Auto-generated method stub
		for(FinanceData fr: list_fr)   				
			this.list_fr.add(0,fr);
		redrawTable();
	}

	@Override
	public void removeDataItem(FinanceData fr) {
		// TODO Auto-generated method stub
		list_fr.remove(fr);
	}

	@Override
	public int getDataCount() {
		// TODO Auto-generated method stub
		return list_fr.size();
	}

	@Override
	public FinanceData getLastData() {
		// TODO Auto-generated method stub
		return list_fr.get(0);
	}
}

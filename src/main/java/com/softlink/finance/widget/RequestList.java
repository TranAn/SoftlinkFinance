package com.softlink.finance.widget;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.DateCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.BrowserEvents;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
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
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.view.client.CellPreviewEvent;
import com.google.gwt.view.client.CellPreviewEvent.Handler;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionModel;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.softlink.finance.datastore.FinancialRequirementsObj;
import com.softlink.finance.datastore.FinancialRequirementsObjAsync;
import com.softlink.financedatastore.client.FinancialRequirements;

public class RequestList extends Composite {

	interface Binder extends UiBinder<DockLayoutPanel, RequestList> { }
	private static final Binder binder = GWT.create(Binder.class);
	private final static FinancialRequirementsObjAsync FinancialRequirementsObj = 
			GWT.create(FinancialRequirementsObj.class);
	@UiField(provided=true) CellTable<FinancialRequirements> cellTable = new 
			CellTable<FinancialRequirements>();
	@UiField(provided=true) SimplePager pager;
	private List<FinancialRequirements> list_fr = new ArrayList<FinancialRequirements>();
	private List<FinancialRequirements> list_selectedfr = new ArrayList<FinancialRequirements>();
	private FinancialRequirements selected_fr = null;
	private Timer elapsedTimer = null;
	private Listener listener;
	private Date UserLog;
	private AbsolutePanel panel = new AbsolutePanel();
	private Label label = new Label("<Folder is empty>");
	
	@UiField ScrollPanel scrollpanel;
	
	//Inner Function-----------------------------------------------
	public void setNotifyStyle(final int newFinancialRequirement, Date updateUserLog){
		UserLog = updateUserLog;
		 cellTable.setRowStyles(new RowStyles<FinancialRequirements>() {
			public String getStyleNames(FinancialRequirements row, int rowIndex) {
				if((row.getStatus().equals("APPROVED"))&&(rowIndex+1>newFinancialRequirement))
					return "approvedRowStyle";
				if((row.getStatus().equals("DENIED"))&&(rowIndex+1>newFinancialRequirement))
					return "deniedRowStyle";
				if((row.getStatus().equals("APPROVED"))&&(rowIndex+1<=newFinancialRequirement))
					return "approvedBoldRowStyle";
				if((row.getStatus().equals("DENIED"))&&(rowIndex+1<=newFinancialRequirement))
					return "deniedBoldRowStyle";
				if((row.getStatus().equals("PENDING"))&&(rowIndex+1<=newFinancialRequirement))
					return "normal1BoldRowStyle";
				return null;
			}
		});
		cellTable.redraw();
	}
	
	public interface Listener {
		void onTableSelected(FinancialRequirements requestselected);
		void onLoadDataFail();
		void onLoadComplete();
		void onDataNotFound();
		void onLoading();
		void onRefresh();
		void onDelete();
		void onUpdateUserLog();
	}
	
	public void setListener(Listener listener) {
	    this.listener = listener;
	}
	
	public void RemoveItem(FinancialRequirements item) {
		list_fr.remove(item);
	}
	
	public FinancialRequirements getSelectedfr() {
		return selected_fr;
	}
	
	protected void refresh() {
		if(list_fr.isEmpty()) {
			if(listener!=null)
			 	listener.onLoading();
			getData();
		} else {
			if(listener!=null)
			 	listener.onRefresh();
			getNewData();
		}
	}
	
	protected void refreshData() {
		list_fr.removeAll(list_fr);
		list_selectedfr.removeAll(list_selectedfr);
		if(listener!=null)
		 	listener.onLoading();
		getData();
	}
	
	protected void stop() {
		if(elapsedTimer!=null)
			elapsedTimer.cancel();
		elapsedTimer = null;
	}
	
	protected void start() {
		warmUpRequest();
	}
	
	//RPC call-----------------------------------------------------

	private void getData(){
		 FinancialRequirementsObj.list_fr(
	    		 new AsyncCallback<List<FinancialRequirements>>() {
	    	 public void onFailure(Throwable caught) {
	    		 cellTable.setTitle("Load Data Fail");
	    		 if(listener!=null)
	    			 listener.onLoadDataFail();
			 }
			 public void onSuccess(List<FinancialRequirements> result) {
				 if(listener!=null)
					 listener.onLoadComplete();
				 if(result.isEmpty()) {
					 scrollpanel.remove(cellTable);
					 scrollpanel.add(panel);
				 } else {
					 for(int i=result.size()-1;i>=0;i--)
						 list_fr.add(result.get(i));
					 scrollpanel.remove(panel);
					 scrollpanel.add(cellTable);
				 }
			 }
		 });
	}
	
	private void getNewData() {
		FinancialRequirementsObj.list_newfr(list_fr.get(0),
 	    		 new AsyncCallback<List<FinancialRequirements>>() {
 	    	 public void onFailure(Throwable caught) {
 	    		 cellTable.setTitle("Update Data Fail");
 	    		 if(listener!=null)
	    			 listener.onLoadDataFail();
 			 }
			 public void onSuccess(List<FinancialRequirements> result) {
	  			 for(FinancialRequirements fr: result) {
	  				 if(fr.getStatus().equals("PENDING"))
	  					 list_fr.add(0,fr);
	  				 if(fr.getStatus().equals("APPROVED")||
	  						 fr.getStatus().equals("DENIED")) {
	  					 Long id = fr.getRequest_id();
	  					 for(FinancialRequirements oldfr: list_fr)
	  						 if(oldfr.getRequest_id().equals(id))
	  							 list_fr.remove(oldfr);
	  					 list_fr.add(0,fr);
	  				 }
	  				 if(fr.getStatus().equals("DRAFT")||
	  						 fr.getStatus().equals("DELETED")) {
	  					Long id = fr.getRequest_id();
	  					for(FinancialRequirements oldfr: list_fr)
	  						if(oldfr.getRequest_id().equals(id))
	  							list_fr.remove(oldfr);
	  				 }
	  			 }
	  			 if(listener!=null)
	    			 listener.onLoadComplete();
	  		 }
 		 });
	}

	public RequestList() {
		 // Do not refresh the headers and footers every time the data is updated.
	    cellTable.setAutoHeaderRefreshDisabled(true);
	    cellTable.setAutoFooterRefreshDisabled(true);

		// Create a Pager to control the table.
		SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
		pager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
		pager.setDisplay(cellTable);
		
		initWidget(binder.createAndBindUi(this));
		
		final SelectionModel<FinancialRequirements> selectionModel = new 
				MultiSelectionModel<FinancialRequirements>();
		cellTable.setSelectionModel(selectionModel,
		        DefaultSelectionEventManager.<FinancialRequirements> createCheckboxManager());
		
		final Column<FinancialRequirements, Boolean> checkColumn = 
				new Column<FinancialRequirements, Boolean>(
		        new CheckboxCell(true, false)) {
		      @Override
		      public Boolean getValue(FinancialRequirements object) { 
		    	  return selectionModel.isSelected(object);
			  }
		    };
		cellTable.addColumn(checkColumn, SafeHtmlUtils.fromSafeConstant("<br/>"));
		cellTable.setColumnWidth(checkColumn,"5%");
		
		final TextColumn<FinancialRequirements> ReporterColumn = new 
				TextColumn<FinancialRequirements>() {
			@Override
		    public String getValue(FinancialRequirements object) {
				return object.getReporter();
		    }
		};
		cellTable.addColumn(ReporterColumn, "Reporter");
		cellTable.setColumnWidth(ReporterColumn,"20%");
		
		final TextColumn<FinancialRequirements> DescriptionColumn = new 
				TextColumn<FinancialRequirements>() {
			@Override
			public String getValue(FinancialRequirements object) {
				if (object.getDescription().length()<=65)
					return object.getDescription();
				return object.getDescription().substring(0, 65)+" . . .";
			}
		};
		cellTable.addColumn(DescriptionColumn, "Description");
		cellTable.setColumnWidth(DescriptionColumn,"45%");
		
		final Column<FinancialRequirements, Date> Update_TimeColumn = new 
		    		Column<FinancialRequirements, Date>(new DateCell()) {
		      @Override
		      public Date getValue(FinancialRequirements object) {
		        return object.getUpdate_time();
		      }
		    };
	    Update_TimeColumn.setSortable(true);
	    cellTable.addColumn(Update_TimeColumn, "Update_Time");
	    cellTable.setColumnWidth(Update_TimeColumn,"20%");
		    
	    final TextColumn<FinancialRequirements> StatusColumn = new 
	    		TextColumn<FinancialRequirements>() {
			@Override
			public String getValue(FinancialRequirements object) {
				return object.getStatus();
			}
		};
		cellTable.addColumn(StatusColumn, "Status");
		cellTable.setColumnWidth(StatusColumn,"10%");
			
		cellTable.addCellPreviewHandler(new Handler<FinancialRequirements>(){
			public void onCellPreview(
					CellPreviewEvent<FinancialRequirements> event) {
				if (BrowserEvents.CLICK.equals(event.getNativeEvent().getType())) {
					if(event.getColumn()!=0){
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
						FinancialRequirements requestselected = event.getValue();
				        if (requestselected != null) {	        	
				        	if (listener != null) 
				        		listener.onTableSelected(requestselected);			        		        
				        }
					}
					else {
						if(checkColumn.getValue(event.getValue())==true)
							list_selectedfr.add(event.getValue());
						else
							list_selectedfr.remove(event.getValue());
					}
				}
			}
		});
		    
	    cellTable.setRowStyles(new RowStyles<FinancialRequirements>() {
			public String getStyleNames(FinancialRequirements row, int rowIndex) {
				if(row.getStatus().equals("APPROVED"))
					return "approvedRowStyle";
				if(row.getStatus().equals("DENIED"))
					return "deniedRowStyle";
				return null;
			}
		});
		
		// Create a data provider.
	    final ListDataProvider<FinancialRequirements> dataProvider = new 
			 ListDataProvider<FinancialRequirements>();
		 
		ListHandler<FinancialRequirements> columnSortHandler = 
		    		new ListHandler<FinancialRequirements>(list_fr);
		columnSortHandler.setComparator(Update_TimeColumn, new Comparator<FinancialRequirements>() {
		      @Override
		      public int compare(FinancialRequirements o1, FinancialRequirements o2) {		    	  
		    	  return o1.getUpdate_time().compareTo(o2.getUpdate_time());	    	  
		      }
		});
	    cellTable.addColumnSortHandler(columnSortHandler);
		 
		dataProvider.addDataDisplay(cellTable);
 		list_fr = dataProvider.getList();
 		
 		if(listener!=null)
		 	listener.onLoading();
 		panel.setSize("100%", "60px");
		panel.add(label,500,10);
 		getData();
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
	
	//Event Handler------------------------------------------------

	@UiHandler("image")
	void onDeleteClick(ClickEvent event) {
		if(list_selectedfr.isEmpty()==false)
			if(Window.confirm("All denied request selected will be delete, are you sure to do that?")==true)
				for(final FinancialRequirements fr: list_selectedfr) {
					final Date date = new Date(System.currentTimeMillis());
					if(fr.getStatus().equals("DENIED"))
						FinancialRequirementsObj.approveRequest(fr, "DELETED", date, "", 
								new AsyncCallback<Void>() {
							@Override
							public void onFailure(Throwable caught) {
								if(listener!=null)
									 listener.onLoadDataFail();
							}
							@Override
							public void onSuccess(Void result) {
								list_selectedfr.remove(fr);
								list_fr.remove(fr);
								if(listener!=null)
									 listener.onDelete();
							}
						});
				}
	}
}

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
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.CellPreviewEvent;
import com.google.gwt.view.client.CellPreviewEvent.Handler;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionModel;
import com.google.gwt.user.client.ui.ScrollPanel;

import com.softlink.finance.datastore.FinanceRequirementsObj;
import com.softlink.finance.datastore.FinanceRequirementsObjAsync;
import com.softlink.financedatastore.client.FinanceRequirements;

public class Trash extends Composite {

	interface Binder extends UiBinder<Widget, Trash> {}
	private static final Binder binder = GWT.create(Binder.class);
	
	private final static FinanceRequirementsObjAsync FinancialRequirementsObj = 
			GWT.create(FinanceRequirementsObj.class);
	private ToolBarPanel toolbar;
	@UiField(provided=true) CellTable<FinanceRequirements> cellTable = new 
			CellTable<FinanceRequirements>();
	@UiField(provided=true) SimplePager pager;
	private List<FinanceRequirements> list_fr = new ArrayList<FinanceRequirements>();
	private List<FinanceRequirements> list_selectedfr = new ArrayList<FinanceRequirements>();
	private FinanceRequirements selectedfr = null;
	private Timer elapsedTimer = null;
	private RequestDetail requestdetail =  new RequestDetail();
	private AbsolutePanel panel = new AbsolutePanel();
	private Label label = new Label("<Folder is empty>");
	
	@UiField DockLayoutPanel docklayoutpanel;
	@UiField AbsolutePanel headerpanel;
	@UiField ScrollPanel table;
	
	//Inner Function-----------------------------------------------
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
			list_fr.removeAll(list_fr);
			getData();
		}
		toolbar.setTrashToolBar();
		docklayoutpanel.remove(requestdetail);
		docklayoutpanel.remove(headerpanel);
		docklayoutpanel.remove(table);
		docklayoutpanel.addNorth(headerpanel, 3);
		docklayoutpanel.add(table);
		list_selectedfr.remove(selectedfr);
	}
	
	public void stop() {
		if(elapsedTimer!=null)
			elapsedTimer.cancel();
		elapsedTimer = null;
	}
	
	public void start() {
		warmUpRequest();
	}
	
	//Periodically Time--------------------------------------------
	private void warmUpRequest() {
		elapsedTimer = new Timer () {
			 public void run() {
				//RPC call to get new data
				 if(list_fr.isEmpty())
					 getData();
				 else {
					 list_fr.removeAll(list_fr);
			  		 getData();
				 }
		  	 }
		 };
		 elapsedTimer.scheduleRepeating(10000);
		 // ... The elapsed timer has started ...
	}
	
	//RPC call-----------------------------------------------------
	
	private void getData(){
		 FinancialRequirementsObj.list_trashfr(
	    		 new AsyncCallback<List<FinanceRequirements>>() {
	    	 public void onFailure(Throwable caught) {
	    		 toolbar.setVisibleNotice();
				 toolbar.setTextNotice("Load failure, the connetion has been interupt!");
			 }
			 public void onSuccess(List<FinanceRequirements> result) { 
				 toolbar.setHideNotice();
				 if(result.isEmpty()) {
					 table.remove(cellTable);
					 table.add(panel);
				 } else {
					 for(int i=result.size()-1;i>=0;i--)
						 list_fr.add(result.get(i));
					 table.remove(panel);
					 table.add(cellTable);
				 }
			 }
		 });
	}
	
//	private void getNewData() {
//		FinanceRequirementsObj.list_newtrashfr(list_fr.get(0),
// 	    		 new AsyncCallback<List<FinanceRequirements>>() {
// 	    	 public void onFailure(Throwable caught) {
// 	    		toolbar.setVisibleNotice();
//				toolbar.setTextNotice("Load failure, the connetion has been interupt!");
// 			 }
//			 public void onSuccess(List<FinanceRequirements> result) {
//				 toolbar.setHideNotice();
//	  			 for(FinanceRequirements fr: result)   				
//	  				 list_fr.add(0,fr);
//	  		 }
// 		 });
//	}
	
	public Trash(final ToolBarPanel toolbar) {
		
		 // Do not refresh the headers and footers every time the data is updated.
	    cellTable.setAutoHeaderRefreshDisabled(true);
	    cellTable.setAutoFooterRefreshDisabled(true);

		// Create a Pager to control the table.
		SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
		pager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
		pager.setDisplay(cellTable);
		
		initWidget(binder.createAndBindUi(this));
		this.toolbar = toolbar;
		
		final SelectionModel<FinanceRequirements> selectionModel = new 
				MultiSelectionModel<FinanceRequirements>();
		cellTable.setSelectionModel(selectionModel,
		        DefaultSelectionEventManager.<FinanceRequirements> createCheckboxManager());
		
		final Column<FinanceRequirements, Boolean> checkColumn = 
				new Column<FinanceRequirements, Boolean>(
		        new CheckboxCell(true, false)) {
		      @Override
		      public Boolean getValue(FinanceRequirements object) { 
		    	  return selectionModel.isSelected(object);
			  }
		    };
		cellTable.addColumn(checkColumn, SafeHtmlUtils.fromSafeConstant("<br/>"));
		cellTable.setColumnWidth(checkColumn,"5%");
		
		final TextColumn<FinanceRequirements> RequestIDColumn = new 
				TextColumn<FinanceRequirements>() {
			@Override
		    public String getValue(FinanceRequirements object) {
				return String.valueOf(object.getRequest_id());
		    }
		};
		cellTable.addColumn(RequestIDColumn, "Request ID");
		cellTable.setColumnWidth(RequestIDColumn,"10%");
		
		final TextColumn<FinanceRequirements> ReporterColumn = new 
				TextColumn<FinanceRequirements>() {
			@Override
			public String getValue(FinanceRequirements object) {
				return object.getReporter();
			}
		};
		cellTable.addColumn(ReporterColumn, "Reporter");
		cellTable.setColumnWidth(ReporterColumn,"15%");
		
		final TextColumn<FinanceRequirements> DescriptionColumn = new 
				TextColumn<FinanceRequirements>() {
			@Override
			public String getValue(FinanceRequirements object) {
				if (object.getDescription().length()<=51)
					return object.getDescription();
				return object.getDescription().substring(0, 51)+" . . .";
			}
		};
		cellTable.addColumn(DescriptionColumn, "Description");
		cellTable.setColumnWidth(DescriptionColumn,"35%");
		
		final Column<FinanceRequirements, Date> Update_TimeColumn = new 
		    		Column<FinanceRequirements, Date>(new DateCell()) {
		      @Override
		      public Date getValue(FinanceRequirements object) {
		        return object.getUpdate_time();
		      }
		    };
	    Update_TimeColumn.setSortable(true);
	    cellTable.addColumn(Update_TimeColumn, "Update_Time");
	    cellTable.setColumnWidth(Update_TimeColumn,"25%");
		    
	    final TextColumn<FinanceRequirements> StatusColumn = new 
	    		TextColumn<FinanceRequirements>() {
			@Override
			public String getValue(FinanceRequirements object) {
				return object.getStatus();
			}
		};
		cellTable.addColumn(StatusColumn, "Status");
		cellTable.setColumnWidth(StatusColumn,"10%");
			
		cellTable.addCellPreviewHandler(new Handler<FinanceRequirements>(){
			public void onCellPreview(
					CellPreviewEvent<FinanceRequirements> event) {
				if (BrowserEvents.CLICK.equals(event.getNativeEvent().getType())) {
					if(event.getColumn()!=0){
						selectedfr = event.getValue();
						list_selectedfr.add(selectedfr);
						toolbar.setTrashDetailToolBar();
						docklayoutpanel.remove(headerpanel);
						docklayoutpanel.remove(table);
						docklayoutpanel.add(requestdetail);
						requestdetail.setItem(event.getValue());
					} else {
						if(checkColumn.getValue(event.getValue())==true)
							list_selectedfr.add(event.getValue());
						else
							list_selectedfr.remove(event.getValue());
					}
				}
			}
		});
		    
	    cellTable.setRowStyles(new RowStyles<FinanceRequirements>() {
			public String getStyleNames(FinanceRequirements row, int rowIndex) {
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
	    cellTable.addColumnSortHandler(columnSortHandler);
		 
		 // Create a new timer to perform an update periodically
		 
		dataProvider.addDataDisplay(cellTable);
 		list_fr = dataProvider.getList();
 		
 		toolbar.setVisibleNotice();
		toolbar.setTextNotice("Loading...");
		panel.setSize("100%", "60px");
		panel.add(label,500,10);
 		getData();
 		
 		toolbar.setTrash_Listener(new ToolBarPanel.Trash_Listener() {
			public void onBackButtonClick() {
				toolbar.setTrashToolBar();
				docklayoutpanel.addNorth(headerpanel, 3);
				docklayoutpanel.remove(requestdetail);
				docklayoutpanel.remove(headerpanel);
				docklayoutpanel.remove(table);
				docklayoutpanel.addNorth(headerpanel, 3);
				docklayoutpanel.add(table);
				list_selectedfr.remove(selectedfr);
			}
			public void onRefreshButtonClick() {
				final Date date = new Date(System.currentTimeMillis());
				if(list_selectedfr.isEmpty()==false)
					if(Window.confirm("Do you want to restore all selected request?"))
						for(final FinanceRequirements fr: list_selectedfr)
							FinancialRequirementsObj.approveRequest(fr, "DENIED", date, "", 
									new AsyncCallback<Void>() {
								@Override
								public void onFailure(Throwable caught) {
									toolbar.setTextNotice("Update Failure, the connection has been interupt!");
									toolbar.setVisibleNotice();
								}
								@Override
								public void onSuccess(Void result) {
									list_fr.remove(fr);
									list_selectedfr.remove(fr);
									toolbar.setTrashToolBar();
									toolbar.setTextNotice("All selected request has been restore");
									toolbar.setVisibleNotice();							
									docklayoutpanel.remove(requestdetail);
									docklayoutpanel.remove(headerpanel);
									docklayoutpanel.remove(table);
									docklayoutpanel.addNorth(headerpanel, 3);
									docklayoutpanel.add(table);
								}
							});
			}
			public void onDeleteButtonClick() {
				if(list_selectedfr.isEmpty()==false)
					if(Window.confirm("Do you want to remove all selected request?"))
						for(final FinanceRequirements fr: list_selectedfr)
							FinancialRequirementsObj.delete(fr, new AsyncCallback<Void>() {
								@Override
								public void onFailure(Throwable caught) {
									toolbar.setTextNotice("Request send failure, the connection has been interrupt!");
									toolbar.setVisibleNotice();
								}
								@Override
								public void onSuccess(Void result) {
									list_fr.remove(fr);
									list_selectedfr.remove(fr);
									toolbar.setTrashToolBar();
									toolbar.setTextNotice("All selected request has been remove!");
									toolbar.setVisibleNotice();
									docklayoutpanel.remove(requestdetail);
									docklayoutpanel.remove(headerpanel);
									docklayoutpanel.remove(table);
									docklayoutpanel.addNorth(headerpanel, 3);
									docklayoutpanel.add(table);
								}
							});
			}
		});
	}
	
}

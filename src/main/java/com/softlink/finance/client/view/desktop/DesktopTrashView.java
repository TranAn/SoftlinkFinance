package com.softlink.finance.client.view.desktop;

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
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.CellPreviewEvent;
import com.google.gwt.view.client.CellPreviewEvent.Handler;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionModel;
import com.softlink.finance.client.view.RequestDetailView;
import com.softlink.finance.client.view.TrashView;
import com.softlink.finance.shared.FinanceRequirements;

public class DesktopTrashView extends Composite 
	implements TrashView{

	interface Binder extends UiBinder<Widget, DesktopTrashView> {}
	private static final Binder binder = GWT.create(Binder.class);

	private List<FinanceRequirements> list_fr = new ArrayList<FinanceRequirements>();
	private List<FinanceRequirements> list_selectedfr = new ArrayList<FinanceRequirements>();
	private FinanceRequirements selectedfr = null;
	private RequestDetailView requestdetail;
	private AbsolutePanel panel = new AbsolutePanel();
	private Label label = new Label("<Folder is empty>");
	
	@UiField(provided=true) CellTable<FinanceRequirements> cellTable = new 
			CellTable<FinanceRequirements>();
	@UiField(provided=true) SimplePager pager;
	@UiField DockLayoutPanel docklayoutpanel;
	@UiField AbsolutePanel headerpanel;
	@UiField ScrollPanel table;
	
	/*
	 * Constructor---
	 */
	public DesktopTrashView() {
		 // Do not refresh the headers and footers every time the data is updated.
	    cellTable.setAutoHeaderRefreshDisabled(true);
	    cellTable.setAutoFooterRefreshDisabled(true);
	
		// Create a Pager to control the table.
		SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
		pager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
		pager.setDisplay(cellTable);
		
		initWidget(binder.createAndBindUi(this));
		
		final SelectionModel<FinanceRequirements> selectionModel = new 
				MultiSelectionModel<FinanceRequirements>();
		cellTable.setSelectionModel(selectionModel,
		        DefaultSelectionEventManager.<FinanceRequirements> createCheckboxManager());
		
		ListHandler<FinanceRequirements> columnSortHandler = 
	    		new ListHandler<FinanceRequirements>(list_fr);
		
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
	    columnSortHandler.setComparator(Update_TimeColumn, new Comparator<FinanceRequirements>() {
		      @Override
		      public int compare(FinanceRequirements o1, FinanceRequirements o2) {		    	  
		    	  return o1.getUpdate_time().compareTo(o2.getUpdate_time());    	  
		      }
		});
	    cellTable.addColumnSortHandler(columnSortHandler);
		    
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
						docklayoutpanel.remove(headerpanel);
						docklayoutpanel.remove(table);
						docklayoutpanel.add(requestdetail);
//						requestdetail.setItem(event.getValue());
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
		dataProvider.addDataDisplay(cellTable);
		list_fr = dataProvider.getList();
//		toolbar.setVisibleNotice();
//		toolbar.setTextNotice("Loading...");
		panel.setSize("100%", "60px");
		panel.add(label,500,10);
//		getData();
	}

	/*
	 * Procedure---
	 */
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
//			list_fr.removeAll(list_fr);
//			getData();
//		}
//		toolbar.setTrashToolBar();
//		docklayoutpanel.remove(requestdetail);
//		docklayoutpanel.remove(headerpanel);
//		docklayoutpanel.remove(table);
//		docklayoutpanel.addNorth(headerpanel, 3);
//		docklayoutpanel.add(table);
//		list_selectedfr.remove(selectedfr);
	}
	
}

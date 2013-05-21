package com.softlink.finance.client.view.desktop;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.cell.client.CheckboxCell;
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
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.CellPreviewEvent;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionModel;
import com.google.gwt.view.client.CellPreviewEvent.Handler;

import com.softlink.finance.client.view.FinanceRequirementView;
import com.softlink.finance.client.view.RequestDetailView;
import com.softlink.finance.shared.FinanceRequirements;
import com.softlink.finance.client.place.FinanceRequirementPlace;

public class DesktopFinanceRequirementView extends Composite 
	implements FinanceRequirementView{
	
	interface Binder extends UiBinder<Widget, DesktopFinanceRequirementView> { }
	private static final Binder binder = GWT.create(Binder.class);

	private List<FinanceRequirements> list_fr = new ArrayList<FinanceRequirements>();
	private List<FinanceRequirements> list_selectedfr = new ArrayList<FinanceRequirements>();
	private FinanceRequirements selected_fr = null;
	private RequestDetailView requestdetail;
	private Date UserLog;
	private AbsolutePanel panel = new AbsolutePanel();
	private Label label = new Label("<Folder is empty>");
	private int newFinancialRequirement;
	private Presenter listener;
	
	@UiField(provided=true) CellTable<FinanceRequirements> cellTable = new 
			CellTable<FinanceRequirements>();
	@UiField(provided=true) SimplePager pager;
	@UiField ListBox amountlist;
	@UiField ListBox accountlist;
	@UiField ListBox currencylist;
	@UiField Hyperlink viewall;
	@UiField DockLayoutPanel docklayoutpanel;
	@UiField AbsolutePanel headerpanel;
	@UiField ScrollPanel scrollpanel;
	
	/*
	 * ---Constructor---
	 */
	public DesktopFinanceRequirementView() {
		// Do not refresh the headers and footers every time the data is updated.
	    cellTable.setAutoHeaderRefreshDisabled(true);
	    cellTable.setAutoFooterRefreshDisabled(true);

		// Create a Pager to control the table.
		SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
		pager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
		pager.setDisplay(cellTable);
		
		initWidget(binder.createAndBindUi(this));
	
		amountlist.addItem("General");
		amountlist.addItem("Real Amount");
		amountlist.addItem("Tax Amount");
		accountlist.addItem("Company's Funds");
		accountlist.addItem("Bank's Account");
		currencylist.addItem("USD");
		currencylist.addItem("VND");
		
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
		cellTable.setColumnWidth(checkColumn,"10px");
		
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
				if (object.getDescription().length()<=45)
					return object.getDescription();
				return object.getDescription().substring(0, 45)+" . . .";
			}
		};
		cellTable.addColumn(DescriptionColumn, "Description");
		cellTable.setColumnWidth(DescriptionColumn,"35%");
		
		final TextColumn<FinanceRequirements> Real_AmountColumn = new 
				TextColumn<FinanceRequirements>() {
		      @Override
		      public String getValue(FinanceRequirements object) {
		        return String.valueOf(object.getReal_amount());
		      }
		    };
	    cellTable.addColumn(Real_AmountColumn, "Real_Amount");
	    cellTable.setColumnWidth(Real_AmountColumn,"20%");
	    
	    final TextColumn<FinanceRequirements> Tax_AmountColumn = new 
				TextColumn<FinanceRequirements>() {
		      @Override
		      public String getValue(FinanceRequirements object) {
		        return String.valueOf(object.getTax_amount());
		      }
		    };
	    cellTable.addColumn(Tax_AmountColumn, "Tax_Amount");
	    cellTable.setColumnWidth(Tax_AmountColumn,"20%");
		    
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
						selected_fr = event.getValue();
//						if(selected_fr.getUpdate_time().equals(UserLog))
//							FinancialRequirementsObj.updateUserLog(selected_fr.getUpdate_time(), 
//									new AsyncCallback<Void> () {
//										public void onFailure(Throwable caught) {}
//										public void onSuccess(Void result) {
//											if(listener!=null)
//												listener.onUpdateUserLog();
//										}	
//							});
//						FinanceRequirements requestselected = event.getValue();
				        if (selected_fr != null) {	        	
				        	String token = String.valueOf(selected_fr.getRequest_id());
				        	listener.goTo(new FinanceRequirementPlace(token), selected_fr);	
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
		    
	    cellTable.setRowStyles(new RowStyles<FinanceRequirements>() {
			public String getStyleNames(FinanceRequirements row, int rowIndex) {
				if(row.getStatus().equals("APPROVED"))
					return "approvedRowStyle";
				if(row.getStatus().equals("DENIED"))
					return "deniedRowStyle";
				return null;
			}
		});
		
		// Create a data provider.
	    final ListDataProvider<FinanceRequirements> dataProvider = new 
			 ListDataProvider<FinanceRequirements>();
		dataProvider.addDataDisplay(cellTable);
 		list_fr = dataProvider.getList();	
// 		if(listener!=null)
//		 	listener.onLoading();
 		panel.setSize("100%", "60px");
		panel.add(label,500,10);
// 		getData();	
	}
	
	/*
	 * ---Procedure---
	 */
	/**
	 * refresh initial page.
	 */
	public void refresh() {
//		docklayoutpanel.clear();
//		docklayoutpanel.addNorth(headerpanel, 3);
//		docklayoutpanel.add(requestlist);
//		toolbar.setFinancialRequirementToolBar();
//		requestlist.refresh();
	}
	
	/**
	 * resize docklayoutpanel.
	 */
	public void setDocSize(String width, String height) {
		docklayoutpanel.setWidth(width);
		docklayoutpanel.setHeight(height);
	}
	
	public void setNotifyStyle(int newFinancialRequirement, Date updateUserLog){
//		UserLog = updateUserLog;
//		this.newFinancialRequirement = newFinancialRequirement;
//		 if(list_fr.isEmpty())
//			 getData();
//		 else {
//	  		 getNewData();
//		 }
	}
	
	public void redrawTable() {
		cellTable.setRowStyles(new RowStyles<FinanceRequirements>() {
			public String getStyleNames(FinanceRequirements row, int rowIndex) {
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
	
	/*
	 * ---Implement Function---
	 */
	@Override
	public void setNewData(List<FinanceRequirements> list_fr) {
		// TODO Auto-generated method stub
		 if(list_fr.isEmpty()) {
			 scrollpanel.remove(cellTable);
			 scrollpanel.add(panel);
		 } else {
			 for(int i=list_fr.size()-1;i>=0;i--)
				 this.list_fr.add(list_fr.get(i));
			 scrollpanel.remove(panel);
			 scrollpanel.add(cellTable);
		 }
	}
	
	@Override
	public void setPresenter(Presenter listener) {
		// TODO Auto-generated method stub
		this.listener = listener;
	}

	@Override
	public int getDataCount() {
		// TODO Auto-generated method stub
		return list_fr.size();
	}

	@Override
	public void setUpdateData(List<FinanceRequirements> list_fr) {
		// TODO Auto-generated method stub
		for(FinanceRequirements fr: list_fr) {
			 if(fr.getStatus().equals("PENDING"))
				 this.list_fr.add(0,fr);
			 if(fr.getStatus().equals("APPROVED")||
					 fr.getStatus().equals("DENIED")) {
				 Long id = fr.getRequest_id();
				 for(FinanceRequirements oldfr: this.list_fr)
					 if(oldfr.getRequest_id().equals(id))
						 this.list_fr.remove(oldfr);
				 this.list_fr.add(0,fr);
			 }
			 if(fr.getStatus().equals("DRAFT")||
					 fr.getStatus().equals("DELETED")) {
				 Long id = fr.getRequest_id();
				for(FinanceRequirements oldfr: this.list_fr)
					if(oldfr.getRequest_id().equals(id))
						this.list_fr.remove(oldfr);
			 }
		 }
		redrawTable();
	}

	@Override
	public FinanceRequirements getLastData() {
		// TODO Auto-generated method stub
		return list_fr.get(0);
	}

	@Override
	public void removeDataItem(FinanceRequirements fr) {
		// TODO Auto-generated method stub
		list_fr.remove(fr);
	}
}

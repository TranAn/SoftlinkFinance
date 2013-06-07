package com.softlink.finance.client.view.desktop;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

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
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.CellPreviewEvent;
import com.google.gwt.view.client.CellPreviewEvent.Handler;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionModel;
import com.softlink.datastore.model.FinanceData;
import com.softlink.finance.client.place.FinanceRequirementPlace;
import com.softlink.finance.client.view.FinanceRequirementView;

public class DesktopFinanceRequirementView extends Composite implements
		FinanceRequirementView {

	interface Binder extends UiBinder<Widget, DesktopFinanceRequirementView> {
	}

	private static final Binder binder = GWT.create(Binder.class);

	private List<FinanceData> list_fr = new ArrayList<FinanceData>();
	private Collection<FinanceData> notifyList = new ArrayList<FinanceData>();
	private List<FinanceData> list_selectedfr = new ArrayList<FinanceData>();
	private FinanceData selected_fr = null;
	private AbsolutePanel panel = new AbsolutePanel();
	private Label label = new Label("<Folder is empty>");
	private Presenter listener;

	@UiField(provided = true)
	CellTable<FinanceData> cellTable = new CellTable<FinanceData>();
	@UiField(provided = true)
	SimplePager pager;
	@UiField
	ListBox amountlist;
	@UiField
	ListBox accountlist;
	@UiField
	ListBox currencylist;
	@UiField
	Hyperlink viewall;
	@UiField
	DockLayoutPanel docklayoutpanel;
	@UiField
	AbsolutePanel headerpanel;
	@UiField
	ScrollPanel scrollpanel;

	/*
	 * ---Constructor---
	 */
	public DesktopFinanceRequirementView() {
		// Do not refresh the headers and footers every time the data is
		// updated.
		cellTable.setAutoHeaderRefreshDisabled(true);
		cellTable.setAutoFooterRefreshDisabled(true);

		// Create a Pager to control the table.
		SimplePager.Resources pagerResources = GWT
				.create(SimplePager.Resources.class);
		pager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0,
				true);
		pager.setDisplay(cellTable);

		initWidget(binder.createAndBindUi(this));

		amountlist.addItem("General");
		amountlist.addItem("Real Amount");
		amountlist.addItem("Tax Amount");
		accountlist.addItem("Company's Funds");
		accountlist.addItem("Bank's Account");
		currencylist.addItem("USD");
		currencylist.addItem("VND");

		final SelectionModel<FinanceData> selectionModel = new MultiSelectionModel<FinanceData>();
		cellTable.setSelectionModel(selectionModel,
				DefaultSelectionEventManager
						.<FinanceData> createCheckboxManager());

		ListHandler<FinanceData> columnSortHandler = new ListHandler<FinanceData>(
				list_fr);

		final Column<FinanceData, Boolean> checkColumn = new Column<FinanceData, Boolean>(
				new CheckboxCell(true, false)) {
			@Override
			public Boolean getValue(FinanceData object) {
				return selectionModel.isSelected(object);
			}
		};
		cellTable.addColumn(checkColumn,
				SafeHtmlUtils.fromSafeConstant("<br/>"));
		cellTable.setColumnWidth(checkColumn, "10px");

		final TextColumn<FinanceData> ReporterColumn = new TextColumn<FinanceData>() {
			@Override
			public String getValue(FinanceData object) {
				return object.getReporter();
			}
		};
		cellTable.addColumn(ReporterColumn, "Reporter");
		cellTable.setColumnWidth(ReporterColumn, "15%");

		final TextColumn<FinanceData> DescriptionColumn = new TextColumn<FinanceData>() {
			@Override
			public String getValue(FinanceData object) {
				if (object.getDescription().length() <= 45)
					return object.getDescription();
				return object.getDescription().substring(0, 45) + " . . .";
			}
		};
		cellTable.addColumn(DescriptionColumn, "Description");
		cellTable.setColumnWidth(DescriptionColumn, "35%");

		final TextColumn<FinanceData> Real_AmountColumn = new TextColumn<FinanceData>() {
			@Override
			public String getValue(FinanceData object) {
				return String.valueOf(object.getReal_amount());
			}
		};
		cellTable.addColumn(Real_AmountColumn, "Real_Amount");
		cellTable.setColumnWidth(Real_AmountColumn, "20%");

		final TextColumn<FinanceData> Tax_AmountColumn = new TextColumn<FinanceData>() {
			@Override
			public String getValue(FinanceData object) {
				return String.valueOf(object.getBill_amount());
			}
		};
		cellTable.addColumn(Tax_AmountColumn, "Bill_Amount");
		cellTable.setColumnWidth(Tax_AmountColumn, "20%");

		final TextColumn<FinanceData> StatusColumn = new TextColumn<FinanceData>() {
			@Override
			public String getValue(FinanceData object) {
				return object.getStatus();
			}
		};
		cellTable.addColumn(StatusColumn, "Status");
		cellTable.setColumnWidth(StatusColumn, "10%");

		cellTable.addCellPreviewHandler(new Handler<FinanceData>() {
			public void onCellPreview(CellPreviewEvent<FinanceData> event) {
				if (BrowserEvents.CLICK
						.equals(event.getNativeEvent().getType())) {
					if (event.getColumn() != 0) {
						selected_fr = event.getValue();
						if (selected_fr != null) {
							String token = String.valueOf(selected_fr
									.getRequest_id());
							listener.goTo(new FinanceRequirementPlace(token));
						}
					} else {
						if (checkColumn.getValue(event.getValue()) == true)
							list_selectedfr.add(event.getValue());
						else
							list_selectedfr.remove(event.getValue());
					}
				}
			}
		});

		cellTable.setRowStyles(new RowStyles<FinanceData>() {
			public String getStyleNames(FinanceData row, int rowIndex) {
				if (row.getStatus().equals("APPROVED")
						&& notifyList.contains(row))
					return "approvedBoldRowStyle";
				if (notifyList.contains(row)
						&& row.getStatus().equals("DENIED"))
					return "deniedBoldRowStyle";
				if (notifyList.contains(row))
					return "normal1BoldRowStyle";
				if (row.getStatus().equals("APPROVED"))
					return "approvedRowStyle";
				if (row.getStatus().equals("DENIED"))
					return "deniedRowStyle";
				return null;
			}
		});

		// Create a data provider.
		final ListDataProvider<FinanceData> dataProvider = new ListDataProvider<FinanceData>();
		dataProvider.addDataDisplay(cellTable);
		list_fr = dataProvider.getList();
		panel.setSize("100%", "60px");
		panel.add(label, 500, 10);
	}

	/*
	 * ---Procedure---
	 */
	public void redrawTable() {
		cellTable.redraw();
	}

	/*
	 * ---Implement View Interface---
	 */
	@Override
	public void setNewData(List<FinanceData> list_fr) {
		// TODO Auto-generated method stub
		if (list_fr.isEmpty()) {
			scrollpanel.remove(cellTable);
			scrollpanel.add(panel);
		} else {
			for (int i = list_fr.size() - 1; i >= 0; i--)
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
	public void setUpdateData(Collection<FinanceData> updateDataList, 
			Map<String,Boolean> notifyList, String currentUser) {
		// TODO Auto-generated method stub
		this.notifyList.clear();
		
		if (list_fr.isEmpty()) {
			scrollpanel.remove(panel);
			scrollpanel.add(cellTable);
		}
		
		for (FinanceData data : updateDataList) {
			boolean isexist = false;
			Long id = data.getRequest_id();
			FinanceData notifydata = null;
			if (data.getStatus().equals("PENDING")
					|| data.getStatus().equals("APPROVED")
					|| data.getStatus().equals("DENIED")) {
				for (FinanceData olddata : list_fr)
					if (olddata.getRequest_id().equals(id)) {
						isexist = true;
						if(data.getVersion() != olddata.getVersion()) {
							list_fr.remove(olddata);
							list_fr.add(0, data);
							notifydata = data;
						} else {
							notifydata = olddata;
						}
					}
				if(!isexist) {
					list_fr.add(0, data);
					notifydata = data;
				}
				String notifyid = String.valueOf(notifydata.getRequest_id());
				if(notifyList.containsKey(String.valueOf(notifyid))) {
					if(notifyList.get(String.valueOf(notifyid)))
						this.notifyList.add(notifydata);
					else
						listener.flushData(notifydata);
				}
			}
			if (data.getStatus().equals("DRAFT")) {
				for (FinanceData oldfr : list_fr)
					if (oldfr.getRequest_id().equals(id))
						list_fr.remove(oldfr);
				if(!data.getReporter().equals(currentUser))
					listener.flushData(data);
			}
			if(data.getStatus().equals("DELETED")) {
				for (FinanceData oldfr : list_fr)
					if (oldfr.getRequest_id().equals(id))
						list_fr.remove(oldfr);
				listener.flushData(data);
			}
		}
		
		redrawTable();
		listener.onRefreshComplete();
	}

	@Override
	public FinanceData getLastData() {
		// TODO Auto-generated method stub
		return list_fr.get(0);
	}

	@Override
	public void removeDataItem(FinanceData fr) {
		// TODO Auto-generated method stub
		list_fr.remove(fr);
	}

	@Override
	public void addDataItem(FinanceData newdata) {
		// TODO Auto-generated method stub
		list_fr.add(0, newdata);
	}

	@Override
	public void onUpdate() {
		// TODO Auto-generated method stub
		listener.onUpdate();
	}
}

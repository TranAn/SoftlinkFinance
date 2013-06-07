package com.softlink.financeuser.client.view;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.google.gwt.cell.client.ActionCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.softlink.financeuser.client.request.FinanceUserRequest;
import com.softlink.financeuser.client.request.FinanceUserRequestAsync;
import com.softlink.datastore.model.FinanceUser;

public class UserList extends Composite {

	interface Binder extends UiBinder<Widget, UserList> { }
	private static final Binder binder = GWT.create(Binder.class);
	private final FinanceUserRequestAsync SeriUserObj = GWT
			  .create(FinanceUserRequest.class);
	@UiField(provided=true) CellTable<FinanceUser> cellTable = 
			new CellTable<FinanceUser>();
	@UiField Button button;
	private List<FinanceUser> list_user = new ArrayList<FinanceUser>();

	public UserList() {
		 // Do not refresh the headers and footers every time the data is updated.
	    cellTable.setAutoHeaderRefreshDisabled(true);
	    cellTable.setAutoFooterRefreshDisabled(true);
	    
	    ListHandler<FinanceUser> columnSortHandler = 
	    		new ListHandler<FinanceUser>(list_user);
	    cellTable.addColumnSortHandler(columnSortHandler);
	    
	    final TextColumn<FinanceUser> UserNameColumn = new 
				TextColumn<FinanceUser>() {
			@Override
		    public String getValue(FinanceUser object) {
				return object.getUsername();
		    }
		};
		UserNameColumn.setSortable(true);
		columnSortHandler.setComparator(UserNameColumn, new Comparator<FinanceUser>() {
		      @Override
		      public int compare(FinanceUser o1, FinanceUser o2) {		    	  
		    	  return o1.getUsername().compareTo(o2.getUsername());	    	  
		      }
		});
		cellTable.addColumn(UserNameColumn, "User");
		cellTable.setColumnWidth(UserNameColumn,"35%");
		
		
		final TextColumn<FinanceUser> RoleColumn = new 
				TextColumn<FinanceUser>() {
			@Override
		    public String getValue(FinanceUser object) {
				return object.getRole();
		    }
		};
		RoleColumn.setSortable(true);
		columnSortHandler.setComparator(RoleColumn, new Comparator<FinanceUser>() {
		      @Override
		      public int compare(FinanceUser o1, FinanceUser o2) {		    	  
		    	  return o1.getRole().compareTo(o2.getRole());	    	  
		      }
		});
		cellTable.addColumn(RoleColumn, "Role");
		cellTable.setColumnWidth(RoleColumn,"15%");
		
		final TextColumn<FinanceUser> TimeWorkColumn = new 
				TextColumn<FinanceUser>() {
			@Override
		    public String getValue(FinanceUser object) {
				return String.valueOf(object.getTimework());
		    }
		};
		cellTable.addColumn(TimeWorkColumn, "User Log");
		cellTable.setColumnWidth(TimeWorkColumn,"50%");
		
		@SuppressWarnings("rawtypes")
		ActionCell action = new ActionCell<FinanceUser>("Kick", new ActionCell.Delegate<FinanceUser>(){
			@Override
			public void execute(final FinanceUser object) {
				SeriUserObj.delete(object, new AsyncCallback<Void>(){
					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Action failure, the connection may be interupt!");
					}
					@Override
					public void onSuccess(Void result) {
						Window.alert("Say goodbye to "+object.getUsername()+"!");
						list_user.remove(object);
					}
				});
			}
		});
		@SuppressWarnings("unchecked")
		Column<FinanceUser,FinanceUser> ActionColumn = new Column<FinanceUser,FinanceUser>(action) {
			@Override
			public FinanceUser getValue(FinanceUser object) {
				return object;
			}
		};
		cellTable.addColumn(ActionColumn);
	    
	    // Create a data provider.
	    final ListDataProvider<FinanceUser> dataProvider = new 
			 ListDataProvider<FinanceUser>();
	    
	    dataProvider.addDataDisplay(cellTable);
 		list_user = dataProvider.getList();
 		
 		initWidget(binder.createAndBindUi(this));
 		getData();
	}
	
	//RPC call-----------------------------------------------------
	private void getData(){
		 SeriUserObj.list_user(new AsyncCallback<List<FinanceUser>>(){
			@Override
			public void onFailure(Throwable caught) {
				cellTable.setTitle("Load Failure, the connection may be interupt!");
			}
			@Override
			public void onSuccess(List<FinanceUser> result) {
				if(result.isEmpty())
					cellTable.setTitle("No Data Has Been Found!");
				for(FinanceUser user: result)
					list_user.add(user);
			}
		 });
	}

	@UiHandler("button")
	void onButtonClick(ClickEvent event) {
		list_user.removeAll(list_user);
		cellTable.setTitle("");
		getData();
	}
}

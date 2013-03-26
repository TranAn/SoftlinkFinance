package com.softlink.finance.widget;


import java.util.Date;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.Widget;
import com.softlink.finance.datastore.FinancialRequirementsObj;
import com.softlink.finance.datastore.FinancialRequirementsObjAsync;
import com.softlink.finance.services.UserServices;
import com.softlink.finance.services.UserServicesAsync;
import com.softlink.financedatastore.client.FinancialRequirements;

public class FinancialRequirement extends ResizeComposite {
	
	interface Binder extends UiBinder<Widget, FinancialRequirement> { }
	private static final Binder binder = GWT.create(Binder.class);
	private final static FinancialRequirementsObjAsync FinancialRequirementsObj = 
			GWT.create(FinancialRequirementsObj.class);
	private final static UserServicesAsync userservices = 
			  GWT.create(UserServices.class);
	private ToolBarPanel toolbar = new ToolBarPanel();
	private RequestList requestlist = new RequestList();
	private RequestDetail requestdetail =  new RequestDetail();
	private Listener listener;
	
	public interface Listener {
		void onUpdateUserLog();
	}
	public void setListener(Listener listener) {
		this.listener = listener;
	}
	
	@UiField ListBox amountlist;
	@UiField ListBox accountlist;
	@UiField ListBox currencylist;
	@UiField Hyperlink viewall;
	@UiField DockLayoutPanel docklayoutpanel;
	@UiField AbsolutePanel headerpanel;
	public FinancialRequirement(){}
	public FinancialRequirement(final ToolBarPanel toolbar) {
		initWidget(binder.createAndBindUi(this));
		this.toolbar = toolbar;
		
		amountlist.addItem("General");
		amountlist.addItem("Real Amount");
		amountlist.addItem("Tax Amount");
		accountlist.addItem("Company's Funds");
		accountlist.addItem("Bank's Account");
		currencylist.addItem("USD");
		currencylist.addItem("VND");
		docklayoutpanel.add(requestlist);
		
		toolbar.setFinancialRequirement_Listener(new ToolBarPanel.FinancialRequirement_Listener() {
			public void onBackButtonClick() {
				toolbar.setFinancialRequirementToolBar();
				docklayoutpanel.clear();
				docklayoutpanel.addNorth(headerpanel, 3);
				docklayoutpanel.add(requestlist);	
			}
			public void onRefreshButtonClick() {
				requestlist.refreshData();
				if(requestlist.getSelectedfr()!=null)
					FinancialRequirementsObj.find(requestlist.getSelectedfr().getRequest_id(),
							new AsyncCallback<List<FinancialRequirements>>() {
						@Override
						public void onFailure(Throwable caught) {
							toolbar.setTextNotice("Update Failure, the connection has been interupt!");
							toolbar.setVisibleNotice();
						}
						@Override
						public void onSuccess(List<FinancialRequirements> result) {
							requestdetail.setItem(result.get(0));
						}
					});
			}
			@Override
			public void onDeleteButtonClick() {
				final Date date = new Date(System.currentTimeMillis());
				final FinancialRequirements selectedfr = requestlist.getSelectedfr();
				if(Window.confirm("Do you want to delete this request?"))
					FinancialRequirementsObj.approveRequest(selectedfr, "DELETED", date, "", 
							new AsyncCallback<Void>() {
						@Override
						public void onFailure(Throwable caught) {
							toolbar.setTextNotice("Update Failure, the connection has been interupt!");
							toolbar.setVisibleNotice();
						}
						@Override
						public void onSuccess(Void result) {
							requestlist.RemoveItem(selectedfr);
							toolbar.setTextNotice("Request has been deleted!");
							toolbar.setVisibleNotice();
							toolbar.setFinancialRequirementToolBar();
							docklayoutpanel.clear();
							docklayoutpanel.addNorth(headerpanel, 3);
							docklayoutpanel.add(requestlist);
						}
					});	
			}
		});
		
		requestlist.setListener(new RequestList.Listener() {
			public void onTableSelected(FinancialRequirements requestselected) {
				docklayoutpanel.clear();
				requestdetail.refresh();
				docklayoutpanel.add(requestdetail);
				requestdetail.setItem(requestselected);
				toolbar.setHideNotice();
				if(requestselected.getStatus().equals("DENIED")==false)
					toolbar.setRequestDetailToolBar1();
				else
					toolbar.setRequestDetailToolBar2();
			}
			public void onLoadDataFail() {
				toolbar.setVisibleNotice();
				toolbar.setTextNotice("Load failure, the connetion has been interupt!");
			}
			public void onLoadComplete() {
				toolbar.setHideNotice();
			}
			public void onDataNotFound() {
				toolbar.setVisibleNotice();
				toolbar.setTextNotice("No data has been found!");
			}
			public void onLoading() {
				toolbar.setVisibleNotice();
				toolbar.setTextNotice("Loading...");
			}	
			public void onRefresh() {
				toolbar.setVisibleNotice();
				toolbar.setTextNotice("Refresh...");
			}
			public void onDelete() {
				toolbar.setVisibleNotice();
				toolbar.setTextNotice("All denied request selected has been delete!");	
			}
			public void onUpdateUserLog() {
				if(listener!=null)
					listener.onUpdateUserLog();
			}
		});
		
		requestdetail.setListener(new RequestDetail.Listener() {
			@Override
			public void onApproveClick(final FinancialRequirements item, final String comment) {
				final Date date = new Date(System.currentTimeMillis());
				if(comment.equals("")==false){
					userservices.getUserName(new AsyncCallback<String>() {
						public void onFailure(Throwable caught) {}
						public void onSuccess(String result) {
							String cm = result.split("@")[0]+":"+comment+"\r\n \r\n";
							FinancialRequirementsObj.approveRequest(item, "APPROVED", date, cm, 
									new AsyncCallback<Void>() {
								@Override
								public void onFailure(Throwable caught) {
									toolbar.setTextNotice("Update Failure, the connection has been interupt!");
									toolbar.setVisibleNotice();
								}
								@Override
								public void onSuccess(Void result) {						
									toolbar.setTextNotice("Request has been Approved!");
									toolbar.setVisibleNotice();
								}
							});
						}
					});	
				} else {
					FinancialRequirementsObj.approveRequest(item, "APPROVED", date, comment, 
							new AsyncCallback<Void>() {
						@Override
						public void onFailure(Throwable caught) {
							toolbar.setTextNotice("Update Failure, the connection has been interupt!");
							toolbar.setVisibleNotice();
						}
						@Override
						public void onSuccess(Void result) {
							toolbar.setTextNotice("Request has been Approved!");
							toolbar.setVisibleNotice();
						}
					});
				}
			}

			@Override
			public void onDenyClick(final FinancialRequirements item, final String comment) {
				final Date date = new Date(System.currentTimeMillis());
				if(comment.equals("")==false){
					userservices.getUserName(new AsyncCallback<String>() {
						public void onFailure(Throwable caught) {}
						public void onSuccess(String result) {
							String cm = result.split("@")[0]+":"+comment+"\r\n \r\n";
							FinancialRequirementsObj.approveRequest(item, "DENIED", date, cm, 
									new AsyncCallback<Void>() {
								@Override
								public void onFailure(Throwable caught) {
									toolbar.setTextNotice("Update Failure, the connection has been interupt!");
									toolbar.setVisibleNotice();
								}
								@Override
								public void onSuccess(Void result) {
									toolbar.setTextNotice("Request has been Denied!");
									toolbar.setVisibleNotice();
								}
							});
						}
					});	
				} else {
					FinancialRequirementsObj.approveRequest(item, "DENIED", date, comment, 
							new AsyncCallback<Void>() {
						@Override
						public void onFailure(Throwable caught) {
							toolbar.setTextNotice("Update Failure, the connection has been interupt!");
							toolbar.setVisibleNotice();
						}
						@Override
						public void onSuccess(Void result) {
							toolbar.setTextNotice("Request has been Denied!");
							toolbar.setVisibleNotice();
						}
					});
				}
			}

			@Override
			public void onMoreInfoClick(final FinancialRequirements item, final String comment) {
				final Date date = new Date(System.currentTimeMillis());
				if(comment.equals("")==false){
					userservices.getUserName(new AsyncCallback<String>() {
						public void onFailure(Throwable caught) {}
						public void onSuccess(String result) {
							String cm = result.split("@")[0]+":"+comment+"\r\n \r\n";
							FinancialRequirementsObj.approveRequest(item, "DRAFT", date, cm, 
									new AsyncCallback<Void>() {
								@Override
								public void onFailure(Throwable caught) {
									toolbar.setTextNotice("Update Failure, the connection has been interupt!");
									toolbar.setVisibleNotice();
								}
								@Override
								public void onSuccess(Void result) {
									requestlist.RemoveItem(item);
									toolbar.setTextNotice("Request has been sent back to the requester!");
									toolbar.setVisibleNotice();
									toolbar.setFinancialRequirementToolBar();
									docklayoutpanel.clear();
									docklayoutpanel.addNorth(headerpanel, 3);
									docklayoutpanel.add(requestlist);
								}
							});
						}
					});	
				} else {
					FinancialRequirementsObj.approveRequest(item, "DRAFT", date, comment, 
							new AsyncCallback<Void>() {
						@Override
						public void onFailure(Throwable caught) {
							toolbar.setTextNotice("Update Failure, the connection has been interupt!");
							toolbar.setVisibleNotice();
						}
						@Override
						public void onSuccess(Void result) {
							requestlist.RemoveItem(item);
							toolbar.setTextNotice("Request has been sent back to the requester!");
							toolbar.setVisibleNotice();
							toolbar.setFinancialRequirementToolBar();
							docklayoutpanel.clear();
							docklayoutpanel.addNorth(headerpanel, 3);
							docklayoutpanel.add(requestlist);
						}
					});
				}
			}

			@Override
			public void onUpdateUserLog() {
				if(listener!=null)
					listener.onUpdateUserLog();
			}
		});
		
	}
	
	//Procedre-----------------------------------------
	/**
	 * set table style when have new notify.
	 */
	public void setNotifyStyle(final int newFinancialRequirement, Date updateUserLog) {
		requestlist.setNotifyStyle(newFinancialRequirement, updateUserLog);
	}
	/**
	 * refresh initial page.
	 */
	public void refresh() {
		docklayoutpanel.clear();
		docklayoutpanel.addNorth(headerpanel, 3);
		docklayoutpanel.add(requestlist);
		toolbar.setFinancialRequirementToolBar();
		requestlist.refresh();
	}
	/**
	 * warm up request will stop.
	 */
	public void stop() {
		requestlist.stop();
	}
	/**
	 * warm up request will start.
	 */
	public void start() {
		requestlist.start();
	}
	/**
	 * resize docklayoutpanel.
	 */
	public void setDocSize(String width, String height) {
		docklayoutpanel.setWidth(width);
		docklayoutpanel.setHeight(height);
	}
}

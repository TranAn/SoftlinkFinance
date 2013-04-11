package com.softlink.finance.client;

import java.util.Date;
import java.util.List;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;

import com.softlink.finance.datastore.FinanceRequirementsObj;
import com.softlink.finance.datastore.FinanceRequirementsObjAsync;
import com.softlink.finance.datastore.SeriUserObjAsync;
import com.softlink.finance.datastore.SeriUserObj;
import com.softlink.finance.services.UserServices;
import com.softlink.finance.services.UserServicesAsync;
import com.softlink.finance.widget.Drafts;
import com.softlink.finance.widget.FinanceRequirement;
import com.softlink.finance.widget.ServiceUnAvailable;
import com.softlink.finance.widget.TabPanel;
import com.softlink.finance.widget.ToolBarPanel;
import com.softlink.finance.widget.TopPanel;
import com.softlink.finance.widget.Trash;
import com.softlink.financedatastore.client.FinanceRequirements;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Finance implements EntryPoint {
	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	/*private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network "
			+ "connection and try again.";*/
	
	private final static UserServicesAsync userservices = 
			  GWT.create(UserServices.class);
	private final static FinanceRequirementsObjAsync FinancialRequirementsObj = 
			GWT.create(FinanceRequirementsObj.class);
	private final static SeriUserObjAsync SeriUserObj =
			GWT.create(SeriUserObj.class);
	private Double tabPanelSize;
	private DockPanel dockPanel = new DockPanel();
	private TopPanel topPanel = new TopPanel();
	private ToolBarPanel toolbarpanel = new ToolBarPanel();
	private TabPanel tabPanel = new TabPanel();
	private ServiceUnAvailable serviceUnAvaiable = new ServiceUnAvailable();
	private FinanceRequirement financialRequirement = new FinanceRequirement(toolbarpanel);
	private Drafts drafts = new Drafts(toolbarpanel);
	private Trash trash = new Trash(toolbarpanel);
	
	//Procedure--------------------------------------------------------
	private void checkNotify() {
		FinancialRequirementsObj.list_newRequestPerUser(
				new AsyncCallback<List<FinanceRequirements>>() {
			@Override
			public void onFailure(Throwable caught) {}
			@Override
			public void onSuccess(List<FinanceRequirements> result) {
				if(result.isEmpty()) {
					tabPanel.setNotifyStyle(0, 0);
					drafts.setNotifyStyle(0,new Date());
					financialRequirement.setNotifyStyle(0, new Date());
				} else {
					int newFinancialRequirement=0;
					int newDraft=0;
					for(FinanceRequirements fr: result) {
						if(fr.getStatus().equals("PENDING")||
								fr.getStatus().equals("APPROVED")||
								fr.getStatus().equals("DENIED"))
							newFinancialRequirement++;
						if(fr.getStatus().equals("DRAFT"))
							newDraft++;
					}
					tabPanel.setNotifyStyle(newFinancialRequirement, newDraft);
					financialRequirement.setNotifyStyle(newFinancialRequirement, result.get(0).getUpdate_time());
					drafts.setNotifyStyle(newDraft, result.get(0).getUpdate_time());
				}
			}	
		});
	}
	
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		
		checkNotify();
		
		RootPanel rootPanel = RootPanel.get();
		rootPanel.setSize("100%", "100%");
		
		rootPanel.add(dockPanel, 10, 0);
		dockPanel.setSize("99%", "100%");
		
		topPanel.setHeight("70px");
	
		toolbarpanel.setHeight("65px");
		
		final SplitLayoutPanel splitLayoutPanel = new SplitLayoutPanel() {
		    @Override
		    public void onResize() {
		        super.onResize();
		        if(tabPanel.getOffsetWidth()<=tabPanelSize) {
		        	financialRequirement.setDocSize("99.85%","478px");
		        	drafts.setDocSize("99.85%","478px");
		        	trash.setDocSize("99.85%","478px");
		        } else {
		        	financialRequirement.setDocSize("1124px","460px");
		        	drafts.setDocSize("1124px","460px");
		        	trash.setDocSize("1124px","460px");
		        }
		    }           
		};
		splitLayoutPanel.setHeight("480px");
		
		final AbsolutePanel absolutePanel = new AbsolutePanel();
		
		final Label lblNewLabel = new Label();
		lblNewLabel.setStyleName("sendButton");
		absolutePanel.add(lblNewLabel, 277, 245);
		
		userservices.isUserLoggedIn(new AsyncCallback<Boolean>() {
			public void onFailure(Throwable caught) {}
			public void onSuccess(Boolean result) {
				if(result==true){
					lblNewLabel.setText("Welcome To Finance, view About to see what new?");
					topPanel.setLogoutState();
					dockPanel.add(topPanel, DockPanel.NORTH);
					dockPanel.add(toolbarpanel,DockPanel.NORTH);
					dockPanel.add(splitLayoutPanel, DockPanel.CENTER);
				}
				if(result==false){
					lblNewLabel.setText("Welcome Page, please sign in to your google account and allow it to access this site.");
					topPanel.setLoginState();
					dockPanel.add(topPanel, DockPanel.NORTH);
					dockPanel.add(absolutePanel, DockPanel.CENTER);
				}
			}
	    });
		
		splitLayoutPanel.addWest(tabPanel, 218);
		splitLayoutPanel.setWidgetMinSize(tabPanel, 218);
		
		tabPanelSize = splitLayoutPanel.getWidgetSize(tabPanel);
		
		final DockLayoutPanel dockLayoutPanel = new DockLayoutPanel(Unit.EM);
		splitLayoutPanel.add(dockLayoutPanel);
		
		dockLayoutPanel.add(absolutePanel);
		
		tabPanel.setListener(new TabPanel.Listener() {
			@Override
			public void onFinancialRequirementSelected() {
				financialRequirement.refresh();
			}

			@Override
			public void onDraftsSelected() {
				drafts.refresh();
			}

			@Override
			public void onTrashSelected() {
				trash.refresh();
			}

			@Override
			public void onFutureSelected() {
				// TODO Auto-generated method stub
				
			}	
		});
		
		financialRequirement.setListener(new FinanceRequirement.Listener() {
			@Override
			public void onUpdateUserLog() {
				checkNotify();
			}
		});
		
		drafts.setListener(new Drafts.Listener() {
			@Override
			public void onUpdateUserLog() {
				 checkNotify();
			}
		});
		
		// Add history listener
		History.addValueChangeHandler(new ValueChangeHandler<String>() {
		      public void onValueChange(ValueChangeEvent<String> event) {
		        String historyToken = event.getValue();

		        // Parse the history token
		        try {
		          if (historyToken.equals("Statistics")) {
		        	  dockLayoutPanel.clear();
		        	  dockLayoutPanel.add(serviceUnAvaiable);
		        	  toolbarpanel.setDefault();
		        	  financialRequirement.stop();
		        	  drafts.stop();
		        	  trash.stop();
		        	  tabPanel.openStack(0);
		        	  tabPanel.setStatisticStyle();
		          } if (historyToken.equals("Static Income")) {
		        	  dockLayoutPanel.clear();
		        	  dockLayoutPanel.add(serviceUnAvaiable);
		        	  toolbarpanel.setDefault();
		        	  financialRequirement.stop();
		        	  drafts.stop();
		        	  trash.stop();    	 
		        	  tabPanel.openStack(1);
		        	  tabPanel.setStaticIncomeStyle();
		          } if (historyToken.equals("Arising Income")) {
		        	  dockLayoutPanel.clear();
		        	  dockLayoutPanel.add(serviceUnAvaiable);
		        	  toolbarpanel.setDefault();
		        	  financialRequirement.stop();
		        	  drafts.stop();
		        	  trash.stop();
		        	  tabPanel.openStack(1);
		        	  tabPanel.setArisingIncomeStyle();
		          } if (historyToken.equals("Static Expenses")) {
		        	  dockLayoutPanel.clear();
		        	  dockLayoutPanel.add(serviceUnAvaiable);
		        	  toolbarpanel.setDefault();
		        	  financialRequirement.stop();
		        	  drafts.stop();
		        	  trash.stop();
		        	  tabPanel.openStack(2);
		        	  tabPanel.setStaticExpensesStyle();
		          } if (historyToken.equals("Financial Requirement")) {
		        	  dockLayoutPanel.clear();
		        	  dockLayoutPanel.add(financialRequirement);
		        	  toolbarpanel.setFinancialRequirementToolBar();
		        	  financialRequirement.start();
		        	  drafts.stop();
		        	  trash.stop();
		        	  tabPanel.openStack(2);
		        	  tabPanel.setFinancialRequirementStyle();	
		          } if (historyToken.equals("Future")) {
		        	  dockLayoutPanel.clear();
		        	  dockLayoutPanel.add(serviceUnAvaiable);
		        	  toolbarpanel.setDefault();
		        	  financialRequirement.stop();
		        	  drafts.stop();
		        	  trash.stop();	        	 
		        	  tabPanel.openStack(2);
		        	  tabPanel.setFutureStyle();	 
		          } if (historyToken.equals("Drafts")) {
		        	  dockLayoutPanel.clear();
		        	  dockLayoutPanel.add(drafts);
		        	  toolbarpanel.setDraftsToolBar();
		        	  financialRequirement.stop();
		        	  drafts.start();
		        	  trash.stop();       	 
		        	  tabPanel.openStack(2);
		        	  tabPanel.setDraftStyle();	
		          } if (historyToken.equals("Trash")) {
		        	  dockLayoutPanel.clear();
		        	  dockLayoutPanel.add(trash);
		        	  toolbarpanel.setTrashToolBar();
		        	  financialRequirement.stop();
		        	  drafts.stop();
		        	  trash.start();
		        	  tabPanel.openStack(2);
		        	  tabPanel.setTrashStyle();
		          } if (historyToken.equals("")) {
		        	  dockLayoutPanel.clear();
		        	  dockLayoutPanel.add(absolutePanel);
		        	  absolutePanel.add(lblNewLabel, 277, 245);
		        	  toolbarpanel.setDefault();
		        	  financialRequirement.stop();
		        	  drafts.stop();
		        	  trash.stop();
		        	  tabPanel.openStack(0);
		        	  tabPanel.RemoveStyle();
		          }
		        } catch (IndexOutOfBoundsException e) {
		        	dockLayoutPanel.clear();
		        	dockLayoutPanel.add(absolutePanel);
		        	absolutePanel.add(lblNewLabel, 277, 245);
		        	toolbarpanel.setDefault();
		        	financialRequirement.stop();
		        	drafts.stop();
		        	trash.stop();
		        }
		      }
		});
		
		// Now that we've setup our listener, fire the initial history state.
	    History.fireCurrentHistoryState();
	    
	    Timer elapsedTimer = new Timer () {
			 public void run() {
				 SeriUserObj.loadUserFromMemcache(new AsyncCallback<Boolean>() {
					public void onFailure(Throwable caught) {}
					public void onSuccess(Boolean result) {
						if(result){
							SeriUserObj.onLoadComplete(new AsyncCallback<Void>(){
								public void onFailure(Throwable caught) {}
								public void onSuccess(Void result) {
									checkNotify();
								}
								
							});
						}
						
					}
				 });
		  	 }
		 };
		 elapsedTimer.scheduleRepeating(20000);
	}
}

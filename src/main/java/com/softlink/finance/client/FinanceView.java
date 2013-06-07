package com.softlink.finance.client;

import com.google.gwt.user.client.ui.DeckLayoutPanel;
import com.google.gwt.user.client.ui.IsWidget;

/**
 * MainView
 *
 */
public interface FinanceView extends IsWidget{
	DeckLayoutPanel getContainerView();
	void setDefaultPlace();
}

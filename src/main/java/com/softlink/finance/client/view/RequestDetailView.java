package com.softlink.finance.client.view;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.IsWidget;
import com.softlink.finance.shared.FinanceRequirements;

public interface RequestDetailView extends IsWidget{
	void setData(FinanceRequirements fr);
	void setUserRole(boolean isAdmin);
	FinanceRequirements getData();
	void setPresenter(Presenter listener);
	
	public interface Presenter {
		void goTo(Place place);
		void onApprovedRequest(String comment);
		void onDeniedRequest(String comment);
		void onSendBackRequest(String comment);
	}
}

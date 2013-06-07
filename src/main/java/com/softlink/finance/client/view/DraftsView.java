package com.softlink.finance.client.view;

import java.util.List;

import com.google.gwt.user.client.ui.IsWidget;
import com.softlink.datastore.model.FinanceData;

public interface DraftsView extends IsWidget{
	void setNewData(List<FinanceData> list_fr);
	void setUpdateData(List<FinanceData> list_fr);
	void removeDataItem(FinanceData fr);
	
	FinanceData getLastData();
	int getDataCount();
	
	void setPresenter(Presenter listener);

	public interface Presenter
	{
		
	}
}

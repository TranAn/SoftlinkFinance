package com.softlink.finance.client.view;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.IsWidget;
import com.softlink.datastore.model.FinanceData;

public interface FinanceRequirementView extends IsWidget {
	void setNewData(List<FinanceData> list_fr);
	void setUpdateData(Collection<FinanceData> updateDataList,
			Map<String, Boolean> notifyList, String currentUser);
	void addDataItem(FinanceData newdata);
	void removeDataItem(FinanceData fr);
	void onUpdate();

	FinanceData getLastData();
	int getDataCount();

	void setPresenter(Presenter listener);

	public interface Presenter {
		void goTo(Place place);
		void onRefreshComplete();
		void flushData(FinanceData data);
		void onUpdate();
	}
}

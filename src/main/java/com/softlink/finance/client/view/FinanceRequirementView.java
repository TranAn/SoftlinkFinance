package com.softlink.finance.client.view;

import java.util.List;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.IsWidget;
import com.softlink.finance.shared.FinanceRequirements;

public interface FinanceRequirementView extends IsWidget{
	/**
	 * Set new data for View
	 * @param list_fr list of {@link com.softlink.finance.shared.FinanceRequirements
	 * FinanceRequirements}
	 */
	void setNewData(List<FinanceRequirements> list_fr);
	
	/**
	 * Set update data for View
	 * @param list_fr list of {@link com.softlink.finance.shared.FinanceRequirements
	 * FinanceRequirements}
	 */
	void setUpdateData(List<FinanceRequirements> list_fr);
	
	/**
	 * Get the last update data in list of {@link com.softlink.finance.shared.FinanceRequirements
	 * FinanceRequirements}
	 * @return {@link com.softlink.finance.shared.FinanceRequirements
	 * FinanceRequirements} last element in list
	 */
	FinanceRequirements getLastData();
	
	/**
	 * Remove a request from list
	 * @param fr request been remove
	 */
	void removeDataItem(FinanceRequirements fr);
	
	/**
	 * Get size of list_fr 
	 * @return size of list_fr
	 */
	int getDataCount();
	
	/**
	 * Set listener from Presenter
	 * @param listener
	 */
	void setPresenter(Presenter listener);

	public interface Presenter
	{
		void goTo(Place place, FinanceRequirements selected_fr);
	}
}

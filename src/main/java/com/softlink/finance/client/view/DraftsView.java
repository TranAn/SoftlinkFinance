package com.softlink.finance.client.view;

import java.util.List;

import com.google.gwt.user.client.ui.IsWidget;
import com.softlink.finance.shared.FinanceRequirements;

public interface DraftsView extends IsWidget{
	/**
	 * Set data for View
	 * @param list_fr list of {@link com.softlink.finance.shared.FinanceRequirements
	 * FinanceRequirements}
	 */
	void setListDraftFinanceRequirement(List<FinanceRequirements> list_fr);
	
	/**
	 * Set selected data for View
	 * @param selected_fr type {@link com.softlink.finance.shared.FinanceRequirements
	 * FinanceRequirements}
	 */
	void setSelectedFinanceRequirement(FinanceRequirements selected_fr);
	
	/**
	 * Set listener from Presenter
	 * @param listener
	 */
	void setPresenter(Presenter listener);

	public interface Presenter
	{
		
	}
}

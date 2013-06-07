package com.softlink.finance.client.view;

import com.google.gwt.user.client.ui.IsWidget;

public interface UnderConstructionView extends IsWidget{
	void startCountDown();
	void setPresenter(Presenter listener);

	public interface Presenter
	{
		void goToPreviousPlace();
	}
}

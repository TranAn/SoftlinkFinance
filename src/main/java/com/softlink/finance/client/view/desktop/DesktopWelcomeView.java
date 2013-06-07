package com.softlink.finance.client.view.desktop;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class DesktopWelcomeView extends Composite {

	private static DesktopWelcomeViewUiBinder uiBinder = GWT
			.create(DesktopWelcomeViewUiBinder.class);

	interface DesktopWelcomeViewUiBinder extends
			UiBinder<Widget, DesktopWelcomeView> {
	}

	public DesktopWelcomeView() {
		initWidget(uiBinder.createAndBindUi(this));
	}

}

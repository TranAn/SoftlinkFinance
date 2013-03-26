package com.softlink.finance.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.Widget;

public class ServiceUnAvailable extends ResizeComposite {

	interface Binder extends UiBinder<Widget, ServiceUnAvailable> { }
	private static final Binder binder = GWT.create(Binder.class);
	
	public ServiceUnAvailable() {
		initWidget(binder.createAndBindUi(this));
	}


}

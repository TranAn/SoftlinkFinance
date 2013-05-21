package com.softlink.finance.client.view.desktop;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.softlink.finance.client.view.UnderConstructionView;

public class DesktopUnderConstructionView extends Composite 
	implements UnderConstructionView{

	interface Binder extends UiBinder<Widget, DesktopUnderConstructionView> { }
	private static final Binder binder = GWT.create(Binder.class);
	
	public DesktopUnderConstructionView() {
		initWidget(binder.createAndBindUi(this));
	}


}

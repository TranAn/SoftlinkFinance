package com.softlink.finance.client.view.desktop;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusListener;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class DesktopSearchTextBoxView extends Composite{

	interface DesktopSearchTextBoxViewUiBinder extends
			UiBinder<Widget, DesktopSearchTextBoxView> {
	}

	public interface Listener {
		void hideSearchTextBox();
	}

	private static DesktopSearchTextBoxViewUiBinder uiBinder = GWT
			.create(DesktopSearchTextBoxViewUiBinder.class);
	

	private Listener listener;
	
	@UiField TextBox searchtxb;
	@UiField Image searchbtn;
	
	public void setListener(Listener listener) {
		this.listener = listener;
	}
	
	public void setFocus(){
		searchtxb.setText("");
		Timer timer = new Timer() {
		      public void run() {
		    	  searchtxb.setFocus(true);
		      }
		};
		timer.schedule(700);
	}

	@SuppressWarnings("deprecation")
	public DesktopSearchTextBoxView() {
		initWidget(uiBinder.createAndBindUi(this));
		searchtxb.getElement().setPropertyString("placeholder", "Search for staff, finance or works");
		
		searchtxb.addFocusListener(new FocusListener() {
			@Override
			@Deprecated
			public
			void onLostFocus(Widget sender) {
				// TODO Auto-generated method stub
				listener.hideSearchTextBox();
			}
			@Override
			@Deprecated
			public
			void onFocus(Widget sender) {}
		});
	}

}
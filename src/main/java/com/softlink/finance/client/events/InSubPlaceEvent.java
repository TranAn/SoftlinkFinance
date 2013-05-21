package com.softlink.finance.client.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * Fire by RequestDetailActivity ContainerView in 
 * this place. It support Main View to show exactly animate direction.
 */
public class InSubPlaceEvent extends GwtEvent<InSubPlaceEvent.Handler>{

	public interface Handler extends EventHandler {
		void inSubPlace(InSubPlaceEvent event);
	}
	
	public static Type<InSubPlaceEvent.Handler> TYPE = new Type<InSubPlaceEvent.Handler>();
	
	@Override
	public Type<InSubPlaceEvent.Handler> getAssociatedType() {
		return TYPE;
	}
	
	@Override
	protected void dispatch(InSubPlaceEvent.Handler handler) {
		handler.inSubPlace(this);
	}
	
}



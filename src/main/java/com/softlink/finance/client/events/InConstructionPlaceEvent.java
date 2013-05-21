package com.softlink.finance.client.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * Fire by ConstructionActivity when go to ConstructonPlace
 */
public class InConstructionPlaceEvent extends GwtEvent<InConstructionPlaceEvent.Handler>{

	public interface Handler extends EventHandler {
		void inConstructionPlace(InConstructionPlaceEvent event);
	}
	
	public static Type<InConstructionPlaceEvent.Handler> TYPE = new Type<InConstructionPlaceEvent.Handler>();
	
	@Override
	public Type<InConstructionPlaceEvent.Handler> getAssociatedType() {
		return TYPE;
	}
	
	@Override
	protected void dispatch(InConstructionPlaceEvent.Handler handler) {
		handler.inConstructionPlace(this);
	}
	
}

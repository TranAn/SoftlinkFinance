package com.softlink.finance.client.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * Fire by TabPanelView or TrashActivity when go to TrashPlace
 */
public class InTrashPlaceEvent extends GwtEvent<InTrashPlaceEvent.Handler>{

	public interface Handler extends EventHandler {
		void inTrashPlace(InTrashPlaceEvent event);
	}
	
	public static Type<InTrashPlaceEvent.Handler> TYPE = new Type<InTrashPlaceEvent.Handler>();
	
	@Override
	public Type<InTrashPlaceEvent.Handler> getAssociatedType() {
		return TYPE;
	}
	
	@Override
	protected void dispatch(InTrashPlaceEvent.Handler handler) {
		handler.inTrashPlace(this);
	}
	
}

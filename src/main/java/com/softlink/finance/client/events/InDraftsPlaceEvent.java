package com.softlink.finance.client.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * Fire by TabPanelView or DraftsActivity when go to DraftsPlace
 */
public class InDraftsPlaceEvent extends GwtEvent<InDraftsPlaceEvent.Handler>{

	public interface Handler extends EventHandler {
		void inDraftsPlace(InDraftsPlaceEvent event);
	}
	
	public static Type<InDraftsPlaceEvent.Handler> TYPE = new Type<InDraftsPlaceEvent.Handler>();
	
	@Override
	public Type<InDraftsPlaceEvent.Handler> getAssociatedType() {
		return TYPE;
	}
	
	@Override
	protected void dispatch(InDraftsPlaceEvent.Handler handler) {
		handler.inDraftsPlace(this);
	}
	
}
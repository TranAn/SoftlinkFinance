package com.softlink.finance.client.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * Fire by CoreView when loading update data
 */
public class RefreshingEvent extends GwtEvent<RefreshingEvent.Handler>{

	public interface Handler extends EventHandler {
		void onRefreshing(RefreshingEvent event);
	}
	
	public static Type<RefreshingEvent.Handler> TYPE = new Type<RefreshingEvent.Handler>();
	
	@Override
	public Type<RefreshingEvent.Handler> getAssociatedType() {
		return TYPE;
	}
	
	@Override
	protected void dispatch(RefreshingEvent.Handler handler) {
		handler.onRefreshing(this);
	}

}

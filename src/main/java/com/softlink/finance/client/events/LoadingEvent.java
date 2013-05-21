package com.softlink.finance.client.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * Fire by CoreView when loading new data.
 */
public class LoadingEvent extends GwtEvent<LoadingEvent.Handler>{

	public interface Handler extends EventHandler {
		void onLoading(LoadingEvent event);
	}
	
	public static Type<LoadingEvent.Handler> TYPE = new Type<LoadingEvent.Handler>();
	
	@Override
	public Type<LoadingEvent.Handler> getAssociatedType() {
		return TYPE;
	}
	
	@Override
	protected void dispatch(LoadingEvent.Handler handler) {
		handler.onLoading(this);
	}

}

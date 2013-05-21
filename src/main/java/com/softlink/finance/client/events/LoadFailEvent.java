package com.softlink.finance.client.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * Fire by CoreView when load data failure.
 */
public class LoadFailEvent extends GwtEvent<LoadFailEvent.Handler>{

	public interface Handler extends EventHandler {
		void onLoadFail(LoadFailEvent event);
	}
	
	public static Type<LoadFailEvent.Handler> TYPE = new Type<LoadFailEvent.Handler>();
	
	@Override
	public Type<LoadFailEvent.Handler> getAssociatedType() {
		return TYPE;
	}
	
	@Override
	protected void dispatch(LoadFailEvent.Handler handler) {
		handler.onLoadFail(this);
	}
	
}

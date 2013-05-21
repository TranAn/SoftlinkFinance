package com.softlink.finance.client.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * Fire by CoreView when load data successful.
 */
public class LoadSuccessEvent extends GwtEvent<LoadSuccessEvent.Handler>{

	public interface Handler extends EventHandler {
		void onLoadSuccess(LoadSuccessEvent event);
	}
	
	public static Type<LoadSuccessEvent.Handler> TYPE = new Type<LoadSuccessEvent.Handler>();
	
	@Override
	public Type<LoadSuccessEvent.Handler> getAssociatedType() {
		return TYPE;
	}
	
	@Override
	protected void dispatch(LoadSuccessEvent.Handler handler) {
		handler.onLoadSuccess(this);
	}
	
}
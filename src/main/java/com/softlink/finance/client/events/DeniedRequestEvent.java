package com.softlink.finance.client.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * Fire by RequestDetailView when deny a request.
 */
public class DeniedRequestEvent extends GwtEvent<DeniedRequestEvent.Handler>{

	public interface Handler extends EventHandler {
		void onDeniedRequest(DeniedRequestEvent event);
	}
	
	public static Type<DeniedRequestEvent.Handler> TYPE = new Type<DeniedRequestEvent.Handler>();
	
	@Override
	public Type<DeniedRequestEvent.Handler> getAssociatedType() {
		return TYPE;
	}
	
	@Override
	protected void dispatch(DeniedRequestEvent.Handler handler) {
		handler.onDeniedRequest(this);
	}
	
}
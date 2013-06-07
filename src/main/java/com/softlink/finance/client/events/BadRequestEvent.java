package com.softlink.finance.client.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * Fire by RequestDetailView when request id not exist.
 */
public class BadRequestEvent extends GwtEvent<BadRequestEvent.Handler>{

	public interface Handler extends EventHandler {
		void onBadRequest(BadRequestEvent event);
	}
	
	public static Type<BadRequestEvent.Handler> TYPE = new Type<BadRequestEvent.Handler>();
	
	@Override
	public Type<BadRequestEvent.Handler> getAssociatedType() {
		return TYPE;
	}
	
	@Override
	protected void dispatch(BadRequestEvent.Handler handler) {
		handler.onBadRequest(this);
	}
	
}

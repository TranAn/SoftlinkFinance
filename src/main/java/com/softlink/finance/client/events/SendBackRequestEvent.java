package com.softlink.finance.client.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * Fire by RequestDetailView when sent back a request.
 */
public class SendBackRequestEvent extends GwtEvent<SendBackRequestEvent.Handler>{
	
	public interface Handler extends EventHandler {
		void onSendBackRequest(SendBackRequestEvent event);
	}
	
	public static Type<SendBackRequestEvent.Handler> TYPE = new Type<SendBackRequestEvent.Handler>();
	
	@Override
	public Type<SendBackRequestEvent.Handler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(SendBackRequestEvent.Handler handler) {
		handler.onSendBackRequest(this);
	}
	
}
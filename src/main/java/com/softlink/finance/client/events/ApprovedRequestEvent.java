package com.softlink.finance.client.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * Fire by RequestDetailView when approve a request.
 */
public class ApprovedRequestEvent extends GwtEvent<ApprovedRequestEvent.Handler>{

	public interface Handler extends EventHandler {
		void onApprovedRequest(ApprovedRequestEvent event);
	}
	
	public static Type<ApprovedRequestEvent.Handler> TYPE = new Type<ApprovedRequestEvent.Handler>();
	
	@Override
	public Type<ApprovedRequestEvent.Handler> getAssociatedType() {
		return TYPE;
	}
	
	@Override
	protected void dispatch(ApprovedRequestEvent.Handler handler) {
		handler.onApprovedRequest(this);
	}
	
}

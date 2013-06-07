package com.softlink.finance.client.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * Fire by RequestDialogView when create request failure.
 */
public class CreateRequestFailEvent extends GwtEvent<CreateRequestFailEvent.Handler>{

	public interface Handler extends EventHandler {
		void onCreateRequestFail(CreateRequestFailEvent event);
	}
	
	public static Type<CreateRequestFailEvent.Handler> TYPE = new Type<CreateRequestFailEvent.Handler>();
	
	@Override
	public Type<CreateRequestFailEvent.Handler> getAssociatedType() {
		return TYPE;
	}
	
	@Override
	protected void dispatch(CreateRequestFailEvent.Handler handler) {
		handler.onCreateRequestFail(this);
	}
	
}

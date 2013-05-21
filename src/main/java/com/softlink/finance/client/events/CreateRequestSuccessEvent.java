package com.softlink.finance.client.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * Fire by RequestDialogView when create request successful.
 */
public class CreateRequestSuccessEvent extends GwtEvent<CreateRequestSuccessEvent.Handler>{

	public interface Handler extends EventHandler {
		void onCreateRequestSuccess(CreateRequestSuccessEvent event);
	}
	
	public static Type<CreateRequestSuccessEvent.Handler> TYPE = new Type<CreateRequestSuccessEvent.Handler>();
	
	@Override
	public Type<CreateRequestSuccessEvent.Handler> getAssociatedType() {
		return TYPE;
	}
	
	@Override
	protected void dispatch(CreateRequestSuccessEvent.Handler handler) {
		handler.onCreateRequestSuccess(this);
	}
	
}

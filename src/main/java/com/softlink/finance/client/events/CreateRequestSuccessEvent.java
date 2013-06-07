package com.softlink.finance.client.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.softlink.datastore.model.FinanceData;

/**
 * Fire by RequestDialogView when create request successful.
 */
public class CreateRequestSuccessEvent extends GwtEvent<CreateRequestSuccessEvent.Handler>{

	public interface Handler extends EventHandler {
		void onCreateRequestSuccess(CreateRequestSuccessEvent event);
	}
	
	private FinanceData newdata;
	
	public static Type<CreateRequestSuccessEvent.Handler> TYPE = new Type<CreateRequestSuccessEvent.Handler>();
	
	@Override
	public Type<CreateRequestSuccessEvent.Handler> getAssociatedType() {
		return TYPE;
	}
	
	public CreateRequestSuccessEvent(FinanceData newdata) {
		this.newdata = newdata;
	}

	public FinanceData getNewdata() {
		return newdata;
	}

	@Override
	protected void dispatch(CreateRequestSuccessEvent.Handler handler) {
		handler.onCreateRequestSuccess(this);
	}
	
}

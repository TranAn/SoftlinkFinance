package com.softlink.finance.client.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.softlink.finance.shared.FinanceRequirements;

/**
 * Fire by RequestDetailView when sent back a request.
 */
public class SendBackRequestEvent extends GwtEvent<SendBackRequestEvent.Handler>{

	private FinanceRequirements sendback_rq;
	
	public interface Handler extends EventHandler {
		void onSendBackRequest(SendBackRequestEvent event);
	}
	
	public static Type<SendBackRequestEvent.Handler> TYPE = new Type<SendBackRequestEvent.Handler>();
	
	@Override
	public Type<SendBackRequestEvent.Handler> getAssociatedType() {
		return TYPE;
	}
	
	public SendBackRequestEvent(FinanceRequirements sendback_rq) {
		this.sendback_rq = sendback_rq;
	}
	
	public FinanceRequirements getSendBackRequest() {
		return sendback_rq;
	}

	@Override
	protected void dispatch(SendBackRequestEvent.Handler handler) {
		handler.onSendBackRequest(this);
	}
	
}
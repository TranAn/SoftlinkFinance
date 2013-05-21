package com.softlink.finance.client.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * Fire by RequestDetailActivity when go to RequestDetailView.
 */
public class InRequestDetailViewEvent extends GwtEvent<InRequestDetailViewEvent.Handler>{

	public interface Handler extends EventHandler {
		void inRequestDetailView(InRequestDetailViewEvent event);
	}
	
	public static Type<InRequestDetailViewEvent.Handler> TYPE = new Type<InRequestDetailViewEvent.Handler>();
	
	@Override
	public Type<InRequestDetailViewEvent.Handler> getAssociatedType() {
		return TYPE;
	}
	
	@Override
	protected void dispatch(InRequestDetailViewEvent.Handler handler) {
		handler.inRequestDetailView(this);
	}
	
}

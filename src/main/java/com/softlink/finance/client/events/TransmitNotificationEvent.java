package com.softlink.finance.client.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * Fire by any layout when data has been update and need notify to the other
 */
public class TransmitNotificationEvent extends GwtEvent<TransmitNotificationEvent.Handler>{

	public interface Handler extends EventHandler {
		void onTransmitNotification(TransmitNotificationEvent event);
	}
	
	public static Type<TransmitNotificationEvent.Handler> TYPE = new Type<TransmitNotificationEvent.Handler>();
	
	@Override
	public Type<TransmitNotificationEvent.Handler> getAssociatedType() {
		return TYPE;
	}
	
	@Override
	protected void dispatch(TransmitNotificationEvent.Handler handler) {
		handler.onTransmitNotification(this);
	}

}


package com.softlink.finance.client.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * Fire by DraftActivity, FinanceRequirementActivity, TrashActivity when ContainerView in 
 * those place. It support Main View to show exactly animate direction.
 */
public class InParentPlaceEvent extends GwtEvent<InParentPlaceEvent.Handler>{

	public interface Handler extends EventHandler {
		void inParentPlace(InParentPlaceEvent event);
	}
	
	public static Type<InParentPlaceEvent.Handler> TYPE = new Type<InParentPlaceEvent.Handler>();
	
	@Override
	public Type<InParentPlaceEvent.Handler> getAssociatedType() {
		return TYPE;
	}
	
	@Override
	protected void dispatch(InParentPlaceEvent.Handler handler) {
		handler.inParentPlace(this);
	}
	
}


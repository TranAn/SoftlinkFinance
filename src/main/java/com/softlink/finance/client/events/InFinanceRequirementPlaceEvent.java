package com.softlink.finance.client.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * Fire by TabPanelView or FinanceRequirementActivity when go to FinanceRequirementPlace
 */
public class InFinanceRequirementPlaceEvent extends GwtEvent<InFinanceRequirementPlaceEvent.Handler>{

	public interface Handler extends EventHandler {
		void inFinanceRequirementPlace(InFinanceRequirementPlaceEvent event);
	}
	
	public static Type<InFinanceRequirementPlaceEvent.Handler> TYPE = new Type<InFinanceRequirementPlaceEvent.Handler>();
	
	@Override
	public Type<InFinanceRequirementPlaceEvent.Handler> getAssociatedType() {
		return TYPE;
	}
	
	@Override
	protected void dispatch(InFinanceRequirementPlaceEvent.Handler handler) {
		handler.inFinanceRequirementPlace(this);
	}
	
}

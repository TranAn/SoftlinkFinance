package com.softlink.finance.client.mvp;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;
import com.softlink.finance.client.ClientFactory;
import com.softlink.finance.client.activity.ConstructionActivity;
import com.softlink.finance.client.activity.DraftsActivity;
import com.softlink.finance.client.activity.FinanceRequirementActivity;
import com.softlink.finance.client.activity.RequestDetailActivity;
import com.softlink.finance.client.activity.TrashActivity;
import com.softlink.finance.client.place.ConstructionPlace;
import com.softlink.finance.client.place.DraftsPlace;
import com.softlink.finance.client.place.FinanceRequirementPlace;
import com.softlink.finance.client.place.TrashPlace;

public class AppActivityMapper implements ActivityMapper {

	private ClientFactory clientFactory;

	/**
	 * AppActivityMapper associates each Place with its corresponding
	 * {@link Activity}
	 * 
	 * @param clientFactory
	 *            Factory to be passed to activities
	 */
	public AppActivityMapper(ClientFactory clientFactory) {
		super();
		this.clientFactory = clientFactory;
	}

	/**
	 * Map each Place to its corresponding Activity. This would be a great use
	 * for GIN.
	 */
	@Override
	public Activity getActivity(Place place) {
		// This is begging for GIN
		if (place instanceof FinanceRequirementPlace) {
			FinanceRequirementPlace currentPlace = (FinanceRequirementPlace) place;
			if(currentPlace.getActivityToken()==null)
				return new FinanceRequirementActivity(currentPlace, clientFactory);
			else
				return new RequestDetailActivity(currentPlace, clientFactory);
		}
		
		else if (place instanceof DraftsPlace)
			return new DraftsActivity((DraftsPlace) place, clientFactory);
		
		else if(place instanceof TrashPlace) {
			TrashPlace currentPlace = (TrashPlace) place;
			if(currentPlace.getActivityToken()==null)
				return new TrashActivity(currentPlace, clientFactory);
			else
				return new RequestDetailActivity(currentPlace, clientFactory);
		}
		
		else if(place instanceof ConstructionPlace) 
			return new ConstructionActivity((ConstructionPlace) place, clientFactory);
		
		return null;
	}

}

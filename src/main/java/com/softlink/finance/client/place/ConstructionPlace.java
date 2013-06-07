package com.softlink.finance.client.place;

import com.google.gwt.place.shared.Place;

public class ConstructionPlace extends Place{
	
	private Place previousPlace;
	
	public ConstructionPlace() {}
	
	public ConstructionPlace(Place previousPlace) {
		this.previousPlace = previousPlace;
	}
	
	public Place getPreviousPlace() {
		return previousPlace;
	}

}

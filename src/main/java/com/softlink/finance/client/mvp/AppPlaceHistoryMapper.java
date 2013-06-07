package com.softlink.finance.client.mvp;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.softlink.finance.client.place.ConstructionPlace;
import com.softlink.finance.client.place.DraftsPlace;
import com.softlink.finance.client.place.FinanceRequirementPlace;
import com.softlink.finance.client.place.TrashPlace;

/**
 * PlaceHistoryMapper interface is used to attach all places which the
 * PlaceHistoryHandler should be aware of.
 */
public class AppPlaceHistoryMapper implements PlaceHistoryMapper {

	String delimiter = "/";
	
	/**
	 * In fact it check URL on browser and set corresponding place in App 
	 */
	@Override
	public Place getPlace(String token) {
		 if(token == null||token.equals(""))
			 return null;
		 
		 if(!token.contains(delimiter))
			 token = token + delimiter;
		 
		 String[] tokens = token.split(delimiter, 2); 
		 
         if (tokens[0].equals("drafts")) {
        	 if(tokens[1].length()!=0)
        		 return new DraftsPlace(tokens[1]);
        	 return new DraftsPlace(null);
         }
         else if (tokens[0].equals("finance_reqs")) {
        	 if(tokens[1].length()!=0)
        		 return new FinanceRequirementPlace(tokens[1]);
        	 return new FinanceRequirementPlace(null);
         }
         else if (tokens[0].equals("trash")) {
        	 if(tokens[1].length()!=0)
        		 return new TrashPlace(tokens[1]);
        	 return new TrashPlace(null);
         }
         else if (tokens[0].equals("construction")||
        		 tokens[0].equals("statistics")||
        		 tokens[0].equals("static_finance")||
        		 tokens[0].equals("future")) {
        	 return new ConstructionPlace();
         } else {
        	 return new FinanceRequirementPlace(null);
         }
	}

	/**
	 * Contrary, it checks where you are and set corresponding URL on browser
	 */
	@Override
	public String getToken(Place place) {
		if (place instanceof DraftsPlace) {
			if(((DraftsPlace) place).getActivityToken()==null)
				return "drafts";
			return "drafts" + delimiter+ ((DraftsPlace) place).getActivityToken();
		}  
		else if (place instanceof FinanceRequirementPlace) {
			if(((FinanceRequirementPlace) place).getActivityToken()==null)
				return "finance_reqs";
            return "finance_reqs" + delimiter+ ((FinanceRequirementPlace) place).getActivityToken();
		}
		else if (place instanceof TrashPlace) {
			if(((TrashPlace) place).getActivityToken()==null)
				return "trash";
			return "trash" + delimiter+ ((TrashPlace) place).getActivityToken();
		}
		else if (place instanceof ConstructionPlace)
			return "construction";
		
		return null;
	}
}

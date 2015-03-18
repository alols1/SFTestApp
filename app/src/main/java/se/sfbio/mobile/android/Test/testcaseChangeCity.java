package se.sfbio.mobile.android.Test;

import se.sfbio.mobile.android.Test.SFBioTestcase;

public class testcaseChangeCity extends SFBioTestcase {
	
	public testcaseChangeCity() throws ClassNotFoundException {
		super();
	}
			
	//Methods
	public void testChangeCity() {
    	waitForApplicationToStart();
    	//For every city in the cities list, push the "Byt Stad" menu button and select the city to test.
   	    //Then go to the "Nu på bio" tab and check that the city name is the correct
   	   // for (int i = 0; i < cities.size(); i++){
    	for (int i = 0; i < 10; i++){
    		changeCityRandom();
//   	    	SFBioTestData.City city = cities.get(i);
//   	    	String cityName = city.getName();
//	   	    changeCity(city);
	   	    loadView(android.widget.ImageView.class, 6, DEFAULT_NUMBER_OF_TRIES_WAITING_FOR_VIEW, "Took too long to load NU på bio screen.");
	   	    searchAndReport(searchTextCaseInsensitive("Nu på bio"), "Found the text Nu på bio", "Could not find the text Nu på bio");
	   	    clickOnTextCaseInsensitive("Nu på bio");
	   	    loadView(android.widget.ImageView.class, 6, DEFAULT_NUMBER_OF_TRIES_WAITING_FOR_VIEW, "Took too long to load NU på bio screen.");
	   	    searchAndReport(searchTextCaseInsensitive("FILMER I " + city.getName()), city.getName() + " was successfully loaded.", "Not able to confirm that" + city.getName() + " was loaded.");
   	    }
   	    reportSuccess();
    }
}
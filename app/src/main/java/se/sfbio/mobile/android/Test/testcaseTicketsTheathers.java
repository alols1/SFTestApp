package se.sfbio.mobile.android.Test;

import android.view.View;

import se.sfbio.mobile.android.Test.SFBioTestcase;

public class testcaseTicketsTheathers extends SFBioTestcase {
	
	public testcaseTicketsTheathers() throws ClassNotFoundException {
		super();
	}
	
	//Methods
	public void testTicketsTheathers() {
    	waitForApplicationToStart();
    	changeCityRandom();
    	goToTicketsTab();
       	View biografButton = solo.getView("se.sfbio.mobile.android:id/tickets_tab_text", 1);
       	solo.clickOnView(biografButton);
       	checkItemsInListFromArray(city.getTheatreList());
       	reportSuccess();
    }
}
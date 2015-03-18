package se.sfbio.mobile.android.Test;

import se.sfbio.mobile.android.Test.SFBioTestcase;

public class testcaseTopMenu extends SFBioTestcase {
	
	public testcaseTopMenu() throws ClassNotFoundException {
		super();
	}
			
	//Methods
    public void testTopMenu() {
    	waitForApplicationToStart();
    	//For every top menu item, click on it and verify that the application is in the correct tab
    	for (int i = 0; i < 3; i++){
    		goToTicketsTab();
    		goToGPSTab();
    		goToNyheterTab();
    		goToKampanjerTab();
    		goToStatTab();
    	}
	
    	reportSuccess();
	
    } 
	
}
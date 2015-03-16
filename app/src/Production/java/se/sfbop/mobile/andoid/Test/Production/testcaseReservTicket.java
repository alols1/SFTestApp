package se.sfbop.mobile.andoid.Test.Production;

import se.sfbio.mobile.android.Test.TicketTestcase;

public class testcaseReservTicket extends TicketTestcase {
	
	public testcaseReservTicket() throws ClassNotFoundException {
		super();
	}
	
	//Methods
	public void testReservTicket() {
    	waitForApplicationToStart();
    	changeCityRandom();
    	for (int i = 0; i < 4; i++){
	    	goToTicketsTab();
	    	getCinemaAndTicketFromTicketsTab(2);
	    	if(confirmSeatWhenReserving(2)){break;}
	    	while(!searchTextCaseInsensitive("Nu på bio") && !searchTextCaseInsensitive("Biografer")){
	    		solo.goBack();
	    	}
    	}
    	firstReservTicketScreenOperations();
    	confirmReservationAndCheckItIsDone();
		reportSuccess();
    }
			
	
}
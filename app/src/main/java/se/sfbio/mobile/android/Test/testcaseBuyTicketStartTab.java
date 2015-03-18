package se.sfbio.mobile.android.Test;

import se.sfbio.mobile.android.Test.*;

public class testcaseBuyTicketStartTab extends se.sfbio.mobile.android.Test.TicketTestcase {

    public testcaseBuyTicketStartTab() throws ClassNotFoundException {
        super();
    }
    //	protected void setUp() throws Exception {
//		this.cities_to_test = new String[]{"Gävle", "Göteborg", "Helsingborg", "Jönköping", "Karlstad", "Linköping", "Luleå", "Lund", "Malmö", "Norrköping", "Stockholm", "Sundsvall", "Umeå", "Uppsala", "Västerås", "Växjö", "Örebro"};
//		super.setUp();
//	}
    public void testBuyTicketStartTab() {
        waitForApplicationToStart();
        changeCityRandom();
        assertTrue("Planned Assert.", false);
        goToTicketsTab();
        goToUnderTabBiografer();
        getCinemaAndTicketFromBiograferUnderTab(8);


//    	goToStatTab();
//    	goToUnderTabNuPABio();
//    	getCinemaAndTicketFromStartTab(2);
        confirmSeatWhenBuying(8);
        fillInEmailatBuyTicketScreen();
//    	fillInPresentkort();
        clearBioKlubbsKort();
        //Maximum amount of bioklubbskort is 4, the second argument.
        fillInBioklubbsKort(8, 4);
        confirmInfoInFirstBuyScreen();
//		fillCreditCardInfo("4925000000000004", "666");
//		doesItemExist("se.sfbio.mobile.android:id/red_button");
//		confirmTicketBuy();
        reportSuccess();
    }


}

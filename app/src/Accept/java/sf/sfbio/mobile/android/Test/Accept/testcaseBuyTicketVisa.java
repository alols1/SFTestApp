package sf.sfbio.mobile.android.Test.Accept;

import se.sfbio.mobile.android.Test.*;

public class testcaseBuyTicketVisa extends se.sfbio.mobile.android.Test.TicketTestcase {

    public testcaseBuyTicketVisa() throws ClassNotFoundException {
        super();
    }


    public void testBuyTicketVisa() {
        waitForApplicationToStart();
        changeCityRandom();
        reportError("Planned error.");
        goToTicketsTab();
        getCinemaAndTicketFromTicketsTab(2);
        confirmSeatWhenBuying(2);
        fillInEmailatBuyTicketScreen();
        clearBioKlubbsKort();
        confirmInfoInFirstBuyScreen();
        boolean fail = true;
        while (fail) {
            fillCreditCardInfo("4925000000000004", "666");
            doesItemExist("se.sfbio.mobile.android:id/red_button");
            if (confirmTicketBuy()) {
                fail = false;
            }
        }
        reportSuccess();
    }

}
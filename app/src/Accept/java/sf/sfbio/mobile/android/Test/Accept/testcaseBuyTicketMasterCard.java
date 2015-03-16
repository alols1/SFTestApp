package sf.sfbio.mobile.android.Test.Accept;

import android.annotation.SuppressLint;

import se.sfbio.mobile.android.Test.*;

public class testcaseBuyTicketMasterCard extends se.sfbio.mobile.android.Test.TicketTestcase {

    public testcaseBuyTicketMasterCard() throws ClassNotFoundException {
        super();
    }

    //Methods
    @SuppressLint("DefaultLocale")
    public void testBuyTicketMaterCard() {
        waitForApplicationToStart();
        changeCityRandom();
        reportError("Planned error.");
        goToTicketsTab();
        goToUnderTabBiografer();
        getCinemaAndTicketFromBiograferUnderTab(2);
        confirmSeatWhenBuying(2);
        fillInEmailatBuyTicketScreen();
        clearBioKlubbsKort();
        confirmInfoInFirstBuyScreen();
        boolean fail = true;
        while (fail) {
            fillCreditCardInfo("5413000000000000", "666");
            doesItemExist("se.sfbio.mobile.android:id/red_button");
            if (confirmTicketBuy()) {
                fail = false;
            }
        }
        reportSuccess();
    }
}

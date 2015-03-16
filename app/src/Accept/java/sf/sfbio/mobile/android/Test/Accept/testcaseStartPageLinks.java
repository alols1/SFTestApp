package sf.sfbio.mobile.android.Test.Accept;

import se.sfbio.mobile.android.Test.SFBioTestcase;

public class testcaseStartPageLinks extends SFBioTestcase {

    public testcaseStartPageLinks() throws ClassNotFoundException {
        super();
    }

    //Methods
    public void testStartPageLinks() {
        waitForApplicationToStart();
        changeCityRandom();
        goToStatTab();
        checkItemsInListFromArrayStartPageLinks(city.getToplist());
        reportSuccess();
    }

}

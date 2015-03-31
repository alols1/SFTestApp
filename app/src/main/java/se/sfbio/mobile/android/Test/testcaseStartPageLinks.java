package se.sfbio.mobile.android.Test;

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

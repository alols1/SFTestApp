package se.sfbio.mobile.android.Test;

import se.sfbio.mobile.android.Test.SFBioTestcase;

public class testcaseNewsFlow extends SFBioTestcase {

    public testcaseNewsFlow() throws ClassNotFoundException {
        super();
    }

    //Methods
    public void testNewsFlow() {
        waitForApplicationToStart();
        goToNyheterTab();
        solo.scrollToTop();
        checkItemsInListFromArray(Nyheter);
        reportSuccess();
        reportSuccess();
    }

}
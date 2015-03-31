package se.sfbio.mobile.android.Test;

import se.sfbio.mobile.android.Test.SFBioTestcase;

public class testcaseToBeRanFirst extends SFBioTestcase {

    public testcaseToBeRanFirst() throws ClassNotFoundException {
        super();
    }

    //Methods
    public void testNewsFlow() {
        logging("Will take away first time installed pop-ups.");
        solo.sleep(60000);
        if (solo.searchText("Välj stad", 0, false)){
            solo.clickOnText("Stockholm");
            logging("Välj stad pop-up screen handled");
        }




    }

}

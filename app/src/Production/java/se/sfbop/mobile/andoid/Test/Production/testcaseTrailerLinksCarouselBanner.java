package se.sfbop.mobile.andoid.Test.Production;

import com.robotium.solo.Solo;
import android.view.View;

import se.sfbio.mobile.android.Test.SFBioTestcase;

public class testcaseTrailerLinksCarouselBanner extends SFBioTestcase {
	
	public testcaseTrailerLinksCarouselBanner() throws ClassNotFoundException {
		super();
	}
	
	//Methods
	 public void testTrailerLinksCarouselBanner() {
	     	waitForApplicationToStart();
	     	changeCityRandom();
	    	goToStatTab();
//	    	doesItemExist("se.sfbio.mobile.android:id/carousel");
	    	View bannerGoAround;
	     	bannerGoAround = solo.getView("se.sfbio.mobile.android:id/carousel");
	     	for(int i = 0; i < 6; i++){
	     		solo.scrollViewToSide(bannerGoAround, Solo.RIGHT);
	     		solo.clickOnView(solo.getView("se.sfbio.mobile.android:id/carousel_play_btn", 1));
	     		loadView(android.widget.VideoView.class, 1, DEFAULT_NUMBER_OF_TRIES_WAITING_FOR_VIEW, "There was no movie loaded in time.");
	     		solo.sleep(5000);
	   	    	solo.goBack();
	     	}
	     	reportSuccess();
	}
	 				
}
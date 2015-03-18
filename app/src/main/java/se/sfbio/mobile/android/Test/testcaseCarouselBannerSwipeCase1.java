package se.sfbio.mobile.android.Test;

import com.robotium.solo.Solo;
import android.view.View;

import se.sfbio.mobile.android.Test.SFBioTestcase;


public class testcaseCarouselBannerSwipeCase1 extends SFBioTestcase {
	
	public testcaseCarouselBannerSwipeCase1() throws ClassNotFoundException {
		super();
	}
	
	//Methods
	public void testCarouselBannerSwipeCase1() {
    	waitForApplicationToStart();
    	changeCityRandom();
    	goToStatTab();
//    	doesItemExist("se.sfbio.mobile.android:id/carousel");
    	View bannerGoAround;
    	bannerGoAround = solo.getView("se.sfbio.mobile.android:id/carousel");
    	for(int i = 0; i < bannerTitles.size(); i++){
    		checkCarouselItem(i);
    		solo.scrollViewToSide(bannerGoAround, Solo.RIGHT);
    	}
    	checkCarouselItem(0);
    	reportSuccess();
    }
			
	
}
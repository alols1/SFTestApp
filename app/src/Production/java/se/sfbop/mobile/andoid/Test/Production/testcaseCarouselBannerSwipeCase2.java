package se.sfbop.mobile.andoid.Test.Production;

import com.robotium.solo.Solo;
import android.view.View;
import java.util.Random;

import se.sfbio.mobile.android.Test.SFBioTestcase;


public class testcaseCarouselBannerSwipeCase2 extends SFBioTestcase {
	
	public testcaseCarouselBannerSwipeCase2() throws ClassNotFoundException {
		super();
	}
	
	//Methods
	public void testCarouselBannerSwipeCase2() {
		logging("\r\n\r\nStarting testCarouselBannerSwipeCase2 test case.");
    	waitForApplicationToStart();
    	changeCityRandom();
    	goToStatTab();
    	logging("The current city is " + city.getName());
    	for (String string: city.getPromotedMovies()){
    		logging("Promoted title " + string);
    	}
//    	doesItemExist("se.sfbio.mobile.android:id/carousel");
    	View bannerGoAround;
    	bannerGoAround = solo.getView("se.sfbio.mobile.android:id/carousel");
    	Random randomGenerator = new Random();
    	//Will go some random (1-4) steps from rigth or left and then click on the link. It will also keep track on
    	//which movie it will be linked to with b.
    	int a;
    	int b = 0;
    	for(int i = 0; i < 10; i++){
    		a = randomGenerator.nextInt(4);
    			if (randomGenerator.nextInt(2) == 1){
    				for (int j = 0; j < a; j++){
    					solo.scrollViewToSide(bannerGoAround, Solo.RIGHT);
    				}
    				b += a;
    				if (b >= bannerTitles.size()){
    					b = b-bannerTitles.size();
    				}
    			}
    			else{
    				for (int j = 0; j < a; j++){
    					solo.scrollViewToSide(bannerGoAround, Solo.LEFT);
    				}
    				b -= a;
    				if (b < 0){
    					b = bannerTitles.size()+b;
    				}
    			}
    		checkCarouselItem(b);
    	}
    	reportSuccess();
    }		
	
}

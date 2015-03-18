package se.sfbio.mobile.android.Test;

import se.sfbio.mobile.android.Test.SFBioTestcase;

public class testcaseTrailersOnStartPageLinks extends SFBioTestcase {
	
	public testcaseTrailersOnStartPageLinks() throws ClassNotFoundException {
		super();
	}
	
	//Methods
	 public void testTrailersOnStartPageLinks() {
    	 waitForApplicationToStart();
    	 changeCityRandom();
    	 goToStatTab();
    	 for (int i = 0; i < city.getToplist().size(); i++){
    		 	solo.scrollToTop();
    	    	solo.scrollListToLine(0, 1);
    		 	String movieTitle = city.getToplist().get(i);
    		 	loadView(android.widget.ImageView.class, 6, DEFAULT_NUMBER_OF_TRIES_WAITING_FOR_VIEW, "Timed out when waiting for link list page to load.");
	   	    	searchAndReport(searchTextCaseInsensitive(movieTitle), movieTitle + " was found in the list.", "The expected item was not found in the list.");// assertTrue("The expected item was not found in the list.", solo.searchText(movieTitlesStart.get(i)));
	   	    	clickOnTextCaseInsensitive(movieTitle);
	   	    	loadView(android.widget.ScrollView.class, 1, DEFAULT_NUMBER_OF_TRIES_WAITING_FOR_VIEW, "The links page did not load fast enough.");
	   	    	searchAndReport(searchTextCaseInsensitive(movieTitle), "Link to " + movieTitle + " was confirmed to be correct.", "The movie link was not correct on start page to " + movieTitle +".");
	   	    	searchAndReport(searchTextCaseInsensitive("se trailer"), "Found a se trailer button on the movies page.","There was no se trailer button on the movies page.");
	   	    	clickOnTextCaseInsensitive("se trailer");
	   	    	searchAndReport(!searchTextCaseInsensitive(movieTitle), "Left the movies hompage, will confirm that a trailer window was loaded.", "The se trailer link seems to not be working.");
	   	    	loadView(android.widget.VideoView.class, 1, DEFAULT_NUMBER_OF_TRIES_WAITING_FOR_VIEW, "There was no movie trailer loaded in time");
	   	    	solo.sleep(5000);
	   	    	solo.goBack();
	   	    	loadView(android.widget.ScrollView.class, 1, DEFAULT_NUMBER_OF_TRIES_WAITING_FOR_VIEW, "The movie page did not load fast enough after having watched the trailer.");
	   	    	solo.goBack();
    	 }
    	 reportSuccess();
      }
}
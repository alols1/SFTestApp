package se.sfbio.mobile.android.Test;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;

import com.robotium.solo.Solo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

@SuppressWarnings("rawtypes")
public class SFBioTestcase extends ActivityInstrumentationTestCase2 implements SFBioUI {

	/*
	Modifier    | Class | Package | Subclass | World
	————————————+———————+—————————+——————————+———————
	public      |  y    |    y    |    y     |   y
	————————————+———————+—————————+——————————+———————
	protected   |  y    |    y    |    y     |   n
	————————————+———————+—————————+——————————+———————
	no modifier |  y    |    y    |    n     |   n
	————————————+———————+—————————+——————————+———————
	private     |  y    |    n    |    n     |   n

	y: accessible
	n: not accessible
	*/


    //class variables

    protected Solo solo;
    protected static final String LAUNCHER_ACTIVITY_FULL_CLASSNAME = "se.sfbio.mobile.android.SplashActivity";
    protected static final int timeout = 60000;
    protected static final int nbrOfTriesFindingAvailableMovie = 5;
    protected static final int DEFAULT_NUMBER_OF_TRIES_WAITING_FOR_VIEW = 3;

    protected static final String FILE_NAME_INIFILE = "/data/local/tmp/SFBio_Test_ini.txt";
    protected static final String FILE_NAME_SF_TOPLIST_DATA = "/data/local/tmp/SF_toplist.txt";
    protected static final String FILE_NAME_TESTCASE_LOG = "/data/local/tmp/logg.txt";

    protected static final String LOG_PRINT_END_OF_TEST = "Test has ended. Timestamp = ";

    //protected ArrayList<String> movieTitlesStart = new ArrayList<String>();
    //protected ArrayList<String> citysToTest = new ArrayList<String>();
    protected ArrayList<String> Nyheter = new ArrayList<String>();
    protected ArrayList<String> bannerTitles = new ArrayList<String>();
    //protected ArrayList<String> cinemasInMalmo = new ArrayList<String>();
    protected static ArrayList<String> settingsFile = new ArrayList<String>();

    protected SFBioTestData.City city;                // <-- Current city under test.
    protected ArrayList<SFBioTestData.City> cities; // <-- All cities read from database.

    protected static final String BUTTON_TEXT_CHANGE_CITY = "Byt stad";
    protected static final String BUTTON_TEXT_OK = "OK";
    protected static final String BUTTON_TEXT_TRY_AGAIN = "Försök igen";
    protected static final String TEXT_TEXTVIEW_START = "Start";
    protected static final String TEXT_TEXTVIEW_TOPLIST_SWEDEN = "TOPPLISTAN FÖR HELA SVERIGE";
    protected static final String TEXT_TEXTVIEW_MOVIES = "Filmer";
    protected static final String TEXT_TEXTVIEW_CINEMAS = "BIOGRAFER";
    protected static final String TEXT_TEXTVIEW_NOW_IN_CINEMAS = "NYHETER";
    protected static final String TEXT_TEXTVIEW_PROMOTIONS = "KAMPANJER";
    protected static final String TEXT_TEXTVIEW_LOADING = "Hämtar data";
    protected static final String TEXT_TEXTVIEW_INITIATING_PAYMENT = "Initierar betalning...";
    protected static final String TEXT_TEXTVIEW_LOADING_SHOWS = "Laddar föreställningar...";
    protected static final String TEXT_TEXTVIEW_PERFORMING_PAYMENT = "Genomför betalning";
    protected ArrayList<String> bioklubbsKort = new ArrayList<String>();

    // These 20 cities will be used by default. When choosing a random city, one of these will be chosen.
    protected static final String[] DEFAULT_CITIES = {
            "Malmö", "Stockholm", "Halmstad", "Helsingborg", "Jönköping",
            "Kalmar", "Karlstad", "Linköping", "Norrköping", "Sundsvall",
            "Uppsala", "Umeå", "Västerås", "Växjö", "Ystad",
            "Örebro", "Göteborg", "Lund", "Falun", "Östersund"
    };
    //    protected static final String[] DEFAULT_CITIES = {"Lund"};
    protected String[] cities_to_test = DEFAULT_CITIES;

    private static Class launcherActivityClass;

    static {
        try {
            launcherActivityClass = Class.forName(LAUNCHER_ACTIVITY_FULL_CLASSNAME);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    protected SFBioTestcase() throws ClassNotFoundException {
        super(launcherActivityClass);

    }

    /**
     * The setup method will read in arrays where things as toplist and banner titles as listed.
     */
    @Override
    protected void setUp() throws Exception {
        logging("Initiating setup....");
        logging("Setting up testcase: " + this.getClass().getSimpleName());

        //Read settings from initilation file, not used yet...
        BufferedReader br = new BufferedReader(new FileReader(FILE_NAME_INIFILE));
        try {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains("<iniSet>")) {
                    line = line.replace("<iniSet>", "");
                    line = line.replace("</iniSet>", "");
                    settingsFile.add(line);
                }
            }
        } finally {
            br.close();
        }

        // Reading test data.
        // Create and initialize the Data class.
        SFBioTestData data = new SFBioTestData(cities_to_test);
////    		SFBioTestData data = new SFBioTestData(new String[]{"Ystad"}); // <-- Use this constructor if you want to determine which cities to chose between.
        try {
            data.initData();
        } catch (IOException e) {
            reportError("DATABASE ERROR during test setup. Testcase = " + this.getClass().getSimpleName() + ", Error = " + e.toString());
            throw e;
        }

        // Put data in ArrayLists.
        cities = data.getCities();
        city = cities.get(0);
        Nyheter = data.getNewsHeadlines();
        bannerTitles = city.getPromotedMovies();

//    		String cityName = city.getName();
//    		logging(cityName);
//    		    		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
//    		String dateInString = "20150209";
//    		Date date = sdf.parse(dateInString);
//    		data.getPriceOfShow("Jupiter Ascending", city, "FILMSTADEN LUND", date);
        logging("Will initate the solo object.");
        // Initialize Solo object.
        super.setUp();
        logging("Will iniate solo.");
        solo = new Solo(getInstrumentation());
        logging("Will get activity.");
        getActivity();
        logging("Finished setting up testcase: " + this.getClass().getSimpleName());
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        solo.finishOpenedActivities();
    }

    /**
     * Will make a log entry of a given string.
     */
    protected void logging(final String s) {
        try {
            String filename = FILE_NAME_TESTCASE_LOG;
            FileWriter fw = new FileWriter(filename, true); //the true will append the new data
            fw.write(getTimestampForLogPrint() + " " + s + "\r\n");//appends the string to the file
            fw.close();
        } catch (IOException ioe) {
            System.err.println("IOException: " + ioe.getMessage());
        }
    }

    /**
     * Called by the testcase when test has either passed or failed.
     * The resulting log print contains a "filename friendly" timestamp that can be used to identify the log files.
     */
    protected void reportEndOfTest() {
        logging(LOG_PRINT_END_OF_TEST + getTimestampFilenameFriendly());
    }

    /**
     * Called by the testcase when all success criteria have been fulfilled.
     * The resulting log print contains a "filename friendly" timestamp that can be used to identify the log files.
     */
    protected void reportSuccess() {
        logging("Test case was PASS");
        reportEndOfTest();
    }

    /**
     * This functions first argument should be called by doing a text search of a string which is wanted to be found, which
     * results in a boolean. The two arguments should be a logging string of success (s) and one telling what went wrong (f).
     * If the boolean is false there will also be a screenshot taken and the test case is asserted.
     *
     * @param b Success condition.
     * @param s String to be printed in case of success.
     * @param f String to be printed in case of failure.
     */
    protected void searchAndReport(final boolean b, final String s, final String f) {
        if (b) {
            logging(s);
        }
        if (!b) {
            logging("An error has occured");
            logging(f);
            reportEndOfTest();
            try {
                takeScreenshot();
            } catch (IOException e) {
                logging(e.toString());
            }
            assertTrue("Error! " + f, false);
        }
    }

    /**
     * This function is used to report errors. It will first logg that an error has occurred, then
     * call the takeScreenshot function and finally assert. Is used when a defenitive error has been seen.
     *
     * @param String to be logged.
     */
    protected void reportError(final String s) {
        logging("An error has occured");
        logging(s);
        reportEndOfTest();
        try {
            takeScreenshot();
        } catch (IOException e) {
            logging(e.toString());
        }
        assertTrue(s, false);
    }

    /**
     * Will check every 5 seconds if there is a progress bar currently in view.
     * If so, it will make a sleep and then check agian. After 1 minute it will report an error.
     */
    protected void waitForProgressBar() {
        int t = 0;
        final int NTIMES_MAX = 15;
        while (!solo.getCurrentViews(android.widget.ProgressBar.class).isEmpty() && t < NTIMES_MAX) {
            logging("There was a progress bar.");
            solo.sleep(5000);
            t++;
            if (t == NTIMES_MAX) {
                reportError("There was a progress bar that time out.");
            }

        }
    }

    /**
     * Waits for a specified view to be loaded within a minute. If the view is not loaded, it will first
     * check if there is a progress bar and wait for it to disappear (or the function will eventually
     * report an error) and then will check if there is any choice to try again.
     *
     * @param classToLoad              The class to load. This needs to be a subclass of android.view.View.
     * @param noObjects                The number of objects of the specified class that this method will expect.
     * @param numberTries              Use this parameter to allow multiple attempts to find the View. Useful if network conditions are suboptimal.
     * @param descriptionOfViewLoading A description of the view. This String will be printed in the logfile if the View is not loaded correctly.
     * @return True if the View is loaded successfully.
     */
    protected <T extends View> boolean loadView(final Class<T> classToLoad, final int noObjects, final int numberTries, final String descriptionOfViewLoading) {
        logging("Will wait for " + classToLoad + " to be loaded.");
        if (!solo.waitForView(classToLoad, noObjects, timeout)) {
            logging("The first attmept to load the view timed out");
            for (int i = 0; i < numberTries; i++) {
                logging("Now searching for buttons to be able to advance.");
                logging("This is try number " + i + " that we have made so far.");
                //The below checks should search for a way to reconnect if there is any
                if (searchTextCaseInsensitive(BUTTON_TEXT_OK)) {
                    clickOnTextCaseInsensitive(BUTTON_TEXT_OK);
                } else if (searchTextCaseInsensitive(BUTTON_TEXT_TRY_AGAIN)) {
                    clickOnTextCaseInsensitive(BUTTON_TEXT_TRY_AGAIN);
                }
                waitForProgressBar();
                if (solo.waitForView(classToLoad, noObjects, timeout)) {
                    logging("Class " + classToLoad + " was loaded correctly.");
                    return true;
                }
            }
            logging(descriptionOfViewLoading);
            reportError("Was not able to load view " + classToLoad);

        }
        logging("Class " + classToLoad + " was loaded correctly.");
        return true;
    }

    /**
     * Returns a timestamp in a "filename friendly" format (no ":", ";", ".", etc.).
     *
     * @return The current timestamp on format yyyyMMdd-HHmmss. Includes full date, but not milliseconds.
     */
    protected String getTimestampFilenameFriendly() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-HHmmss", Locale.GERMANY);
        return sdf.format(date);
    }

    /**
     * See getTimestampFilenameFriendly
     *
     * @return See getTimestampFilenameFriendly
     */
    protected String getTimestampForScreenshotFileName() {
        return getTimestampFilenameFriendly();
    }

    /**
     * Returns a timestamp in a more precise style suitable for log prints.
     *
     * @return Timsestamp in format HH:mm:ss.SSS. Includes milliseconds, but not the full date.
     */
    protected String getTimestampForLogPrint() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS", Locale.GERMANY);
        return sdf.format(date);
    }

    /**
     * Returns the path in which screenshots will be stored.
     * This path is (probably) dependent on device model and Android version.
     * This method also sets the class variable, so it can be quickly accessed when we take screenshots.
     *
     * @return The path.
     */
    private File getScreenshotDirectory() {
        // The 3 directories below are NOT working.
        //File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS); // open failed: EACCES (Permission denied)
        //File directory = Environment.getExternalStorageDirectory(); // open failed: EACCES (Permission denied)
        //File directory = Environment.getDataDirectory(); // open failed: EACCES (Permission denied)

        // This gives the app's private file storage directory.
        // This is the only directory we are currently able to create files in.
        return solo.getCurrentActivity().getExternalFilesDir(null);
    }

    /**
     * Workaround since the Solo.takeScreenshot() doesn't work.
     * http://stackoverflow.com/questions/17021338/how-to-make-takescreenshot-work-on-robotium-when-calling-junit-via-command-lin
     */
    private void takeScreenshot() throws IOException {

        if (solo.equals(null)) {
            // This means that the error occurred before Solo object was initialized - probably database error in setUp() method.
            // Screenshot is not possible.
            logging("takeScreenshot(): Error occurred before Robotium framework was initialized, screenshot cannot be taken.");
            return;
        }

        String filename =
                "SFBioScreenshot_" +
                        getTimestampForScreenshotFileName() +
                        "_" +
                        this.getClass().getSimpleName() + // <-- Name of Testcase class e.g. "testcaseTicketsTheathers"
                        ".jpg";
        File directory = getScreenshotDirectory();

        // Capture screenshot and store data in a Bitmap object.
        View view = solo.getCurrentViews().get(0).getRootView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();

        // Create file to write to.
        File file = new File(directory, filename);
        if (file.equals(null)) {
            throw new IOException("Failed to open file for screenshot. ");
        }

        // Write to the file, and close the file stream when done.
        FileOutputStream outputstream;
        try {
            outputstream = new FileOutputStream(file);
        } catch (IOException e) {
            throw new IOException("Failed to open output stream for screenshot: " + e.toString());
        }
        bitmap.compress(CompressFormat.JPEG, 100, outputstream);
        outputstream.close();

        // Writes the path of the screenshot to the log, so that the shell script can pull the files.
        String pathToScreenshot = file.toString().replace(
                "/storage/emulated/0/Android/data/",
                "/storage/emulated/legacy/Android/data/" // <-- Confirmed to be correct directory on all phones so far.
        );
        logging("Path to screenshot: " + pathToScreenshot);
    }


    /**
     * Will change city within the application.
     *
     * @param City object which it should be changed to.
     */
    public void changeCity(SFBioTestData.City city) {
        this.city = city;
        String cityName = city.getName();
        logging("Will change city to " + cityName);
        solo.sleep(2000);
        solo.clickOnMenuItem(BUTTON_TEXT_CHANGE_CITY);
        loadView(android.widget.CheckedTextView.class, 3, DEFAULT_NUMBER_OF_TRIES_WAITING_FOR_VIEW, "Not able to load the city list.");
        //if (!solo.waitForView(android.widget.CheckedTextView.class, 3, timeout)){timedOut("Took too long time to load the city list.", "solo.waitForView(android.widget.CheckedTextView.class");};
        logging("Selecting city.");
        searchAndReport(searchTextCaseInsensitive(cityName), "The correct city " + cityName + " was found.", "The correct city " + cityName + " was not found.");
        clickOnTextCaseInsensitive(cityName);
        bannerTitles = city.getPromotedMovies();
        loadView(android.widget.ImageView.class, 6, DEFAULT_NUMBER_OF_TRIES_WAITING_FOR_VIEW, "Not able to load the page after selecting city");
        //if (!solo.waitForView(android.widget.ImageView.class, 6, timeout)){timedOut("Waited to long after selecting city.", "android.widget.ImageView.class");}
    }

    /**
     * Will select a random city and then call changeCity() to make the change in the application.
     */
    public void changeCityRandom() {
        SFBioTestData.City randomCity = cities.get(new Random().nextInt(cities.size()));
        logging("Selecting random city: " + randomCity.getName());
        changeCity(randomCity);
    }

    /**
     * Directs the application to the starting page.
     */
    public void goToStatTab() {
        logging("Attempting to click on the start upper tab.");
        solo.clickOnImage(0);
        searchAndReport(searchTextCaseInsensitive(TEXT_TEXTVIEW_START), "Start tab found,", "Could not find tab with Start.");
        clickOnTextCaseInsensitive(TEXT_TEXTVIEW_START);
        loadView(android.widget.RelativeLayout.class, 1, DEFAULT_NUMBER_OF_TRIES_WAITING_FOR_VIEW, "Not able to load start screen with under tab start.");
        searchAndReport(searchTextCaseInsensitive(TEXT_TEXTVIEW_TOPLIST_SWEDEN), "Went to the start tab", "Did not go to the Start tab");
    }

    /**
     * Directs the application to the tickets tab and undertab filmer.
     */
    public void goToTicketsTab() {
        logging("Attempting to click on the ticket upper tab.");
        solo.clickOnImage(1);
        searchAndReport(searchTextCaseInsensitive(TEXT_TEXTVIEW_MOVIES), "Filmer tab found,", "Could not find tab with filmer.");
        clickOnTextCaseInsensitive(TEXT_TEXTVIEW_MOVIES);
        loadView(android.widget.ImageView.class, 6, DEFAULT_NUMBER_OF_TRIES_WAITING_FOR_VIEW, "Too long loading time when waiting for available movies.");
        searchAndReport(searchTextCaseInsensitive(TEXT_TEXTVIEW_MOVIES), "Went to the tickets tab", "Did not go to the tickets tab");
    }

    /**
     * Directs the application to the GPS tab.
     */
    public void goToGPSTab() {
        logging("Attempting to click on the GPS tab.");
        solo.clickOnImage(2);
        loadView(android.widget.ZoomControls.class, 1, DEFAULT_NUMBER_OF_TRIES_WAITING_FOR_VIEW, "GPS page was not loaded.");
        searchAndReport(searchTextCaseInsensitive(TEXT_TEXTVIEW_CINEMAS), "Found the GPS tab", "Did not go to the GPS tab");
    }

    /**
     * Directs the application to the nyheter tab.
     */
    public void goToNyheterTab() {
        logging("Attempting to click on the nyheter tab.");
        solo.clickOnImage(3);
        loadView(android.widget.TextView.class, 3, DEFAULT_NUMBER_OF_TRIES_WAITING_FOR_VIEW, "Timed out when waiting for NYHETER page to load.");
        searchAndReport(searchTextCaseInsensitive(TEXT_TEXTVIEW_NOW_IN_CINEMAS), "Found the NYHETER tab", "Did not go to the NYHETER tab");
    }

    /**
     * Directs the application to the kampanjer tab.
     */
    public void goToKampanjerTab() {
        logging("Attempting to click on the kampanjer tab.");
        solo.clickOnImage(4);
        loadView(android.widget.TextView.class, 2, DEFAULT_NUMBER_OF_TRIES_WAITING_FOR_VIEW, "Timed out when waiting for KAMPANJER page to load.");
        searchAndReport(searchTextCaseInsensitive(TEXT_TEXTVIEW_PROMOTIONS), "Found the KAMPANJER tab", "Did not go to the KAMPANJER tab");
    }

    /**
     * Needs to be under the tickets tab and then this function will direct you to the under tab biografer.
     */
    public void goToUnderTabBiografer() {
        logging("Attempting to click on the under tab biografer.");
        doesItemExist("se.sfbio.mobile.android:id/tickets_tab_text");
        View biografButton = solo.getView("se.sfbio.mobile.android:id/tickets_tab_text", 1);

        solo.clickOnView(biografButton);
        int timer = 0;
        while (!searchTextCaseInsensitive("Filmer")) {
            solo.sleep(500);
            timer++;
            if (timer > 120) {
                reportError("Not able to load list with available biografer.");
            }
        }

    }

    /**
     * Needs to be on the start page, will click on Nu på bio..
     */
    public void goToUnderTabNuPABio() {
        logging("Attempting to click on the under tab Nu på bio.");
        doesItemExist("se.sfbio.mobile.android:id/button_middle");
        View nuPaBioButton = solo.getView("se.sfbio.mobile.android:id/button_middle", 0);
        solo.clickOnView(nuPaBioButton);
    }

    /**
     * Waits for the SF application to start. Should be called at the beginning of every test case.
     * If there is a commercial pop-up at the beginning of a test case, this will be removed.
     */
    public void waitForApplicationToStart() {
        logging("Starting application");
        solo.sleep(10000);
        loadView(android.widget.RelativeLayout.class, 2, DEFAULT_NUMBER_OF_TRIES_WAITING_FOR_VIEW, "Took too long to load start screen.");
        if (doesItemExistWithoutAssert("se.sfbio.mobile.android:id/button_close_add")) {
            logging("Found an add that needs to be close.");
            solo.clickOnView(solo.getView("se.sfbio.mobile.android:id/button_close_add", 0));
            solo.sleep(2000);
        }
        if (solo.searchText("Välj stad", 0, false)){
            solo.clickOnText("Stockholm");
            logging("Välj stad pop-up screen handled");
        }
        solo.sleep(5000);
    }

    /**
     * Check if the view resource id name exist in the current activity views.
     * Otherwise there is an error reported.
     *
     * @param Resource id of the object
     * @return True if object exists.l
     */
    public boolean doesItemExist(final String name) {

        if (solo.getCurrentActivity().findViewById(solo.getCurrentActivity().getResources().getIdentifier(name, "id", solo.getCurrentActivity().getPackageName())) != null) {
            return true;
        } else {
            reportError("Was not able to find the object " + name + " and therefore the test case fails.");
            return false;
        }
    }

    /**
     * Same as doesItemExist but will not fail test case if the object is not found.
     */
    public boolean doesItemExistWithoutAssert(final String name) {

        if (solo.getCurrentActivity().findViewById(solo.getCurrentActivity().getResources().getIdentifier(name, "id", solo.getCurrentActivity().getPackageName())) != null) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Check carousel item links to see if the carousel banner item clicked on corresponds to the correct title.
     * The int argument is the index of the title being searched for, first banner title is 0 and then rising for
     * every swipe to the right.
     */
    public void checkCarouselItem(final int i) {
        checkCarouselItem(bannerTitles.get(i));
    }

    public void checkCarouselItem(final String bannerName) {
        searchAndReport(searchTextCaseInsensitive(bannerName), "Found the correct banner title " + bannerName, "Not the correct banner title " + bannerName);
        clickOnTextCaseInsensitive(bannerName);
        loadView(android.widget.ScrollView.class, 1, DEFAULT_NUMBER_OF_TRIES_WAITING_FOR_VIEW, "The links page did not load fast enough.");
        searchAndReport(searchTextCaseInsensitive(bannerName), "The banner link contained the correct name.", "The banner link did not contain the correct name.");
        solo.goBack();
        loadView(android.widget.ImageView.class, 6, DEFAULT_NUMBER_OF_TRIES_WAITING_FOR_VIEW, "Timed out when waiting for start page to load.");
    }

    /**
     * Checks that all the items in the provided array are present in a list on the page where we currently are.
     * For example that all the top movies are actually in the top movie list. This method is for the start page where
     * there are displayed movies on top, which needs to be scrolled to the correct scrren position in  order to work.
     */
    public void checkItemsInListFromArrayStartPageLinks(final ArrayList<String> list) {
        logging("Size of list is " + list.size());
        solo.scrollToTop();
        solo.scrollListToLine(0, 1);
        for (int i = 0; i < list.size(); i++) {
            logging("Now searching for " + list.get(i));
            loadView(android.widget.ImageView.class, 6, DEFAULT_NUMBER_OF_TRIES_WAITING_FOR_VIEW, "Timed out when waiting for link list page to load.");
            searchAndReport(searchTextCaseInsensitive(list.get(i)), list.get(i) + " was successfully found.", list.get(i) + " was not found inte the list.");
            clickOnTextCaseInsensitive(list.get(i));
            loadView(android.widget.ScrollView.class, 1, DEFAULT_NUMBER_OF_TRIES_WAITING_FOR_VIEW, "The links page did not load fast enough.");
            //To check that the link we are in is for the correct movie
            searchAndReport(searchTextCaseInsensitive(list.get(i)), "The link " + list.get(i) + " was correct", "The link " + list.get(i) + " could not be found.");
            solo.goBack();
        }
    }

    /**
     * Checks that all the items in the provided array are present in a list on the page where we currently are.
     * For example that all the top movies are actually in the top movie list.
     */
    public void checkItemsInListFromArray(final ArrayList<String> list) {
        logging("Size of list is " + list.size());
        for (int i = 0; i < list.size(); i++) {
            solo.scrollToTop();
            logging("Now searching for " + list.get(i));
            loadView(android.widget.ImageView.class, 6, DEFAULT_NUMBER_OF_TRIES_WAITING_FOR_VIEW, "Timed out when waiting for link list page to load.");
            searchAndReport(searchTextCaseInsensitive(list.get(i)), list.get(i) + " was successfully found.", list.get(i) + " was not found inte the list.");
            clickOnTextCaseInsensitive(list.get(i));
            loadView(android.widget.ScrollView.class, 1, DEFAULT_NUMBER_OF_TRIES_WAITING_FOR_VIEW, "The links page did not load fast enough.");
            //To check that the link we are in is for the correct movie
            searchAndReport(searchTextCaseInsensitive(list.get(i)), "The link " + list.get(i) + " was correct", "The link " + list.get(i) + " could not be found.");
            solo.goBack();
        }
    }

    public void checkItemsInListFromArray(final ArrayList<String> list, String s, int t) {
        logging("Size of list is " + list.size());
        for (int i = 0; i < t; i++) {
            solo.scrollToTop();
            logging("Now searching for " + list.get(i));
            loadView(android.widget.ImageView.class, 6, DEFAULT_NUMBER_OF_TRIES_WAITING_FOR_VIEW, "Timed out when waiting for link list page to load.");
            searchAndReport(searchTextCaseInsensitive(list.get(i)), list.get(i) + " was successfully found.", list.get(i) + " was not found inte the list.");
            clickOnTextCaseInsensitive(list.get(i));
            loadView(android.widget.ScrollView.class, 1, DEFAULT_NUMBER_OF_TRIES_WAITING_FOR_VIEW, "The links page did not load fast enough.");
            //To check that the link we are in is for the correct movie
            searchTextCaseInsensitive(s, true);
            searchAndReport(searchTextCaseInsensitive(list.get(i)), "The link " + list.get(i) + " was correct", "The link " + list.get(i) + " could not be found.");
            solo.goBack();
        }
    }
    /**
     * Clicks on a button with the specified String. Does not take into account upper or lower case.
     * This is the same as calling solo.clickOnText(text), but matches regardless of case.
     *
     * @param text The text String to search for. This should contain no special characters.
     */
    protected void clickOnTextCaseInsensitive(String text) {
        solo.clickOnText("(?i)" + text);
    }

    /**
     * Searches for the specified String, and returns True if the String was found.
     * Does not take into account upper or lower case.
     * This is the same as calling solo.searchText(text), but matches regardless of case.
     *
     * @param text The text String to search for. This should contain no special characters.
     * @return True if the String was found.
     */
    protected boolean searchTextCaseInsensitive(String text) {
        return solo.searchText("(?i)" + text);
    }

    /**
     * Searches for the specified String, and returns True if the String was found.
     * Does not take into account upper or lower case.
     * This is the same as calling solo.searchText(text), but matches regardless of case.
     *
     * @param text        The text String to search for. This should contain no special characters.
     * @param onlyVisible Whether or not Solo should attempt to scroll in order to find the specified String.
     * @return True if the String was found.
     */
    protected boolean searchTextCaseInsensitive(String text, boolean onlyVisible) {
        return solo.searchText("(?i)" + text,onlyVisible );
    }
}

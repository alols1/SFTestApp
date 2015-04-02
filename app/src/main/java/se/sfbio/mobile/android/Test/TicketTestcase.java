package se.sfbio.mobile.android.Test;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

import android.annotation.SuppressLint;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.robotium.solo.WebElement;

public abstract class TicketTestcase extends SFBioTestcase {

    private static final String USER_EMAIL_ADDRESS = "sfbiotestmail@gmail.com"; // Used when buying and reserving tickets.

    //	private static final SimpleDateFormat String = null;
    public TicketTestcase() throws ClassNotFoundException {
        super();
    }

    protected static final String TEXT_TEXTVIEW_Today = "idag";
    protected static final String TEXT_TEXTVIEW_Tomorrow = "imorgon";
    protected static int nbrOfTries = 5;

    /**
     * To use this method, application should already be in the tickets tab (second image), use goToTicketsTab().
     * This method will randomly select a movie and then find an available show for which seats are choosen.
     * If there are any problems in the selection process, the process will start over.
     *
     * @param numberOfSeats The number of seats that should be booked
     */
    public void getCinemaAndTicketFromTicketsTab(int numberOfSeats) {
        int try_again = 0; // <--- This variable keeps track on how many times the application has failed to select a movie, cinema or show
        Random rn = new Random();
        int movieToWatch; // <--- Randomly created number to randomize which movie is choosen from top list
        while (try_again < nbrOfTriesFindingAvailableMovie) {
            while (try_again < nbrOfTriesFindingAvailableMovie) {
                movieToWatch = rn.nextInt(city.getToplist().size());
                int timesSearchingForMovieInList = 0;
                logging("Will search for the movie " + city.getToplist().get(movieToWatch).toUpperCase());
                while (!searchTextCaseInsensitive(city.getToplist().get(movieToWatch).toUpperCase())) {
                    movieToWatch = rn.nextInt(city.getToplist().size());
                    solo.scrollToTop();
                    timesSearchingForMovieInList++;
                    if (timesSearchingForMovieInList > 15) {
                        reportError("There was no movie found in the start page list corresponding to the movie array, made " + timesSearchingForMovieInList + " tries.");
                    }
                    logging("Will insetad search for the movie " + city.getToplist().get(movieToWatch) + ".");
                }
                clickOnTextCaseInsensitive(city.getToplist().get(movieToWatch).toUpperCase());
                loadView(android.widget.TextView.class, 6, DEFAULT_NUMBER_OF_TRIES_WAITING_FOR_VIEW, "Timed out when waiting for selected movies shows.");
                if (chooseFirstAvailableShowinAvailableShows(false)) {
                    break;
                }
                logging("No available shows found, try again.");
                try_again++;
                solo.goBack();
                solo.scrollToTop();
            }
            if (try_again == nbrOfTries) {
                reportError("Could not find any available cinema.");
            }
            if (choosePlaceToSit(numberOfSeats)) {
                break;
            }
            solo.goBack();
            try_again++;
        }
        if (try_again == nbrOfTries) {
            reportError("There were no available seats in the cinemas");
        }
    }

    /**
     * To use this method, application should be in the start tab or the under tab Nu på bio.
     * This method will randomly select a movie and then find an available show for which seats are choosen.
     * If there are any problems in the selection process, the process will start over.
     *
     * @param numberOfSeats The number of seats that should be booked
     */
    @SuppressLint("DefaultLocale")
    public void getCinemaAndTicketFromStartTab(int numberOfSeats) {
        int try_again = 0; // <--- This variable keeps track on how many times the application has failed to select a movie, cinema or show
        Random rn = new Random();
        while (try_again < nbrOfTries) {
            int movieToWatch; // <--- Randomly created number to randomize which movie is choosen from top list
            while (try_again < nbrOfTries) {
                movieToWatch = rn.nextInt(10);
                logging("Will search for the movie " + city.getToplist().get(movieToWatch) + ".");
                int timesSearchingForMovieInList = 0;
                //Scrolling to position the screen under the promotion banner
                solo.scrollToTop();
                solo.scrollListToLine(0, 1);
                while (!searchTextCaseInsensitive(city.getToplist().get(movieToWatch))) {
                    logging("Was not able to find movie " + city.getToplist().get(movieToWatch) + ".");
                    solo.scrollToTop();
                    solo.scrollListToLine(0, 1);
                    movieToWatch = rn.nextInt(10);
                    timesSearchingForMovieInList++;
                    if (timesSearchingForMovieInList > 15) {
                        reportError("There was no movie found in the start page list corresponding to the movie array, made " + timesSearchingForMovieInList + " tries.");
                    }
                    logging("Will insetad search for the movie " + city.getToplist().get(movieToWatch) + ".");
                }

                clickOnTextCaseInsensitive(city.getToplist().get(movieToWatch));
                loadView(android.widget.TextView.class, 6, DEFAULT_NUMBER_OF_TRIES_WAITING_FOR_VIEW, "Timed out when waiting for selected movie page.");
                if (!searchTextCaseInsensitive("se trailer")) {
                    reportError("Was not able to confirm that the movie page was not loaded.");
                }
                logging("Movie has been clicked on and page has been loaded.");
                logging("Will call the choose first available show now.");
                if (chooseFirstAvailableShowinAvailableShows(true)) {
                    break;
                }
                logging("Not available, try again.");
                try_again++;
                solo.goBack();
                solo.scrollToTop();
            }
            if (try_again == nbrOfTries) {
                reportError("Could not find any available cinema.");
            }
            if (choosePlaceToSit(numberOfSeats)) {
                break;
            }
            solo.goBack();
            try_again++;
        }
        if (try_again == nbrOfTries) {
            reportError("There were no available seats in the cinemas");
        }
    }

    /**
     * To use this method, application should be in the under tab to tickets, biografer (use goToTicketsTab and goToUnderTabBiografer.
     * This method will randomly select a movie and then find an available show for which seats are choosen.
     * If there are any problems in the selection process, the process will start over.
     *
     * @param numberOfSeats The number of seats that should be booked
     */
    public void getCinemaAndTicketFromBiograferUnderTab(int numberOfSeats) {
        int try_again = 0; // <--- This variable keeps track on how many times the application has failed to select a movie, cinema or show

        while (try_again < nbrOfTries) {
            while (try_again < nbrOfTries) {
                if (!choosingCinemaFromBiograferUnderTab()) {
                    reportError("There were no available cinemas found.");
                }
                logging("Will now choose an available show.");
                if (chooseFirstAvailableShowinAvailableShows(false)) {
                    break;
                }
                try_again++;
                solo.goBack();
                solo.scrollToTop();
            }
            if (try_again == nbrOfTries) {
                reportError("Could not find any available cinema.");
            }
            if (choosePlaceToSit(numberOfSeats)) {
                break;
            }
            solo.goBack();
            try_again++;
        }
        if (try_again == nbrOfTries) {
            reportError("There were no available seats in the cinemas");
        }
    }

    /**
     * Called to find an available show when application is in the tickets tabs undertab biografer.
     *
     * @return True if a cinema was found, otherwise false.
     */
    public boolean choosingCinemaFromBiograferUnderTab() {
        Random rn = new Random();
        for (int i = 0; i < city.getTheatreList().size() * 2; i++) { // <--- getTheatreList will give the number of availble cinemas in the city
            solo.scrollToTop();
            logging("Will try to click on a cinema. Try number " + i + ".");
            solo.scrollListToLine(0, rn.nextInt(city.getTheatreList().size()));
            int slump = city.getTheatreList().size();
            if (slump > 6) {
                slump = 6;
            }
            solo.clickInList(rn.nextInt(slump));
            if (searchTextCaseInsensitive("platser kvar")) { // <--- Checks if we have advanced to the next screen, if so break
                logging("A cinema was found");
                return true;
            }

        }
        return false;
    }

    /**
     * Will check if the date displayed when choosing shows is already passed or to far ahead in the future.
     *
     * @return True if the date displayed is in valid range, or otherwise false.
     */
    public boolean isAhead() {
        Date date = new Date();
        SimpleDateFormat day = new SimpleDateFormat("dd", Locale.GERMANY);
        SimpleDateFormat month = new SimpleDateFormat("MM", Locale.GERMANY);
        logging("Will check if the date displayed is valid.");
        logging("The current month is " + month.format(date));
        logging("Date displayed by application " + getDateDisplayedByApp("month"));
        String[] monthsList = {"januari", "februari", "mars", "april", "maj", "juni", "juli", "augusti", "september", "oktober", "november", "december"};

        if (monthsList[Integer.parseInt(month.format(date).toString()) - 1].equals(getDateDisplayedByApp("month"))) {
            //Will currently allow yesterday as a valid date
            if (Integer.parseInt(getDateDisplayedByApp("date")) < Integer.parseInt(day.format(date).toString()) - 1) {
                logging("The date displayed by the app is has already passed, will assert. Date:" + getDateDisplayedByApp("date") + " Month: " + getDateDisplayedByApp("month"));
                return false;
            }
            if (Integer.parseInt(getDateDisplayedByApp("date")) == Integer.parseInt(day.format(date).toString()) - 1) {
                logging("The date show is for yesterday, will move till today.");
                doesItemExist("se.sfbio.mobile.android:id/toolbar_btn_next");
                solo.clickOnView(solo.getView("se.sfbio.mobile.android:id/toolbar_btn_next"));
                waitForShowsToLoad();
            }
            return true;
        }
        int k = Integer.parseInt(month.format(date).toString());
        if (k == 12) {
            k = 0;
        }
        if (monthsList[k].equals(getDateDisplayedByApp("month"))) {
            logging("The movie is due to the upcoming month, will let it pass");
            return true;
        }
        logging("The month displayed is not acceptable, will assert");
        return false;
    }

    /**
     * Will change the day displayed till the next day.
     */
    public void moveDateTillTomorrow() {
        logging("The correct day was found but as it's late, try for tomorrow as there will be more available shows then.");
        doesItemExist("se.sfbio.mobile.android:id/toolbar_btn_next");
        solo.clickOnView(solo.getView("se.sfbio.mobile.android:id/toolbar_btn_next"));
        waitForShowsToLoad();
        if (!getDateDisplayedByApp("day").equals("TEXT_TEXTVIEW_Tomorrow")) {
            reportError("Could not find text with tomorrow");
        }
    }

    /**
     * When displaying the different available shows, this function will make sure that idag is displayed or call the isAhead method
     * to check weather the deviance can be accepted. If the clock when running the test is more than 19:00, it will change
     * till imorgon as this should make more shows available to book tickets for.
     */
    public void getValidDate() {
        Date date = new Date();
        SimpleDateFormat hour = new SimpleDateFormat("HH", Locale.GERMANY);
        logging("The day displayed is " + getDateDisplayedByApp("day"));
        if (!getDateDisplayedByApp("day").equals("TEXT_TEXTVIEW_Today")) {
            logging("Will check the isahead function.");
            if (!isAhead()) {
                reportError("Date not valid");
            }
        }
        logging("The current time is " + hour.format(date).toString());
        if (getDateDisplayedByApp("day").equals("TEXT_TEXTVIEW_Today") && Integer.parseInt(hour.format(date).toString()) > 18) {
            moveDateTillTomorrow();
        }
    }

    /**
     * This method returns the day (idag, imorgon, lördag etc), date and month for which the available shows are displayed.
     *
     * @param s Either day, date or month
     * @return The current weekday (or idag and imorgon), date or year.
     */
    public String getDateDisplayedByApp(String s) {
        TextView readDate = (TextView) solo.getView("se.sfbio.mobile.android:id/theaterdetail_dateinfo_text");
        String readFromApp = (String) readDate.getText();
        String dayFromApp = readFromApp.substring(0, readFromApp.indexOf(" "));
        if (s.equals("day")) {
            return dayFromApp;
        }
        String temp = readFromApp.replace(dayFromApp + " ", "");
        String dateFromApp = temp.substring(0, temp.indexOf(" "));
        if (s.equals("date")) {
            return dateFromApp;
        }
        String monthFromApp = temp.replace(dateFromApp + " ", "");
        if (s.equals("month")) {
            return monthFromApp;
        }
        reportError("Invalid input for the getDateDisplayedByApp method");
        return "";
    }

    /**
     * When we have selected a movie we want to but/make a reservation for, this function can be called and it will
     * choose the first available (clickable) show available on the page. Then it will direct to the page where
     * the seats are choosen.
     *
     * @return If the available shows have been loaded.
     */
    public boolean waitForShowsToLoad() {
        int timer = 0;
        logging("Will check if available shows yet has been loaded.");
        while (!searchTextCaseInsensitive("platser kvar", true) && searchTextCaseInsensitive("Laddar föreställningar...", true)) {
            logging("Still waiting for available shows to load, will try again in a sec.");
            solo.sleep(1000);
            timer++;
            if (timer > 60) {
                return false;
            }
        }
        return true;
    }

    /**
     * Will check for the first available show in the list (the application should have been directed to a screen showing already).
     * First it will check if the date displayed is ok and depending on the outcome go on, change it or assert.
     * It will then click on the available shows in a random fashion until it finds a show that is available.
     *
     * @param scroll Should the list of available shows be scrolled to the bottom?
     * @return IF cinema was found or not.
     */
    public boolean chooseFirstAvailableShowinAvailableShows(boolean scroll) {

        logging("Will search for the next available show.");
        logging("Will first check if today is displayed");
        getValidDate();
        if (!waitForShowsToLoad()) {
            return false;
        }
        solo.sleep(2000);
        //if(searchTextCaseInsensitive("Det finns inga tillgängliga föreställningar för den valda dagen", true)){logging("There were no available shows for this cinema.");return false;}

        if (scroll) {
            solo.scrollToBottom();
        } // <--- Depending of which flow we arrived to a select show screen, this should be true or false
        //to allow scrolling down the list to the bottom.
        int availableShows = 0;
        ArrayList<View> test = solo.getCurrentViews();
        doesItemExist("se.sfbio.mobile.android:id/auditorium_show_item_main_layout");
        for (View view : test) {
            if (view.getId() == solo.getView("se.sfbio.mobile.android:id/auditorium_show_item_main_layout").getId()) {
                availableShows++;
            }
        }

        logging(availableShows + " available shows were found.");
        Random rn = new Random();
        for (int i = 0; i < availableShows * 2; i++) {
            int temp = rn.nextInt(availableShows);
            solo.clickOnView(solo.getView("se.sfbio.mobile.android:id/auditorium_show_item_main_layout", temp));
            if (!searchTextCaseInsensitive("platser kvar")) {
                logging("Found an available show.");
                loadView(android.widget.Button.class, 1, DEFAULT_NUMBER_OF_TRIES_WAITING_FOR_VIEW, "Timed out while waiting for cinema salon to load.");
                return true;
            }
        }
        logging("Not able to find an available cinema.");
        return false;
    }

    /**
     * Checks if there is a pop-up which usually comes just after having installed the application, and if there is removes it.
     */
    public void checkForInformationAndOK() {
        solo.sleep(500);
        if (searchTextCaseInsensitive("Information"))
            clickOnTextCaseInsensitive("Visa inte igen");
    }

    /**
     * In the screen where the user is asked to choose a seat to sit in, this function will choose two
     * available seats if possible.
     *
     * @param numberOfSeats The number of seats to be booked.
     * @return True if seats were successfully booked, otherwise false.
     */
    public boolean choosePlaceToSit(int numberOfSeats) {
        checkForInformationAndOK();
        View plus = solo.getView("se.sfbio.mobile.android:id/gpv_button_add_seat");
        View minus = solo.getView("se.sfbio.mobile.android:id/gpv_button_remove_seat");
        //Two seats are standard, if devient number of seats is inputed, change the seats accordingly
        if (numberOfSeats == 1) {
            solo.clickOnView(minus);
        } else if (numberOfSeats > 2) {
            for (int i = 0; i < numberOfSeats - 2; i++) {
                solo.clickOnView(plus);
            }
        }
        //The choose seat screen does not allow much interaction, random clicking on the screen will be used. To better the
        //odds of getting a hit (finding a seat) the screen is zoomed in, and when seats are available, zoomed out.
        View zoom = solo.getView("se.sfbio.mobile.android:id/gpv_button_zoom_plus");
        for (int i = 0; i < 5; i++) {
            solo.clickOnView(zoom);
        }
        int x = 100;
        int y = 350;
        Random randomGenerator = new Random();
        for (int i = 0; i < 200; i++) {
            solo.clickOnScreen(x + randomGenerator.nextInt(400), y + randomGenerator.nextInt(400));
            if (!searchTextCaseInsensitive("Välj " + numberOfSeats + " Platser")) {
                break;
            }
        }
        zoom = solo.getView("se.sfbio.mobile.android:id/gpv_zoom_button_minus");
        for (int i = 0; i < 5; i++) {
            solo.clickOnView(zoom);
        }
        //This function will use the arrow buttons and try to find a free seat.
        for (int i = 8; i != 0; i -= 2) {
            if (goThroughSeats(solo.getView("se.sfbio.mobile.android:id/gpv_button_right"), i) == 0) {
                break;
            }
            if (goThroughSeats(solo.getView("se.sfbio.mobile.android:id/gpv_button_up"), i) == 0) {
                break;
            }
            if (goThroughSeats(solo.getView("se.sfbio.mobile.android:id/gpv_button_left"), i) == 0) {
                break;
            }
            if (goThroughSeats(solo.getView("se.sfbio.mobile.android:id/gpv_button_down"), i) == 0) {
                break;
            }
        }
        if (!searchTextCaseInsensitive("kan ej bokas") && !searchTextCaseInsensitive("Tiden har tagit slut")) {
            logging("Seats were successfully choosen.");
            return true;
        } else {
            if (searchTextCaseInsensitive("Tiden har tagit slut")) {
                logging("The time ran out when choosing seats.");
                searchAndReport(searchTextCaseInsensitive("Nej"), "Found the Nej button to try with another movie.", "No Nej button was found although the time had run out.");
                clickOnTextCaseInsensitive("Nej");
            } else {
                logging("A problem appeared when selecting seat.");
                solo.goBack();
            }
            return false;
        }
    }

    public int goThroughSeats(View chooseSeat, int i) {
        for (int j = 0; j < i; j++) {
            solo.clickOnView(chooseSeat);
            if (!searchTextCaseInsensitive("kan ej bokas")) {
                return 0;
            }
        }
        return 1;
    }

    public void confirmSeatWhenBuying(int numberOfSeats) {
        ArrayList<View> test = solo.getCurrentViews();
        for (View view : test) {
            logging(view.toString());
        }
        searchAndReport(searchTextCaseInsensitive("KLAR"), "Found button KLAR.", "Was not able to find KLAR button.");
        clickOnTextCaseInsensitive("KLAR");
        loadView(android.widget.ListView.class, 1, DEFAULT_NUMBER_OF_TRIES_WAITING_FOR_VIEW, "Timed out after pressing KLART button.");
        searchAndReport(searchTextCaseInsensitive("Köp biljetter"), "Will click on button Köp biljetter.", "Was not able to find button Köp biljetter.");
        clickOnTextCaseInsensitive("Köp biljetter");
        solo.sleep(2000);
        //Give a second chance if connection is broken when trying to buy ticket
        if (searchTextCaseInsensitive("Din anslutning tappades, vill du försöka igen?")) {
            logging("The connection was brooken when trying to buy a ticket, retrying.");
            loadView(android.widget.Button.class, 1, DEFAULT_NUMBER_OF_TRIES_WAITING_FOR_VIEW, "Timed out while waiting for cinema salon to load.");
            searchAndReport(searchTextCaseInsensitive("Ja"), "Found JA button to reconnect after loosing connection", "Could not find JA button to reconnect after loosing connection");
            clickOnTextCaseInsensitive("Ja");
            if (!choosePlaceToSit(numberOfSeats)) {
                reportError("Could not find a seat after reconnecting");
            }
            searchAndReport(searchTextCaseInsensitive("KLAR"), "Found button KLAR.", "Was not able to find KLAR button.");
            clickOnTextCaseInsensitive("KLAR");
            if (searchTextCaseInsensitive("Din anslutning tappades, vill du försöka igen?")) {
                reportError("The connection was broken for the second when trying to buy a ticket.");
            }
            searchAndReport(searchTextCaseInsensitive("Köp biljetter"), "Will click on button Köp biljetter.", "Was not able to find button Köp biljetter.");
            clickOnTextCaseInsensitive("Köp biljetter");
            solo.sleep(2000);
        }
        loadView(android.widget.TextView.class, 5, DEFAULT_NUMBER_OF_TRIES_WAITING_FOR_VIEW, "Failed to load the screen for buying ticket.");
    }

    /**
     * When performing a purchase, the user is asked to fill in an email adress which is done by using this method.
     */
    public void fillInEmailatBuyTicketScreen() {
        searchAndReport(searchTextCaseInsensitive("En bekräftelse kommer att skickas till den angivna e-postadressen"), "Application successfully loaded the firts buy ticket screen.", "Could not identify that test case loaded the first buy ticket screen.");
        solo.clearEditText(0);
        solo.enterText(0, USER_EMAIL_ADDRESS);
    }

    /**
     * Will fill in numberOfBioklubbskort (up to 5). The card numbers will be randomly choosen and the function will check that they are accepted.
     *
     * @param numberOfBioklubbskort Numer of bioklubbskort to be used.
     * @param numberOfSeats The number of seats to be booked.
     */
    public void fillInBioklubbsKort(int numberOfSeats, int numberOfBioklubbskort) {
        logging("Will put input bioklubbkort information and check that they are accepted.");
        ArrayList<String> bioklubbsKort = new ArrayList<String>(); // <--- A list with all the available bioklubbskort
        bioklubbsKort.add("30581656820");
        bioklubbsKort.add("30012928912");
        bioklubbsKort.add("30012826310");
        bioklubbsKort.add("30012855191");
        searchAndReport(searchTextCaseInsensitive("Bioklubbskort"), "Found a button for bioklubbskort.", "Was not able to find bioklubbskort anywhere on the screen.");
        clickOnTextCaseInsensitive("Bioklubbskort");
        loadView(android.widget.EditText.class, 1, DEFAULT_NUMBER_OF_TRIES_WAITING_FOR_VIEW, "Loading the screen where the bioklubbskort are being inputed.");
        for (int i = 0; i < numberOfSeats; i++) {
            solo.clearEditText(i); // <--- Will clear old data if there is any in the fields
        }
        Random rn = new Random();
        int randomNumber;
        for (int i = 0; i < numberOfBioklubbskort; i++) { // <--- This function will randomize a card number and fill it in, it then erases the number from the arraylist
            logging("The size of the bioklubbsarray is " + bioklubbsKort.size());
            randomNumber = rn.nextInt(bioklubbsKort.size());
            solo.typeText(i, bioklubbsKort.get(randomNumber));
            bioklubbsKort.remove(randomNumber);
        }
        solo.sleep(5000);
        searchAndReport(!searchTextCaseInsensitive("Kortnumret är ej giltigt"), "The given bioklubbskort were valid.", "There was one bioklubbskort which was not valid.");
        searchAndReport(!searchTextCaseInsensitive("Kortet finns inte"), "The given bioklubbskort were valid.", "There was one bioklubbskort which was not valid.");
        doesItemExist("se.sfbio.mobile.android:id/membership_done_button");
        logging("Now clicking on KLAR button");
        solo.clickOnView(solo.getView("se.sfbio.mobile.android:id/membership_done_button"));
        loadView(android.widget.ImageView.class, 1, DEFAULT_NUMBER_OF_TRIES_WAITING_FOR_VIEW, "Failed to load the screen for buying ticket after entering bioklubbskort.");
    }

    /**
     * Will clear any previous history of bioklubbkort that might have been saved.
     */
    public void clearBioKlubbsKort() {
        logging("Will check if the show offers the user to input bioklubbskort.");
        if (searchTextCaseInsensitive("Bioklubbskort", true)) {
            logging("Will clear bioklubbskort information in application.");
            clickOnTextCaseInsensitive("Bioklubbskort");
            loadView(android.widget.EditText.class, 1, DEFAULT_NUMBER_OF_TRIES_WAITING_FOR_VIEW, "Loading the screen where the bioklubbskort are being inputed.");
            TextView readCard = (TextView) solo.getView("se.sfbio.mobile.android:id/membership_item_cardnumber");
            String readFromApp = readCard.getText().toString();
            logging("The string read is " + readFromApp);
            if (readFromApp.equals("11 siffror") || readFromApp.equals("")) {
                solo.goBack();
            } else {
                solo.clearEditText(0);
                doesItemExist("se.sfbio.mobile.android:id/membership_done_button");
                logging("Now clicking on KLAR button");
                solo.clickOnView(solo.getView("se.sfbio.mobile.android:id/membership_done_button"));
                loadView(android.widget.TextView.class, 5, DEFAULT_NUMBER_OF_TRIES_WAITING_FOR_VIEW, "Failed to load the screen for buying ticket after erasing information about bioklubbskort.");
            }
        }
    }

    /**
     * This function is supposed to allow usage of presentkort etc. Currently having big problems.
     */
    @SuppressLint("NewApi")
    public void fillInPresentkort() {
        searchAndReport(searchTextCaseInsensitive("SF Bios betalmedel"), "Found a button for presentkort (SF Bios betalmedel).", "Was not able to find presentkort (SF Bios betalmedel) anywhere on the screen.");
        clickOnTextCaseInsensitive("SF Bios betalmedel");
        loadView(android.widget.EditText.class, 1, DEFAULT_NUMBER_OF_TRIES_WAITING_FOR_VIEW, "Loading the screen where the presentkort can be inputed.");
        if (!searchTextCaseInsensitive("Skriv in ditt kort- eller biljettnummer. Följande kort är giltiga:")) {
            reportError("Was not able to confirm that application acctually had advanced to the presentkort screen.");
        }
        solo.enterText(0, "26208621094");
        ImeDone();
        solo.sleep(2000);
        solo.clickOnView(solo.getView("se.sfbio.mobile.android:id/card_item_two_amount_select_button"));
        ArrayList<View> views = solo.getCurrentViews();
        for (View view : views) {
            logging(view.toString());
        }
        logging("Will try to modify the number.");
        View numberPicker = solo.getView("net.simonvt.numberpicker.NumberPicker$CustomEditText");
//		numberPicker = solo.getView(android.widget.NumberPicker.class, 0);
        numberPicker.scrollTo(2, 0);
        //setNumberPicker(numberPicker, 2);

        doesItemExist("id/button_red_done");
        logging("Now clicking on KLAR button");
        solo.clickOnView(solo.getView("se.sfbio.mobile.android:id/button_red_done"));
        loadView(android.widget.TextView.class, 9, DEFAULT_NUMBER_OF_TRIES_WAITING_FOR_VIEW, "Failed to load the screen for buying ticket after entering sf betalmedel.");
    }

    /**
     * After having selected all different presentkort and bioklubbskort, this method will press the
     * big red button to advance to the next screen.
     */
    public void confirmInfoInFirstBuyScreen() {
        loadView(android.widget.TextView.class, 9, DEFAULT_NUMBER_OF_TRIES_WAITING_FOR_VIEW, "Could not load the first but ticket screen.");
        logging("Will click on the big red button to advance to the next screen where credit card information is filled in and confirmation of purchase is made.");
        doesItemExist("se.sfbio.mobile.android:id/red_button");
        solo.clickOnView(solo.getView("se.sfbio.mobile.android:id/red_button"));
        loadView(android.webkit.WebView.class, 1, DEFAULT_NUMBER_OF_TRIES_WAITING_FOR_VIEW, "Did not load the confirm buy ticket screen.");
    }

    /**
     * Fills in the credit card information for purchasing a ticket. Credit card number and CCV code
     * needs to be provided when calling this function.
     *
     * @param cardNo Credit card number.
     * @param ccv CCV    code to the credit card.
     */
    public void fillCreditCardInfo(String cardNo, String ccv) {
        loadView(android.webkit.WebView.class, 1, DEFAULT_NUMBER_OF_TRIES_WAITING_FOR_VIEW, "Did not load the web view  item to fill in credit card.");
        solo.scrollToBottom();
        loadView(android.widget.CheckBox.class, 1, DEFAULT_NUMBER_OF_TRIES_WAITING_FOR_VIEW, "Did not find the check box when filling in credit card info.");

        doesItemExist("se.sfbio.mobile.android:id/checkboxEasyPayment");
        solo.sleep(5000);
        Date date = new Date();
        SimpleDateFormat month = new SimpleDateFormat("MM", Locale.GERMANY);
        SimpleDateFormat year = new SimpleDateFormat("yyyy", Locale.GERMANY);
        logging("The year is " + year.format(date));
        logging("The month is " + month.format(date));
        if (solo.isCheckBoxChecked(0)) { // <--- There is a check box which can be checked if the application is to save credit card data. This box should be unchecked.
            solo.clickOnCheckBox(0);
            solo.sleep(5000);
            solo.scrollToBottom();
        }
        ArrayList<WebElement> test = solo.getCurrentWebElements();
        if (test.size() < 27) {
            logging("Will wait to see if webelement is loaded.");
            solo.sleep(10000);
            test = solo.getCurrentWebElements();
            if (test.size() < 27) {
                reportError("There were not enough webelements loaded so it's not possible to buy a ticket.");
            }
        }
//		for (WebElement view: test) {
//			logging(view.toString());
//		}
        solo.typeTextInWebElement(test.get(12), cardNo); // <--- The index 12 corresponds to the view used to put in credit card number
        solo.typeTextInWebElement(test.get(test.size() - 1), ccv); // <--- Index size-1 is the web view for the CCV code
        solo.clickOnWebElement(test.get(18));
        clickOnTextCaseInsensitive(month.format(date));
        solo.sleep(1000);
        solo.clickOnWebElement(test.get(21));
        clickOnTextCaseInsensitive(year.format(date));
        solo.sleep(1000);
    }

    /**
     * When all the information (credit card) is filled in, this method will confirm the purchase and also
     * check that it has been successfully made.
     *
     * @return True if ticket was successfully bought, false if not.
     */
    public boolean confirmTicketBuy() {
        solo.clickOnView(solo.getView("se.sfbio.mobile.android:id/red_button")); // <---Checks if the KLAR button exists and will then press it
        loadView(android.widget.ScrollView.class, 1, DEFAULT_NUMBER_OF_TRIES_WAITING_FOR_VIEW, "Did not load the confirm purchase of ticket screen.");
        if (searchTextCaseInsensitive("Försök igen", true)) {
            logging("The payment could not be confirmed, will try again");
            clickOnTextCaseInsensitive("Försök igen");
            solo.sleep(10000);
        }

        searchAndReport(searchTextCaseInsensitive("Tack för ditt köp"), "Purchase was successfull!", "Purchase was not successful.");
        clickOnTextCaseInsensitive("Stäng");
        loadView(android.widget.RelativeLayout.class, 1, DEFAULT_NUMBER_OF_TRIES_WAITING_FOR_VIEW, "Took too long to load start screen..");
        searchAndReport(searchTextCaseInsensitive("TOPPLISTAN FÖR HELA SVERIGE"), "testBuyTicket test case was PASS.", "Did not go to the tab Start tab");
        return true;
    }

    /**
     *
     * Will confirm a reservation of selected seats.
     *
     * @param numberOfSeats Number of seats to be booked.
     * @return Was a seat booked?
     */
    public boolean confirmSeatWhenReserving(int numberOfSeats) {
        searchAndReport(searchTextCaseInsensitive("KLAR"), "Klar button was found.", "KLAR button was not found.");
        clickOnTextCaseInsensitive("KLAR");
        loadView(android.widget.ListView.class, 1, DEFAULT_NUMBER_OF_TRIES_WAITING_FOR_VIEW, "Timed out after pressing KLART button.");
        searchAndReport(searchTextCaseInsensitive("Reservera biljetter"), "Found the text Reservera biljetter.", "Was not able to find the text Reservera biljetter.");
        clickOnTextCaseInsensitive("Reservera biljetter");
        solo.sleep(2000);
        if (searchTextCaseInsensitive("Reservera biljetter")) {
            return false;
        }
        if (searchTextCaseInsensitive("Din anslutning tappades, vill du försöka igen?")) {
            loadView(android.widget.Button.class, 1, DEFAULT_NUMBER_OF_TRIES_WAITING_FOR_VIEW, "Timed out while waiting for cinema salon to load.");
            clickOnTextCaseInsensitive("Ja");
            choosePlaceToSit(numberOfSeats);
            clickOnTextCaseInsensitive("KLAR");
            assertTrue("The connection was broken when trying to reserve a ticket.", searchTextCaseInsensitive("Din anslutning tappades, vill du försöka igen?"));
            clickOnTextCaseInsensitive("Reservera biljetter");
            solo.sleep(2000);
        }
        return true;
    }

    /**
     * Handles the first screen when reserving tickets, filling in email and phone number and clicking klar.
     */
    public void firstReservTicketScreenOperations() {
        loadView(android.widget.TextView.class, 5, DEFAULT_NUMBER_OF_TRIES_WAITING_FOR_VIEW, "Failed to load the screen for reserving ticket.");
        searchAndReport(searchTextCaseInsensitive("Uthämtning"), "Confirmed being on Uthämtning page.", "Could not find Uthämtning.");
        solo.clearEditText(0);
        solo.clearEditText(1);
        long Number = (long) Math.floor(Math.random() * 900000000L) + 100000000L;
        String phoneNumber = "0" + String.valueOf(Number);
        solo.enterText(0, phoneNumber);
        solo.enterText(1, USER_EMAIL_ADDRESS);
        searchAndReport(searchTextCaseInsensitive(phoneNumber), "Confimed that phone number is correctly typed in.", "The correct phone number was not found.");
        searchAndReport(searchTextCaseInsensitive(USER_EMAIL_ADDRESS), "Confimed that email adress is correctly typed in.", "The correct email adress was not found.");
        logging("Will now attempt to press the RESERVERA button");
        doesItemExist("se.sfbio.mobile.android:id/done_red_button");
        solo.clickOnView(solo.getView("se.sfbio.mobile.android:id/done_red_button", 0));
    }

    public void abortReservationAtConfirmationScreen() {
        if (!solo.waitForView(android.widget.Button.class, 2, timeout)) {
            reportError("Did not load the confirm reservation of ticket screen.");
        }
        searchAndReport(searchTextCaseInsensitive("Vill du boka"), "Application went to the confirmation of reservation screen.", "Was not able to confirm the reserved ticket.");
        searchAndReport(searchTextCaseInsensitive("Avbryt"), "Found the Avbryt button at the ticket reservation confirmation screen", "Did not find the Avbryt button at the ticket reservation confirmation screen");
        solo.sleep(2000);
        clickOnTextCaseInsensitive("Avbryt");
    }

    /**
     * Checks if tickets were booked successfully.
     */
    public void confirmReservationAndCheckItIsDone() {
        if (!solo.waitForView(android.widget.Button.class, 2, timeout)) {
            reportError("Did not load the confirm reservation of ticket screen.");
        }
        solo.sleep(2000);
        clickOnTextCaseInsensitive("Ja, tack");
        solo.sleep(2000);
        searchAndReport(searchTextCaseInsensitive("Dela"), "Reservation was successfully made.", "The reservation was not successfull.");
        loadView(android.widget.Button.class, 2, DEFAULT_NUMBER_OF_TRIES_WAITING_FOR_VIEW, "Timed out while waiting for confirmation that the reservation was successfull.");
//		doesItemExist("se.sfbio.mobile.android:id/button2");
//		solo.clickOnView(solo.getView("se.sfbio.mobile.android:id/button2", 0));
        if (!searchTextCaseInsensitive("Stäng", true)) {
            reportError("Was not able to confirm that purchase was successfull.");
        }
        clickOnTextCaseInsensitive("Stäng");
    }

    /**
     * Will simulate a click on the DONE button (soft keyboard) which might be needed for the app to accept some input data.
     */
    public void ImeDone() //throws Exception
    {
        //Grab a reference to your EditText.  This code grabs the first Edit Text in the Activity
        //Alternatively, you can get the EditText by resource id or using a method like getCurrentEditTexts()
        //Make sure it's final, we'll need it in a nested block of code.
        final EditText editText = solo.getEditText(0);

        //Create a runnable which triggers the onEditorAction callback
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                editText.onEditorAction(EditorInfo.IME_ACTION_DONE);
            }
        };

        //Use Solo to get the current activity, and pass our runnable to the UI thread.
        solo.getCurrentActivity().runOnUiThread(runnable);
    }


}

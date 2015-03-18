package se.sfbio.mobile.android.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import se.sfbio.mobile.services.domain.json.Auditorium;

/**
 * Holds all data needed by the test routines in order to confirm the contents of the SF Bio app.
 * This class reads the data from the database using SFBioRESTClient class, and provides this data to the testcase classes.
 *
 * @author lemor1
 */
public class SFBioTestData {

    /**
     * Set this to true if all cities should be regarded as "needed" if no other info is given.
     * If it is false, DEFAULT_CITIES array will be used if no other info is given.
     */
    private static final boolean READ_ALL_CITIES_BY_DEFAULT = false;

    /**
     * The 10 latest news headlines will be read.
     */
    private static final int NBR_OF_NEWS_HEADLINES_TO_READ = 10;

    private static final String[] DEFAULT_CITIES = {
            "Stockholm",
            "Malmö",
            "Göteborg"
    };

    /**
     * Class to hold data for cities.
     *
     * @author lemor1
     */
    public class City {
        private String name;
        private String id;
        private ArrayList<String> toplist;
        private ArrayList<String> theatreList;
        private ArrayList<String> currentMovies;
        private ArrayList<String> promotedMovies; // <-- AKA "Carousel banner" list.
        private HashMap<String, String> currentMovieIds;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public ArrayList<String> getToplist() {
            return toplist;
        }

        public void setToplist(ArrayList<String> toplist) {
            this.toplist = toplist;
        }

        public ArrayList<String> getCurrentMovies() {
            return currentMovies;
        }

        public void setCurrentMovies(ArrayList<String> currentMovies) {
            this.currentMovies = currentMovies;
        }

        public HashMap<String, String> getCurrentMovieIds() {
            return currentMovieIds;
        }

        public void setCurrentMovieIds(HashMap<String, String> currentMovieIds) {
            this.currentMovieIds = currentMovieIds;
        }

        public ArrayList<String> getTheatreList() {
            return theatreList;
        }

        public void setTheatreList(ArrayList<String> theatreList) {
            this.theatreList = theatreList;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public ArrayList<String> getPromotedMovies() {
            return promotedMovies;
        }

        public void setPromotedMovies(ArrayList<String> promotedMovies) {
            this.promotedMovies = promotedMovies;
        }

        public City(String name, String id) {
            this.name = name;
            this.id = id;
            toplist = new ArrayList<String>();
            theatreList = new ArrayList<String>();
        }
    }

    private ArrayList<City> cities = null;            // <-- Contains name and id of ALL cities in database.
    //     For the cities that are needed for current test, it also contains additional information.
    private List<String> citiesNeededStr = null;    // <-- The names of the cities needed for the current test.
    private ArrayList<String> newsHeadlines = null;

    /**
     * Default constructor: Use this if it is not known which cities are needed for the test.
     * There are 2 options in this case:
     * 1. READ_ALL_CITIES_BY_DEFAULT set to false: Program should consider the cities in DEFAULT_CITIES array as needed.
     * 2. READ_ALL_CITIES_BY_DEFAULT set to true: Program should consider ALL cities as needed.
     * Option 1 is less time-consuming since less data is read from database and parsed. (saves ~1 second during startup)
     * In case of option 2, citiesNeeded will remain as null here, and it will be handled in initCities().
     */
    public SFBioTestData() {
        if (READ_ALL_CITIES_BY_DEFAULT) {
            this.citiesNeededStr = Arrays.asList(DEFAULT_CITIES);
        }
    }

    /**
     * Use this if it is know which cities are needed for the test.
     *
     * @param citiesNeeded The cities specified here will be fetched from the database, nothing else.
     */
    public SFBioTestData(String... citiesNeededStr) {
        this(Arrays.asList(citiesNeededStr));
    }

    /**
     * Use this if it is known which cities are needed for the test.
     *
     * @param citiesNeeded The cities specified here will be fetched from the database, nothing else.
     */
    public SFBioTestData(List<String> citiesNeededStr) {
        this.citiesNeededStr = citiesNeededStr;
    }

    /**
     * Initializes all data. Syncs the data structures in this class with the data in the database.
     * NOTE that before this method has been called, the data held by this class is NOT VALID.
     *
     * @throws IOException if any problems happen while reading from database.
     */
    public void initData() throws IOException {

        // Read all cities from database. This only reads the "name" and "id" of the cities.
        //initCities();

        // Read the toplist for every city that is considered needed.
        //initToplists();

        // Read the theatre list for every city that is considered needed.
        //initTheatreLists();

        // Read the news headlines from the database.
        //initNewsHeadlines();

        // Read the list of promoted movies for every city that is considered needed.
        //initPromotedMovies();

        // Read the list of current movies for every city that is considered needed.
       // initCurrentMovies();
    }

    /**
     * Gets a list of Strings containing the names of all cities that are considered needed.
     *
     * @return A list of Strings containing the names of all cities that are considered needed.
     * @throws IOException If data was not initialized yet when this method was called.
     */
    public ArrayList<City> getCities() throws IOException {
        if (cities == null || cities.size() == 0 || citiesNeededStr.size() == 0) {
            throw new IOException("SFBioTestData must be initialized before calling getCities().");
        }
        ArrayList<City> citiesNeeded = new ArrayList<City>();
        for (City city : cities) {
            if (citiesNeededStr.contains(city.getName())) {
                citiesNeeded.add(city);
            }
        }
        return citiesNeeded;
    }

    /**
     * Returns the Toplist for the given city.
     *
     * @param cityName The name of the city.
     * @return An ArrayList containing the names of the movies in the toplist.
     * @throws IOException If data was not initialized yet when this method was called or if the given city was not found.
     */
    public ArrayList<String> getToplistForCity(String cityName) throws IOException {
        if (cities == null || cities.size() == 0) {
            throw new IOException("SFBioTestData must be initialized before calling getToplistForCity().");
        }
        for (City city : cities) {
            if (city.name.equals(cityName)) {
                return city.toplist;
            }
        }
        throw new IOException("The given city name is not in the list.");
    }

    /**
     * Returns the Theatrelist for the given city.
     *
     * @param cityName The name of the city.
     * @return An ArrayList containing the names of the movies in the toplist.
     * @throws IOException If data was not initialized yet when this method was called or if the given city was not found.
     */
    public ArrayList<String> getTheatreListForCity(String cityName) throws IOException {
        if (cities == null || cities.size() == 0) {
            throw new IOException("SFBioTestData must be initialized before calling getTheatreListForCity().");
        }
        for (City city : cities) {
            if (city.name.equals(cityName)) {
                return city.theatreList;
            }
        }
        throw new IOException("The given city name is not in the list.");
    }

    /**
     * Returns the news headlines.
     *
     * @return An ArrayList containing the headlines of the news articles in the app.
     * @throws IOException If data was not initialized yet when this method was called.
     */
    public ArrayList<String> getNewsHeadlines() throws IOException {
        if (newsHeadlines == null) {
            throw new IOException("SFBioTestData must be initialized before calling getNewsHeadlines().");
        }
        return newsHeadlines;
    }

    /**
     * Returns the price of the specified show.
     * Note that price-related GETs trigger a database query, unlike other GETs in this class. The reason for this is that
     * there are so many shows, that if all shows were read from database at the start of the test, then it would
     * take too long. It is more optimal to make one query for every time the test needs to know the price of a show.
     *
     * @param movieName   The name of the movie.
     * @param city        The relevant city.
     * @param theatreName The name of the theatre that will show the movie.
     * @param date        The date that the show will occur.
     * @return The price of the show.
     * @throws IOException If anything goes wrong during database communication.
     */
    public int getPriceOfShow(String movieName, SFBioTestData.City city, String theatreName, Date date) throws IOException {

        // Find the relevant Show in the database. This will trigger a database query.
        se.sfbio.mobile.services.domain.json.Show show = getShow(movieName, city, theatreName, date);

        // Get price.
        int price = show.getPrice();
        if (price == 0) {
            throw new IOException("0 price retrieved from database. Movie = '" + movieName + "', City = '" + city.name + "', Date = '" + date.toString() + "'. ");
        }

        return price;
    }

    /**
     * Returns the children's price of the specified show.
     * Note that price-related GETs trigger a database query, unlike other GETs in this class. The reason for this is that
     * there are so many shows, that if all shows were read from database at the start of the test, then it would
     * take too long. It is more optimal to make one query for every time the test needs to know the price of a show.
     *
     * @param movieName   The name of the movie.
     * @param city        The relevant city.
     * @param theatreName The name of the theatre that will show the movie.
     * @param date        The date that the show will occur.
     * @return The price of the show.
     * @throws IOException If anything goes wrong during database communication.
     */
    public int getPriceOfShowChild(String movieName, SFBioTestData.City city, String theatreName, Date date) throws IOException {

        // Find the relevant Show in the database. This will trigger a database query.
        se.sfbio.mobile.services.domain.json.Show show = getShow(movieName, city, theatreName, date);

        // Get price.
        return show.getChildPrice();
    }

    /**
     * Returns the senior price (pensionärspris) of the specified show.
     * Note that price-related GETs trigger a database query, unlike other GETs in this class. The reason for this is that
     * there are so many shows, that if all shows were read from database at the start of the test, then it would
     * take too long. It is more optimal to make one query for every time the test needs to know the price of a show.
     *
     * @param movieName   The name of the movie.
     * @param city        The relevant city.
     * @param theatreName The name of the theatre that will show the movie.
     * @param date        The date that the show will occur.
     * @return The price of the show.
     * @throws IOException If anything goes wrong during database communication.
     */
    public int getPriceOfShowSenior(String movieName, SFBioTestData.City city, String theatreName, Date date) throws IOException {
        // Find the relevant Show in the database. This will trigger a database query.
        se.sfbio.mobile.services.domain.json.Show show = getShow(movieName, city, theatreName, date);

        // Get price.
        return show.getSeniorPrice();
    }

    /**
     * Gets the movie ID of the specified movie in the specified city.
     * The movie ID is a string of numbers. It is needed in order to request information about a specific Show.
     * It is not clear whether the ID for a certain movie is always the same in all cities. Since we can't guarantee
     * that this is the case, we will keep a Dictionary of movie IDs for every city.
     *
     * @param city      The relevant city.
     * @param movieName The name of the movie to get the ID from.
     * @return The movie ID as a String.
     * @throws IOException
     */
    public String getMovieId(SFBioTestData.City city, String movieName) throws IOException {
        String movieId = city.currentMovieIds.get(movieName);
        if (movieName == null) {
            throw new IOException("No Movie ID exists for Movie = '" + movieName + "' in city = '" + city.name + "'.");
        }
        return movieId;
    }

    /**
     * Reads the toplist for the given city from the database and parses it into an ArrayList.
     * To be called privately when initializing data.
     *
     * @param city The City to fetch the toplist for.
     * @return A list containing the names of all of the movies in the toplist for the given city.
     * @throws IOException If anything goes wrong when reading from database.
     */
    private void getToplistForCity(City city) throws IOException {

        se.sfbio.mobile.services.domain.json.MovieList toplistMovieList = null;

        // Read list of toplist movies from database, store in SFBio API object.
        try {
            toplistMovieList = SFBioRESTClient.getToplist(city.id);
        } catch (IOException e) {
            throw new IOException("Problem when reading toplist from database, city = " + city.name + ", error: " + e.toString());
        }

        // Store the data in an ArrayList containing only the names of the toplist movies.
        // That is all the info that the test application needs.
        ArrayList<String> toplistMovies = new ArrayList<String>();
        for (se.sfbio.mobile.services.domain.json.Movie toplistMovie : toplistMovieList.getMovies()) {
            toplistMovies.add(
                    toplistMovie.getMovieName().
                            replaceAll("\\(.*\\)", "") // <-- Also remove parenthesis.
            );
        }

        // Store the data in the SFBioTestData.City object.
        city.toplist = toplistMovies;
    }

    /**
     * Reads the theatre list for the given city from the database and parses it into an ArrayList.
     * To be called privately when initializing data.
     *
     * @param city The City to fetch the theatre list for.
     * @return A list containing the names of all of the theatres available in the given city.
     * @throws IOException If anything goes wrong when reading from database.
     */
    private void getTheatreListForCity(City city) throws IOException {

        // Read list of promoted movies from database, store in SFBio API object.
        se.sfbio.mobile.services.domain.json.TheatreList theatreListFromDB = null;
        try {
            theatreListFromDB = SFBioRESTClient.getTheatreList(city.id);
        } catch (IOException e) {
            throw new IOException("Problem when reading theatre list from database: " + e.toString());
        }

        // Store the data in an ArrayList containing only the names of the theatres.
        // That is all the info that the test application needs.
        ArrayList<String> theatreList = new ArrayList<String>();
        for (se.sfbio.mobile.services.domain.json.Theatre theatre : theatreListFromDB.getTheatres()) {
            theatreList.add(
                    theatre.getName()
                            .replaceAll("\\(.*\\)", "") // <-- Also remove parenthesis.
            );
        }
        city.theatreList = theatreList;
    }

    /**
     * Reads the list of promoted movies for the given city from database and parses it into an ArrayList.
     * To be called privately when initializing data.
     *
     * @param city The City to fetch the Promoted movies list for.
     * @return A list containing the names of all of the promoted movies for the given city.
     * @throws IOException If anything goes wrong when reading from database.
     */
    private void getPromotedMoviesListForCity(City city) throws IOException {

        // Read list of promoted movies from database, store in SFBio API object.
        se.sfbio.mobile.services.domain.json.MovieList promotedMoviesMovieList = null;
        try {
            promotedMoviesMovieList = SFBioRESTClient.getPromotedMovies(city.id);
        } catch (IOException e) {
            throw new IOException("Problem when reading promoted movie list from database, city = " + city.name + ", error: " + e.toString());
        }

        // Store the data in an ArrayList containing only the names of the promoted movies.
        // That is all the info that the test application needs.
        ArrayList<String> promotedMovies = new ArrayList<String>();
        for (se.sfbio.mobile.services.domain.json.Movie promotedMovie : promotedMoviesMovieList.getMovies()) {
            promotedMovies.add(
                    promotedMovie.getMovieName()
                            .replaceAll("\\(.*\\)", "") // <-- Also remove parenthesis.
            );
        }
        city.promotedMovies = promotedMovies;
    }

    /**
     * Reads the list of current movies in the given city from database and parses it into an ArrayList.
     * Also reads the movie IDs of all current movies and puts them in a Dictionary.
     * To be called privately when initializing data.
     *
     * @param city The City to fetch the Current movies list for.
     * @return A list containing the names of all of the current movies in the given city.
     * @throws IOException If anything goes wrong when reading from database.
     */
    private void getCurrentMoviesListForCity(City city) throws IOException {

        // Read list of current movies from database, store in SFBio API object.
        se.sfbio.mobile.services.domain.json.MovieList currentMoviesMovieList = null;
        try {
            currentMoviesMovieList = SFBioRESTClient.getCurrentMovies(city.id);
        } catch (IOException e) {
            throw new IOException("Problem when reading current movie list from database, city = " + city.name + ", error: " + e.toString());
        }

        // Store the movie names in an ArrayList and the prices of movies in a Dictionary.
        // That is all the info that the test application needs.
        ArrayList<String> currentMovies = new ArrayList<String>();
        HashMap<String, String> currentMovieIds = new HashMap<String, String>();
        for (se.sfbio.mobile.services.domain.json.Movie currentMovie : currentMoviesMovieList.getMovies()) {
            String movieName =
                    currentMovie.getMovieName()
                            .replaceAll("\\(.*\\)", ""); // <-- Also remove parenthesis.
            String movieId = currentMovie.getId();
            currentMovies.add(movieName);
            currentMovieIds.put(movieName, movieId);
        }

        city.currentMovies = currentMovies;
        city.setCurrentMovieIds(currentMovieIds);
    }

    /**
     * Reads info about a specific Show from the database, and returns the result.
     * To be called privately when info regarding a Show is needed.
     * This will typically happen during test execution, not at startup.
     *
     * @param movieName   The name of the movie.
     * @param city        The relevant city.
     * @param theatreName The name of the theatre that will show the movie.
     * @param date        The date (time) that the show will occur.
     * @return The show. Null if no matching Show was found - but all went well during database communication.
     * @throws IOException If anything goes wrong during database communication.
     */
    private se.sfbio.mobile.services.domain.json.Show getShow(String movieName, SFBioTestData.City city, String theatreName, Date date) throws IOException {
        // First, get the TicketShow object from the database.
        se.sfbio.mobile.services.domain.json.TicketShow ticketShow = null;
        movieName = "Kingsman: The Secret Service"; //, city, "FILMSTADEN LUND", date
        try {
            ticketShow = SFBioRESTClient.getTicketShow(
                    getMovieId(city, movieName),
                    city.id,
                    new SimpleDateFormat("yyyyMMdd", Locale.GERMANY).format(date)
            );
            if (ticketShow == null) {
                throw new IOException("No data retrieved when reading TicketShow from database.");
            }
        } catch (IOException e) {
            throw new IOException("Problem when reading TicketShow from database: " + e.toString());
        }
        // We now have our TicketShow object.
        // This object contains a List of Auditoriums - all the auditoriums that show the given movie in the given city on the given day.
        // Let's find the correct Theatre that the caller specified.
        se.sfbio.mobile.services.domain.json.Auditorium auditorium = null;
        for (se.sfbio.mobile.services.domain.json.Auditorium auditoriumTmp : ticketShow.getAuditoriums()) {
            if (auditoriumTmp.getName().equals(theatreName)) {
                auditorium = auditoriumTmp;
            }
        }
        if (auditorium == null) {
            return null; // The specified theatre was not found.
        }

        // Now we have the correct Auditorium.
        // Go through all Shows in this Auditorium and check if any Show matches the Date that the caller specified.
        for (se.sfbio.mobile.services.domain.json.Show show : auditorium.getShows()) {
            if (show.getTimeMs().equals(date)) {
                return show;
            }
        }

        // No matching Show was found.
        return null;
    }

    /**
     * Initializes the toplist in all cities that are considered needed.
     * This includes using the REST Client to read data from the database.
     *
     * @throws IOException If anything goes wrong when reading from database.
     */
    private void initToplists() throws IOException {

        // Read the toplist for every city that is regarded as "needed".
        for (City city : cities) {
            if (citiesNeededStr.contains(city.name)) { // Do we need this city for current test? If so, read its info.
                getToplistForCity(city);
            }
        }
    }

    /**
     * Initializes the theatre list for all cities that are considered needed.
     * This includes using the REST Client to read data from the database.
     *
     * @throws IOException If anything goes wrong when reading from database.
     */
    private void initTheatreLists() throws IOException {

        // Read the theatre list for every city that is regarded as "needed".
        for (City city : cities) {
            if (citiesNeededStr.contains(city.name)) { // Do we need this city for current test? If so, read its info.
                getTheatreListForCity(city);
            }
        }
    }

    /**
     * Initializes the list of news headlines.
     * This includes using the REST Client to read data from the database.
     *
     * @throws IOException If anything goes wrong when reading from database.
     */
    private void initNewsHeadlines() throws IOException {

        // Read news headlines from database.
        se.sfbio.mobile.services.domain.json.ArticleList newsArticleList = null;
        try {
            newsArticleList = SFBioRESTClient.getArticleList();
        } catch (IOException e) {
            throw new IOException("IO Error while reading News headlines from database: " + e.toString());
        }

        // Store the data in an ArrayList containing only the titles of the news articles.
        // That is all the info that the test application needs.
        // Also, only store the 10 first headlines in the list.
        newsHeadlines = new ArrayList<String>();
        while (newsHeadlines.size() < NBR_OF_NEWS_HEADLINES_TO_READ) {
            se.sfbio.mobile.services.domain.json.Article article = newsArticleList.getArticles().get(newsHeadlines.size());
            newsHeadlines.add(
                    article.getHeadline()
                            .replaceAll("\\(.*\\)", "")    // <-- Remove parenthesis.
                            .toUpperCase(Locale.GERMANY)    // <-- All upper case, because that's how the app displays the headlines.
            );
        }
    }

    /**
     * Initializes the list of cities.
     * This includes using the REST Client to read data from the database.
     * Important: This method does not read all data for the cities. It only reads the "name" and "id" fields.
     * It reads this info for ALL cities in the database.
     * This info is needed in order to identify a city by its "id", so this method should be called before anything else.
     *
     * @throws IOException If anything goes wrong when reading from database.
     */
    private void initCities() throws IOException {

        // Read cities from database, store data in an SFBio API object.
        // Note the plural in CitiesLists. The object contains 2 lists, "mainCities" and "cities".
        se.sfbio.mobile.services.domain.json.CitiesLists citiesLists = null;
        try {
            citiesLists = SFBioRESTClient.getCitiesLists();
        } catch (IOException e) {
            throw new IOException("IO Error while reading Cities from database: " + e.toString());
        }

        // Go through both lists and initiate the "cities" ArrayList with city name and id for every city in database.
        cities = new ArrayList<City>();
        for (se.sfbio.mobile.services.domain.json.City city : citiesLists.getMainCities()) {
            cities.add(
                    new City(
                            city.getName().replaceAll("\\(.*\\)", ""), // Removing parenthesis just to be sure...
                            city.getId().replaceAll("\\(.*\\)", "")
                    )
            );
        }
        for (se.sfbio.mobile.services.domain.json.City city : citiesLists.getCities()) {
            cities.add(
                    new City(
                            city.getName().replaceAll("\\(.*\\)", ""), // Removing parenthesis just to be sure...
                            city.getId().replaceAll("\\(.*\\)", "")
                    )
            );
        }
    }

    /**
     * Initializes the Promoted movies list for all cities that are considered needed.
     * This includes using the REST Client to read data from the database.
     *
     * @throws IOException If anything goes wrong when reading from database.
     */
    private void initPromotedMovies() throws IOException {

        // Read the promoted movie list for every city that is regarded as "needed".
        for (City city : cities) {
            if (citiesNeededStr.contains(city.name)) { // Do we need this city for current test? If so, read its info.
                getPromotedMoviesListForCity(city);
            }
        }
    }

    /**
     * Initializes the Current movies list for all cities that are considered needed.
     * Also, initializes the movie IDs of the current movies of the needed cities.
     * This includes using the REST Client to read data from the database.
     *
     * @throws IOException If anything goes wrong when reading from database.
     */
    private void initCurrentMovies() throws IOException {

        // Read the current movie list for every city that is regarded as "needed".
        for (City city : cities) {
            if (citiesNeededStr.contains(city.name)) { // Do we need this city for current test? If so, read its info.
                getCurrentMoviesListForCity(city);
            }
        }
    }
}

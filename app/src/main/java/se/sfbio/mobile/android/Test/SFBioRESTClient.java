package se.sfbio.mobile.android.Test;

import java.io.IOException;

/**
 * Apache HTTP framework used for connecting to server.
 */
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.BasicHttpParams;

/**
 * Jackson framework used for JSON parsing. 
 * jar needed: 
 * 	- jackson-all-1.9.11.jar
 */
import org.codehaus.jackson.map.ObjectMapper;

/**
 * Handles all communication with REST server.
 * This class knows nothing about the SFBioTest application. It only knows how to fetch certain objects from the database.
 *
 * @author lemor1
 */
public final class SFBioRESTClient {

    // QUERY-specific info:
    // Example of query: "https://sfbm-acc-v4.cybercomhosting.com/services/4/movies/toplist?cityid=SE";
    private static final String QUERY_START = SFBioTestcase.settingsFile.get(2);
    private static final String QUERY_TOPLIST_EXCEPT_CITY_ID = "movies/toplist?cityid=";
    private static final String QUERY_THEATRE_LIST_EXCEPT_CITY_ID = "theatres/";
    private static final String QUERY_CITIES = "cities";
    private static final String QUERY_NEWS_HEADLINES = "news";
    // Re: News headlines: There is also a "news/top" query which returns 10 headlines, but it does not seem to match the ones in the app.
    private static final String QUERY_PROMOTED_MOVIES_EXCEPT_CITY_ID = "movies/promotedmovies/";
    private static final String QUERY_CURRENT_MOVIES_EXCEPT_CITY_ID = "movies/currentmovies/";
    private static final String QUERY_TICKETSHOW_EXCEPT_PARAMS = "tickets/show?";


    // Info regarding REST client authentication / HTTP headers:
    private static final String USERNAME = SFBioTestcase.settingsFile.get(0);
    //"SFbioAPI";
    private static final String PASSWORD = SFBioTestcase.settingsFile.get(1);
    //"bSF5PFHcR4Z3";
    private static final Header[] HEADERS = {
            new BasicHeader("X-SF-Android-Version", "403"),
            new BasicHeader("Accept", "application/json")
    };

    /**
     * Gets the TheatreList of the given city. This object contains all of the theatres available in the city.
     * To know which cityID matches which city, it is necessary to call the getCitiesLists() method first.
     * For example, Stockholm = "SE".
     *
     * @param cityID
     * @return The TheatreList of the given city
     * @throws IOException
     */
    public static se.sfbio.mobile.services.domain.json.TheatreList getTheatreList(String cityID) throws IOException {
        return (se.sfbio.mobile.services.domain.json.TheatreList) getObjectFromServer(
                se.sfbio.mobile.services.domain.json.TheatreList.class,
                QUERY_START + QUERY_THEATRE_LIST_EXCEPT_CITY_ID + cityID
        );
    }

    /**
     * Gets the MovieList object containing the "Promoted movies" of the given city.
     * The MovieList contains a list of Movie objects. A Movie object contains lots of info about a movie.
     * To know which cityID matches which city, it is necessary to call the getCitiesLists() method first.
     * For example, Stockholm = "SE".
     *
     * @param cityID The cityID of the city for which to fetch the current movies.
     * @return The MovieList object containing the "Promoted movies" of the given city.
     * @throws IOException
     */
    public static se.sfbio.mobile.services.domain.json.MovieList getPromotedMovies(String cityID) throws IOException {
        return (se.sfbio.mobile.services.domain.json.MovieList) getObjectFromServer(
                se.sfbio.mobile.services.domain.json.MovieList.class,
                QUERY_START + QUERY_PROMOTED_MOVIES_EXCEPT_CITY_ID + cityID
        );
    }

    /**
     * Gets the MovieList object containing the "Current movies" of the given city.
     * The MovieList contains a list of Movie objects. A Movie object contains lots of info about a movie.
     * To know which cityID matches which city, it is necessary to call the getCitiesLists() method first.
     * For example, Stockholm = "SE".
     *
     * @param cityID The cityID of the city for which to fetch the current movies.
     * @return The MovieList object containing the "Current movies" of the given city.
     * @throws IOException
     */
    public static se.sfbio.mobile.services.domain.json.MovieList getCurrentMovies(String cityID) throws IOException {
        return (se.sfbio.mobile.services.domain.json.MovieList) getObjectFromServer(
                se.sfbio.mobile.services.domain.json.MovieList.class,
                QUERY_START + QUERY_CURRENT_MOVIES_EXCEPT_CITY_ID + cityID
        );
    }

    /**
     * Gets the MovieList object containing the "Toplist" of the given city.
     * To know which cityID matches which city, it is necessary to call the getCitiesLists() method first.
     * The MovieList contains a list of Movie objects. A Movie object contains lots of info about a movie.
     * For example, Stockholm = "SE".
     *
     * @param cityID The cityID of the city for which to fetch the toplist.
     * @return The MovieList object containing the "Toplist" of the given city.
     * @throws IOException
     */
    public static se.sfbio.mobile.services.domain.json.MovieList getToplist(String cityID) throws IOException {
        return (se.sfbio.mobile.services.domain.json.MovieList) getObjectFromServer(
                se.sfbio.mobile.services.domain.json.MovieList.class,
                QUERY_START + QUERY_TOPLIST_EXCEPT_CITY_ID + cityID
        );
    }

    /**
     * Gets the CitiesLists object. This object contains 2 lists of City objects: mainCities and cities.
     * Together, these lists contain ALL cities in the database.
     * The city.getId() from the City objects retrieved by this method is necessary in order to identify a city by its cityID.
     *
     * @return The CitiesLists object.
     * @throws IOException
     */
    public static se.sfbio.mobile.services.domain.json.CitiesLists getCitiesLists() throws IOException {
        return (se.sfbio.mobile.services.domain.json.CitiesLists) getObjectFromServer(
                se.sfbio.mobile.services.domain.json.CitiesLists.class,
                QUERY_START + QUERY_CITIES
        );
    }

    /**
     * Gets the ArticleList object. This object contains a list of all the news articles in the database.
     *
     * @return The ArticleList object.
     * @throws IOException
     */
    public static se.sfbio.mobile.services.domain.json.ArticleList getArticleList() throws IOException {
        return (se.sfbio.mobile.services.domain.json.ArticleList) getObjectFromServer(
                se.sfbio.mobile.services.domain.json.ArticleList.class,
                QUERY_START + QUERY_NEWS_HEADLINES
        );
    }

    /**
     * Gets a TicketShow object. This object contains information about a specific show.
     *
     * @param movieId The name of the relevant movie. This id must be retrieved first by requesting current movies.
     * @param cityId  The id of the relevant city. 2 letters. Example: cityId of Stockholm is "SE".
     * @param date    The date of the show. Should be in format "YYYYMMDD", for example "20150206".
     * @return The Show object.
     * @throws IOException
     */
    public static se.sfbio.mobile.services.domain.json.TicketShow getTicketShow(String movieId, String cityId, String date) throws IOException {

        // TODO: Find out:
        // What does this query GET?
        // "shows" - Looks like it should GET a list of Shows. And this makes sense:
        // There will be many shows of a given movie in a given city on a given day.
        // BUT: There is no "ShowList" class in the library. What object should we GET? Very unclear.

        // Format of query:
        // https://sfbm-acc-v4.cybercomhosting.com/services/4/tickets/shows?cityid=SE&movieid=10015002&date=20150205
        String query = QUERY_START + QUERY_TICKETSHOW_EXCEPT_PARAMS + "cityid=" + cityId + "&movieid=" + movieId + "&date=" + date;
//		String query = "https://sfbm-acc-v4.cybercomhosting.com/services/4/tickets/shows?cityid=SE&movieid=18070114&date=20150209";
//		return (se.sfbio.mobile.services.domain.json.TicketShow)getObjectFromServer(
//				se.sfbio.mobile.services.domain.json.TicketShow.class, 
//				"https://sfbm-acc-v4.cybercomhosting.com/services/4/tickets/shows?cityid=SE&movieid=18070114&date=20150209"
//		);

        return (se.sfbio.mobile.services.domain.json.TicketShow) getObjectFromServer(
                se.sfbio.mobile.services.domain.json.TicketShow.class, query);
    }

    /**
     * Reads an object of a specific class from the database, using a specific query.
     * Uses org.apache.http framework to open the connection.
     * Uses org.codehaus.jackson framework (ObjectMapper) to retrieve the object from the open stream (JSON parsing).
     *
     * @param classToGet The class to get. See: se.sfbio.mobile.services.domain
     * @param query      The query to use.
     * @return The object retrieved from the database.
     * @throws IOException Throws exception if anything goes wrong at any point.
     */
    private static <T> Object getObjectFromServer(Class<T> classToGet, String query) throws IOException {
        // Execute the GET towards the server using the given query.
        // This gives us a Response from which we can get the data.
        HttpResponse response;
        try {
            response = getResponseFromServer(query);
        } catch (ClientProtocolException e) {
            throw new ClientProtocolException("Issue when executing GET towards server: " + e.toString());
        } catch (IOException e) {
            throw new IOException("Issue when executing GET towards server: " + e.toString());
        }

        if (response == null) {
            throw new IOException("ERROR: No data obtained for query: '" + query + "'.");
        }

        // From the Response, read the appropriate POJO and return it.
        Object result = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            result = mapper.readValue(response.getEntity().getContent(), classToGet);
        } catch (IOException e) {
            throw new IOException("mapper.readValue(): " + e.toString());
        }
        return result;
    }

    /**
     * Executes the specified query towards the server, and returns the HttpResponse that the server answers with.
     * Uses org.apache.http framework.
     *
     * @param query The query to use. This should be the full URL.
     * @return The response from the server.
     * @throws ClientProtocolException
     * @throws IOException
     */
    private static HttpResponse getResponseFromServer(String query) throws ClientProtocolException, IOException {

        // Create HTTP Client using default config parameters.
        DefaultHttpClient httpclient = new DefaultHttpClient(new BasicHttpParams());

        // Set authentication parameters in Client.
        httpclient.getCredentialsProvider().setCredentials(
                AuthScope.ANY,
                new UsernamePasswordCredentials(USERNAME, PASSWORD)
        );

        // Execute query towards server and retrieve the Response.
        HttpResponse response = null;
        HttpGet httpget = new HttpGet(query);
        httpget.setHeaders(HEADERS);
        response = httpclient.execute(httpget);

        return response;
    }
}


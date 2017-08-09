package elrast.com.book;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by iman on 8/1/17.
 */

class ApiUtil {


    private static final String QUERY_PARAMETER_KEY = "q";
    private static final String API_KEY = "AIzaSyDfuwVR9j-WCrate9rcNEDBfeEoJnemvl8";
    private static final String KEY = "key";


    private ApiUtil() {
    }

    ;
    private static final String BASE_API_URL = "https://www.googleapis.com/books/v1/volumes/";

    static URL buildURL(String title) {

        URL url = null;
        Uri uri = Uri.parse(BASE_API_URL).buildUpon()
                .appendQueryParameter(QUERY_PARAMETER_KEY, title)
                // .appendQueryParameter(KEY, API_KEY)
                .build();

        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    static String getJson(URL url) throws IOException {

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        Scanner scanner;

        try {
            InputStream stream = connection.getInputStream();
            scanner = new Scanner(stream);
            scanner.useDelimiter("\\A");
            boolean hasNext = scanner.hasNext();
            if (hasNext) {
                return scanner.next();
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("Error", e.toString());
            return null;
        } finally {
            connection.disconnect();
        }
    }

    public static ArrayList<Book> getBooksFromJson(String json) {
        ArrayList<Book> books = new ArrayList<>();

        final String ID = "id";
        final String TITLE = "title";
        final String SUBTITLE = "subtitle";
        final String AUTHORS = "authors";
        final String PUBLISHER = "publisher";
        final String PUBLISHED_DATE = "publishedDate";
        final String ITEMS = "items";
        final String VOLUME_INFO = "volumeInfo";

        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray arrayBooks = jsonObject.getJSONArray(ITEMS);
            int numberOfBooks = arrayBooks.length();
            for (int i = 0; i < numberOfBooks; i++) {
                JSONObject bookJson = arrayBooks.getJSONObject(i);
                JSONObject volumeInfoJson = bookJson.getJSONObject(VOLUME_INFO);
                int autherNum = volumeInfoJson.getJSONArray(AUTHORS).length();
                String[] authors = new String[autherNum];
                for (int j = 0; j < autherNum; j++) {
                    authors[j] = volumeInfoJson.getJSONArray(AUTHORS).get(j).toString();
                }

                Book book = new Book(bookJson.getString(ID),
                        volumeInfoJson.getString(TITLE),
                        (volumeInfoJson.isNull(SUBTITLE) ? "" : volumeInfoJson.getString(SUBTITLE)),
                        authors,
                        volumeInfoJson.getString(PUBLISHER),
                        volumeInfoJson.getString(PUBLISHED_DATE)
                );
                books.add(book);

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return books;
    }
}

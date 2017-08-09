package elrast.com.book;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
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
}

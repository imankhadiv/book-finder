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
import java.util.Scanner;


class ApiUtil {


    private static final String QUERY_PARAMETER_KEY = "q";
    private static final String API_KEY = "AIzaSyDfuwVR9j-WCrate9rcNEDBfeEoJnemvl8";
    private static final String KEY = "key";
    private static final String PUBLISHER = "inpublisher:";
    private static final String AUTHOR = "inauthor:";
    private static final String TITLE = "intitle:";
    private static final String ISBN = "inisbn:";


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

    static URL buildURL(String title, String author, String publisher, String isbn) {

        URL url = null;
        StringBuilder sb = new StringBuilder();
        if (!title.isEmpty()) sb.append(TITLE).append(title).append("+");
        if (!author.isEmpty()) sb.append(AUTHOR).append(author).append("+");
        if (!publisher.isEmpty()) sb.append(PUBLISHER).append(publisher).append("+");
        if (!isbn.isEmpty()) sb.append(ISBN).append(isbn).append("+");
        sb.setLength(sb.length() - 1);
        String query = sb.toString();
        Uri uri = Uri.parse(BASE_API_URL).buildUpon()
                .appendQueryParameter(QUERY_PARAMETER_KEY, query)
                 //.appendQueryParameter(KEY, API_KEY)
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

    static ArrayList<Book> getBooksFromJson(String json) {
        ArrayList<Book> books = new ArrayList<>();

        final String ID = "id";
        final String TITLE = "title";
        final String SUBTITLE = "subtitle";
        final String AUTHORS = "authors";
        final String PUBLISHER = "publisher";
        final String PUBLISHED_DATE = "publishedDate";
        final String ITEMS = "items";
        final String VOLUME_INFO = "volumeInfo";
        final String DESCRIPTION = "description";
        final String THUMBNAIL = "thumbnail";
        final String IMAGE_LINKS = "imageLinks";

        try {
            JSONObject jsonObject = new JSONObject(json);
//            JSONArray arrayBooks = jsonObject.getJSONArray(ITEMS);
            JSONArray arrayBooks = jsonObject.isNull(ITEMS) ? new JSONArray() : jsonObject.getJSONArray(ITEMS);
            int numberOfBooks = arrayBooks.length();
            for (int i = 0; i < numberOfBooks; i++) {
                JSONObject bookJson = arrayBooks.getJSONObject(i);
                JSONObject volumeInfoJson = bookJson.getJSONObject(VOLUME_INFO);
                int authorNum = 0;
                try {
                    authorNum = volumeInfoJson.getJSONArray(AUTHORS).length();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JSONObject imageLinksObject = null;
                if (volumeInfoJson.has(IMAGE_LINKS)) {
                    imageLinksObject = volumeInfoJson.getJSONObject(IMAGE_LINKS);
                }
                String[] authors = new String[authorNum];
                for (int j = 0; j < authorNum; j++) {
                    authors[j] = volumeInfoJson.getJSONArray(AUTHORS).get(j).toString();
                }

                Book book = new Book(bookJson.getString(ID),
                        volumeInfoJson.getString(TITLE),
                        (volumeInfoJson.isNull(SUBTITLE) ? "" : volumeInfoJson.getString(SUBTITLE)),
                        authors,
                        volumeInfoJson.isNull(PUBLISHER) ? "" : volumeInfoJson.getString(PUBLISHER),
                        volumeInfoJson.isNull(PUBLISHED_DATE) ? "" : volumeInfoJson.getString(PUBLISHED_DATE),
                        volumeInfoJson.isNull(DESCRIPTION) ? "" : volumeInfoJson.getString(DESCRIPTION),
                        (imageLinksObject == null) ? "" : imageLinksObject.getString(THUMBNAIL)

                );
                books.add(book);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return books;
    }
}

package elrast.com.book;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class BookListActivity extends AppCompatActivity {
    public static final String TAG = BookListActivity.class.getSimpleName();
    private ProgressBar progressBar;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_book_list);


        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        LinearLayoutManager bookManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(bookManager);

        Intent intent = getIntent();
        String query = intent.getStringExtra("Query");
        URL bookUrl;


        try {
            if (query == null || query.isEmpty()) {
                bookUrl = ApiUtil.buildURL("cooking");
            } else {
                bookUrl = new URL(query);
            }

            new BookQueryTask().execute(bookUrl);
        } catch (Exception e) {
            e.printStackTrace();


            Log.e("Error", e.toString());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.advanced_search:
                Intent intent = new Intent(this, SearchActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.book_list_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                try {
                    URL url = ApiUtil.buildURL(query);
                    new BookQueryTask().execute(url);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, e.toString());
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return true;

    }

    private class BookQueryTask extends AsyncTask<URL, Void, String> {

        @Override
        protected String doInBackground(URL... params) {

            URL searchUrl = params[0];
            String result = null;

            try {
                result = ApiUtil.getJson(searchUrl);
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("Error", e.getMessage());
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            TextView errorTextView = (TextView) findViewById(R.id.error_text_view);
            if (result == null) {
                recyclerView.setVisibility(View.INVISIBLE);
                errorTextView.setVisibility(View.VISIBLE);
            } else {
                recyclerView.setVisibility(View.VISIBLE);
                errorTextView.setVisibility(View.INVISIBLE);
            }
            progressBar.setVisibility(View.INVISIBLE);

            ArrayList<Book> books = ApiUtil.getBooksFromJson(result);

            BookAdapter bookAdapter = new BookAdapter(books);
            recyclerView.setAdapter(bookAdapter);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }
    }

}

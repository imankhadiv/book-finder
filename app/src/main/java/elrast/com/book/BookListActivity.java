package elrast.com.book;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;
import java.net.URL;

public class BookListActivity extends AppCompatActivity {
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_book_list);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);


        try {
            URL url = ApiUtil.buildURL("cooking");
            new BookQueryTask().execute(url);
        } catch (Exception e) {
            e.printStackTrace();


            Log.e("Error", e.toString());
        }
    }

    public class BookQueryTask extends AsyncTask<URL, Void, String> {

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
            TextView textView = (TextView) findViewById(R.id.tvResponse);
            TextView errorTextView = (TextView) findViewById(R.id.error_text_view);
            if (result == null) {
                textView.setVisibility(View.INVISIBLE);
                errorTextView.setVisibility(View.VISIBLE);
            } else {
                textView.setVisibility(View.VISIBLE);
                errorTextView.setVisibility(View.INVISIBLE);
            }
            progressBar.setVisibility(View.INVISIBLE);
            textView.setText(result);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }
    }
}

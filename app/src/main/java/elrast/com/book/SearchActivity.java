package elrast.com.book;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.net.URL;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        final EditText etTitle = (EditText) findViewById(R.id.etTitle);
        final EditText etAuthor = (EditText) findViewById(R.id.etAuther);
        final EditText etPublisher = (EditText) findViewById(R.id.etPublisher);
        final EditText etIsbn = (EditText) findViewById(R.id.etIsbn);
        final Button btnSearch = (Button) findViewById(R.id.btnSearch);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = etTitle.getText().toString().trim();
                String author = etAuthor.getText().toString().trim();
                String publisher = etPublisher.getText().toString().trim();
                String isbn = etIsbn.getText().toString().trim();

                if (title.isEmpty() && author.isEmpty() && publisher.isEmpty() && isbn.isEmpty()) {
                    Toast.makeText(SearchActivity.this, R.string.no_search_data, Toast.LENGTH_SHORT).show();
                } else {

                    URL queryURL = ApiUtil.buildURL(title, author, publisher, isbn);
                    Intent intent = new Intent(getApplicationContext(), BookListActivity.class);
                    intent.putExtra("Query", queryURL.toString());
                    startActivity(intent);
                }
            }
        });
    }
}

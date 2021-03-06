package elrast.com.book;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import elrast.com.book.databinding.ActivityBookDetailBinding;

public class BookDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        Book book = getIntent().getParcelableExtra("BOOK");

        ActivityBookDetailBinding binding = DataBindingUtil.setContentView(this,
                R.layout.activity_book_detail);
        binding.setBook(book);

    }
}

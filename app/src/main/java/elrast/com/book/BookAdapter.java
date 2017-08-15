package elrast.com.book;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import static android.content.ContentValues.TAG;


public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {

    private List<Book> books;

    public BookAdapter(List<Book> books) {
        this.books = books;
    }

    @Override
    public BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.book_list_item, parent, false);
        return new BookViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(BookViewHolder holder, int position) {
        holder.bindData(books.get(position));
    }

    @Override
    public int getItemCount() {
        return books.size();
    }


    class BookViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView author;
        private final TextView publisher;
        private final TextView publishedDate;
        private final TextView title;

        BookViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title_text_view);
            author = (TextView) itemView.findViewById(R.id.authorr_text_view);
            publisher = (TextView) itemView.findViewById(R.id.publisher_text_view);
            publishedDate = (TextView) itemView.findViewById(R.id.publish_date_text_view);
            itemView.setOnClickListener(this);
        }

        void bindData(Book book) {

            title.setText(book.title);
            // String authors = "";
//            int i = 0;
//            for (String author :
//                    book.authors) {
//                authors += author;
//                i++;
//                if (i < book.authors.length) {
//                    authors += ", ";
//                }
//
//            }

            author.setText(book.authors);
            publisher.setText(book.publisher);
            publishedDate.setText(book.publishedDate);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Log.i(TAG, position + "....." + getLayoutPosition());
            Book book = books.get(position);
            Intent intent = new Intent(v.getContext(), BookDetail.class);
            intent.putExtra("BOOK", book);
            v.getContext().startActivity(intent);
        }
    }
}

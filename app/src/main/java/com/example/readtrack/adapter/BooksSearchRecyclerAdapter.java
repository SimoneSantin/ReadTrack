package com.example.readtrack.adapter;

import android.app.Application;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.readtrack.R;
import com.example.readtrack.model.Book;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BooksSearchRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static final int BOOKS_VIEW_TYPE = 0;
    private static final int LOADING_VIEW_TYPE = 1;
    public interface OnItemClickListener {
        void onBooksItemClick(Book book);
    }

    private final List<Book> bookList;

    private final Application application;
    private final OnItemClickListener onItemClickListener;

    public BooksSearchRecyclerAdapter(List<Book> bookList, Application application, OnItemClickListener onItemClickListener) {
        this.application=application;
        this.bookList = bookList;
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemViewType(int position) {
        if (bookList.get(position) == null) {
            return LOADING_VIEW_TYPE;
        } else {
            return BOOKS_VIEW_TYPE;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;

        if (viewType == BOOKS_VIEW_TYPE) {
            view = LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.book_search_list_item, parent, false);
            return new BooksSearchViewHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.book_loading_item, parent, false);
            return new LoadingBooksViewHolder(view);
        }
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof BooksSearchRecyclerAdapter.BooksSearchViewHolder) {
            ((BooksSearchRecyclerAdapter.BooksSearchViewHolder) holder).bind(bookList.get(position));
        } else if (holder instanceof BooksSearchRecyclerAdapter.LoadingBooksViewHolder) {
            ((BooksSearchRecyclerAdapter.LoadingBooksViewHolder) holder).activate();
        }
    }

    @Override
    public int getItemCount() {
        if (bookList != null) {
            return bookList.size();
        }
        return 0;
    }

    public void clear(){

        this.bookList.clear();

    }
    public class BooksSearchViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final ImageView imageViewThumbnail;
        private final TextView title;
        private final TextView author;

        private final TextView publisher;

        private final TextView date;

        public BooksSearchViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewThumbnail = itemView.findViewById(R.id.image_cover);
            title = itemView.findViewById(R.id.book_title);
            author = itemView.findViewById(R.id.book_author);
            publisher = itemView.findViewById(R.id.book_publisher);
            date = itemView.findViewById(R.id.book_date);
            itemView.setOnClickListener(this);
        }
        public void bind(Book book) {
            try{
                Picasso.get()
                        .load("https"+book.getVolumeInfo().getImageLinks().getThumbnail().substring(4))
                        .into(imageViewThumbnail);
                title.setText(setContent(book.getVolumeInfo().getTitle()));
                author.setText(setContent(book.getVolumeInfo().getAuthors().get(0)));
                publisher.setText(setContent(book.getVolumeInfo().getPublisher()));
                date.setText(setContent(book.getVolumeInfo().getPublishedDate().substring(0,4)));
            }catch(NullPointerException pointerException){
                Log.d("pointer exception", pointerException.toString());
            }
        }
        public String setContent(String content){
            String contentRidotto="";
            final int MAX_CHAR=30;
            if(content.length()>MAX_CHAR){
                contentRidotto=content.substring(0,MAX_CHAR)+"...";
            }else{
                return content;
            }
            return contentRidotto;
        }
        @Override
        public void onClick(View v) {
            onItemClickListener.onBooksItemClick(bookList.get(getAdapterPosition()));
        }
    }

    public static class LoadingBooksViewHolder extends RecyclerView.ViewHolder {
        private final ProgressBar progressBar;

        LoadingBooksViewHolder(View view) {
            super(view);
            progressBar = view.findViewById(R.id.progressbar_loading_books);
        }

        public void activate() {
            progressBar.setIndeterminate(true);
        }
    }
}
package com.example.readtrack.source.books;

import static com.example.readtrack.util.Constants.API_KEY_ERROR;
import static com.example.readtrack.util.Constants.RETROFIT_ERROR;
import static com.example.readtrack.util.Constants.TOP_HEADLINES_PAGE_SIZE_VALUE;

import android.util.Log;

import com.example.readtrack.model.Books;
import com.example.readtrack.model.BooksApiResponse;
import com.example.readtrack.service.BookApiService;
import com.example.readtrack.util.ServiceLocator;

import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BooksDataSource extends BaseBooksSource {

    private final BookApiService bookApiService;
    private final String apiKey;

    public BooksDataSource(String apiKey) {
        this.bookApiService = ServiceLocator.getInstance().getBookApiService();
        this.apiKey = apiKey;
    }

    @Override
    public void searchBooks(String query, int page) {
        Call<BooksApiResponse> call = bookApiService.searchBooks(query, TOP_HEADLINES_PAGE_SIZE_VALUE, page );
        call.enqueue(new Callback<BooksApiResponse>() {

            @Override
            public void onResponse(Call<BooksApiResponse> call, Response<BooksApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("chiamata", "");
                    booksResponseCallback.onSuccessFromRemote(response.body());
                } else {
                    booksResponseCallback.onFailureFromRemote(new Exception(API_KEY_ERROR));
                }
            }

            @Override
            public void onFailure(Call<BooksApiResponse> call, Throwable t) {
                booksResponseCallback.onFailureFromRemote(new Exception(RETROFIT_ERROR));
            }
        });
    }

    @Override
    public void searchBooksById(String id) {
        Call<Books> call = bookApiService.searchBooksById(id);
        call.enqueue(new Callback<Books>() {

            @Override
            public void onResponse(Call<Books> call, Response<Books> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("chiamata id", response.body().getVolumeInfo().getTitle());
                    booksResponseCallback.onSuccessFromRemoteId(new BooksApiResponse(1, "", Arrays.asList(response.body())));
                } else {
                    booksResponseCallback.onFailureFromRemote(new Exception(API_KEY_ERROR));
                }
            }

            @Override
            public void onFailure(Call<Books> call, Throwable t) {
                booksResponseCallback.onFailureFromRemote(new Exception(RETROFIT_ERROR));
            }
        });
    }
}

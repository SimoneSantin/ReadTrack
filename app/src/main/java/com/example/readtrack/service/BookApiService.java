package com.example.readtrack.service;

import com.example.readtrack.model.Books;
import com.example.readtrack.model.BooksApiResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface BookApiService {
    @GET("v1/volumes")
    Call<BooksApiResponse> searchBooks(
            @Query("q") String query,
            @Query("maxResults") int maxResults
    );
    @GET("v1/volumes/{id}")
    Call<Books> searchBooksById(
            @Path("id") String id
    );
}

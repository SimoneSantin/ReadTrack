package com.example.readtrack.repository.books;

import static com.example.readtrack.util.Constants.FAVOURITES_BOOKS;
import static com.example.readtrack.util.Constants.READING_BOOKS;
import static com.example.readtrack.util.Constants.RED_BOOKS;
import static com.example.readtrack.util.Constants.WANT_TO_READ;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.readtrack.model.BooksApiResponse;
import com.example.readtrack.model.Result;
import com.example.readtrack.model.User;
import com.example.readtrack.source.books.BaseBooksSource;
import com.example.readtrack.source.books.BaseFavoriteBooksSource;
import com.example.readtrack.source.books.BaseFinishedBooksSource;
import com.example.readtrack.source.books.BaseReadingBooksSource;
import com.example.readtrack.source.books.BaseSavedBooksSource;
import com.example.readtrack.util.OnFavouriteCheckListener;

import java.util.HashMap;
import java.util.Set;

public class BooksRepository implements BooksResponseCallback {

    private static final String TAG = BooksRepository.class.getSimpleName();

    private  MutableLiveData<Result> apiBooksLiveData;
    private  MutableLiveData<Result> idBooksLiveData;
    private MutableLiveData<Result> favBooksListLiveData;
    private MutableLiveData<Result> readingBooksLiveData;

    private MutableLiveData<Result> finishedBooksLiveData;

    private MutableLiveData<Result> savedBooksLiveData;
    private MutableLiveData<Result> markerLiveData;
    private final BaseBooksSource booksDataSource;
    private final BaseFavoriteBooksSource favoriteBooksSource;
    private final BaseReadingBooksSource readingBooksSource;
    private final BaseSavedBooksSource savedBooksSource;
    private final BaseFinishedBooksSource finishedBooksSource;


    public BooksRepository(BaseBooksSource booksDataSource, BaseFavoriteBooksSource favoriteBooksSource,
                           BaseReadingBooksSource readingBooksSource, BaseSavedBooksSource savedBooksSource, BaseFinishedBooksSource finishedBooksSource) {

        apiBooksLiveData = new MutableLiveData<>();
        idBooksLiveData = new MutableLiveData<>();
        favBooksListLiveData = new MutableLiveData<>();
        readingBooksLiveData = new MutableLiveData<>();
        finishedBooksLiveData = new MutableLiveData<>();
        savedBooksLiveData = new MutableLiveData<>();
        markerLiveData = new MutableLiveData<>();
        this.booksDataSource = booksDataSource;
        this.favoriteBooksSource = favoriteBooksSource;
        this.readingBooksSource = readingBooksSource;
        this.savedBooksSource = savedBooksSource;
        this.finishedBooksSource = finishedBooksSource;
        this.booksDataSource.setBooksCallback(this);
        this.favoriteBooksSource.setBooksCallback(this);
        this.readingBooksSource.setBooksCallback(this);
       /* this.savedBooksSource.setBooksCallback(this);
        this.finishedBooksSource.setBooksCallback(this);*/

    }


    public MutableLiveData<Result> searchBooks(String query, int page) {
        booksDataSource.searchBooks(query, page);
        return apiBooksLiveData;
    }
    public void reset(){
        apiBooksLiveData = new MutableLiveData<Result>();
        idBooksLiveData = new MutableLiveData<Result>();
        markerLiveData = new MutableLiveData<Result>();
    }

    public MutableLiveData<Result> searchBooksById(String id) {
        booksDataSource.searchBooksById(id);
        return idBooksLiveData;
    }

    @Override
    public void onSuccessFromRemote(BooksApiResponse booksApiResponse) {
        Result.BooksResponseSuccess result = new Result.BooksResponseSuccess(booksApiResponse);
        apiBooksLiveData.postValue(result);
    }

    @Override
    public void onSuccessFromRemoteId(BooksApiResponse booksApiResponse) {
        Result.BooksResponseSuccess result = new Result.BooksResponseSuccess(booksApiResponse);
        idBooksLiveData.setValue(result);
    }


    @Override
    public void onFailureFromRemote(Exception exception) {
       Log.d("errore",  exception.getMessage() );
    }

    public MutableLiveData<Result> getUserFavBooks(String idToken){
        favoriteBooksSource.getUserFavBooks(idToken);
        return favBooksListLiveData;
    }

    public MutableLiveData<Result> getUserReadingBooks(String idToken){
        readingBooksSource.getUserReadingBooks(idToken);
        return readingBooksLiveData;
    }

    /*public MutableLiveData<Result> getUserFinishedBooks(String idToken){
        savedBooksSource.getUserFinishedBooks(idToken);
        return finishedBooksLiveData;
    }*/


    /*public MutableLiveData<Result> getUserStartBooks(String idToken){
        savedBooksSource.getUserStartBooks(idToken);
        return savedBooksLiveData;
    }*/

    public MutableLiveData<Result> getMarker(String idBook, String idToken){
        readingBooksSource.getSegnalibro(idBook, idToken);
        return markerLiveData;
    }


    @Override
    public void onSuccessFromRemoteDatabase(HashMap<String,String> booksList, String path) {
        Result.BooksResponseSuccess result = new Result.BooksResponseSuccess(booksList);
        switch (path) {
            case FAVOURITES_BOOKS:
                favBooksListLiveData.postValue(result);
                break;
            case READING_BOOKS:
                readingBooksLiveData.postValue(result);
                break;

            case RED_BOOKS:
                finishedBooksLiveData.postValue(result);
                break;

            case WANT_TO_READ:
                savedBooksLiveData.postValue(result);
                break;
        }
    }

    public void onSuccessFromRemoteMarkReading(HashMap<String, String> booksList) {
        Result.BooksResponseSuccess result = new Result.BooksResponseSuccess(booksList);
        markerLiveData.postValue(result);
    }


    public void isFavouriteBook(String idBook, String idToken, OnFavouriteCheckListener listener){
        favoriteBooksSource.isFavouriteBook(idBook,idToken,listener);
    }


    public void removeFavouriteBook(String idBook, String idToken) {
        favoriteBooksSource.removeFavouriteBook(idBook,idToken);
    }


    public void addFavouriteBook(String idBook, String imageLink, String idToken) {
        favoriteBooksSource.addFavouriteBook(idBook, imageLink, idToken);
    }

    public void updateReadingBook(String idBook, int page, String linkImg, String idToken){
        readingBooksSource.updateReadingBook(idBook,page,linkImg,idToken);
    }


}
package com.example.readtrack.repository.books;

import static com.example.readtrack.util.Constants.FAVOURITES_BOOKS;
import static com.example.readtrack.util.Constants.READING_BOOKS;
import static com.example.readtrack.util.Constants.FINISHED_BOOKS;
import static com.example.readtrack.util.Constants.WANT_TO_READ;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.readtrack.model.Book;
import com.example.readtrack.model.BooksApiResponse;
import com.example.readtrack.model.BooksResponse;
import com.example.readtrack.model.Result;
import com.example.readtrack.source.books.BaseBooksLocalDataSource;
import com.example.readtrack.source.books.BaseBooksSource;
import com.example.readtrack.source.books.BaseFavoriteBooksSource;
import com.example.readtrack.source.books.BaseFinishedBooksSource;
import com.example.readtrack.source.books.BaseReadingBooksSource;
import com.example.readtrack.source.books.BaseSavedBooksSource;
import com.example.readtrack.util.OnCheckListener;

import java.util.List;

public class BooksRepository implements BooksResponseCallback {

    private static final String TAG = BooksRepository.class.getSimpleName();

    private  MutableLiveData<Result> apiBooksLiveData;
    private  MutableLiveData<Result> idBooksLiveData;
    private MutableLiveData<Result> favBooksListLiveData;
    private MutableLiveData<Result> readingBooksLiveData;

    private MutableLiveData<Result> finishedBooksLiveData;

    private MutableLiveData<Result> savedBooksLiveData;
    private MutableLiveData<Result> markerLiveData;
    private MutableLiveData<Result> successWriting;

    private final BaseBooksSource booksDataSource;
    private final BaseFavoriteBooksSource favoriteBooksSource;

    private final BaseBooksLocalDataSource booksLocalDataSource;
    private final BaseReadingBooksSource readingBooksSource;
    private final BaseSavedBooksSource savedBooksSource;
    private final BaseFinishedBooksSource finishedBooksSource;



    public BooksRepository(BaseBooksSource booksDataSource, BaseFavoriteBooksSource favoriteBooksSource,
                           BaseReadingBooksSource readingBooksSource, BaseSavedBooksSource savedBooksSource,
                           BaseFinishedBooksSource finishedBooksSource,  BaseBooksLocalDataSource booksLocalDataSource) {

        apiBooksLiveData = new MutableLiveData<>();
        idBooksLiveData = new MutableLiveData<>();
        favBooksListLiveData = new MutableLiveData<>();
        readingBooksLiveData = new MutableLiveData<>();
        finishedBooksLiveData = new MutableLiveData<>();
        savedBooksLiveData = new MutableLiveData<>();
        markerLiveData = new MutableLiveData<>();
        successWriting = new MutableLiveData<>();
        this.booksDataSource = booksDataSource;
        this.favoriteBooksSource = favoriteBooksSource;
        this.readingBooksSource = readingBooksSource;
        this.savedBooksSource = savedBooksSource;
        this.finishedBooksSource = finishedBooksSource;
        this.booksLocalDataSource = booksLocalDataSource;
        this.booksDataSource.setBooksCallback(this);
        this.favoriteBooksSource.setBooksCallback(this);
        this.readingBooksSource.setBooksCallback(this);
        this.savedBooksSource.setBooksCallback(this);
        this.finishedBooksSource.setBooksCallback(this);
        this.booksLocalDataSource.setBooksCallback(this);
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

    public MutableLiveData<Result> getUserFavBooks(String idToken) {
        favoriteBooksSource.getUserFavBooks(idToken);
        return favBooksListLiveData;
    }
    public MutableLiveData<Result> getUserReadingBooks(String idToken, boolean isConnected){
        if(isConnected)
            readingBooksSource.getUserReadingBooks(idToken);
        else
            booksLocalDataSource.getBooks();
        return readingBooksLiveData;
    }
    public MutableLiveData<Result> getUserFinishedBooks(String idToken){
        finishedBooksSource.getUserFinishedBooks(idToken);
        return finishedBooksLiveData;
    }
    public MutableLiveData<Result> getUserSavedBooks(String idToken){
        savedBooksSource.getUserSavedBooks(idToken);
        return savedBooksLiveData;
    }
    public MutableLiveData<Result> getMarker(String idBook, String idToken){
        readingBooksSource.getSegnalibro(idBook, idToken);
        return markerLiveData;
    }

    public void isFavouriteBook(String idBook, String idToken, OnCheckListener listener){
        favoriteBooksSource.isFavouriteBook(idBook,idToken,listener);
    }
    public void isSavedBook(String idBook, String idToken, OnCheckListener listener){
        savedBooksSource.isSavedBook(idBook,idToken,listener);
    }
    public void isFinishedBook(String idBook, String idToken, OnCheckListener listener){
        finishedBooksSource.isFinishedBook(idBook,idToken,listener);
    }

    public void isReadingBook(String idBook, String idToken, OnCheckListener listener){
        readingBooksSource.isReadingBook(idBook,idToken,listener);
    }

    public void removeFavouriteBook(String idBook, String idToken) {
        favoriteBooksSource.removeFavouriteBook(idBook,idToken);
    }
    public void removeSavedBook(String idBook, String idToken) {
        savedBooksSource.removeSavedBook(idBook, idToken);
    }
    public void removeFinishedBook(String idBook, String idToken) {
        finishedBooksSource.removeUserFinishedBook(idBook,idToken);
    }

    public void removeReadingBook(String idBook, String idToken) {
        readingBooksSource.removeReadingBook(idBook,idToken);
    }


    public void addFavouriteBook(String idBook, String imageLink, String idToken) {
        favoriteBooksSource.addFavouriteBook(idBook, imageLink, idToken);
    }
    public void addSavedBook(String idBook, String imageLink, String title, String idToken) {
        savedBooksSource.addSavedBook(idBook, imageLink, title, idToken);
    }
    public void addFinishedBook(String idBook, String imageLink, String idToken) {
        finishedBooksSource.addUserFinishedBook(idBook, imageLink, idToken);
    }
    public MutableLiveData<Result> updateReadingBook(String idBook, int page, String linkImg, String title, int numPages,  String idToken){
        readingBooksSource.updateReadingBook(idBook,page,linkImg,title,numPages,idToken);
        return successWriting;
    }

    @Override
    public void onSuccessFromRemoteDatabase(List<Book> bookList, String path) {
        Result.BooksResponseSuccess result = new Result.BooksResponseSuccess(new BooksResponse(bookList));
        switch (path) {
            case FAVOURITES_BOOKS:
                favBooksListLiveData.postValue(result);
                break;

            case READING_BOOKS:
                readingBooksLiveData.postValue(result);
                break;

            case FINISHED_BOOKS:
                finishedBooksLiveData.postValue(result);
                break;

            case WANT_TO_READ:
                savedBooksLiveData.postValue(result);
                break;
        }
    }
    public void onSuccessFromRemoteMarkReading(List<Book> bookList) {
        Result.BooksResponseSuccess result = new Result.BooksResponseSuccess(new BooksResponse(bookList));
        markerLiveData.postValue(result);
    }

    @Override
    public void onSuccessFromLocal(BooksResponse booksResponse) {
        Result.BooksResponseSuccess result = new Result.BooksResponseSuccess(booksResponse);
        readingBooksLiveData.postValue(result);
    }

    @Override
    public void onSuccessFromDeletion(Book book) {
        booksLocalDataSource.delete(book);
    }

    @Override
    public void onSuccessFromRemoteWriting(List<Book> bookList) {
        Result.BooksResponseSuccess result = new Result.BooksResponseSuccess(new BooksResponse(bookList));
        successWriting.postValue(result);
        booksLocalDataSource.insertBook(new BooksResponse(bookList));
    }

}

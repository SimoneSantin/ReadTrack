package com.example.readtrack.util;

import android.app.Application;

import com.example.readtrack.database.BookRoomDatabase;
import com.example.readtrack.repository.books.BooksRepository;
import com.example.readtrack.repository.user.IUserRepository;
import com.example.readtrack.repository.user.UserRepository;
import com.example.readtrack.service.BookApiService;
import com.example.readtrack.source.books.BaseBooksSource;
import com.example.readtrack.source.books.BooksDataSource;
import com.example.readtrack.source.books.BooksLocalDataSource;
import com.example.readtrack.source.books.FavoriteBooksSource;
import com.example.readtrack.source.books.FinishedBooksSource;
import com.example.readtrack.source.books.ReadingBooksSource;
import com.example.readtrack.source.books.SavedBooksSource;
import com.example.readtrack.source.user.BaseUserAuthenticationRemoteDataSource;
import com.example.readtrack.source.user.BaseUserDataRemoteDataSource;
import com.example.readtrack.source.user.UserAuthenticationRemoteDataSource;
import com.example.readtrack.source.user.UserDataRemoteDataSource;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceLocator {
    private static volatile ServiceLocator INSTANCE = null;

    private ServiceLocator() {}

    public static ServiceLocator getInstance() {
        if (INSTANCE == null) {
            synchronized(ServiceLocator.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ServiceLocator();
                }
            }
        }
        return INSTANCE;
    }


    public BookApiService getBookApiService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BOOKS_API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(BookApiService.class);
    }


    public BookRoomDatabase getBooksDao(Application application) {
        return BookRoomDatabase.getDatabase(application);
    }

    public BooksRepository getBookRepository(Application application) {
        BaseBooksSource booksDataSource;
        FavoriteBooksSource favoriteBooksSource;
        ReadingBooksSource readingBooksSource;
        FinishedBooksSource finishedBooksSource;
        SavedBooksSource savedBooksSource;
        BooksLocalDataSource booksLocalDataSource;
        booksDataSource = new BooksDataSource(Constants.BOOKS_API_BASE_URL);
        DataEncryptionUtil dataEncryptionUtil = new DataEncryptionUtil(application);
        favoriteBooksSource = new FavoriteBooksSource();
        readingBooksSource = new ReadingBooksSource();
        finishedBooksSource = new FinishedBooksSource();
        savedBooksSource = new SavedBooksSource();
        booksLocalDataSource = new BooksLocalDataSource(getBooksDao(application),dataEncryptionUtil);
        return new BooksRepository(booksDataSource, favoriteBooksSource, readingBooksSource, savedBooksSource, finishedBooksSource, booksLocalDataSource);
    }
    public IUserRepository getUserRepository(Application application) {
        BaseUserAuthenticationRemoteDataSource userRemoteAuthenticationDataSource =
                new UserAuthenticationRemoteDataSource();

        BaseUserDataRemoteDataSource userDataRemoteDataSource =
                new UserDataRemoteDataSource();


        return (IUserRepository) new UserRepository(userRemoteAuthenticationDataSource,
                userDataRemoteDataSource);
    }
}

package com.example.readtrack.source.user;

import com.example.readtrack.model.User;
import com.example.readtrack.repository.user.UserResponseCallback;
import com.example.readtrack.util.OnFavouriteCheckListener;

public abstract class BaseUserDataRemoteDataSource {
    protected UserResponseCallback userResponseCallback;

    public void setUserResponseCallback(UserResponseCallback userResponseCallback) {
        this.userResponseCallback = userResponseCallback;
    }
    public abstract void saveUserData(User user);
    public abstract void getUserFavoriteBooks(String idToken);

    public abstract void isFavouriteBook(String idBook, String idToken, OnFavouriteCheckListener listener);
    public abstract void removeFavouriteBook(String idBook, String idToken);
    public abstract void addFavouriteBook(String idBook, String imageLink, String idToken);

}
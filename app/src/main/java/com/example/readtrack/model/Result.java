package com.example.readtrack.model;

public abstract class Result {
    private Result() {}

    public boolean isSuccess() {
        if (this instanceof BooksResponseSuccess || this instanceof UserResponseSuccess) {
            return true;
        } else {
            return false;
        }
    }

    public static final class BooksResponseSuccess extends Result {
        private BooksApiResponse booksResponse;
        public BooksResponseSuccess(BooksApiResponse booksResponse) {
            this.booksResponse = booksResponse;
        }
        public BooksApiResponse getData() {
            return booksResponse;
        }
    }

    public static final class UserResponseSuccess extends Result {
        private final User user;
        public UserResponseSuccess(User user) {
            this.user = user;
        }
        public User getData() {
            return user;
        }
    }
    public static final class Error extends Result {
        private final String message;
        public Error(String message) {
            this.message = message;
        }
        public String getMessage() {
            return message;
        }
    }
}

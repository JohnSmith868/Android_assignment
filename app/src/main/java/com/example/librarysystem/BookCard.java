package com.example.librarysystem;

public class BookCard {
    String title;
    String author;
    int bookid;

    public int getBookid() {
        return bookid;
    }

    public void setBookid(int bookid) {
        this.bookid = bookid;
    }

    public BookCard() {
    }

    public BookCard(String title, String author, int bookid) {
        this.title = title;
        this.author = author;
        this.bookid = bookid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}

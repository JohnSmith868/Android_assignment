package com.example.librarysystem;

public class BookCard {
    String title;
    String author;
    int bookid;
    String isbn;

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public int getBookid() {
        return bookid;
    }

    public void setBookid(int bookid) {
        this.bookid = bookid;
    }

    public BookCard() {
    }

    public BookCard(String title, String author, int bookid, String isbn) {
        this.title = title;
        this.author = author;
        this.bookid = bookid;
        this.isbn = isbn;
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

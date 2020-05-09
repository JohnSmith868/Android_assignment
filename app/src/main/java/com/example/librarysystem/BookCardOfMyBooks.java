package com.example.librarysystem;

public class BookCardOfMyBooks {
    String title;
    String author;
    int appointmentid;
    String date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getAppointmentid() {
        return appointmentid;
    }

    public void setAppointmentid(int appointmentid) {
        this.appointmentid = appointmentid;
    }

    public BookCardOfMyBooks() {
    }

    public BookCardOfMyBooks(String title, String author, int appointmentid, String date) {
        this.title = title;
        this.author = author;
        this.appointmentid = appointmentid;
        this.date = date;
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

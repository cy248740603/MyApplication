package com.example.cy.myapplication;

/**
 * Created by CY on 2017/8/11.
 */

public class Book {
    private int id;
    private String author;
    private double price;
    private int pages;
    private String name;
    private int imageId;

    public Book(String name,int imageId){
        this.name = name;
        this.imageId = imageId;
    }
    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

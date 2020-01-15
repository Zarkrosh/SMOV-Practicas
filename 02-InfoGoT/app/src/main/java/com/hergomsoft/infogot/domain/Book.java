package com.hergomsoft.infogot.domain;



public class Book {

    private Integer id;
    private String name;
    private Integer npages;
    private String released;
    private String author;

    //TODO cosnstructor: vacio, estandar, basado en cursor o varios a la vez
    public Book(){}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getNpages() {
        return npages;
    }

    public void setNpages(Integer npages) {
        this.npages = npages;
    }

    public String getReleased() {
        return released;
    }

    public void setReleased(String released) {
        this.released = released;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}

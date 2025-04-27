package com.example.cookey;

public class CountryModel {
    private long id;
    private String name;
    private int flagResource;

    public CountryModel(long id, String name, int flagResource) {
        this.id = id;
        this.name = name;
        this.flagResource = flagResource;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getFlagResource() {
        return flagResource;
    }
}

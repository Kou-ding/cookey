package com.example.cookey;

public class CountryModel {
    private long id;
    private String name;
    private String code;
    private int flagResource;

    public CountryModel(long id, String name, String code, int flagResource) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.flagResource = flagResource;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    public String getCode() { return code; }
    public int getFlagResource() {
        return flagResource;
    }
}

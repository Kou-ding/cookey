package com.example.cookey;

public class IngredientModel {
    private long id;
    private String name;
    private String unit;

    public IngredientModel(long id, String name, String unit) {
        this.id = id;
        this.name = name;
        this.unit = unit;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUnit() {
        return unit;
    }
}

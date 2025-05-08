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

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}

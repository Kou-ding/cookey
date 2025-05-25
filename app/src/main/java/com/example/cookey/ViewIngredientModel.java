package com.example.cookey;
    public class ViewIngredientModel {
        private final String name;
        private final String unit;
        private final float quantity;

        public ViewIngredientModel(String name, String unit, float quantity) {
            this.name = name;
            this.unit = unit;
            this.quantity = quantity;
        }

        public String getName() {
            return name;
        }

        public String getUnit() {
            return unit;
        }

        public float getQuantity() {
            return quantity;
        }
    }

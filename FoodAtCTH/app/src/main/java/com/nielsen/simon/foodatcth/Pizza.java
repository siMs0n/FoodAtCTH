package com.nielsen.simon.foodatcth;

/**
 * Created by Simon on 2015-07-20.
 */
public class Pizza {

    private int nr;
    private String name;
    private String ingredients;
    private int price;
    private int groupNr;

    public Pizza(){

    }

    public Pizza(int nr, String name, String ingredients, int price, int groupNr){
        this.nr = nr;
        this.name = name;
        this.ingredients = ingredients;
        this.price = price;
        this.groupNr = groupNr;
    }

    public int getNr() {
        return nr;
    }

    public String getName() {
        return name;
    }

    public String getIngredients() {
        return ingredients;
    }

    public int getPrice() {
        return price;
    }

    public int getGroupNr() {
        return groupNr;
    }
}

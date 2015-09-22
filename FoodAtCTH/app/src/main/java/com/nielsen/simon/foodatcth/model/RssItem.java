package com.nielsen.simon.foodatcth.model;

/**
 * Created by Simon on 2015-06-03.
 */
public class RssItem {
    private String title;
    private String description;

    public RssItem(String title, String description){
        this.title = title;
        this.description = description;
    }

    public String getTitle(){
        return title;
    }

    public String getDescription() {
        return description;
    }
}

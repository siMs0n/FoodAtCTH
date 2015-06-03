package com.nielsen.simon.foodatcth;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Simon on 2015-06-03.
 */
public class RssReader {

    private String rssUrl;
    private String errorMessage = "Couldn't get menu for you.";
    private ArrayList<RssItem> rssItems;

    public RssReader(String rssUrl){
        this.rssUrl = rssUrl;
    }

    public void readRss() throws IOException{
        InputStream inputStream = null;
        try {
            URL url = new URL(rssUrl);
            URLConnection connection = url.openConnection();
            inputStream = connection.getInputStream();
        }catch (MalformedURLException m){
            throw new IOException(errorMessage);
        }catch (IOException ioException){
            throw new IOException(errorMessage);
        }finally {
            if(inputStream!=null) {
                try {
                    inputStream.close();
                } catch (IOException e) {

                }
            }
        }

    }

    public List<RssItem> getItems(){
        return rssItems;
    }

}

package com.nielsen.simon.foodatcth;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Simon on 2015-06-03.
 */
public class RssReader {

    private String rssUrl;
    private String errorMessage = "Couldn't get menu for you.";
    private List<RssItem> rssItems;

    public RssReader(String rssUrl){
        this.rssUrl = rssUrl;
    }

    public void readRss() throws IOException{
        InputStream inputStream = null;
        try {
            inputStream = downloadUrl(rssUrl);
            RssParser rssParser = new RssParser();
            rssItems = rssParser.parse(inputStream);
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

    // Given a string representation of a URL, sets up a connection and gets
// an input stream.
    private InputStream downloadUrl(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000 /* milliseconds */);
        conn.setConnectTimeout(15000 /* milliseconds */);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        // Starts the query
        conn.connect();
        return conn.getInputStream();
    }

}

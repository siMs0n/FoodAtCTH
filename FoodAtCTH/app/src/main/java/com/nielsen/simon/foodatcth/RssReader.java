package com.nielsen.simon.foodatcth;

import android.util.Log;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by Simon on 2015-06-03.
 */
public class RssReader {

    private String rssUrl;
    private List<RssItem> rssItems;

    public RssReader(String rssUrl){
        this.rssUrl = rssUrl;
    }

    public List<RssItem> readRss() throws IOException{
        try {
            InputStream inputStream = downloadUrl(rssUrl);
            RssParser rssParser = new RssParser();
            rssItems = rssParser.parse(inputStream);
        }catch (IOException ioException){
            throw new IOException();
        }catch (XmlPullParserException e){
            throw new IOException();
        }
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

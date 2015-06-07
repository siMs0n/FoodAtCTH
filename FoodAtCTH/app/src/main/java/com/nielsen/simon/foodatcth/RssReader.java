package com.nielsen.simon.foodatcth;

import android.util.Log;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Simon on 2015-06-03.
 */
public class RssReader {

    private String[] rssUrls, days;
    private List<RssItem> rssItems;

    public RssReader(String[] rssUrls, String days[]) {
        this.rssUrls = rssUrls;
        this.days = days;
        rssItems = new ArrayList<>();
    }

    public List<RssItem> readRss() throws IOException {
        try {
            RssParser rssParser = new RssParser();
            for (int i = 0; i < rssUrls.length; i++) {
                this.rssItems.add(new RssItem(days[i], ""));
                InputStream inputStream = downloadUrl(rssUrls[i]);
                rssItems.addAll(rssParser.parse(inputStream));
            }
        } catch (IOException ioException) {
            throw new IOException();
        } catch (XmlPullParserException e) {
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

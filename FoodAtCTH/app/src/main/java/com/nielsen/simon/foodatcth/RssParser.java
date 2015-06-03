package com.nielsen.simon.foodatcth;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;
import java.util.List;

/**
 * Created by Simon on 2015-06-03.
 */
public class RssParser {

    public List<RssItem> parse(InputStream inputStream){
        XmlPullParser parser = Xml.newPullParser();
        return readFeed(parser);
    }

    public List<RssItem> readFeed(XmlPullParser parser){
       return null;
    }
}

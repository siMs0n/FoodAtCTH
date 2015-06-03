package com.nielsen.simon.foodatcth;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by Simon on 2015-06-03.
 */
public class RssParser {

    public List<RssItem> parse(InputStream inputStream) throws IOException, XmlPullParserException {
        try{
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(inputStream, null);
            return readFeed(parser);
        }finally {
            inputStream.close();
        }
    }

    private List<RssItem> readFeed(XmlPullParser parser){
       return null;
    }
}

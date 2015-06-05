package com.nielsen.simon.foodatcth;

import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Simon on 2015-06-03.
 */
public class RssParser {

    List<RssItem> rssItems = new ArrayList<>();

    private static final String ns = null;

    public List<RssItem> parse(InputStream inputStream) throws IOException, XmlPullParserException {
        try{
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(inputStream, null);
            parser.nextTag(); //Skip to the menu items
            parser.nextTag(); //Our items is inside this tag
            return readFeed(parser);
        }finally {
            inputStream.close();
        }
    }

    private List<RssItem> readFeed(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "channel");
        while(parser.next()!= XmlPullParser.END_TAG) {
            if(parser.getEventType() != XmlPullParser.START_TAG){
                continue;
            }
            String name = parser.getName();
            if (name.equals("item")) {
                rssItems.add(readMenuItem(parser));
            }else{
                skip(parser);
            }
        }
        return rssItems;
    }

    private RssItem readMenuItem(XmlPullParser parser) throws IOException, XmlPullParserException {
        String title = "Empty item";
        String description = "Empty description";

        while(parser.next()!= XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String name = parser.getName();
            if(name.equals("title")){
                title = readText(parser);
            }else if(name.equals("description")){
                description = readText(parser);
            }else{
                skip(parser);
            }
        }
        return new RssItem(title, description);
    }

    // For the tags title and description, extracts their text values.
    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            String[] tmp = parser.getText().split("@"); //Removes the '@' bit
            if(tmp.length>0){
                result = tmp[0];
            }else if(parser.getText().equals("@")){
                result = "";
            }else{
                result = parser.getText();
            }
            parser.nextTag();
        }
        return result;
    }

    /**
     * Skips the tag the parser currently is at
     */
    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }
}

package com.nielsen.simon.foodatcth;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Simon on 2015-07-20.
 */
public class Message {
    public static void simpleMessage(Context context, String message){
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}

package com.arris.prodconfig;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.StringReader;
import java.util.Objects;


public class prodConfig {
    private static final String LOG_TAG = "prodConfig";

    public static String ParseAndGetProdConfigData(String strTagNameToRead)
    {
        String retVal = null;
        try {
            BufferedReader reader = new BufferedReader(
                    new FileReader("/vendor/etc/tlv/tlv00b0"));
            String line;
            while ((line = reader.readLine()) != null) {
                String value = ParseTlvContent(line, strTagNameToRead);
                if (Objects.equals(value, "N/A")) {
                    continue;
                }
                retVal = value;

            }
            reader.close();
        } catch (java.io.IOException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
        }
        return retVal;
    }


    private static String ParseTlvContent(String str, String strTagNameToRead)
    {
        String strRet = "N/A";
        try {
            boolean bGetData = false;
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput( new StringReader (str ));
            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if(eventType == XmlPullParser.START_DOCUMENT) {
                    Log.i(LOG_TAG,"Start document");
                } else if(eventType == XmlPullParser.START_TAG) {
                    Log.i(LOG_TAG,"Start tag "+xpp.getName());
                    if(xpp.getName().equals(strTagNameToRead)){
                        bGetData = true;
                        Log.i(LOG_TAG,"Found the tag: " + strTagNameToRead);
                    }
                } else if(eventType == XmlPullParser.END_TAG) {
                    Log.i(LOG_TAG,"End tag " + xpp.getName());
                } else if(eventType == XmlPullParser.TEXT) {
                    if(bGetData) {
                        strRet = xpp.getText();
                        Log.i(LOG_TAG,"Text " + xpp.getText());
                        break;
                    }
                }
                eventType = xpp.next();
            }
            Log.i(LOG_TAG,"End document");
        }        catch(Exception e){
            Log.i(LOG_TAG, "ParseTlvContent:" + e);
        }
        return strRet;
    }


}

package com.example.hp.lively;

import android.os.Build;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Sergii Varenyk on 27.09.15.
 */
public class LivelyPin {

    private String id;
    private String link;
    private String note;
    private String imageUrl;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }


    public static LivelyPin makePin(Object obj) {
        LivelyPin pin = new LivelyPin();
        try {
            if (obj instanceof JSONObject) {

                JSONObject dataObj = (JSONObject)obj;

                if (dataObj.has("id")) {
                    pin.setId(dataObj.getString("id"));
                }

                if (dataObj.has("link")) {
                    pin.setLink(dataObj.getString("link"));
                }

                if (dataObj.has("note")) {
                    pin.setNote(dataObj.getString("note"));
                }

                if (dataObj.has("image")) {
                    JSONObject imageObj = dataObj.getJSONObject("image");
                    Iterator<String> keys = imageObj.keys();
                    while(keys.hasNext()) {
                        String key = keys.next();
                        if ((imageObj.get(key) instanceof JSONObject) && (key.equals("original"))) {
                            JSONObject iObj = imageObj.getJSONObject(key);
                            if (iObj.has("url")) {
                                pin.setImageUrl(iObj.getString("url"));
                            }
                        }
                    }
                }
            }
        } catch (JSONException e) {
            Log.i("LivelyPin JSON parse error %s", e.getLocalizedMessage());
        }
        return pin;
    }


    //from this to the end remove in ListFragment??

    public static ArrayList<LivelyPin> makePinList(Object obj) {
        ArrayList<LivelyPin> pinList = new ArrayList<LivelyPin>();
        JSONArray ja = new JSONArray();
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject((String) obj);
            ja = (JSONArray) jsonObject.get("data");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            if (ja instanceof JSONArray) {
                int size = ja.length();
                for (int i = 0; i < size; i++) {
                    JSONObject dataObj = ja.getJSONObject(i);
                    Log.i("JSONObj",""+dataObj.toString());
                    pinList.add(makePin(dataObj));
                }
            }
        } catch (JSONException e){
            Log.i("LivelyPinList parse JSON error %s", e.getLocalizedMessage());
        }
        return pinList;
    }
}

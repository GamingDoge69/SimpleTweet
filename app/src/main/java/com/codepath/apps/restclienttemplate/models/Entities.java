package com.codepath.apps.restclienttemplate.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;

@Parcel
public class Entities {
    public ArrayList<Media> allMediaList = new ArrayList<>(4);

    public Entities() { /* Parsable Library Constructor */}

    public static Entities fromJSON(JSONObject jsonObject) throws JSONException {
        Entities entities = new Entities();
        JSONArray entityList = jsonObject.getJSONArray("media");
        for (int entityIndex = 0; entityIndex < entityList.length(); entityIndex++){
            entities.allMediaList.add(Media.fromJSON(entityList.getJSONObject(entityIndex)));
        }
        return entities;
    }
}

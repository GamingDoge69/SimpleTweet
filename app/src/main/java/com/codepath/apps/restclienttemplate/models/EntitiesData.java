package com.codepath.apps.restclienttemplate.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;

@Parcel
public class EntitiesData {
    public ArrayList<MediaData> allMediaDataList = new ArrayList<>(4);

    public EntitiesData() { /* Parsable Library Constructor */}

    public static EntitiesData fromJSON(JSONObject jsonObject) throws JSONException {
        EntitiesData entitiesData = new EntitiesData();
        JSONArray entityList = jsonObject.getJSONArray("media");
        for (int entityIndex = 0; entityIndex < entityList.length(); entityIndex++){
            entitiesData.allMediaDataList.add(MediaData.fromJSON(entityList.getJSONObject(entityIndex)));
        }
        return entitiesData;
    }
}

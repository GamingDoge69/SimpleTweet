package com.codepath.apps.restclienttemplate.models;

import androidx.core.util.Pair;

import com.codepath.apps.restclienttemplate.media.MediaType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;

@Parcel
public class VideoData extends MediaData {
    public VideoData() { /* Parsable Library Constructor */}

    private static JSONObject highestBitRateVideo(JSONArray array) throws JSONException {
        ArrayList<Pair<Integer, Integer>> indexBitratePairArray = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            if (((JSONObject) array.get(i)).has("bitrate")) {
                indexBitratePairArray.add(new Pair<>(i, ((JSONObject) array.get(i)).getInt("bitrate")));
            }
        }
        Pair<Integer, Integer> maxIndexBitratePair = indexBitratePairArray.get(0);

        for (Pair<Integer, Integer> indexBitratePair: indexBitratePairArray) {
            maxIndexBitratePair = indexBitratePair.second > maxIndexBitratePair.second? indexBitratePair : maxIndexBitratePair;
        }
        return (JSONObject) array.get(maxIndexBitratePair.first);
    }

    public VideoData(JSONObject jsonObject) throws JSONException {
        this.mediaType = MediaType.VIDEO;
        this.mediaUrl = highestBitRateVideo(jsonObject
                .getJSONObject("video_info")
                .getJSONArray("variants"))
                .getString("url");
        this.ratio[0] =
                jsonObject
                        .getJSONObject("video_info")
                        .getJSONArray("aspect_ratio").getInt(0);
        this.ratio[1] =
                jsonObject
                        .getJSONObject("video_info")
                        .getJSONArray("aspect_ratio").getInt(1);
    }

}

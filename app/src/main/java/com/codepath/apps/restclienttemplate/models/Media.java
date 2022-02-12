package com.codepath.apps.restclienttemplate.models;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;

@Parcel
public class Media {
    private static final String TAG = "Media";
    public static final Media FAILED_MEDIA = new Media(MEDIA_TYPE.NOT_SUPPORTED);

    public enum MEDIA_TYPE {
        PHOTO("photo"),
        VIDEO("video"),
        ANIMATED_GIF("animated_gif"),
        NOT_SUPPORTED("not_supported");

        public final String typeStr;

        @NonNull
        @Override
        public String toString() {
            return typeStr;
        }

        MEDIA_TYPE(String typeStr) {
            this.typeStr = typeStr;
        }

        public static MEDIA_TYPE fromString(String typeStr) {
            switch(typeStr) {
                case "photo":
                    return MEDIA_TYPE.PHOTO;
                case "video":
                    return MEDIA_TYPE.VIDEO;
                case  "animated_gif":
                    return MEDIA_TYPE.ANIMATED_GIF;
                default:
                    return MEDIA_TYPE.NOT_SUPPORTED;
            }
        }
    }

    public MEDIA_TYPE mediaType;
    public String mediaUrl;
    public int[] ratio = new int[2];


    public Media() { /* Parsable Library Constructor */}

    private Media(MEDIA_TYPE mediaType) {
        this.mediaType = mediaType;
    }

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

    public static Media fromJSON(JSONObject jsonObject) {
        try {
            Media media = new Media();
            media.mediaType = MEDIA_TYPE.fromString(jsonObject.getString("type"));
            switch (media.mediaType) {
                case PHOTO:
                    media.mediaUrl = jsonObject.getString("media_url_https");
                    // Any size will give us a ratio
                    JSONObject aSize = jsonObject.getJSONObject("sizes").getJSONObject("medium");
                    media.ratio[0] = aSize.getInt("w");
                    media.ratio[1] = aSize.getInt("h");
                    Log.i(TAG, "fromJSON: " + media.mediaType + " " + media.mediaUrl);
                    break;
                case ANIMATED_GIF:
                    media.mediaUrl = ((JSONObject) jsonObject
                            .getJSONObject("video_info")
                            .getJSONArray("variants")
                            .get(0))
                            .getString("url");
                    media.ratio[0] =
                            jsonObject
                                    .getJSONObject("video_info")
                                    .getJSONArray("aspect_ratio").getInt(0);
                    media.ratio[1] =
                            jsonObject
                                    .getJSONObject("video_info")
                                    .getJSONArray("aspect_ratio").getInt(1);
                    Log.i(TAG, "fromJSON: " + media.mediaType + " " + media.mediaUrl);
                    break;
                case VIDEO:
                    media.mediaUrl = highestBitRateVideo(jsonObject
                            .getJSONObject("video_info")
                            .getJSONArray("variants"))
                            .getString("url");
                    media.ratio[0] =
                            jsonObject
                                    .getJSONObject("video_info")
                                    .getJSONArray("aspect_ratio").getInt(0);
                    media.ratio[1] =
                            jsonObject
                                    .getJSONObject("video_info")
                                    .getJSONArray("aspect_ratio").getInt(1);
                    Log.i(TAG, "fromJSON: " + media.mediaType + " " + media.mediaUrl);
                    break;
                default:
                    return FAILED_MEDIA;
            }
            return media;
        } catch (JSONException exception) {
            Log.e(TAG, "fromJSON: ", exception);
            return FAILED_MEDIA;
        }
    }
}

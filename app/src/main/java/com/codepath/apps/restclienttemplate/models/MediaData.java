package com.codepath.apps.restclienttemplate.models;

import android.util.Log;


import com.codepath.apps.restclienttemplate.media.MediaFactory;
import com.codepath.apps.restclienttemplate.media.MediaType;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;


@Parcel
public class MediaData {
    private static final String TAG = "Media";
    public static final MediaData FAILED_MEDIA_DATA = new MediaData(MediaType.NOT_SUPPORTED);

    public MediaType mediaType;
    public String mediaUrl;
    public int[] ratio = new int[2];


    public MediaData() { /* Parsable Library Constructor */}

    private MediaData(MediaType mediaType) {
        this.mediaType = mediaType;
    }

    public static MediaData fromJSON(JSONObject jsonObject) {
        try {
            return MediaFactory.createMediaData(MediaType.fromString(jsonObject.getString("type")), jsonObject);
        } catch (JSONException | IllegalArgumentException exception) {
            Log.e(TAG, "fromJSON: ", exception);
            return FAILED_MEDIA_DATA;
        }
    }
}

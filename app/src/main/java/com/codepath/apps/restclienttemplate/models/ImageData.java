package com.codepath.apps.restclienttemplate.models;

import com.codepath.apps.restclienttemplate.media.MediaType;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

@Parcel
public class ImageData extends MediaData {
    public ImageData() { /* Parsable Library Constructor */}

    public ImageData(JSONObject jsonObject) throws JSONException {
        this.mediaType = MediaType.PHOTO;
        this.mediaUrl = jsonObject.getString("media_url_https");
        // Any size will give us a ratio
        JSONObject aSize = jsonObject.getJSONObject("sizes").getJSONObject("medium");
        this.ratio[0] = aSize.getInt("w");
        this.ratio[1] = aSize.getInt("h");
    }
}

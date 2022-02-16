package com.codepath.apps.restclienttemplate.models;

import com.codepath.apps.restclienttemplate.media.MediaType;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

@Parcel
public class GIFData extends MediaData {
    public GIFData() { /* Parsable Library Constructor */}

    public GIFData(JSONObject jsonObject) throws JSONException {
        this.mediaType = MediaType.ANIMATED_GIF;
        this.mediaUrl = ((JSONObject) jsonObject
                .getJSONObject("video_info")
                .getJSONArray("variants")
                .get(0))
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

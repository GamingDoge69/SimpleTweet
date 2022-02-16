package com.codepath.apps.restclienttemplate.media;

import com.codepath.apps.restclienttemplate.interfaces.Media;
import com.codepath.apps.restclienttemplate.models.GIFData;
import com.codepath.apps.restclienttemplate.models.ImageData;
import com.codepath.apps.restclienttemplate.models.MediaData;
import com.codepath.apps.restclienttemplate.models.VideoData;

import org.json.JSONException;
import org.json.JSONObject;

public class MediaFactory {
    public static Media createMedia(MediaType mediaType) {
        switch (mediaType) {
            case PHOTO:
                return new ImageMedia();
            case VIDEO:
                return new VideoMedia();
            case ANIMATED_GIF:
                return new GIFMedia();
            default:
                throw new IllegalArgumentException("Invalid Media Type");
        }
    }

    public static MediaData createMediaData(MediaType mediaType, JSONObject object) throws JSONException, IllegalArgumentException {
        switch (mediaType) {
            case PHOTO:
                return new ImageData(object);
            case VIDEO:
                return new VideoData(object);
            case ANIMATED_GIF:
                return new GIFData(object);
            default:
                throw new IllegalArgumentException("Invalid Media Type");
        }
    }
}

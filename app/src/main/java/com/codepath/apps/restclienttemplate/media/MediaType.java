package com.codepath.apps.restclienttemplate.media;

import androidx.annotation.NonNull;

public enum MediaType {
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

    MediaType(String typeStr) {
        this.typeStr = typeStr;
    }

    public static MediaType fromString(String typeStr) {
        switch(typeStr) {
            case "photo":
                return MediaType.PHOTO;
            case "video":
                return MediaType.VIDEO;
            case  "animated_gif":
                return MediaType.ANIMATED_GIF;
            default:
                return MediaType.NOT_SUPPORTED;
        }
    }
}

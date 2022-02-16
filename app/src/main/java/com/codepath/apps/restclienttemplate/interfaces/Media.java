package com.codepath.apps.restclienttemplate.interfaces;

import android.content.Context;
import android.view.View;

import com.codepath.apps.restclienttemplate.media.MediaType;
import com.codepath.apps.restclienttemplate.models.MediaData;

public interface Media {
    MediaType getMediaType();

    View createView(Context context, MediaData data);
}

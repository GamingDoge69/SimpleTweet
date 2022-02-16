package com.codepath.apps.restclienttemplate.media;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

import com.codepath.apps.restclienttemplate.interfaces.Media;
import com.codepath.apps.restclienttemplate.models.MediaData;

public class VideoMedia implements Media {
    @Override
    public MediaType getMediaType() {
        return MediaType.VIDEO;
    }

    @Override
    public View createView(Context context, MediaData data) {
        VideoView vv = new VideoView(context);
        vv.setVideoURI(Uri.parse(data.mediaUrl));

        MediaController mediaController = new MediaController(context);
        vv.setMediaController(mediaController);

        vv.start();
        return vv;
    }
}

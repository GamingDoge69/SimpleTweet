package com.codepath.apps.restclienttemplate.media;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.interfaces.Media;
import com.codepath.apps.restclienttemplate.models.MediaData;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class ImageMedia implements Media {
    @Override
    public MediaType getMediaType() {
        return MediaType.PHOTO;
    }

    @Override
    public View createView(Context context, MediaData data) {
        ImageView iv = new ImageView(context);
        Glide.with(context)
                .load(data.mediaUrl)
                .fitCenter()
                .transform(new RoundedCornersTransformation(15, 0))
                .into(iv);
        return iv;
    }
}

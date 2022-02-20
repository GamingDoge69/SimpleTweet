package com.codepath.apps.restclienttemplate.media;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.codepath.apps.restclienttemplate.interfaces.Media;
import com.codepath.apps.restclienttemplate.models.MediaData;

import jp.wasabeef.glide.transformations.CropSquareTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class ImageMedia implements Media {
    @Override
    public MediaType getMediaType() {
        return MediaType.PHOTO;
    }

    @Override
    public View createView(Context context, MediaData data) {
        ImageView iv = new ImageView(context);
        MultiTransformation<Bitmap> effects = new MultiTransformation<>(
                new CropSquareTransformation(),
                new RoundedCornersTransformation(25, 0));
        Glide.with(context)
                .load(data.mediaUrl)
                .transform(effects)
                .into(iv);
        return iv;
    }
}

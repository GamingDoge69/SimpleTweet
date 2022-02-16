package com.codepath.apps.restclienttemplate.media;

import android.content.Context;
import android.view.View;

import com.codepath.apps.restclienttemplate.interfaces.Media;
import com.codepath.apps.restclienttemplate.models.MediaData;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.ui.PlayerView;

public class GIFMedia implements Media {

    @Override
    public MediaType getMediaType() {
        return MediaType.ANIMATED_GIF;
    }

    @Override
    public View createView(Context context, MediaData data) {
        PlayerView pv = new PlayerView(context);
        ExoPlayer player = new ExoPlayer.Builder(context).build();

        pv.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View view) {
                player.setMediaItem(MediaItem.fromUri(data.mediaUrl));
                player.addListener(new Player.Listener() {
                    @Override
                    public void onPlaybackStateChanged(int playbackState) {
                        if (playbackState == ExoPlayer.STATE_ENDED) player.seekTo(0);
                    }
                });
                pv.setPlayer(player);
                pv.setUseController(false);
                player.setPlayWhenReady(true);
                player.prepare();
            }

            @Override
            public void onViewDetachedFromWindow(View view) {
                player.release(); }
        });

        return pv;
    }
}

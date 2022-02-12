package com.codepath.apps.restclienttemplate;

import static com.codepath.apps.restclienttemplate.models.Media.FAILED_MEDIA;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.codepath.apps.restclienttemplate.databinding.ActivityDetailedTweetBinding;
import com.codepath.apps.restclienttemplate.models.Entities;
import com.codepath.apps.restclienttemplate.models.Media;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.utils.MeasurementConverter;
import com.codepath.apps.restclienttemplate.utils.TweetActionHelper;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.ui.PlayerView;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class DetailedTweetActivity extends AppCompatActivity {
    ActivityDetailedTweetBinding binding;
    Toolbar toolbar;
    Tweet tweetData;

    ArrayList<ExoPlayer> videoRefHolder = new ArrayList<>(1);


    private void setupToolBar() {
        toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null) return;
        actionBar.setDisplayHomeAsUpEnabled(true);
        Drawable backIcon = toolbar.getNavigationIcon();
        if (backIcon == null) return;
        backIcon.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_ATOP);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detailed_tweet);

        setupToolBar();

        tweetData = Parcels.unwrap(getIntent().getParcelableExtra("TweetData"));
        binding.setTweet(tweetData);

        TweetActionHelper.bindLike(binding, binding.ivLikeButton, tweetData);
        TweetActionHelper.bindRetweet(binding, binding.ivRetweetButton, tweetData);
        TweetActionHelper.bindReply(binding, binding.ivReplyButton, tweetData);

        Glide.with(this)
                .load(tweetData.user.profileImageUrl)
                .apply(RequestOptions.circleCropTransform())
                .into(binding.ivProfileImage);

        binding.mediaGrid.post(new Runnable() {
            @Override
            public void run() {
                handleEntities();
            }
        });
    }

    public void handleEntities() {
        GridLayout mediaGrid = binding.mediaGrid;
        Entities entities = tweetData.entities;
        List<Media> mediaList;
        if (entities != null && entities.allMediaList != null) {
            mediaList = entities.allMediaList;

            int failedMediaCount = 0;
            for (Media media: mediaList) failedMediaCount += (media == FAILED_MEDIA)? 1 : 0;

            int widthSize = mediaGrid.getWidth();
            int interPadding = MeasurementConverter.dpToPx(this, 1);

            int displayListSize = mediaList.size() - failedMediaCount;
            if (displayListSize != 0) binding.mediaGrid.setVisibility(View.VISIBLE);

            if (displayListSize == 1) {
                mediaGrid.setRowCount(1);
                mediaGrid.setColumnCount(1);
            }
            else if (displayListSize == 2) {
                mediaGrid.setRowCount(1);
                mediaGrid.setColumnCount(2);
                widthSize/=2;
            }
            else if (displayListSize - failedMediaCount >= 3) {
                mediaGrid.setRowCount(2);
                mediaGrid.setColumnCount(2);
                widthSize/=2;
            }

            for (Media media : mediaList) {
                switch (media.mediaType) {
                    case PHOTO:
                        ImageView iv = new ImageView(this);
                        iv.setPadding(interPadding, interPadding, interPadding, interPadding);
                        iv.setLayoutParams(new ViewGroup.LayoutParams(
                                widthSize,
                                (int)(((float)media.ratio[1])/media.ratio[0] * widthSize))
                        );
                        Glide.with(this)
                                .load(media.mediaUrl)
                                .fitCenter()
                                .transform(new RoundedCornersTransformation(15, 0))
                                .into(iv);
                        mediaGrid.addView(iv);
                        break;
                    case ANIMATED_GIF:
                    case VIDEO:
                        PlayerView pv = new PlayerView(this);
                        pv.setLayoutParams(new ViewGroup.LayoutParams(
                                widthSize,
                                (int)(((float)media.ratio[1])/media.ratio[0] * widthSize)
                        ));
                        ExoPlayer player = new ExoPlayer.Builder(this).build();
                        player.setMediaItem(MediaItem.fromUri(media.mediaUrl));
                        player.addListener(new Player.Listener() {
                            @Override
                            public void onPlaybackStateChanged(int playbackState) {
                                if (playbackState == ExoPlayer.STATE_ENDED) player.seekTo(0);
                            }
                        });
                        pv.setPlayer(player);
                        pv.setUseController(media.mediaType == Media.MEDIA_TYPE.VIDEO);
                        if (media.mediaType == Media.MEDIA_TYPE.VIDEO) {
                            pv.setShowPreviousButton(false);
                            pv.setShowNextButton(false);
                            pv.setShowFastForwardButton(false);
                            pv.setShowRewindButton(false);
                            pv.hideController();
                        }
                        player.setPlayWhenReady(true);
                        player.prepare();
                        mediaGrid.addView(pv);
                        videoRefHolder.add(player);
                        break;
                    case NOT_SUPPORTED:
                        break;
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        for (ExoPlayer player: videoRefHolder) {
            player.play();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        for (ExoPlayer player : videoRefHolder) {
            player.pause();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        for (ExoPlayer player: videoRefHolder) {
            player.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        for (ExoPlayer player: videoRefHolder) {
            player.release();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
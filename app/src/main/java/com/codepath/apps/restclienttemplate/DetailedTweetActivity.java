package com.codepath.apps.restclienttemplate;

import static com.codepath.apps.restclienttemplate.models.MediaData.FAILED_MEDIA_DATA;

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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.codepath.apps.restclienttemplate.databinding.ActivityDetailedTweetBinding;
import com.codepath.apps.restclienttemplate.media.MediaType;
import com.codepath.apps.restclienttemplate.models.EntitiesData;
import com.codepath.apps.restclienttemplate.models.MediaData;
import com.codepath.apps.restclienttemplate.media.MediaFactory;
import com.codepath.apps.restclienttemplate.models.TweetData;
import com.codepath.apps.restclienttemplate.utils.MeasurementConverter;
import com.codepath.apps.restclienttemplate.utils.TweetActionHelper;

import org.parceler.Parcels;

import java.util.List;

public class DetailedTweetActivity extends AppCompatActivity {
    ActivityDetailedTweetBinding binding;
    Toolbar toolbar;
    TweetData tweetData;


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
        binding.setTweetData(tweetData);

        TweetActionHelper.bindLike(binding, binding.ivLikeButton, tweetData);
        TweetActionHelper.bindRetweet(binding, binding.ivRetweetButton, tweetData);
        TweetActionHelper.bindReply(binding, binding.ivReplyButton, tweetData);

        Glide.with(this)
                .load(tweetData.userData.profileImageUrl)
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
        EntitiesData entities = tweetData.entitiesData;
        List<MediaData> mediaDataList;
        if (entities != null && entities.allMediaDataList != null) {
            mediaDataList = entities.allMediaDataList;

            int failedMediaCount = 0;
            for (MediaData mediaData : mediaDataList) failedMediaCount += (mediaData == FAILED_MEDIA_DATA)? 1 : 0;

            int widthSize = mediaGrid.getWidth();
            int interPadding = MeasurementConverter.dpToPx(this, 1);

            int displayListSize = mediaDataList.size() - failedMediaCount;
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

            for (MediaData mediaData : mediaDataList) {
                if (mediaData.mediaType == MediaType.NOT_SUPPORTED) continue;
                View mediaView = MediaFactory.createMedia(mediaData.mediaType).createView(this, mediaData);
                mediaView.setPadding(interPadding, interPadding, interPadding, interPadding);
                mediaView.setLayoutParams(new ViewGroup.LayoutParams(
                        widthSize,
                        (int)(((float) mediaData.ratio[1])/ mediaData.ratio[0] * widthSize))
                );
                mediaGrid.addView(mediaView);
            }
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
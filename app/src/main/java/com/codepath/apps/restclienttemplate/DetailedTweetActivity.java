package com.codepath.apps.restclienttemplate;


import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.codepath.apps.restclienttemplate.databinding.ActivityDetailedTweetBinding;
import com.codepath.apps.restclienttemplate.utils.EntitiesHelper;
import com.codepath.apps.restclienttemplate.models.TweetData;
import com.codepath.apps.restclienttemplate.utils.TweetActionHelper;

import org.parceler.Parcels;


public class DetailedTweetActivity extends AppCompatActivity {
    ActivityDetailedTweetBinding binding;
    Toolbar toolbar;
    TweetData tweetData;
    TwitterClient client;


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

        client = TwitterApp.getRestClient(this);

        tweetData = Parcels.unwrap(getIntent().getParcelableExtra("tweetData"));
        binding.setTweetData(tweetData);

        TweetActionHelper.bindLike(binding, binding.ivLikeButton, tweetData, client);
        TweetActionHelper.bindRetweet(binding, binding.ivRetweetButton, tweetData, client);
        TweetActionHelper.bindReply(binding, binding.ivReplyButton, tweetData, getSupportFragmentManager(), TimelineActivity.offlineTweetListener);

        Glide.with(this)
                .load(tweetData.userData.profileImageUrl)
                .apply(RequestOptions.circleCropTransform())
                .into(binding.ivProfileImage);

        binding.mediaGrid.post(new Runnable() {
            @Override
            public void run() {
                EntitiesHelper.insertEntitiesIntoGrid(DetailedTweetActivity.this, binding.mediaGrid, tweetData);
            }
        });
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

    @Override
    public void onBackPressed() {
        Intent i = new Intent();
        i.putExtra("tweetData", Parcels.wrap(tweetData));
        setResult(Activity.RESULT_OK, i);
        super.onBackPressed();
    }
}
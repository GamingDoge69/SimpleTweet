package com.codepath.apps.restclienttemplate.utils;

import android.view.View;
import android.widget.ImageView;

import androidx.databinding.ViewDataBinding;

import com.codepath.apps.restclienttemplate.models.Tweet;

public class TweetActionHelper {
    public static void bindLike(ViewDataBinding binding, ImageView iv, Tweet tweetData) {
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tweetData.actions[tweetData.LIKE_BUTTON] = !tweetData.actions[tweetData.LIKE_BUTTON];
                iv.post(new Runnable() {
                    @Override
                    public void run() {
                        binding.invalidateAll();
                    }
                });
            }
        });
    }

    public static void bindRetweet(ViewDataBinding binding, ImageView iv, Tweet tweetData) {
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tweetData.actions[tweetData.RETWEET_BUTTON] = !tweetData.actions[tweetData.RETWEET_BUTTON];
                iv.post(new Runnable() {
                    @Override
                    public void run() {
                        binding.invalidateAll();
                    }
                });
            }
        });
    }

    public static void bindReply(ViewDataBinding binding, ImageView iv, Tweet tweetData) {
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tweetData.actions[tweetData.REPLY_BUTTON] = !tweetData.actions[tweetData.REPLY_BUTTON];
                iv.post(new Runnable() {
                    @Override
                    public void run() {
                        binding.invalidateAll();
                    }
                });
            }
        });
    }
}

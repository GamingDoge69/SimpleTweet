package com.codepath.apps.restclienttemplate.utils;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.FragmentManager;

import com.codepath.apps.restclienttemplate.ComposeFragment;
import com.codepath.apps.restclienttemplate.TwitterClient;
import com.codepath.apps.restclienttemplate.interfaces.OfflineTweetListener;
import com.codepath.apps.restclienttemplate.models.TweetData;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import okhttp3.Headers;

public class TweetActionHelper {
    public static void bindLike(ViewDataBinding binding, ImageView iv, TweetData tweetData, TwitterClient client) {
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tweetData.actions[tweetData.LIKE_BUTTON] = !tweetData.actions[tweetData.LIKE_BUTTON];
                iv.post(new Runnable() {
                    @Override
                    public void run() {
                        client.postLike(new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Headers headers, JSON json) { }

                            @Override
                            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) { }
                        }, tweetData);

                        binding.invalidateAll();
                    }
                });
            }
        });
    }

    public static void bindRetweet(ViewDataBinding binding, ImageView iv, TweetData tweetData, TwitterClient client) {
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tweetData.actions[tweetData.RETWEET_BUTTON] = !tweetData.actions[tweetData.RETWEET_BUTTON];
                iv.post(new Runnable() {
                    @Override
                    public void run() {
                        client.postRetweet(new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Headers headers, JSON json) { }

                            @Override
                            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) { }
                        }, tweetData);
                        binding.invalidateAll();
                    }
                });
            }
        });
    }

    public static void bindReply(ViewDataBinding binding, ImageView iv, TweetData tweetData,
                                 FragmentManager fm, @Nullable OfflineTweetListener offlineTweetListener) {
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iv.post(new Runnable() {
                    @Override
                    public void run() {
                        ComposeFragment composeFragment = ComposeFragment.newInstance(tweetData, new ComposeFragment.ComposeFragmentListener() {
                            @Override
                            public void onTweetPosted(TweetData tweetData) {
                                if (offlineTweetListener != null)
                                    offlineTweetListener.notifyOfflineTweet(tweetData);
                            }
                        });
                        composeFragment.show(fm, "composeFragment");
                    }
                });
            }
        });
    }
}

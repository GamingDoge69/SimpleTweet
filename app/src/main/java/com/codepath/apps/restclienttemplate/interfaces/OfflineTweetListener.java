package com.codepath.apps.restclienttemplate.interfaces;

import com.codepath.apps.restclienttemplate.models.TweetData;

public interface OfflineTweetListener {
    void notifyOfflineTweet(TweetData tweetData);
}
package com.codepath.apps.restclienttemplate.interfaces;

import com.codepath.apps.restclienttemplate.models.TweetData;

public interface TweetDataModificationListener {
    void notifyTweetDataModified(TweetData data);
}

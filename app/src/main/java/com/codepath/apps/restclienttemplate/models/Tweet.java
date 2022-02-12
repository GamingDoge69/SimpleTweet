package com.codepath.apps.restclienttemplate.models;

import android.view.View;

import androidx.databinding.BindingAdapter;

import com.codepath.apps.restclienttemplate.utils.TimeFormatter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel
public class Tweet {
    public String id;
    public String body;
    public String createdAt;
    public User user;
    public Entities entities;

    public boolean[] actions = new boolean[3];
    public int LIKE_BUTTON = 0;
    public int RETWEET_BUTTON = 1;
    public int REPLY_BUTTON = 2;

    public Tweet() { /* Parsable Library Constructor */}

    public static Tweet fromJSON(JSONObject jsonObject) throws JSONException {
        Tweet tweet = new Tweet();
        tweet.id = jsonObject.getString("id_str");
        tweet.body = jsonObject.getString("full_text");
        tweet.createdAt = jsonObject.getString("created_at");
        tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));
        tweet.actions[tweet.LIKE_BUTTON] = jsonObject.getBoolean("favorited");
        tweet.actions[tweet.RETWEET_BUTTON] = jsonObject.getBoolean("retweeted");;
        tweet.actions[tweet.REPLY_BUTTON] = false;
        try {
            tweet.entities = Entities.fromJSON(jsonObject.getJSONObject("extended_entities"));
        } catch (JSONException ignored) { }
        return tweet;
    }

    public static List<Tweet> fromJSONArray(JSONArray jsonArray) throws JSONException {
        List<Tweet> tweets = new ArrayList<>(jsonArray.length());
        for (int i = 0; i < jsonArray.length(); i++) {
            tweets.add(fromJSON(jsonArray.getJSONObject(i)));
        }
        return tweets;
    }

    @BindingAdapter("is_selected")
    public static void is_selected(View view, boolean selected) {
        view.setSelected(selected);
    }

    public Boolean getLiked() { return actions[LIKE_BUTTON];}

    public Boolean getRetweeted() { return actions[RETWEET_BUTTON];}

    public Boolean getReplied() { return actions[REPLY_BUTTON];}

    public String getId() {
        return id;
    }

    public String getBody() {
        return body;
    }

    public String getCreatedAt() {
        return TimeFormatter.getTimeDifference(createdAt);
    }

    public String getCustomAtString() {
        return "@" + user.getScreenName() + " · " + getCreatedAt();
    }

    public User getUser() {
        return user;
    }
}

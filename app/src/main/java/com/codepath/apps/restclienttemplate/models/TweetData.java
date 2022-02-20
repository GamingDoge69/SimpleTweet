package com.codepath.apps.restclienttemplate.models;

import android.view.View;

import androidx.databinding.BindingAdapter;

import com.codepath.apps.restclienttemplate.utils.TimeFormatter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Parcel
public class TweetData {
    public String id;
    public String body;
    public String createdAt;
    public UserData userData;
    public EntitiesData entitiesData;

    public long likeCount;
    private int offlineLike = 0;
    private boolean DB_LIKE;

    public long retweetCount;
    private int offlineRetweet = 0;
    private boolean DB_RETWEET;

    public boolean[] actions = new boolean[2];
    public int LIKE_BUTTON = 0;
    public int RETWEET_BUTTON = 1;

    public TweetData() { /* Parsable Library Constructor */}

    public static TweetData fromJSON(JSONObject jsonObject) throws JSONException {
        TweetData tweetData = new TweetData();
        tweetData.id = jsonObject.getString("id_str");
        tweetData.body = jsonObject.getString("full_text");
        tweetData.createdAt = jsonObject.getString("created_at");
        tweetData.userData = UserData.fromJSON(jsonObject.getJSONObject("user"));
        tweetData.actions[tweetData.LIKE_BUTTON] = jsonObject.getBoolean("favorited");
        tweetData.actions[tweetData.RETWEET_BUTTON] = jsonObject.getBoolean("retweeted");
        tweetData.DB_LIKE = tweetData.actions[tweetData.LIKE_BUTTON];
        tweetData.DB_RETWEET = tweetData.actions[tweetData.RETWEET_BUTTON];
        try {
            tweetData.entitiesData = EntitiesData.fromJSON(jsonObject.getJSONObject("extended_entities"));
        } catch (JSONException ignored) { }
        tweetData.likeCount = jsonObject.getLong("favorite_count");
        tweetData.retweetCount = jsonObject.getLong("retweet_count");
        return tweetData;
    }

    public static List<TweetData> fromJSONArray(JSONArray jsonArray) throws JSONException {
        List<TweetData> tweetData = new ArrayList<>(jsonArray.length());
        for (int i = 0; i < jsonArray.length(); i++) {
            tweetData.add(fromJSON(jsonArray.getJSONObject(i)));
        }
        return tweetData;
    }

    @BindingAdapter("is_selected")
    public static void is_selected(View view, boolean selected) {
        view.setSelected(selected);
    }

    public Boolean getLiked() { return actions[LIKE_BUTTON];}

    public Boolean getRetweeted() { return actions[RETWEET_BUTTON];}

    public void handleOfflineLike() {
        if (DB_LIKE)
            offlineLike = this.actions[LIKE_BUTTON]? 0 : -1;
        else
            offlineLike = this.actions[LIKE_BUTTON]? 1 : 0;
    }
    public long getLikeCount() {
        handleOfflineLike();
        return likeCount + offlineLike;
    }

    public void handleOfflineRetweet() {
        if (DB_RETWEET)
            offlineRetweet = this.actions[RETWEET_BUTTON]? 0 : -1;
        else
            offlineRetweet = this.actions[RETWEET_BUTTON]? 1 : 0;
    }

    public long getRetweetCount() {
        handleOfflineRetweet();
        return retweetCount + offlineRetweet;
    }

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
        return "@" + userData.getScreenName() + " · " + getCreatedAt();
    }

    public UserData getUser() {
        return userData;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TweetData tweetData = (TweetData) o;
        return this.id.equals(tweetData.id);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, body, createdAt, userData, entitiesData);
        result = 31 * result + Arrays.hashCode(actions);
        return result;
    }
}

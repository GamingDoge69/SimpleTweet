package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import android.os.Bundle;
import android.util.Log;

import com.codepath.apps.restclienttemplate.interfaces.RequestCompleteCallback;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class TimelineActivity extends AppCompatActivity {
    public static final String TAG = "TimelineActivity";

    private static final int INIT_TIMELINE_COUNT = 25;
    private static final int REFRESH_TIMELINE_COUNT = 50;
    private static final int TAIL_TIMELINE_COUNT = 25;

    List<Tweet> tweets;
    TweetsAdapter adapter;
    TwitterClient client;
    RecyclerView rvTweets;
    SwipeRefreshLayout swipeRefreshContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        rvTweets = findViewById(R.id.rvTweets);
        swipeRefreshContainer = findViewById(R.id.swipeRefreshContainer);

        tweets = new ArrayList<>(INIT_TIMELINE_COUNT);

        client = TwitterApp.getRestClient(this);

        adapter = new TweetsAdapter(this, tweets);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvTweets.setLayoutManager(linearLayoutManager);
        rvTweets.setAdapter(adapter);

        rvTweets.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view, RequestCompleteCallback callback) {
                getMoreHomeTimeline(callback);
            }
        });

        swipeRefreshContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshHomeTimeline();
            }
        });

        populateHomeTimeline();
    }


    private void populateHomeTimeline() {
        swipeRefreshContainer.setRefreshing(true);
        client.getHomeTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                JSONArray timelineArray = json.jsonArray;
                try {
                    adapter.insertTweetsInFront(Tweet.fromJSONArray(timelineArray));
                    swipeRefreshContainer.setRefreshing(false);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.e(TAG, "onFailure: " + response, throwable);
            }
        }, INIT_TIMELINE_COUNT);
    }

    private void refreshHomeTimeline() {
        client.refreshHomeTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                JSONArray timelineArray = json.jsonArray;
                try {
                    adapter.insertTweetsInFront(Tweet.fromJSONArray(timelineArray));
                    swipeRefreshContainer.setRefreshing(false);
                    rvTweets.post(new Runnable() {
                        @Override
                        public void run() {
                            rvTweets.smoothScrollToPosition(0);
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.e(TAG, "onFailure: " + response, throwable);
            }
        }, tweets.get(0).id, REFRESH_TIMELINE_COUNT);
    }

    private void getMoreHomeTimeline(RequestCompleteCallback callback) {
        client.getMoreHomeTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                JSONArray timelineArray = json.jsonArray;
                try {
                    adapter.insertTweetsInBack(Tweet.fromJSONArray(timelineArray));
                    callback.requestComplete(true);
                    Log.i("onScrolled", "onSuccess: More Items Loaded");
                } catch (JSONException e) {
                    e.printStackTrace();
                    callback.requestComplete(false);
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.e(TAG, "onFailure: " + response, throwable);
                callback.requestComplete(false);
            }
        }, tweets.get(tweets.size() - 1).id, TAIL_TIMELINE_COUNT);
    }
}
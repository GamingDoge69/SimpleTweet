package com.codepath.apps.restclienttemplate;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.codepath.apps.restclienttemplate.interfaces.OfflineTweetListener;
import com.codepath.apps.restclienttemplate.interfaces.RequestCompleteCallback;
import com.codepath.apps.restclienttemplate.interfaces.TweetDataModificationListener;
import com.codepath.apps.restclienttemplate.models.TweetData;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.Headers;

public class TimelineActivity extends AppCompatActivity implements OfflineTweetListener {
    public static final String TAG = "TimelineActivity";

    private static final int INIT_TIMELINE_COUNT = 25;
    private static final int REFRESH_TIMELINE_COUNT = 50;
    private static final int TAIL_TIMELINE_COUNT = 25;

    ActivityResultLauncher<Intent> launchDetailedTweetActivity = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data == null) return;
                        TweetData tweetData = Parcels.unwrap(data.getParcelableExtra("tweetData"));
                        tweetDataModificationListener.notifyTweetDataModified(tweetData);
                    }
                }
            });

    List<TweetData> tweetData;
    TweetsAdapter adapter;
    TwitterClient client;
    RecyclerView rvTweets;
    SwipeRefreshLayout swipeRefreshContainer;
    TweetDataModificationListener tweetDataModificationListener;
    FloatingActionButton fabCompose;
    @Nullable public static OfflineTweetListener offlineTweetListener;

    private void setupToolBar() {
        setSupportActionBar(findViewById(R.id.toolbar));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        setupToolBar();

        rvTweets = findViewById(R.id.rvTweets);
        swipeRefreshContainer = findViewById(R.id.swipeRefreshContainer);
        fabCompose = findViewById(R.id.fabCompose);

        tweetData = new ArrayList<>(INIT_TIMELINE_COUNT);
        offlineTweetListener = this;

        client = TwitterApp.getRestClient(this);

        adapter = new TweetsAdapter(this, tweetData, client, launchDetailedTweetActivity, getSupportFragmentManager());
        tweetDataModificationListener = adapter;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvTweets.setLayoutManager(linearLayoutManager);
        rvTweets.setAdapter(adapter);
        ((SimpleItemAnimator) Objects.requireNonNull(rvTweets.getItemAnimator())).setSupportsChangeAnimations(false);


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

        fabCompose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getSupportFragmentManager();
                ComposeFragment composeFragment = ComposeFragment.newInstance(new ComposeFragment.ComposeFragmentListener() {
                    @Override
                    public void onTweetPosted(TweetData tweetData) {
                        addLocalTweet(tweetData);
                    }
                });
                composeFragment.show(fm, "composeFragment");
            }
        });

        populateHomeTimeline();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private void populateHomeTimeline() {
        swipeRefreshContainer.setRefreshing(true);
        client.getHomeTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                JSONArray timelineArray = json.jsonArray;
                try {
                    adapter.insertTweetsInFront(TweetData.fromJSONArray(timelineArray));
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
                    adapter.insertTweetsInFront(TweetData.fromJSONArray(timelineArray));
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
        }, adapter.getLatestLiveTweetId(), REFRESH_TIMELINE_COUNT);
    }

    private void getMoreHomeTimeline(RequestCompleteCallback callback) {
        client.getMoreHomeTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                JSONArray timelineArray = json.jsonArray;
                try {
                    adapter.insertTweetsInBack(TweetData.fromJSONArray(timelineArray));
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
        }, tweetData.get(tweetData.size() - 1).id, TAIL_TIMELINE_COUNT);
    }

    public void addLocalTweet(TweetData tweetData) {
        adapter.addLocalTweet(tweetData);
        rvTweets.smoothScrollToPosition(0);
    }

    @Override
    public void notifyOfflineTweet(TweetData tweetData) {
        addLocalTweet(tweetData);
    }
}
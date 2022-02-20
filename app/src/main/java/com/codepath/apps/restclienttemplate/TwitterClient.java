package com.codepath.apps.restclienttemplate;

import android.content.Context;

import com.codepath.apps.restclienttemplate.models.TweetData;
import com.codepath.asynchttpclient.RequestParams;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.codepath.oauth.OAuthBaseClient;
import com.github.scribejava.apis.TwitterApi;
import com.github.scribejava.core.builder.api.BaseApi;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/scribejava/scribejava/tree/master/scribejava-apis/src/main/java/com/github/scribejava/apis
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class TwitterClient extends OAuthBaseClient {
	public static final BaseApi REST_API_INSTANCE = TwitterApi.instance(); // Change this
	public static final String REST_URL = "https://api.twitter.com/1.1"; // Change this, base API URL
	public static final String REST_CONSUMER_KEY = BuildConfig.CONSUMER_KEY;       // Change this inside apikey.properties
	public static final String REST_CONSUMER_SECRET = BuildConfig.CONSUMER_SECRET; // Change this inside apikey.properties

	// Landing page to indicate the OAuth flow worked in case Chrome for Android 25+ blocks navigation back to the app.
	public static final String FALLBACK_URL = "https://codepath.github.io/android-rest-client-template/success.html";

	// See https://developer.chrome.com/multidevice/android/intents
	public static final String REST_CALLBACK_URL_TEMPLATE = "intent://%s#Intent;action=android.intent.action.VIEW;scheme=%s;package=%s;S.browser_fallback_url=%s;end";

	public TwitterClient(Context context) {
		super(context, REST_API_INSTANCE,
				REST_URL,
				REST_CONSUMER_KEY,
				REST_CONSUMER_SECRET,
				null,  // OAuth2 scope, null for OAuth1
				String.format(REST_CALLBACK_URL_TEMPLATE, context.getString(R.string.intent_host),
						context.getString(R.string.intent_scheme), context.getPackageName(), FALLBACK_URL));
	}

	public void getHomeTimeline(JsonHttpResponseHandler handler, int count) {
		getHomeTimelineOverallRequest(handler, "-1", "-1", count);
	}

	public void refreshHomeTimeline(JsonHttpResponseHandler handler, String since_id, int count) {
		getHomeTimelineOverallRequest(handler, since_id, "-1", count);
	}

	public void getMoreHomeTimeline(JsonHttpResponseHandler handler, String max_id, int count) {
		getHomeTimelineOverallRequest(handler, "-1", max_id, count);
	}

	private void getHomeTimelineOverallRequest(JsonHttpResponseHandler handler, String since_id, String max_id, int count) {
		String apiUrl = getApiUrl("statuses/home_timeline.json");
		// Can specify query string params directly or through RequestParams.
		RequestParams params = new RequestParams();
		params.put("tweet_mode", "extended");
		params.put("count", count);
		if (!since_id.equals("-1")) params.put("since_id", since_id);
		if (!max_id.equals("-1")) params.put("max_id", max_id);
		client.get(apiUrl, params, handler);
	}

	public void postTweet(JsonHttpResponseHandler handler, String tweetContent) {
		String apiUrl = getApiUrl("statuses/update.json");
		RequestParams params = new RequestParams();
		params.put("tweet_mode", "extended");
		params.put("status", tweetContent);
		client.post(apiUrl, params, "", handler);
	}

	public void postLike(JsonHttpResponseHandler handler, TweetData tweetData) {
		String apiUrl = tweetData.actions[tweetData.LIKE_BUTTON]?
				getApiUrl("favorites/create.json") :
				getApiUrl("favorites/destroy.json");
		RequestParams params = new RequestParams();
		params.put("id", tweetData.id);
		params.put("include_entities", false);
		params.put("tweet_mode", "extended");
		client.post(apiUrl, params, "", handler);
	}

	public void postRetweet(JsonHttpResponseHandler handler, TweetData tweetData) {
		String apiUrl = String.format( (tweetData.actions[tweetData.RETWEET_BUTTON])?
				getApiUrl("statuses/retweet/%s.json") :
				getApiUrl("statuses/unretweet/%s.json"), tweetData.id);
		RequestParams params = new RequestParams();
		params.put("include_entities", false);
		params.put("tweet_mode", "extended");
		client.post(apiUrl, params, "", handler);
	}

	public void postReply(JsonHttpResponseHandler handler, String tweetContent, TweetData tweetData) {
		String apiUrl = getApiUrl("statuses/update.json");
		RequestParams params = new RequestParams();
		params.put("tweet_mode", "extended");
		params.put("in_reply_to_status_id", tweetData.id);
		params.put("status", tweetContent);
		client.post(apiUrl, params, "", handler);
	}
}

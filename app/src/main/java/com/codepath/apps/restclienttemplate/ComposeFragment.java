package com.codepath.apps.restclienttemplate;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.codepath.apps.restclienttemplate.databinding.FragmentComposeBinding;
import com.codepath.apps.restclienttemplate.models.TweetData;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONException;

import okhttp3.Headers;

public class ComposeFragment extends DialogFragment {
    public static final int MAX_TWEET_LENGTH = 280;

    FragmentComposeBinding binding;
    TwitterClient client;
    ComposeFragmentListener listener;
    @Nullable TweetData tweetData;

    public ComposeFragment() {}

    public interface ComposeFragmentListener {
        void onTweetPosted(TweetData tweetData);
    }

    public static ComposeFragment newInstance(ComposeFragmentListener listener) {
        return newInstance(null, listener);
    }

    public static ComposeFragment newInstance(@Nullable TweetData tweetData, ComposeFragmentListener listener) {
        ComposeFragment frag = new ComposeFragment();
        frag.listener = listener;
        frag.tweetData = tweetData;
        return frag;
    }

    private void setResult(TweetData tweetData) {
        listener.onTweetPosted(tweetData);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentComposeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        client = TwitterApp.getRestClient(getContext());

        // If TweetData available, replying to tweet
        binding.rlTweetDataHolder.setVisibility(tweetData != null? View.VISIBLE:View.GONE);
        binding.setTweetData(tweetData);
        if (tweetData != null) Glide.with(this)
                .load(tweetData.userData.profileImageUrl)
                .apply(RequestOptions.circleCropTransform())
                .into(binding.ivProfileImage);

        binding.etComposeInputLayout.setCounterEnabled(true);
        binding.etComposeInputLayout.setCounterMaxLength(MAX_TWEET_LENGTH);

        binding.btnTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tweetContent = binding.etCompose.getText().toString();
                if (tweetContent.isEmpty()) {
                    Toast.makeText(getContext(), R.string.emptyTweetToastWarning, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (tweetContent.length() > MAX_TWEET_LENGTH) {
                    Toast.makeText(getContext(), R.string.longTweetToastWarning, Toast.LENGTH_SHORT).show();
                    return;
                }
                binding.btnTweet.setEnabled(false);
                binding.etCompose.setEnabled(false);
                if (tweetData == null) {
                    client.postTweet(new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Headers headers, JSON json) {
                            try {
                                TweetData tweetData = TweetData.fromJSON(json.jsonObject);
                                setResult(tweetData);
                                dismiss();
                            } catch (JSONException e) {
                                onFailure(statusCode, headers, json.toString(), e);
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                            Toast.makeText(getContext(), R.string.tweetPostFailedToastWarning, Toast.LENGTH_SHORT).show();
                            binding.btnTweet.setEnabled(true);
                            binding.etCompose.setEnabled(true);
                        }
                    }, tweetContent);
                } else {
                    client.postReply(new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Headers headers, JSON json) {
                            try {
                                TweetData tweetData = TweetData.fromJSON(json.jsonObject);
                                setResult(tweetData);
                                dismiss();
                            } catch (JSONException e) {
                                onFailure(statusCode, headers, json.toString(), e);
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                            Toast.makeText(getContext(), R.string.tweetPostFailedToastWarning, Toast.LENGTH_SHORT).show();
                            binding.btnTweet.setEnabled(true);
                            binding.etCompose.setEnabled(true);
                        }
                    }, tweetContent, tweetData);
                }
            }
        });
    }


    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
    }
}
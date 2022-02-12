package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.codepath.apps.restclienttemplate.databinding.ItemTweetBinding;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.utils.TweetActionHelper;

import org.parceler.Parcels;

import java.util.List;


public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.TweetViewHolder> {

    Context context;
    List<Tweet> tweets;

    public TweetsAdapter(Context context, List<Tweet> tweets) {
        this.context = context;
        this.tweets = tweets;
    }

    @NonNull
    @Override
    public TweetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tweet, parent, false);
        return new TweetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TweetViewHolder holder, int position) {
        Tweet tweetData = tweets.get(position);
        holder.bind(tweetData);
    }

    @Override
    public int getItemCount() {
        return tweets.size();
    }

    public void clear() {
        int sizeBeforeClear = tweets.size();
        tweets.clear();
        notifyItemRangeRemoved(0, sizeBeforeClear);
    }

    public void insertTweetsInFront(List<Tweet> tweetList) {
        tweets.addAll(0, tweetList);
        notifyItemRangeInserted(0, tweetList.size());
    }

    public void insertTweetsInBack(List<Tweet> tweetList) {
        int lastIndex = tweets.size();
        tweets.addAll(tweetList);
        notifyItemRangeInserted(lastIndex, tweetList.size());
    }

    public class TweetViewHolder extends RecyclerView.ViewHolder {
        final ItemTweetBinding binding;

        public TweetViewHolder(@NonNull View itemView) {
            super(itemView);

            binding = DataBindingUtil.bind(itemView);
        }

        public void bind(Tweet tweet) {
            binding.setTweet(tweet);


            Glide.with(context)
                    .load(tweet.user.profileImageUrl)
                    .apply(RequestOptions.circleCropTransform())
                    .into(binding.ivProfileImage);

            binding.tweetItemContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(context, DetailedTweetActivity.class);
                    i.putExtra("TweetData", Parcels.wrap(tweet));
                    context.startActivity(i);
                }
            });


            TweetActionHelper.bindLike(binding, binding.ivLikeButton, tweet);
            TweetActionHelper.bindRetweet(binding, binding.ivRetweetButton, tweet);
            TweetActionHelper.bindReply(binding, binding.ivReplyButton, tweet);
        }
    }
}

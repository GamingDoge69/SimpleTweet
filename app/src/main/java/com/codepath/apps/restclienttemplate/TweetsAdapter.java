package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.codepath.apps.restclienttemplate.databinding.ItemTweetBinding;
import com.codepath.apps.restclienttemplate.interfaces.TweetDataModificationListener;
import com.codepath.apps.restclienttemplate.models.TweetData;
import com.codepath.apps.restclienttemplate.utils.TweetActionHelper;

import org.parceler.Parcels;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;


public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.TweetViewHolder> implements TweetDataModificationListener {

    Context context;
    TwitterClient client;
    List<TweetData> tweetData;
    int localTweetCount = 0;
    ActivityResultLauncher<Intent> launchDetailedTweetActivity;
    FragmentManager fragmentManager;

    public TweetsAdapter(Context context, List<TweetData> tweetData, TwitterClient client,
                            ActivityResultLauncher<Intent> launchDetailedTweetActivity,
                            FragmentManager fragmentManager) {
        this.context = context;
        this.tweetData = tweetData;
        this.client = client;
        this.launchDetailedTweetActivity = launchDetailedTweetActivity;
        this.fragmentManager = fragmentManager;

    }

    @NonNull
    @Override
    public TweetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tweet, parent, false);
        return new TweetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TweetViewHolder holder, int position) {
        TweetData tweetData = this.tweetData.get(position);
        holder.bind(tweetData);
    }

    @Override
    public int getItemCount() {
        return tweetData.size();
    }

    public void clear() {
        int sizeBeforeClear = tweetData.size();
        tweetData.clear();
        notifyItemRangeRemoved(0, sizeBeforeClear);
    }

    public void insertTweetsInFront(List<TweetData> tweetDataList) {
        clearLocalTweets();
        tweetData.addAll(0, tweetDataList);
        notifyItemRangeInserted(0, tweetDataList.size());
    }

    public void insertTweetsInBack(List<TweetData> tweetDataList) {
        int lastIndex = tweetData.size();
        tweetData.addAll(tweetDataList);
        notifyItemRangeInserted(lastIndex, tweetDataList.size());
    }

    public String getLatestLiveTweetId() {
        return tweetData.get(localTweetCount).id;
    }

    private void clearLocalTweets() {
        if (localTweetCount == 0) return;
        int notifyRemoved = localTweetCount;
        while (localTweetCount != 0) {
            tweetData.remove(0);
            localTweetCount--;
        }
        notifyItemRangeRemoved(0, notifyRemoved);
    }
    public void addLocalTweet(TweetData tweet) {
        localTweetCount++;
        tweetData.add(0, tweet);
        notifyItemInserted(0);
    }

    @Override
    public void notifyTweetDataModified(TweetData newTweetData) {
        TweetData[] tempTweetDataArray = new TweetData[tweetData.size()];
        int indexOfTweetData = Arrays.binarySearch(tweetData.toArray(tempTweetDataArray), newTweetData, new Comparator<TweetData>() {
            @Override
            public int compare(TweetData tweetData, TweetData t1) {
                return -tweetData.id.compareTo(t1.id);
            }
        });
        if (indexOfTweetData < 0) return;

        tweetData.set(indexOfTweetData, newTweetData);
        notifyItemChanged(indexOfTweetData);
    }

    public class TweetViewHolder extends RecyclerView.ViewHolder {
        final ItemTweetBinding binding;

        public TweetViewHolder(@NonNull View itemView) {
            super(itemView);

            binding = DataBindingUtil.bind(itemView);
        }

        public void bind(TweetData tweetData) {
            binding.setTweetData(tweetData);


            Glide.with(context)
                    .load(tweetData.userData.profileImageUrl)
                    .apply(RequestOptions.circleCropTransform())
                    .into(binding.ivProfileImage);

            binding.tweetItemContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(context, DetailedTweetActivity.class);
                    i.putExtra("tweetData", Parcels.wrap(tweetData));
                    launchDetailedTweetActivity.launch(i);
                }
            });


            TweetActionHelper.bindLike(binding, binding.ivLikeButton, tweetData, client);
            TweetActionHelper.bindRetweet(binding, binding.ivRetweetButton, tweetData, client);
            TweetActionHelper.bindReply(binding, binding.ivReplyButton, tweetData, fragmentManager, TimelineActivity.offlineTweetListener);
        }
    }
}

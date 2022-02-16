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
import com.codepath.apps.restclienttemplate.models.TweetData;
import com.codepath.apps.restclienttemplate.utils.TweetActionHelper;

import org.parceler.Parcels;

import java.util.List;


public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.TweetViewHolder> {

    Context context;
    List<TweetData> tweetData;

    public TweetsAdapter(Context context, List<TweetData> tweetData) {
        this.context = context;
        this.tweetData = tweetData;
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
        tweetData.addAll(0, tweetDataList);
        notifyItemRangeInserted(0, tweetDataList.size());
    }

    public void insertTweetsInBack(List<TweetData> tweetDataList) {
        int lastIndex = tweetData.size();
        tweetData.addAll(tweetDataList);
        notifyItemRangeInserted(lastIndex, tweetDataList.size());
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
                    i.putExtra("TweetData", Parcels.wrap(tweetData));
                    context.startActivity(i);
                }
            });


            TweetActionHelper.bindLike(binding, binding.ivLikeButton, tweetData);
            TweetActionHelper.bindRetweet(binding, binding.ivRetweetButton, tweetData);
            TweetActionHelper.bindReply(binding, binding.ivReplyButton, tweetData);
        }
    }
}

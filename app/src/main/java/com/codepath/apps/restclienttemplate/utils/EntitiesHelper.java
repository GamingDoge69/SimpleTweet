package com.codepath.apps.restclienttemplate.utils;

import static com.codepath.apps.restclienttemplate.models.MediaData.FAILED_MEDIA_DATA;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;

import com.codepath.apps.restclienttemplate.media.MediaFactory;
import com.codepath.apps.restclienttemplate.media.MediaType;
import com.codepath.apps.restclienttemplate.models.EntitiesData;
import com.codepath.apps.restclienttemplate.models.MediaData;
import com.codepath.apps.restclienttemplate.models.TweetData;

import java.util.List;

public class EntitiesHelper {
    public static void insertEntitiesIntoGrid(Context context, GridLayout mediaGrid, TweetData tweetData) {
        mediaGrid.removeAllViews();
        mediaGrid.removeAllViewsInLayout();
        EntitiesData entities = tweetData.entitiesData;
        List<MediaData> mediaDataList;
        if (entities != null && entities.allMediaDataList != null) {
            mediaDataList = entities.allMediaDataList;

            int failedMediaCount = 0;
            for (MediaData mediaData : mediaDataList) failedMediaCount += (mediaData == FAILED_MEDIA_DATA)? 1 : 0;

            int widthSize = mediaGrid.getWidth();
            int interPadding = MeasurementConverter.dpToPx(context, 1);

            int displayListSize = mediaDataList.size() - failedMediaCount;
            mediaGrid.setVisibility(displayListSize != 0? View.VISIBLE : View.GONE);

            mediaGrid.setRowCount(displayListSize <= 2? 1 : 2);
            mediaGrid.setColumnCount(displayListSize <= 1? 1 : 2);
            widthSize /= (displayListSize <= 1) ? 1 : 2;

            for (MediaData mediaData : mediaDataList) {
                if (mediaData.mediaType == MediaType.NOT_SUPPORTED) continue;
                View mediaView = MediaFactory.createMedia(mediaData.mediaType).createView(context, mediaData);
                mediaView.setPadding(interPadding, interPadding, interPadding, interPadding);
                mediaView.setLayoutParams(new ViewGroup.LayoutParams(
                        widthSize,
                        widthSize)
                );
                mediaGrid.addView(mediaView);
            }
        }
    }
}

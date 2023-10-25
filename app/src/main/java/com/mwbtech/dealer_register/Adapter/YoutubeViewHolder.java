package com.mwbtech.dealer_register.Adapter;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.youtube.player.YouTubeThumbnailView;
import com.mwbtech.dealer_register.R;

/**
 * Created by Prerna Sharma on 04-02-2023
 */
public class YoutubeViewHolder extends RecyclerView.ViewHolder {

    public YouTubeThumbnailView videoThumbnailImageView;
    public TextView videoTitle, videoDuration;

    public YoutubeViewHolder(View itemView) {
        super(itemView);
        videoThumbnailImageView = itemView.findViewById(R.id.thumbnail);
        videoTitle = itemView.findViewById(R.id.text);
//        videoDuration = itemView.findViewById(R.id.video_duration_label);
    }
}
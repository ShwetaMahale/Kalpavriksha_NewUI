package com.mwbtech.dealer_register.Adapter;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mwbtech.dealer_register.PojoClass.VideoItems;
import com.mwbtech.dealer_register.R;

import java.util.List;

public class HelpVideoAdapter extends RecyclerView.Adapter<HelpVideoAdapter.ViewHolder> {

    private List<VideoItems> videoItems;
    private Context context;
    int index=0;

    public HelpVideoAdapter(List<VideoItems> videoItems, Context context) {
        this.videoItems = videoItems;
        this.context = context;
    }

    @NonNull
    @Override
    public HelpVideoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final HelpVideoAdapter.ViewHolder holder, int position) {
        VideoItems videoItem = videoItems.get(position);


        try {
            holder.videoName.setText(videoItem.getVideoName());
            String link = videoItem.getVideoURL();
            MediaController mediaController = new MediaController(context);
            mediaController.setAnchorView(holder.videoView);
            Uri video = Uri.parse(link);
            holder.videoView.setMediaController(mediaController);
            holder.videoView.setVideoURI(video);
            holder.previewImaegView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holder.previewImaegView.setVisibility(View.GONE);
                    holder.videoView.start();

                }
            });

            holder.videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {

                    holder.videoView.seekTo(0);
                }
            });

        } catch (Exception e) {
            Toast.makeText(context, "Error connection", Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    public int getItemCount() {
        return videoItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        VideoView videoView;
        ProgressBar progressBar;
        ImageView previewImaegView;
        TextView videoName;
        public ViewHolder(@NonNull View view) {
            super(view);
            videoView = view.findViewById(R.id.videoView);
            progressBar = view.findViewById(R.id.progress);
            previewImaegView = view.findViewById(R.id.previewImageView);
            videoName = view.findViewById(R.id.txtVideo);
        }

    }
}

package com.mwbtech.dealer_register.Dashboard.Support;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.mwbtech.dealer_register.R;

/**
 * Created by Prerna Sharma on 06-02-2023
 */
public class HelpVideoPlayerActivity extends YouTubeBaseActivity {
    private static final String TAG = HelpVideoPlayerActivity.class.getSimpleName();
    private String videoID;
//    private VideoView youTubePlayerView;
    private YouTubePlayerView youTubePlayerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help_video_player_activity);
        //get the video id
        videoID = getIntent().getStringExtra("video_id");
        youTubePlayerView = findViewById(R.id.youtube_player_view);
        initializeYoutubePlayer();

        /*youTubePlayerView.setBackgroundColor(Color.TRANSPARENT);

//        String path = "android.resource://" + getPackageName() + "/raw/" + R.raw.logo_anim;
        youTubePlayerView.setVideoURI(Uri.parse("https://www.youtube.com/watch?v=8Rbl8H7rgKU"));
        youTubePlayerView.setVisibility(View.VISIBLE);
        youTubePlayerView.setOnCompletionListener(mp -> {
            Log.d("PlayVideo", "Video completed");
//            new Handler().postDelayed(this::showAuthNavigationDialog, SPLASH_TIME_OUT);
        });
        youTubePlayerView.setOnErrorListener((mp, what, extra) -> {
            Log.e("PlayVideo", "An Error Occurred While Playing Video");
            return false;
        });
        youTubePlayerView.setOnPreparedListener(mp -> {
            youTubePlayerView.setVisibility(View.VISIBLE);
            youTubePlayerView.setBackgroundColor(Color.TRANSPARENT);
            youTubePlayerView.start();
        });*/
    }

    /**
     * initialize the youtube player
     */
    private void initializeYoutubePlayer() {
        youTubePlayerView.initialize(YoutubeDeveloperKey.DEVELOPER_KEY, new YouTubePlayer.OnInitializedListener() {

            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer,
                                                boolean wasRestored) {

                //if initialization success then load the video id to youtube player
                if (!wasRestored) {
                    //set the player style here: like CHROMELESS, MINIMAL, DEFAULT
                    youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);

                    //load the video
                    youTubePlayer.loadVideo(videoID);
                    youTubePlayer.play();

                    //OR

                    //cue the video
                    //youTubePlayer.cueVideo(videoID);

                    //if you want when activity start it should be in full screen uncomment below comment
                    //  youTubePlayer.setFullscreen(true);

                    //If you want the video should play automatically then uncomment below comment
                    //  youTubePlayer.play();

                    //If you want to control the full screen event you can uncomment the below code
                    //Tell the player you want to control the fullscreen change
//                   player.setFullscreenControlFlags(YouTubePlayer.FULLSCREEN_FLAG_CUSTOM_LAYOUT);
                    //Tell the player how to control the change
                    /*player.setOnFullscreenListener(new YouTubePlayer.OnFullscreenListener() {
                        @Override
                        public void onFullscreen(boolean arg0) {
                            // do full screen stuff here, or don't.
                            Log.e(TAG,"Full screen mode");
                        }
                    });*/

                }
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider arg0, YouTubeInitializationResult arg1) {
                //print or show error if initialization failed
                Log.e(TAG, "Youtube Player View initialization failed");
            }
        });
    }
}

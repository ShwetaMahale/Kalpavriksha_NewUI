package com.mwbtech.dealer_register.Dashboard.Support;

import static com.mwbtech.dealer_register.Dashboard.DashboardActivity.isVideoPlayed;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.youtube.player.YouTubeApiServiceUtil;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.mwbtech.dealer_register.Adapter.AdapterListener;
import com.mwbtech.dealer_register.Adapter.YouTubeVideoAdapter;
import com.mwbtech.dealer_register.Dashboard.DashboardActivity;
import com.mwbtech.dealer_register.PojoClass.VideoItems;
import com.mwbtech.dealer_register.R;

import java.util.ArrayList;

public class HelpVideoListActivity  extends AppCompatActivity implements YouTubePlayer.OnFullscreenListener, AdapterListener {

    /** The duration of the animation sliding up the video in portrait. */
    private static final int ANIMATION_DURATION_MILLIS = 300;
    /** The padding between the video list and the video in landscape orientation. */
    private static final int LANDSCAPE_VIDEO_PADDING_DP = 5;
    private static final int portrait_video_padding = 5;

    /** The request code when calling startActivityForResult to recover from an API service error. */
    private static final int RECOVERY_DIALOG_REQUEST = 1;

//    private VideoListFragment listFragment;
    private RecyclerView recyclerView;
//    private VideoFragment videoFragment;

//    private View videoBox;
//    private View closeButton;
//    private boolean isFullscreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_video_list);

      /*  ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient_color));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
*/
//        listFragment = (VideoListFragment) getFragmentManager().findFragmentById(R.id.list_fragment);
//        videoFragment = (VideoFragment) getFragmentManager().findFragmentById(R.id.video_fragment_container);
//        videoBox = findViewById(R.id.video_box);
//        closeButton = findViewById(R.id.close_button);
//        videoBox.setVisibility(View.INVISIBLE);
//        layout();
//        checkYouTubeApi();
        setUpRecyclerView();
        populateRecyclerView();

        findViewById(R.id.info_ic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HelpVideoListActivity.this,Pdfhelp.class));

            }
        });  findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onArrowBackPressed();
                finish();
            }
        });

    }


    /**
     * setup the recyclerview here
     */
    private void setUpRecyclerView() {
        recyclerView = findViewById(R.id.videos_recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    /**
     * populate the recyclerview and implement the click event here
     */
    private void populateRecyclerView() {
        final ArrayList<VideoItems> youtubeVideoModelArrayList = generateDummyVideoList();
        YouTubeVideoAdapter adapter = new YouTubeVideoAdapter(this, youtubeVideoModelArrayList, this);
        recyclerView.setAdapter(adapter);
    }

    private ArrayList<VideoItems> generateDummyVideoList() {
        ArrayList<VideoItems> videoList = new ArrayList<>();
//        videoList.add(new VideoItems("How to register and complete your KYC in app ?", "V8E4brErGPg"));
//        videoList.add(new VideoItems("How to login with fingerprint ?", "ty8Ln7SbBbM"));
//        videoList.add(new VideoItems("How to login with password ?", "uXJcQoEbwHA"));
        videoList.add(new VideoItems("How to send New enquiry ?", "8Rbl8H7rgKU"));
        videoList.add(new VideoItems("How to send Group enquiry ?", "euYDVbArKBY"));
        videoList.add(new VideoItems("How to check received enquiry ?", "k9O0hzWSBew"));
//        videoList.add(new VideoItems("How to change your password if forgotten ?", "70MzjJ6OIJI"));
//        videoList.add(new VideoItems("How to mark chat as favorite ?", "YccvoMkXlms"));
//        videoList.add(new VideoItems("How to Filter and Delete chat?", "nc6oBc-j76A"));
//        videoList.add(new VideoItems("How to mark chat as read / unread ?", "qR5vAZ_0TFY"));
//        videoList.add(new VideoItems("How to reach us ?", "F-UGz0I-29s"));
        videoList.add(new VideoItems("How to Book Advertisement ?", "FAWpSc1mEl8"));
        videoList.add(new VideoItems("How to check your sent enquiry ?", "cX79VwkovdQ"));
//        videoList.add(new VideoItems("How to reset your password ?", "U0I66Z_meco"));
//        videoList.add(new VideoItems("How to logout from the Application ?", "-bnJNQft-80"));
        return videoList/*Collections.unmodifiableList(videoList)*/;
    }

    public void onArrowBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, DashboardActivity.class).putExtra("isNewUser", false);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        HelpVideoListActivity.this.finish();
    }

    private void checkYouTubeApi() {
        YouTubeInitializationResult errorReason =
                YouTubeApiServiceUtil.isYouTubeApiServiceAvailable(this);
        if (errorReason.isUserRecoverableError()) {
            errorReason.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show();
        } else if (errorReason != YouTubeInitializationResult.SUCCESS) {
            String errorMessage =
                    String.format(getString(R.string.error_player), errorReason.toString());
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RECOVERY_DIALOG_REQUEST) {
            // Recreate the activity if user performed a recovery action
            recreate();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT)
        {
            // handle change here
//            layout();
        }

    }

    @Override
    public void onFullscreen(boolean isFullscreen) {
//        this.isFullscreen = isFullscreen;
//        layout();
    }

    private void layout() {
        /*boolean isPortrait =
                getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
        listFragment.getView().setVisibility(isFullscreen ? View.GONE : View.VISIBLE);
        listFragment.setLabelVisibility(isPortrait);
        closeButton.setVisibility(isPortrait ? View.VISIBLE : View.GONE);

        if (isFullscreen) {
            videoBox.setTranslationY(0); // Reset any translation that was applied in portrait.
            setLayoutSize(videoFragment.getView(), MATCH_PARENT, MATCH_PARENT);
            setLayoutSizeAndGravity(videoBox, MATCH_PARENT, MATCH_PARENT, Gravity.TOP | Gravity.LEFT);
        } else if (isPortrait) {
            videoBox.setTranslationY(0); // Reset any translation that was applied in portrait.
            int screenWidth = dpToPx(getResources().getConfiguration().screenWidthDp);
            setLayoutSize(listFragment.getView(), screenWidth / 4, MATCH_PARENT);
            setLayoutSize(listFragment.getView(), MATCH_PARENT, MATCH_PARENT);
            setLayoutSize(videoFragment.getView(), MATCH_PARENT, MATCH_PARENT);
            setLayoutSizeAndGravity(videoBox, MATCH_PARENT, MATCH_PARENT, Gravity.BOTTOM);
        } else {
            videoBox.setTranslationY(0); // Reset any translation that was applied in portrait.
            int screenWidth = dpToPx(getResources().getConfiguration().screenWidthDp);
            setLayoutSize(listFragment.getView(), screenWidth / 4, MATCH_PARENT);
            int videoWidth = screenWidth - screenWidth / 4 - dpToPx(portrait_video_padding);
            setLayoutSize(videoFragment.getView(), videoWidth, WRAP_CONTENT);
            setLayoutSizeAndGravity(videoBox, videoWidth, WRAP_CONTENT,
                    Gravity.RIGHT | Gravity.CENTER_VERTICAL);
        }*/
    }

    public void onClickClose(@SuppressWarnings("unused") View view) {
        isVideoPlayed=false;
//        listFragment.getListView().clearChoices();
//        listFragment.getListView().requestLayout();
//        videoFragment.pause();
        /*ViewPropertyAnimator animator = videoBox.animate()
                .translationYBy(videoBox.getHeight())
                .setDuration(ANIMATION_DURATION_MILLIS);
        runOnAnimationEnd(animator, new Runnable() {
            @Override
            public void run() {
                videoBox.setVisibility(View.INVISIBLE);
            }
        });*/
    }

    private void runOnAnimationEnd(ViewPropertyAnimator animator, final Runnable runnable) {
        if (Build.VERSION.SDK_INT >= 16) {
            animator.withEndAction(runnable);
        } else {
            animator.setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    runnable.run();
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
       if (isVideoPlayed)
       {
//           listFragment.getListView().clearChoices();
//           listFragment.getListView().requestLayout();
//           videoBox.setVisibility(View.INVISIBLE);
           isVideoPlayed=false;
       }else
       {
           super.onBackPressed();
           Intent intent = new Intent(this, DashboardActivity.class).putExtra("isNewUser", false);
           intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
           intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
           startActivity(intent);
           HelpVideoListActivity.this.finish();
       }

    }
    public boolean onCreateOptionsMenu( Menu menu ) {

        getMenuInflater().inflate(R.menu.help_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onItemTapped(VideoItems videoItems) {
        //start youtube player activity by passing selected video id via intent
        startActivity(new Intent(this, HelpVideoPlayerActivity.class)
                .putExtra("video_id", videoItems.getVideoURL()));
    }

    /**
     * A fragment that shows a static list of videos.
     */
    /*public static final class VideoListFragment extends ListFragment {
        public static List<VideoItems> VIDEO_LIST;
        public PageAdapter adapter;
        private View videoBox;
        private EditText edSearch;
        Customer_Interface customer_interface;
        List<VideoItems> videoItems=new ArrayList<>();
        List<VideoItems> list = new ArrayList<VideoItems>();
        String Token;
        PrefManager prefManager;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            list.add(new VideoItems("How to register and complete your KYC in app ?", "V8E4brErGPg"));
            list.add(new VideoItems("How to login with fingerprint ?", "ty8Ln7SbBbM"));
            list.add(new VideoItems("How to login with password ?", "uXJcQoEbwHA"));
            list.add(new VideoItems("How to send New enquiry ?", "8Rbl8H7rgKU"));
            list.add(new VideoItems("How to send Group enquiry ?", "euYDVbArKBY"));
            list.add(new VideoItems("How to check received enquiry ?", "k9O0hzWSBew"));
            list.add(new VideoItems("How to change your password if forgotten ?", "70MzjJ6OIJI"));
            list.add(new VideoItems("How to mark chat as favorite ?", "YccvoMkXlms"));
            list.add(new VideoItems("How to Filter and Delete chat?", "nc6oBc-j76A"));
            list.add(new VideoItems("How to mark chat as read / unread ?", "qR5vAZ_0TFY"));
            list.add(new VideoItems("How to reach us ?", "F-UGz0I-29s"));
            list.add(new VideoItems("How to Book Advertisement ?", "FAWpSc1mEl8"));
            list.add(new VideoItems("How to check your sent enquiry ?", "cX79VwkovdQ"));
            list.add(new VideoItems("How to reset your password ?", "U0I66Z_meco"));
            list.add(new VideoItems("How to logout from the Application ?", "-bnJNQft-80"));
            VIDEO_LIST = Collections.unmodifiableList(list);
            adapter = new PageAdapter(getActivity(),VIDEO_LIST);
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            videoBox = getActivity().findViewById(R.id.video_box);
            getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            setListAdapter(adapter);
        }

        @Override
        public void onListItemClick(ListView l, View v, int position, long id) {
            isVideoPlayed = true;
            String videoId = VIDEO_LIST.get(position).getVideoURL();

            VideoFragment videoFragment =
                    (VideoFragment) getFragmentManager().findFragmentById(R.id.video_fragment_container);
            videoFragment.setVideoId(videoId);

            // The videoBox is INVISIBLE if no video was previously selected, so we need to show it now.
            if (videoBox.getVisibility() != View.VISIBLE) {
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                    // Initially translate off the screen so that it can be animated in from below.
                    videoBox.setTranslationY(videoBox.getHeight());
                }
                videoBox.setVisibility(View.VISIBLE);
            }

            // If the fragment is off the screen, we animate it in.
            if (videoBox.getTranslationY() > 0) {
                videoBox.animate().translationY(0).setDuration(ANIMATION_DURATION_MILLIS);
            }
        }

        @Override
        public void onDestroyView() {
            super.onDestroyView();

            adapter.releaseLoaders();
        }

        public void setLabelVisibility(boolean visible) {
            adapter.setLabelVisibility(visible);
        }
    }*/

    /**
     * Adapter for the video list. Manages a set of YouTubeThumbnailViews, including initializing each
     * of them only once and keeping track of the loader of each one. When the ListFragment gets
     * destroyed it releases all the loaders.
     */
    /*private static final class PageAdapter extends BaseAdapter {

        private final List<VideoItems> entries;
        private final List<View> entryViews;
        private final Map<YouTubeThumbnailView, YouTubeThumbnailLoader> thumbnailViewToLoaderMap;
        private final LayoutInflater inflater;
        public static List<VideoItems> list2 ;
        private final ThumbnailListener thumbnailListener;

        private boolean labelsVisible;

        public PageAdapter(Context context, List<VideoItems> entries) {
            this.entries = entries;

            entryViews = new ArrayList<View>();
            thumbnailViewToLoaderMap = new HashMap<YouTubeThumbnailView, YouTubeThumbnailLoader>();
            inflater = LayoutInflater.from(context);
            thumbnailListener = new ThumbnailListener();

            labelsVisible = true;
        }

        public void releaseLoaders() {
            for (YouTubeThumbnailLoader loader : thumbnailViewToLoaderMap.values()) {
                loader.release();
            }
        }

        public void setLabelVisibility(boolean visible) {
            labelsVisible = visible;
            for (View view : entryViews) {
                view.findViewById(R.id.text).setVisibility(visible ? View.VISIBLE : View.GONE);
            }
        }

        @Override
        public int getCount() {
            return entries.size();
        }

        @Override
        public VideoItems getItem(int position) {
            return entries.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }
        public Filter getFilter() {
            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence charSequence) {
                    String charString = charSequence.toString();
                    if (charString.isEmpty()) {

                    } else {
                        ArrayList<VideoItems> filteredList = new ArrayList<>();
                        for (VideoItems row : list2) {
                            if(row.getVideoName().toLowerCase().contains(charString.toLowerCase()))
                                filteredList.add(row);
                        }
                    }
                    FilterResults filterResults = new FilterResults();
                    filterResults.values = list2;
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

                }
            };
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            VideoItems entry = entries.get(position);

            // There are three cases here
            if (view == null) {
                // 1) The view has not yet been created - we need to initialize the YouTubeThumbnailView.
                view = inflater.inflate(R.layout.video_list_item, parent, false);
                YouTubeThumbnailView thumbnail = (YouTubeThumbnailView) view.findViewById(R.id.thumbnail);
                thumbnail.setTag(entry.getVideoURL());
                thumbnail.initialize(YoutubeDeveloperKey.DEVELOPER_KEY, thumbnailListener);
            } else {
                YouTubeThumbnailView thumbnail = (YouTubeThumbnailView) view.findViewById(R.id.thumbnail);
                YouTubeThumbnailLoader loader = thumbnailViewToLoaderMap.get(thumbnail);
                if (loader == null) {
                    // 2) The view is already created, and is currently being initialized. We store the
                    //    current videoId in the tag.
                    thumbnail.setTag(entry.getVideoURL());
                } else {
                    // 3) The view is already created and already initialized. Simply set the right videoId
                    //    on the loader.
                    thumbnail.setImageResource(R.drawable.loading_thumbnail);
                    loader.setVideo(entry.getVideoURL());
                }
            }
            TextView label = ((TextView) view.findViewById(R.id.text));
            label.setText(entry.getVideoName());
            label.setVisibility(labelsVisible ? View.VISIBLE : View.GONE);

            //TextView description=((TextView) view.findViewById(R.id.description));
            //description.setText(entry.getPdfurl());
            //description.setVisibility(labelsVisible ? View.VISIBLE : View.GONE);
//            description.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                   startNew();
//                }
//            });
            return view;
        }

        private final class ThumbnailListener implements
                YouTubeThumbnailView.OnInitializedListener,
                YouTubeThumbnailLoader.OnThumbnailLoadedListener {

            @Override
            public void onInitializationSuccess(
                    YouTubeThumbnailView view, YouTubeThumbnailLoader loader) {
                loader.setOnThumbnailLoadedListener(this);
                thumbnailViewToLoaderMap.put(view, loader);
                view.setImageResource(R.drawable.loading_thumbnail);
                String videoId = (String) view.getTag();
                loader.setVideo(videoId);
            }

            @Override
            public void onInitializationFailure(
                    YouTubeThumbnailView view, YouTubeInitializationResult loader) {
                view.setImageResource(R.drawable.no_thumbnail);
            }

            @Override
            public void onThumbnailLoaded(YouTubeThumbnailView view, String videoId) {
            }

            @Override
            public void onThumbnailError(YouTubeThumbnailView view, YouTubeThumbnailLoader.ErrorReason errorReason) {
                view.setImageResource(R.drawable.no_thumbnail);
            }
        }

    }*/

    /*public static final class VideoFragment extends YouTubePlayerFragment
            implements YouTubePlayer.OnInitializedListener {

        private YouTubePlayer player;
        private String videoId;

        public static VideoFragment newInstance() {
            return new VideoFragment();
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            initialize(YoutubeDeveloperKey.DEVELOPER_KEY, this);
        }

        @Override
        public void onDestroy() {
            if (player != null) {
                player.release();
            }
            super.onDestroy();
        }

        public void setVideoId(String videoId) {
            if (videoId != null && !videoId.equals(this.videoId)) {
                this.videoId = videoId;
                if (player != null) {
                    player.cueVideo(videoId);
                }
            }
        }

        public void pause() {
            if (player != null) {
                player.pause();
            }
        }

        @Override
        public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean restored) {
            this.player = player;
            player.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_CUSTOM_LAYOUT);
            player.setOnFullscreenListener((HelpVideoListActivity) getActivity());
            if (!restored && videoId != null) {
                player.cueVideo(videoId);
            }
        }

        @Override
        public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult result) {
            this.player = null;
        }
    }*/

    private static final class VideoEntry {
        private final String text;
        private final String videoId;

        public VideoEntry(String text, String videoId) {
            this.text = text;
            this.videoId = videoId;
        }
    }

    // Utility methods for layouting.

    private int dpToPx(int dp) {
        return (int) (dp * getResources().getDisplayMetrics().density + 0.5f);
    }

    private static void setLayoutSize(View view, int width, int height) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.width = width;
        params.height = height;
        view.setLayoutParams(params);
    }

    private static void setLayoutSizeAndGravity(View view, int width, int height, int gravity) {
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
        params.width = width;
        params.height = height;
        params.gravity = gravity;
        view.setLayoutParams(params);
    }

}
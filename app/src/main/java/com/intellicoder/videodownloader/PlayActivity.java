package com.intellicoder.videodownloader;

import android.app.PictureInPictureParams;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Rational;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player.EventListener;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.LoopingMediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.measurement.api.AppMeasurementSdk.ConditionalUserProperty;
import com.intellicoder.videodownloader.databinding.ActivityPlayBinding;

public class PlayActivity extends AppCompatActivity {
    private static final int REQUEST_WRITE_PERMISSION = 999;
    private static final String SHARE_ID = "com.android.all";

    PictureInPictureParams.Builder pictureInPictureParamsBuilder;

    public Boolean playing;
    private final Boolean downloading;
    private String extension;
    private boolean firstcall = true;

    private String name;
    private SimpleExoPlayer player;
    private String title;
    private DefaultTrackSelector trackSelector;
    private String urlToDownload;
    private String videourl;

    private final boolean visibleuser;
    private ActivityPlayBinding binding;
    private ImageView exo_pause;
    public ImageView exo_play;

    public PlayActivity() {
        Boolean valueOf = Boolean.valueOf(false);
        this.downloading = valueOf;
        this.visibleuser = false;
        this.playing = valueOf;
    }


    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        binding = ActivityPlayBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(view);

        this.name = getIntent().getStringExtra(ConditionalUserProperty.NAME);
        this.videourl = getIntent().getStringExtra("videourl");
        this.exo_pause = findViewById(R.id.exo_pause);
        this.exo_play = findViewById(R.id.exo_play);

        initplayer();
        binding.back.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                PlayActivity.this.onBackPressed();
            }
        });

        binding.imgPictureinpicture.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                startPictureInPictureFeature();
            }
        });
    }

    private void initplayer() {


        String str = this.videourl;
        this.urlToDownload = str;
        this.title = this.name;
        this.extension = "mp4";
        binding.videoName.setText(this.name);
        ThumbnailUtils.createVideoThumbnail(this.videourl, 1);

        this.firstcall = false;
        Uri parse = Uri.parse(this.videourl);
        this.player = ExoPlayerFactory.newSimpleInstance(this, new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(new DefaultBandwidthMeter())));
        binding.videoView.setPlayer(this.player);
        ExtractorMediaSource extractorMediaSource = new ExtractorMediaSource(parse, new DefaultDataSourceFactory(this, Util.getUserAgent(this, "CloudinaryExoplayer")), new DefaultExtractorsFactory(), null, null);
        this.player.prepare(new LoopingMediaSource(extractorMediaSource));
        this.player.setPlayWhenReady(true);
//        this.exo_play.setVisibility(View.GONE);
        binding.videoView.setControllerShowTimeoutMs(ConnectionResult.DRIVE_EXTERNAL_STORAGE_REQUIRED);
        this.player.addListener(new EventListener() {
            public void onLoadingChanged(boolean z) {
            }

            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
            }

            public void onPlayerError(ExoPlaybackException exoPlaybackException) {
            }

            public void onPositionDiscontinuity(int i) {
            }

            public void onRepeatModeChanged(int i) {
            }


            public void onShuffleModeEnabledChanged(boolean z) {
            }

            public void onTimelineChanged(Timeline timeline, Object obj, int i) {
            }

            public void onTracksChanged(TrackGroupArray trackGroupArray, TrackSelectionArray trackSelectionArray) {
            }

            public void onPlayerStateChanged(boolean z, int i) {
                if (i == 3) {
                    try {
                        binding.loader.setVisibility(View.GONE);
                        binding.videoView.setVisibility(View.VISIBLE);
                        binding.videoView.showController();
//                        PlayActivity.this.exo_play.setVisibility(View.GONE);
                        PlayActivity.this.playing = Boolean.valueOf(true);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return;
                    }
                } else if (i == 2) {
                    binding.loader.setVisibility(View.VISIBLE);
                    binding.videoView.showController();
                    binding.videoView.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    public void onResume() {
        super.onResume();
        if (this.player == null && this.playing.booleanValue()) {
            initplayer();
        }
    }

    public void onDestroy() {
        super.onDestroy();
        releaseplayer();
    }

    public void onPause() {
        super.onPause();
        // releaseplayer();

        // If called while in PIP mode, do not pause playback
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (isInPictureInPictureMode()) {
                // Continue playback

            } else {
                // Use existing playback logic for paused Activity behavior.
                releaseplayer();
            }
        }

    }

    private void releaseplayer() {
        SimpleExoPlayer simpleExoPlayer = this.player;
        if (simpleExoPlayer != null) {
            simpleExoPlayer.release();
            this.player = null;
            this.trackSelector = null;
        }
    }

    public void onStop() {
        super.onStop();
        releaseplayer();
    }

    public void onBackPressed() {
        super.onBackPressed();
    }


    @Override
    protected void onNewIntent(Intent intent) {

        this.name = intent.getStringExtra(ConditionalUserProperty.NAME);
        this.videourl = intent.getStringExtra("videourl");

        releaseplayer();
        initplayer();

        super.onNewIntent(intent);
    }

    private void startPictureInPictureFeature() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

//            Rational aspectRatio = new Rational(2, 1);

            pictureInPictureParamsBuilder = new PictureInPictureParams.Builder();

            Rational aspectRatio = new Rational(16, 9);

            pictureInPictureParamsBuilder.setAspectRatio(aspectRatio).build();

            enterPictureInPictureMode(pictureInPictureParamsBuilder.build());
        }
    }

    @Override
    public void onUserLeaveHint() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (!isInPictureInPictureMode()) {
                Rational aspectRatio = new Rational(binding.videoView.getWidth(), binding.videoView.getHeight());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    try {
                        pictureInPictureParamsBuilder.setAspectRatio(aspectRatio).build();

                        enterPictureInPictureMode(pictureInPictureParamsBuilder.build());

                        releaseplayer();
                    } catch (Exception e) {
                        Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    @Override
    public void onPictureInPictureModeChanged(boolean isInPictureInPictureMode,
                                              Configuration newConfig) {
        if (isInPictureInPictureMode) {


        } else {


        }
    }


}

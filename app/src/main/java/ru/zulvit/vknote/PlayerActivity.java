package ru.zulvit.vknote;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.PlaybackParams;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.chip.Chip;

import java.io.IOException;

public class PlayerActivity extends AppCompatActivity {
    private MediaPlayer player;
    private boolean playerState;
    private ImageButton buttonPlay;
    private ImageButton buttonBackward;
    private ImageButton buttonForward;
    private Chip chip;
    private SeekBar seekBar;
    private Handler handler = new Handler();
    private static int JUMP_VALUE = 1000;
    private static float playSpeed = 1f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        buttonPlay = findViewById(R.id.btnPlay);
        buttonBackward = findViewById(R.id.btnBackward);
        buttonForward = findViewById(R.id.btnForward);
        chip = findViewById(R.id.chip);
        seekBar = findViewById(R.id.seekBar);

        Intent intent = getIntent();
        String header = intent.getStringExtra("noteHeading");
        String description = intent.getStringExtra("noteDescription");
        Log.d(PlayerActivity.class.getName(), header);
        Log.d(PlayerActivity.class.getName(), description);

        Context context = getApplicationContext();
        String dirPath = context.getFilesDir().getAbsolutePath() + "/notes/" + header;

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (player != null && fromUser) {
                    player.seekTo(progress * 1000);
                }
            }
        });

        PlayerActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (player != null) {
                    int mCurrentPosition = player.getCurrentPosition() / 1000;
                    seekBar.setProgress(mCurrentPosition);
                }
                handler.postDelayed(this, 1000);
            }
        });

        buttonPlay.setOnClickListener(view -> {
            if (!playerState) {
                playStart(dirPath);
            } else {
                playStop();
            }
        });

        buttonForward.setOnClickListener(view -> {
            player.seekTo(player.getCurrentPosition() + JUMP_VALUE);
        });

        buttonBackward.setOnClickListener(view -> {
            player.seekTo(player.getCurrentPosition() - JUMP_VALUE);
        });

        chip.setOnClickListener(view -> {
            if (playSpeed != 2f) {
                playSpeed += 0.5f;
            } else {
                playSpeed = 0.5f;
            }
            player.setPlaybackParams(new PlaybackParams().setSpeed(playSpeed));
            chip.setText("x " + playSpeed);
        });

        playStart(dirPath);
    }

    private void playStart(String dirPath) {
        playerState = true;

        player = new MediaPlayer();
        try {
            player.setDataSource(dirPath);
            Log.d("Prepare:", dirPath);
            player.prepare();
            player.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        seekBar.setMax(player.getDuration() / 1000);
    }

    private void playStop() {
        playerState = false;
        player.stop();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (player != null) {
            player.stop();
            player.release();
            player = null;
        }
    }
}
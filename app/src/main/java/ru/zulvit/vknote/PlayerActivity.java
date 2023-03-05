package ru.zulvit.vknote;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
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
    private Chip speedChip;
    private SeekBar seekBar;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        buttonPlay = findViewById(R.id.btnPlay);
        buttonBackward = findViewById(R.id.btnBackward);
        buttonForward = findViewById(R.id.btnForward);
        speedChip = findViewById(R.id.chip);
        seekBar = findViewById(R.id.seekBar);

        Intent intent = getIntent();
        String header = intent.getStringExtra("noteHeading");
        String description = intent.getStringExtra("noteDescription");
        Log.d(PlayerActivity.class.getName(), header);
        Log.d(PlayerActivity.class.getName(), description);

        Context context = getApplicationContext();
        String dirPath = context.getFilesDir().getAbsolutePath() + "/notes/" + header;

        buttonPlay.setOnClickListener(view -> {
            if (!playerState) {
                playStart(dirPath);
            } else {
                playStop();
            }
        });
        playStart(dirPath);
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

        playStart(dirPath);
        seekBar.setMax(player.getDuration());
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
        seekBar.setMax(player.getDuration() / 1000); // where mFileDuration is mMediaPlayer.getDuration();
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
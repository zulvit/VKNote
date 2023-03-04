package ru.zulvit.vknote;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private MediaRecorder recorder;
    private MediaPlayer player;
    private String fileName;
    private boolean recordState;
    private boolean playerState;

    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private boolean permissionToRecordAccepted = false;
    private final String[] permissions = {Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE};

    private static final int SAMPLE_RATE = 44100;
    private static final int BIT_RATE = 96000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);

        fileName = getExternalCacheDir().getAbsolutePath();
        fileName += "/audiorecordtest.m4a";
        Button buttonRecord = findViewById(R.id.button_record);
        Button buttonPlay = findViewById(R.id.button_play);
        playerState = false;
        recordState = false;

        buttonRecord.setOnClickListener(view -> {
            if (!recordState) {
                if (permissionToRecordAccepted) {
                    recordStart();
                }
                recordState = true;
            } else {
                recordStop();
                recordState = false;
            }
        });

        buttonPlay.setOnClickListener(view -> {
            if (!playerState) {
                playStart();
                playerState = true;
            } else {
                playStop();
                playerState = false;
            }
        });
    }

    private void recordStart() {
        try {
            Context context = getApplicationContext();
            String dirPath = context.getFilesDir().getAbsolutePath();
            File dir = new File(dirPath + "/notes");
            if (dir.mkdirs()) {
                Log.d(MainActivity.class.getName(), "note dir created.");
            }
            File file = new File(dir, "audioTest.m4a");
            recorder = new MediaRecorder();
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            recorder.setAudioSamplingRate(SAMPLE_RATE);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            recorder.setAudioEncodingBitRate(BIT_RATE);
            recorder.setOutputFile(file.getAbsolutePath());
            recorder.prepare();
            recorder.start();
            Log.d(MainActivity.class.getName(), fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void recordStop() {
        recorder.stop();
        recorder.release();
        recorder = null;
    }

    private void playStart() {
        player = new MediaPlayer();
        try {
            player.setDataSource(fileName);
            player.prepare();
            player.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void playStop() {
        player.release();
        player = null;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionToRecordAccepted) finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (recorder != null) {
            recorder.release();
            recorder = null;
        }

        if (player != null) {
            player.release();
            player = null;
        }
    }
}
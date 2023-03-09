package ru.zulvit.vknote;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Chronometer;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    private MediaRecorder recorder;
    private Chronometer chronometer;
    private boolean recordState;
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private boolean permissionToRecordAccepted = false;
    private final String[] permissions = {Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE};
    private static final int SAMPLE_RATE = 44100;
    private static final int BIT_RATE = 96000;
    private String dirPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);

        FloatingActionButton floatingActionButton = findViewById(R.id.button_record);
        chronometer = findViewById(R.id.chronometer);

        Context context = getApplicationContext();
        dirPath = context.getFilesDir().getAbsolutePath() + "/notes/";

        recordState = false;

        replaceFragment(new NotesFragment());

        floatingActionButton.setOnClickListener(view -> {
                    if (!recordState) {
                        if (permissionToRecordAccepted) {
                            chronometer.start();
                            recordStart();
                        }
                        recordState = true;
                    } else {
                        recordStop();
                        recordState = false;
                        showBottomSheet();
                    }
                }
        );
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }

    private void showBottomSheet() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(MainActivity.this);
        View bottomSheetView = LayoutInflater.from(getApplicationContext())
                .inflate(R.layout.bottom_sheet,
                        findViewById(R.id.bottomSheetContainer));
        bottomSheetDialog.setContentView(bottomSheetView);
        EditText editText = bottomSheetDialog.findViewById(R.id.editTextNoteName);
        assert editText != null;
        editText.setText(getDataAndTime());
        bottomSheetDialog.show();

        bottomSheetView.findViewById(R.id.buttonSheetSave).setOnClickListener(view -> {
            File lastCreatedFile = getLastCreatedFile();
            if (lastCreatedFile != null) {
                File renamedLastFile = new File(dirPath + editText.getText());
                boolean success = lastCreatedFile.renameTo(renamedLastFile);
                if (success) {
                    Log.d("Saving...", "success");
                } else {
                    Log.d("Saving...", "file err");
                }
            }
            bottomSheetDialog.cancel();
            replaceFragment(new NotesFragment());
        });

        bottomSheetView.findViewById(R.id.buttonSheetCancel).setOnClickListener(view -> {
            File lastCreatedFile = getLastCreatedFile();
            boolean success = lastCreatedFile.delete();
            if (success) {
                Log.d("Canceling...", "success");
            } else {
                Log.d("Canceling...", "err");
            }
            bottomSheetDialog.cancel();
            replaceFragment(new NotesFragment());
        });
    }

    private File getLastCreatedFile() {
        File folder = new File(dirPath);
        File[] folderFiles = folder.listFiles();
        File lastCreatedFile = null;
        long newestTime = Long.MIN_VALUE;
        if (folderFiles != null) {
            for (File file : folderFiles) {
                if (file.isFile() && file.lastModified() > newestTime) {
                    lastCreatedFile = file;
                    newestTime = file.lastModified();
                }
            }
        }
        return lastCreatedFile;
    }

    private String getDataAndTime() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        return year + "-" + month + "-" + day + "(" +
                hour + "-" + minute + "-" + second + ")";
    }

    private void recordStart() {
        String titleNote = getDataAndTime();
        File dir = new File(dirPath);
        if (dir.mkdirs()) {
            dirPath += titleNote;
            Log.d(MainActivity.class.getName(), dirPath);
            Log.d(MainActivity.class.getName(), "note dir created");
        }
        File file = new File(dir, titleNote + ".m4a");
        try {
            chronometer.setBase(SystemClock.elapsedRealtime());
            chronometer.start();
            recorder = new MediaRecorder();
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            recorder.setAudioSamplingRate(SAMPLE_RATE);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            recorder.setAudioEncodingBitRate(BIT_RATE);
            recorder.setOutputFile(file.getAbsolutePath());
            recorder.prepare();
            recorder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void recordStop() {
        chronometer.stop();
        chronometer.setBase(SystemClock.elapsedRealtime());
        recorder.stop();
        recorder.release();
        recorder = null;
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
    }
}
package com.example.cnit355_lab9m1_junsu_yoon;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    Button buttonPlay;   // id: button1
    Button buttonStop;   // id: button2
    SeekBar seekbar;
    PlayMusic music;

    private boolean receiverRegistered = false;

    private final BroadcastReceiver appSmsReceiver = new BroadcastReceiver() {
        @Override public void onReceive(Context context, Intent intent) {
            if (!MyReceiver.ACTION_APP_SMS.equals(intent.getAction())) return;
            String msg = intent.getStringExtra(MyReceiver.EXTRA_MSG);
            Toast.makeText(context, "SMS received: " + (msg == null ? "" : msg), Toast.LENGTH_SHORT).show();
            if (music != null) music.pause();
        }
    };

    private final ActivityResultLauncher<String[]> permLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), r -> {});

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonPlay = findViewById(R.id.button1);
        buttonStop = findViewById(R.id.button2);
        seekbar = findViewById(R.id.seekBar1);

        music = new PlayMusic(this, R.raw.music); // res/raw/music.mp3 required
        music.setOnProgress(new PlayMusic.OnProgress() {
            @Override public void onTick(int positionMs) { seekbar.setProgress(positionMs); }
            @Override public void onReady(int durationMs) { seekbar.setMax(durationMs); }
            @Override public void onCompleted() {
                seekbar.setProgress(0);
                Toast.makeText(MainActivity.this, "Playback completed", Toast.LENGTH_SHORT).show();
            }
        });

        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser && music != null) music.seekTo(progress);
            }
        });

        buttonPlay.setOnClickListener(this::playMethod);
        buttonStop.setOnClickListener(this::stopMethod);

        // check if raw/music.mp3 exists
        if (!music.prepareIfNeeded()) {
            Toast.makeText(this, "Please put music.mp3 into res/raw/", Toast.LENGTH_LONG).show();
            Log.e(TAG, "music resource missing: put app/src/main/res/raw/music.mp3");
        }

        // request SMS permission if needed
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            permLauncher.launch(new String[]{Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS});
        }
    }

    public void playMethod(View v){
        if (!music.prepareIfNeeded()) {
            Toast.makeText(this, "Failed to load the music file", Toast.LENGTH_SHORT).show();
            return;
        }
        music.play();
        Toast.makeText(this, "Music started", Toast.LENGTH_SHORT).show();
    }

    public void stopMethod(View v) {
        if (music != null) {
            music.stop();
            seekbar.setProgress(0);
            Toast.makeText(this, "Music stopped", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!receiverRegistered) {
            IntentFilter f = new IntentFilter(MyReceiver.ACTION_APP_SMS);
            
            if (android.os.Build.VERSION.SDK_INT >= 33) {
                registerReceiver(appSmsReceiver, f, Context.RECEIVER_NOT_EXPORTED);
            } else {
                registerReceiver(appSmsReceiver, f);
            }
            receiverRegistered = true;
        }
    }


    @Override protected void onStop() {
        super.onStop();
        if (receiverRegistered) {
            try { unregisterReceiver(appSmsReceiver); } catch (IllegalArgumentException e) {
                Log.w(TAG, "Receiver already unregistered", e);
            }
            receiverRegistered = false;
        }
    }

    @Override protected void onDestroy() {
        if (music != null) music.release();
        super.onDestroy();
    }
}

package com.example.cnit355_lab8m1_junsu_yoon;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Spinner spinnerMusic;
    private Button btnPlay, btnStop;
    private Intent serviceIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinnerMusic = findViewById(R.id.spinnerMusic);
        btnPlay = findViewById(R.id.btnPlay);
        btnStop = findViewById(R.id.btnStop);

        // 🎵 raw 폴더 안의 mp3 파일 이름 (확장자 빼고!)
        String[] musicList = {"music1", "music2","music3", "music4"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                musicList
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMusic.setAdapter(adapter);

        // Service 실행용 Intent
        serviceIntent = new Intent(this, MyService.class);

        // ▶ Play 버튼
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selected = spinnerMusic.getSelectedItem().toString();
                serviceIntent.putExtra("music", selected);
                startService(serviceIntent);
                Toast.makeText(getApplicationContext(),
                        "Playing " + selected, Toast.LENGTH_SHORT).show();
            }
        });

        // ⏹ Stop 버튼
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopService(serviceIntent);
                Toast.makeText(getApplicationContext(),
                        "Music stopped", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

package com.example.cnit355_lab7m1_junsu_yoon;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private TextView tvStatus;
    private ImageView ivAnim;
    private Button btnStart;

    private final Handler ui = new Handler(Looper.getMainLooper());
    private final int intervalMs = 300; // 0.3s

    private boolean isRunning = false;
    private int frameIdx = 0;

    private final Runnable tick = new Runnable() {
        @Override
        public void run() {
            if (!isRunning) return;

            int current = progressBar.getProgress();
            int max = progressBar.getMax();

            // 1) 이미지 프레임 전환
            int[] frames = buildFrames();
            if (frames.length > 0) {
                ivAnim.setImageResource(frames[frameIdx % frames.length]);
                frameIdx = (frameIdx + 1) % frames.length;
            }

            // 2) 진행도 +1
            if (current < max) {
                int next = current + 1;
                progressBar.setProgress(next);
                tvStatus.setText("Status: " + next + "/" + max);
                // 다음 틱 예약
                ui.postDelayed(this, intervalMs);
            } else {
                // 종료: 멈추고 버튼 활성화
                stopRun();
            }
        }
    };

    private int[] buildFrames() {
        return new int[]{
                R.drawable.picture1, R.drawable.picture2, R.drawable.picture3,
                R.drawable.picture4, R.drawable.picture5, R.drawable.picture6,
                R.drawable.picture7, R.drawable.picture8, R.drawable.picture9
        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.progressBar);
        tvStatus = findViewById(R.id.tvStatus);
        ivAnim = findViewById(R.id.ivAnim);
        btnStart = findViewById(R.id.btnStart);

        updateStatus();
    }

    private void updateStatus() {
        tvStatus.setText("Status: " + progressBar.getProgress() + "/" + progressBar.getMax());
    }

    public void onStartClick(View v) {
        if (isRunning) return; // 실행 중이면 무시

        resetToStart();
        startRun();
    }

    private void startRun() {
        isRunning = true;
        btnStart.setEnabled(false);
        ui.post(tick);
    }

    private void stopRun() {
        isRunning = false;
        btnStart.setEnabled(true); // 끝까지 가면 멈추고 버튼 다시 활성화
        // 버튼 문구를 'Start' 그대로 두면 다음 클릭 시 다시 처음부터 시작
    }

    private void resetToStart() {
        frameIdx = 0;
        progressBar.setProgress(0);
        updateStatus();

        int[] frames = buildFrames();
        if (frames.length > 0) ivAnim.setImageResource(frames[0]);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // 화면에서 벗어나면 안전하게 중단
        ui.removeCallbacks(tick);
        isRunning = false;
        btnStart.setEnabled(true);
    }
}

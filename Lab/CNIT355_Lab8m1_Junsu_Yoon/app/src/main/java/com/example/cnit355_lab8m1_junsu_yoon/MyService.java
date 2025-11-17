package com.example.cnit355_lab8m1_junsu_yoon;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.widget.Toast;

public class MyService extends Service {

    private MediaPlayer player;
    // ✅ 중복 재생 방지용 플래그 (노트 요구사항)
    private boolean isPlaying = false;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 스피너에서 넘어온 파일명(확장자 없이)
        String music = intent != null ? intent.getStringExtra("music") : null;
        if (music == null) {
            Toast.makeText(this, "No music selected", Toast.LENGTH_SHORT).show();
            return START_NOT_STICKY;
        }

        // 이미 재생 중이면 반드시 멈추고 새로 시작 (노트: 현재 음악은 새로 재생 전 반드시 stop)
        stopCurrentIfNeeded();

        // raw 리소스 아이디 찾기
        int resId = getResources().getIdentifier(music, "raw", getPackageName());
        if (resId == 0) {
            Toast.makeText(this, "Resource not found: " + music, Toast.LENGTH_SHORT).show();
            return START_NOT_STICKY;
        }

        // 재생 시작 (노트: onStartCommand에서 시작)
        player = MediaPlayer.create(this, resId);
        if (player == null) {
            Toast.makeText(this, "Failed to create MediaPlayer", Toast.LENGTH_SHORT).show();
            return START_NOT_STICKY;
        }

        player.setOnCompletionListener(mp -> {
            // 한 곡 끝나면 상태 정리
            isPlaying = false;
            stopSelf();
        });

        player.start();
        isPlaying = true;

        // ✅ 앱이 백그라운드로 가도 계속 재생 (노트 요구사항)
        return START_STICKY;
    }

    private void stopCurrentIfNeeded() {
        if (player != null) {
            try { player.stop(); } catch (Exception ignored) {}
            player.release();
            player = null;
        }
        isPlaying = false;
    }

    @Override
    public void onDestroy() {
        stopCurrentIfNeeded();
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null; // 바인딩 사용 안 함
    }
}

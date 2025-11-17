package com.example.cnit355_lab9m1_junsu_yoon;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.util.Log;

public class PlayMusic {
    public interface OnProgress {
        void onTick(int positionMs);
        void onReady(int durationMs);
        void onCompleted();
    }

    private static final String TAG = "PlayMusic";
    private final Context ctx;
    private final Handler handler = new Handler();
    private final int resId;

    private MediaPlayer player;
    private OnProgress cb;

    private final Runnable tick = new Runnable() {
        @Override public void run() {
            try {
                if (player != null && player.isPlaying() && cb != null) {
                    cb.onTick(player.getCurrentPosition());
                }
            } catch (Exception e) { Log.e(TAG, "tick error", e); }
            handler.postDelayed(this, 500);
        }
    };

    public PlayMusic(Context ctx, int rawResId) {
        this.ctx = ctx.getApplicationContext();
        this.resId = rawResId;
    }

    public void setOnProgress(OnProgress cb) { this.cb = cb; }

    public boolean prepareIfNeeded() {
        if (player != null) return true;
        try {
            player = MediaPlayer.create(ctx, resId);
            if (player == null) {
                Log.e(TAG, "MediaPlayer.create returned null (check res/raw/music.mp3)");
                return false;
            }
            if (cb != null) cb.onReady(player.getDuration());
            player.setOnCompletionListener(mp -> {
                stopInternal(false);
                if (cb != null) cb.onCompleted();
            });
            return true;
        } catch (Exception e) { Log.e(TAG, "prepareIfNeeded", e); return false; }
    }

    public void play() {
        if (player == null) return;
        try { if (!player.isPlaying()) { player.start(); handler.post(tick); } }
        catch (IllegalStateException e) { Log.e(TAG, "play IllegalState", e); }
    }

    public void pause() {
        if (player == null) return;
        try { if (player.isPlaying()) player.pause(); } catch (Exception ignored) {}
    }

    public void seekTo(int ms) {
        if (player == null) return;
        try { player.seekTo(ms); } catch (Exception ignored) {}
    }

    public void stop() { stopInternal(true); }

    public void release() { stopInternal(false); }

    private void stopInternal(boolean fromUser) {
        handler.removeCallbacks(tick);
        if (player != null) {
            try { if (player.isPlaying()) player.stop(); } catch (Exception ignored) {}
            try { player.release(); } catch (Exception ignored) {}
            player = null;
        }
    }
}

package com.focus.focus;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import androidx.annotation.Nullable;

public class FloatingService extends Service {

    private WindowManager windowManager;
    private View floatingView;
    private TextView timeTextView;
    private Handler handler;
    private long startTime;

    @Override
    public void onCreate() {
        super.onCreate();

        floatingView = LayoutInflater.from(this).inflate(R.layout.floating_layout, null);
        timeTextView = floatingView.findViewById(R.id.timeText);

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                        WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN |
                        WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.TOP | Gravity.END;
        params.x = 50;
        params.y = 100;

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        windowManager.addView(floatingView, params);

        startTime = System.currentTimeMillis();
        handler = new Handler();
        handler.post(updateTimeRunnable);
    }

    private final Runnable updateTimeRunnable = new Runnable() {
        @Override
        public void run() {
            long elapsed = System.currentTimeMillis() - startTime;
            int seconds = (int) (elapsed / 1000) % 60;
            int minutes = (int) ((elapsed / (1000 * 60)) % 60);
            int hours = (int) ((elapsed / (1000 * 60 * 60)));

            String time = String.format("%02d:%02d:%02d", hours, minutes, seconds);
            timeTextView.setText(time);

            handler.postDelayed(this, 1000);
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (floatingView != null) windowManager.removeView(floatingView);
        if (handler != null) handler.removeCallbacks(updateTimeRunnable);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

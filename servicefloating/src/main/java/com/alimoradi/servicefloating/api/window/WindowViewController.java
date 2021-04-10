
package com.alimoradi.servicefloating.api.window;

import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;

/**
 * Controls {@code View}s' positions, visibility, etc within a {@code Window}.
 */
public class WindowViewController {

    private WindowManager mWindowManager;

    public WindowViewController(@NonNull WindowManager windowManager) {
        mWindowManager = windowManager;
    }

    public void addView(int width, int height, boolean isTouchable, @NonNull View view) {
        // If this view is untouchable then add the corresponding flag, otherwise set to zero which
        // won't have any effect on the OR'ing of flags.
        int touchableFlag = isTouchable ? 0 : WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;

        int windowType = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
                ? WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
                : WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;

        int flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS |
//                WindowManager.LayoutParams.FLAG_FULLSCREEN |
//                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN |
                touchableFlag;

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                width,
                height,
                windowType,
                flags,
                PixelFormat.TRANSLUCENT
        );
        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.x = 0;
        params.y = 0;

        mWindowManager.addView(view, params);
    }

    public void removeView(@NonNull View view) {
        if (null != view.getParent()) {
            mWindowManager.removeView(view);
        }
    }

    @NonNull
    public Point getViewPosition(@NonNull View view) {
        WindowManager.LayoutParams params = (WindowManager.LayoutParams) view.getLayoutParams();
        return new Point(params.x, params.y);
    }

    public void moveViewTo(@NonNull View view, int x, int y) {
        WindowManager.LayoutParams params = (WindowManager.LayoutParams) view.getLayoutParams();
        params.x = x;
        params.y = y;
        mWindowManager.updateViewLayout(view, params);
    }

    public void showView(@NonNull View view) {
        try {
            WindowManager.LayoutParams params = (WindowManager.LayoutParams) view.getLayoutParams();
            mWindowManager.addView(view, params);
        } catch (IllegalStateException e) {
            e.printStackTrace();
            // The view is already visible.
        }
    }

    public void hideView(View view) {
        try {
            mWindowManager.removeView(view);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            // The View wasn't visible to begin with.
        }
    }

    public void makeTouchable(@NonNull View view) {
        WindowManager.LayoutParams params = (WindowManager.LayoutParams) view.getLayoutParams();
        params.flags = params.flags & ~WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE & ~WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        mWindowManager.updateViewLayout(view, params);
    }

    public void makeUntouchable(@NonNull View view) {
        WindowManager.LayoutParams params = (WindowManager.LayoutParams) view.getLayoutParams();
        params.flags = params.flags | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        mWindowManager.updateViewLayout(view, params);
    }

}

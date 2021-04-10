
package com.alimoradi.servicefloating.api;

import android.graphics.Point;
import android.util.Log;
import android.view.View;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;import com.alimoradi.servicefloating.api.FloatingTab;import com.alimoradi.servicefloating.api.PositionDock;

/**
 * Connects one {@link FloatingTab}s position to that of another {@link FloatingTab}. The space
 * between the tabs can be configured at construction time.
 */
class TabChain {

    private static final String TAG = "TabChain";

    @NonNull
    private final FloatingTab mTab;
    private final int mTabSpacingInPx;
    @Nullable
    private Point mLockedPosition;
    @Nullable
    private FloatingTab mPredecessorTab;
    private final Set<FloatingTab.OnPositionChangeListener> mOnPositionChangeListeners = new CopyOnWriteArraySet<FloatingTab.OnPositionChangeListener>();

    private final FloatingTab.OnPositionChangeListener mOnPredecessorPositionChange = new FloatingTab.OnPositionChangeListener() {
        @Override
        public void onPositionChange(@NonNull Point position) {
            // No-op. We only care when our predecessor's dock changes.
        }

        @Override
        public void onDockChange(@NonNull Point dock) {
            Log.d(TAG, hashCode() + "'s predecessor dock moved to: " + dock);
            moveToChainedPosition(false);
        }
    };

    TabChain(@NonNull FloatingTab tab, int tabSpacingInPx) {
        mTab = tab;
        mTabSpacingInPx = tabSpacingInPx;
    }

    @NonNull
    public FloatingTab getTab() {
        return mTab;
    }

    public void chainTo(@NonNull FloatingTab tab) {
        chainTo(tab, null);
    }

    public void chainTo(@NonNull FloatingTab tab, @Nullable final Runnable onChained) {
        if (null != mPredecessorTab) {
            mPredecessorTab.removeOnPositionChangeListener(mOnPredecessorPositionChange);
        }

        Log.d(TAG, mTab.getTabId() + " is now chained to " + tab.getTabId());
        mPredecessorTab = tab;
        mLockedPosition = null;
        Point myPosition = getMyChainPositionRelativeTo(mPredecessorTab);
        mTab.setDock(new PositionDock(myPosition));
    }

    public void chainTo(@NonNull Point lockedPosition) {
        chainTo(lockedPosition, null);
    }

    public void chainTo(@NonNull Point lockedPosition, @Nullable Runnable onChained) {
        if (null != mPredecessorTab) {
            mPredecessorTab.removeOnPositionChangeListener(mOnPredecessorPositionChange);
        }

        Log.d(TAG, mTab.getTabId() + " is now chained to position " + lockedPosition);
        mPredecessorTab = null;
        mLockedPosition = lockedPosition;
        mTab.setDock(new PositionDock(mLockedPosition));
    }

    public void tightenChain() {
        tightenChain(false);
    }

    public void tightenChain(boolean immediate) {
        moveToChainedPosition(immediate);

        if (null != mPredecessorTab) {
            mPredecessorTab.addOnPositionChangeListener(mOnPredecessorPositionChange);
        }
    }

    private void moveToChainedPosition(boolean immediate) {
        if (View.VISIBLE == mTab.getVisibility()) {
            if (immediate) {
                mTab.dockImmediately();
            } else {
                mTab.dock();
            }
        } else {
            mTab.moveTo(mTab.getDockPosition());
            mTab.appear(null);
        }
    }

    private Point getMyChainPositionRelativeTo(@NonNull FloatingTab tab) {
        Point predecessorTabPosition = tab.getDockPosition();
        Log.d(TAG, "Predecessor position: " + predecessorTabPosition);
        return new Point(
                predecessorTabPosition.x - mTabSpacingInPx,
                predecessorTabPosition.y
        );
    }

    public void unchain() {
        unchain(null);
    }

    public void unchain(@Nullable final Runnable onUnchained) {
        if (null != mPredecessorTab) {
            mPredecessorTab.removeOnPositionChangeListener(mOnPredecessorPositionChange);
        }
        mTab.disappear(onUnchained);
    }
}

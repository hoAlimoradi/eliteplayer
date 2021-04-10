
package com.alimoradi.servicefloating.api;

import android.graphics.Point;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import androidx.annotation.NonNull;

/**
 * A location on the screen that resolves to a {@link Point} when requested.  A {@code Dock} can
 * change its location.  {@link Listener}s can be added to receive updates when a {@code Dock}
 * changes its location.
 */
abstract class Dock {

    private final Set<Listener> mListeners = new CopyOnWriteArraySet<Listener>();

    @NonNull
    public abstract Point position();

    public void addListener(@NonNull Listener listener) {
        mListeners.add(listener);
    }

    public void removeListener(@NonNull Listener listener) {
        mListeners.remove(listener);
    }

    public void clearListeners() {
        mListeners.clear();
    }

    protected void notifyListeners() {
        for (Listener listener : mListeners) {
            listener.onDockChange(this);
        }
    }

    public interface Listener {
        void onDockChange(@NonNull Dock dock);
    }

}

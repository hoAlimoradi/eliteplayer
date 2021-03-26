
package com.alimoradi.servicefloating.api;

import android.view.View;
import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;import com.alimoradi.servicefloating.api.HoverView;


/**
 * Content to be displayed in a {@link HoverView}.
 */
public abstract class Content implements LifecycleOwner {

    private final LifecycleRegistry lifecycleRegistry = new LifecycleRegistry(this);

    public Content() {
        lifecycleRegistry.setCurrentState(Lifecycle.State.INITIALIZED);
    }

    /**
     * Returns the visual display of this content.
     *
     * @return the visual representation of this content
     */
    @NonNull
    public abstract View getView();

    /**
     * @return true to fill all available space, false to wrap content height
     */
    public abstract boolean isFullscreen();

    /**
     * Called when this content is displayed to the user.
     */
    @CallSuper
    public void onShown() {
        lifecycleRegistry.setCurrentState(Lifecycle.State.STARTED);
        lifecycleRegistry.setCurrentState(Lifecycle.State.RESUMED);
    }

    /**
     * Called when this content is no longer displayed to the user.
     * <p>
     * Implementation Note: {@code Content} can be brought back due to user navigation so
     * this call must not release resources that are required to show this content again.
     */
    @CallSuper
    public void onHidden() {
        lifecycleRegistry.setCurrentState(Lifecycle.State.STARTED);
        lifecycleRegistry.setCurrentState(Lifecycle.State.CREATED);
    }

    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return lifecycleRegistry;
    }
}


package com.alimoradi.servicefloating.api;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * A state of a {@link HoverView}. {@code HoverView} is implemented with a state pattern and this
 * is the interface that is implemented by all such states.
 */
interface HoverViewState {
    /**
     * Activates this state.
     * @param hoverView hoverView
     */
    void takeControl(@NonNull HoverView hoverView);

    /**
     * Expands the HoverView.
     */
    void expand();

    /**
     * Collapses the HoverView.
     */
    void collapse();

    /**
     * Closes the HoverView (no menu or tabs are visible).
     */
    void close();

    /**
     * Displays the given {@code menu} within the HoverView.
     * @param menu menu
     */
    void setMenu(@Nullable HoverMenu menu);

    /**
     * Does this state respond to physical back button presses?
     * @return true if this state responds to physical back button presses, false otherwise
     */
    boolean respondsToBackButton();

    /**
     * Hook called when the hardware back button is pressed.  This method is only invoked if
     * {@link #respondsToBackButton()} returns true.
     */
    void onBackPressed();

    /**
     * Adds the HoverView to the Android device's Window.
     */
    void addToWindow();

    /**
     * Removes the HoverView from the Android device's Window.
     */
    void removeFromWindow();

    /**
     * Assuming that the HoverView is added to the Android device's Window, makes the HoverView
     * touchable.
     */
    void makeTouchableInWindow();

    /**
     * Assuming that the HoverView is added to the Android device's Window, makes the HoverView
     * untouchable (touch events pass through the overlay to whatever is beneath).
     */
    void makeUntouchableInWindow();
}

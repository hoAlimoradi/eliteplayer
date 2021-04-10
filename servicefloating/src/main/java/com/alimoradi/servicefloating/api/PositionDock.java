
package com.alimoradi.servicefloating.api;

import android.graphics.Point;

import androidx.annotation.NonNull;

/**
 * {@link Dock} that has a static position as defined by a provided {@link Point}. A
 * {@code PositionDock} never changes its position after construction.
 */
class PositionDock extends Dock {

    private static final String TAG = "SideDock";

    private Point mPosition;

    PositionDock(@NonNull Point position) {
        mPosition = position;
    }

    @NonNull
    @Override
    public Point position() {
        return mPosition;
    }

}

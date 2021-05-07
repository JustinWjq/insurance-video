package com.txt.video.common.glide.manager;

/**
 * An interface for listening to Activity/Fragment lifecycle events.
 */
public interface Lifecycle {
    /**
     * Adds the given listener to the set of listeners managed by this Lifecycle implementation.
     */
    void addListener(LifecycleListener listener);
}

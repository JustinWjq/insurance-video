package com.txt.video.common.glide.load.resource.bitmap;

import com.txt.video.common.glide.load.engine.bitmap_recycle.BitmapPool;
import com.txt.video.common.glide.load.resource.drawable.DrawableResource;
import com.txt.video.common.glide.util.Util;

/**
 * A resource wrapper for {@link com.txt.video.common.glide.load.resource.bitmap.GlideBitmapDrawable}.
 */
public class GlideBitmapDrawableResource extends DrawableResource<GlideBitmapDrawable> {
    private final BitmapPool bitmapPool;

    public GlideBitmapDrawableResource(GlideBitmapDrawable drawable, BitmapPool bitmapPool) {
        super(drawable);
        this.bitmapPool = bitmapPool;
    }

    @Override
    public int getSize() {
        return Util.getBitmapByteSize(drawable.getBitmap());
    }

    @Override
    public void recycle() {
        bitmapPool.put(drawable.getBitmap());
    }
}

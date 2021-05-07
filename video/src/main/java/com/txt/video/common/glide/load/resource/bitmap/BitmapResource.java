package com.txt.video.common.glide.load.resource.bitmap;

import android.graphics.Bitmap;

import com.txt.video.common.glide.load.engine.Resource;
import com.txt.video.common.glide.load.engine.bitmap_recycle.BitmapPool;
import com.txt.video.common.glide.util.Util;

/**
 * A resource wrapping a {@link Bitmap} object.
 */
public class BitmapResource implements Resource<Bitmap> {
    private final Bitmap bitmap;
    private final BitmapPool bitmapPool;

    /**
     * Returns a new {@link com.txt.video.common.glide.load.resource.bitmap.BitmapResource} wrapping the given {@link Bitmap} if the Bitmap is non-null or null if the
     * given Bitmap is null.
     *
     * @param bitmap A Bitmap.
     * @param bitmapPool A non-null {@link com.txt.video.common.glide.load.engine.bitmap_recycle.BitmapPool}.
     */
    public static com.txt.video.common.glide.load.resource.bitmap.BitmapResource obtain(Bitmap bitmap, BitmapPool bitmapPool) {
        if (bitmap == null) {
            return null;
        } else {
            return new com.txt.video.common.glide.load.resource.bitmap.BitmapResource(bitmap, bitmapPool);
        }
    }

    public BitmapResource(Bitmap bitmap, BitmapPool bitmapPool) {
        if (bitmap == null) {
            throw new NullPointerException("Bitmap must not be null");
        }
        if (bitmapPool == null) {
            throw new NullPointerException("BitmapPool must not be null");
        }
        this.bitmap = bitmap;
        this.bitmapPool = bitmapPool;
    }

    @Override
    public Bitmap get() {
        return bitmap;
    }

    @Override
    public int getSize() {
        return Util.getBitmapByteSize(bitmap);
    }

    @Override
    public void recycle() {
        if (!bitmapPool.put(bitmap)) {
            bitmap.recycle();
        }
    }
}

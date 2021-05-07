package com.txt.video.common.glide.load.resource.transcode;

import android.graphics.Bitmap;

import com.txt.video.common.glide.load.engine.Resource;
import com.txt.video.common.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.txt.video.common.glide.load.resource.drawable.GlideDrawable;
import com.txt.video.common.glide.load.resource.gifbitmap.GifBitmapWrapper;

/**
 * An {@link com.txt.video.common.glide.load.resource.transcode.ResourceTranscoder} that can transcode either an
 * {@link Bitmap} or an {@link com.txt.video.common.glide.load.resource.gif.GifDrawable} into an
 * {@link android.graphics.drawable.Drawable}.
 */
public class GifBitmapWrapperDrawableTranscoder implements ResourceTranscoder<GifBitmapWrapper, GlideDrawable> {
    private final ResourceTranscoder<Bitmap, GlideBitmapDrawable> bitmapDrawableResourceTranscoder;

    public GifBitmapWrapperDrawableTranscoder(
            ResourceTranscoder<Bitmap, GlideBitmapDrawable> bitmapDrawableResourceTranscoder) {
        this.bitmapDrawableResourceTranscoder = bitmapDrawableResourceTranscoder;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Resource<GlideDrawable> transcode(Resource<GifBitmapWrapper> toTranscode) {
        GifBitmapWrapper gifBitmap = toTranscode.get();
        Resource<Bitmap> bitmapResource = gifBitmap.getBitmapResource();

        final Resource<? extends GlideDrawable> result;
        if (bitmapResource != null) {
            result = bitmapDrawableResourceTranscoder.transcode(bitmapResource);
        } else {
            result = gifBitmap.getGifResource();
        }
        // This is unchecked but always safe, anything that extends a Drawable can be safely cast to a Drawable.
        return (Resource<GlideDrawable>) result;
    }

    @Override
    public String getId() {
        return "GifBitmapWrapperDrawableTranscoder.com.txt.video.widget.glide.load.resource.transcode";
    }
}

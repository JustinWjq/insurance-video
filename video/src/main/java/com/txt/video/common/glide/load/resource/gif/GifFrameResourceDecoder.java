package com.txt.video.common.glide.load.resource.gif;

import android.graphics.Bitmap;

import com.txt.video.common.glide.load.ResourceDecoder;
import com.txt.video.common.glide.load.engine.Resource;
import com.txt.video.common.glide.load.engine.bitmap_recycle.BitmapPool;
import com.txt.video.common.glide.load.resource.bitmap.BitmapResource;

class GifFrameResourceDecoder implements ResourceDecoder<GifDecoder, Bitmap> {
    private final BitmapPool bitmapPool;

    public GifFrameResourceDecoder(BitmapPool bitmapPool) {
        this.bitmapPool = bitmapPool;
    }

    @Override
    public Resource<Bitmap> decode(GifDecoder source, int width, int height) {
        Bitmap bitmap = source.getNextFrame();
        return BitmapResource.obtain(bitmap, bitmapPool);
    }

    @Override
    public String getId() {
        return "GifFrameResourceDecoder.com.txt.video.widget.glide.load.resource.gif";
    }
}

package com.txt.video.common.glide.load.resource.transcode;

import android.graphics.Bitmap;

import com.txt.video.common.glide.load.engine.Resource;
import com.txt.video.common.glide.load.resource.bytes.BytesResource;

import java.io.ByteArrayOutputStream;

/**
 * An {@link com.txt.video.common.glide.load.resource.transcode.ResourceTranscoder} that converts
 * {@link Bitmap}s into byte arrays using
 * {@link Bitmap#compress(Bitmap.CompressFormat, int, java.io.OutputStream)}.
 */
public class BitmapBytesTranscoder implements ResourceTranscoder<Bitmap, byte[]> {
    private final Bitmap.CompressFormat compressFormat;
    private final int quality;

    public BitmapBytesTranscoder() {
        this(Bitmap.CompressFormat.JPEG, 100);
    }

    public BitmapBytesTranscoder(Bitmap.CompressFormat compressFormat, int quality) {
        this.compressFormat = compressFormat;
        this.quality = quality;
    }

    @Override
    public Resource<byte[]> transcode(Resource<Bitmap> toTranscode) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        toTranscode.get().compress(compressFormat, quality, os);
        toTranscode.recycle();
        return new BytesResource(os.toByteArray());
    }

    @Override
    public String getId() {
        return "BitmapBytesTranscoder.com.txt.video.widget.glide.load.resource.transcode";
    }
}

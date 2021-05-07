package com.txt.video.common.glide.load.resource.transcode;

import com.txt.video.common.glide.load.engine.Resource;
import com.txt.video.common.glide.load.resource.bytes.BytesResource;
import com.txt.video.common.glide.load.resource.gif.GifDrawable;

/**
 * An {@link com.txt.video.common.glide.load.resource.transcode.ResourceTranscoder} that converts
 * {@link com.txt.video.common.glide.load.resource.gif.GifDrawable} into bytes by obtaining the original bytes of the GIF from
 * the {@link com.txt.video.common.glide.load.resource.gif.GifDrawable}.
 */
public class GifDrawableBytesTranscoder implements ResourceTranscoder<GifDrawable, byte[]> {
    @Override
    public Resource<byte[]> transcode(Resource<GifDrawable> toTranscode) {
        GifDrawable gifData = toTranscode.get();
        return new BytesResource(gifData.getData());
    }

    @Override
    public String getId() {
        return "GifDrawableBytesTranscoder.com.txt.video.widget.glide.load.resource.transcode";
    }
}

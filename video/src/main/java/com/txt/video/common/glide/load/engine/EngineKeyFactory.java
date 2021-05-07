package com.txt.video.common.glide.load.engine;

import com.txt.video.common.glide.load.Encoder;
import com.txt.video.common.glide.load.Key;
import com.txt.video.common.glide.load.ResourceDecoder;
import com.txt.video.common.glide.load.ResourceEncoder;
import com.txt.video.common.glide.load.Transformation;
import com.txt.video.common.glide.load.resource.transcode.ResourceTranscoder;

class EngineKeyFactory {

    @SuppressWarnings("rawtypes")
    public EngineKey buildKey(String id, Key signature, int width, int height, ResourceDecoder cacheDecoder,
                              ResourceDecoder sourceDecoder, Transformation transformation, ResourceEncoder encoder,
                              ResourceTranscoder transcoder, Encoder sourceEncoder) {
        return new EngineKey(id, signature, width, height, cacheDecoder, sourceDecoder, transformation, encoder,
                transcoder, sourceEncoder);
    }

}

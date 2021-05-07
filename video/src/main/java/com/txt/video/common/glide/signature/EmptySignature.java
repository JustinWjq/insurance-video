package com.txt.video.common.glide.signature;

import com.txt.video.common.glide.load.Key;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

/**
 * An empty key that is always equal to all other empty keys.
 */
public final class EmptySignature implements Key {
    private static final com.txt.video.common.glide.signature.EmptySignature EMPTY_KEY = new com.txt.video.common.glide.signature.EmptySignature();

    public static com.txt.video.common.glide.signature.EmptySignature obtain() {
        return EMPTY_KEY;
    }

    private EmptySignature() {
        // Empty.
    }

    @Override
    public void updateDiskCacheKey(MessageDigest messageDigest) throws UnsupportedEncodingException {
        // Do nothing.
    }
}

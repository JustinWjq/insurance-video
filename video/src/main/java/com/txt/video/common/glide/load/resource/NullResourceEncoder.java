package com.txt.video.common.glide.load.resource;

import com.txt.video.common.glide.load.ResourceEncoder;
import com.txt.video.common.glide.load.engine.Resource;

import java.io.OutputStream;

/**
 * A simple {@link com.txt.video.common.glide.load.ResourceEncoder} that never writes data.
 *
 * @param <T> The type of the resource that will always fail to be encoded.
 */
public class NullResourceEncoder<T> implements ResourceEncoder<T> {
    private static final com.txt.video.common.glide.load.resource.NullResourceEncoder<?> NULL_ENCODER = new com.txt.video.common.glide.load.resource.NullResourceEncoder<Object>();

    /**
     * Returns a NullResourceEncoder for the given type.
     *
     * @param <T> The type of data to be written (or in this case not written).
     */
    @SuppressWarnings("unchecked")
    public static <T> com.txt.video.common.glide.load.resource.NullResourceEncoder<T> get() {
        return (com.txt.video.common.glide.load.resource.NullResourceEncoder<T>) NULL_ENCODER;
    }

    @Override
    public boolean encode(Resource<T> data, OutputStream os) {
        return false;
    }

    @Override
    public String getId() {
        return "";
    }
}

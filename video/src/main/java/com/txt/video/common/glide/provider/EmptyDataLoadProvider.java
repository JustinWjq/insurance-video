package com.txt.video.common.glide.provider;

import com.txt.video.common.glide.load.Encoder;
import com.txt.video.common.glide.load.ResourceDecoder;
import com.txt.video.common.glide.load.ResourceEncoder;

import java.io.File;

/**
 * A {@link com.txt.video.common.glide.provider.DataLoadProvider} that returns {@code null} for every class.
 *
 * @param <T> unused data type.
 * @param <Z> unused resource type.
 */
public class EmptyDataLoadProvider<T, Z> implements DataLoadProvider<T, Z> {
    private static final DataLoadProvider<?, ?> EMPTY_DATA_LOAD_PROVIDER = new com.txt.video.common.glide.provider.EmptyDataLoadProvider<Object, Object>();

    @SuppressWarnings("unchecked")
    public static <T, Z> DataLoadProvider<T, Z> get() {
        return (DataLoadProvider<T, Z>) EMPTY_DATA_LOAD_PROVIDER;
    }

    @Override
    public ResourceDecoder<File, Z> getCacheDecoder() {
        return null;
    }

    @Override
    public ResourceDecoder<T, Z> getSourceDecoder() {
        return null;
    }

    @Override
    public Encoder<T> getSourceEncoder() {
        return null;
    }

    @Override
    public ResourceEncoder<Z> getEncoder() {
        return null;
    }
}

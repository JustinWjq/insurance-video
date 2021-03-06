package com.txt.video.common.glide.load.model.stream;

import android.content.Context;
import android.net.Uri;

import com.txt.video.common.glide.TxGlide;
import com.txt.video.common.glide.load.model.FileLoader;
import com.txt.video.common.glide.load.model.GenericLoaderFactory;
import com.txt.video.common.glide.load.model.ModelLoader;
import com.txt.video.common.glide.load.model.ModelLoaderFactory;

import java.io.File;
import java.io.InputStream;

/**
 * A {@link ModelLoader} For translating {@link File} models for local uris into {@link InputStream} data.
 */
public class StreamFileLoader extends FileLoader<InputStream> implements StreamModelLoader<File> {

    /**
     * The default factory for {@link com.txt.video.common.glide.load.model.stream.StreamFileLoader}s.
     */
    public static class Factory implements ModelLoaderFactory<File, InputStream> {
        @Override
        public ModelLoader<File, InputStream> build(Context context, GenericLoaderFactory factories) {
            return new com.txt.video.common.glide.load.model.stream.StreamFileLoader(factories.buildModelLoader(Uri.class, InputStream.class));
        }

        @Override
        public void teardown() {
            // Do nothing.
        }
    }

    public StreamFileLoader(Context context) {
        this(TxGlide.buildStreamModelLoader(Uri.class, context));
    }

    public StreamFileLoader(ModelLoader<Uri, InputStream> uriLoader) {
        super(uriLoader);
    }

}

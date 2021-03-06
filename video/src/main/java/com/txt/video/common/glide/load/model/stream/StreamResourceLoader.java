package com.txt.video.common.glide.load.model.stream;

import android.content.Context;
import android.net.Uri;

import com.txt.video.common.glide.TxGlide;
import com.txt.video.common.glide.load.model.GenericLoaderFactory;
import com.txt.video.common.glide.load.model.ModelLoader;
import com.txt.video.common.glide.load.model.ModelLoaderFactory;
import com.txt.video.common.glide.load.model.ResourceLoader;

import java.io.InputStream;

/**
 * A {@link ModelLoader} For translating android resource id models for local uris into {@link InputStream} data.
 */
public class StreamResourceLoader extends ResourceLoader<InputStream> implements StreamModelLoader<Integer> {

    /**
     * The default factory for {@link com.txt.video.common.glide.load.model.stream.StreamResourceLoader}s.
     */
    public static class Factory implements ModelLoaderFactory<Integer, InputStream> {

        @Override
        public ModelLoader<Integer, InputStream> build(Context context, GenericLoaderFactory factories) {
            return new com.txt.video.common.glide.load.model.stream.StreamResourceLoader(context, factories.buildModelLoader(Uri.class, InputStream.class));
        }

        @Override
        public void teardown() {
            // Do nothing.
        }
    }

    public StreamResourceLoader(Context context) {
        this(context, TxGlide.buildStreamModelLoader(Uri.class, context));
    }

    public StreamResourceLoader(Context context, ModelLoader<Uri, InputStream> uriLoader) {
        super(context, uriLoader);
    }
}

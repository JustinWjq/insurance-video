package com.txt.video.common.glide.load.model.stream;

import android.content.Context;
import android.net.Uri;

import com.txt.video.common.glide.TxGlide;
import com.txt.video.common.glide.load.model.GenericLoaderFactory;
import com.txt.video.common.glide.load.model.ModelLoader;
import com.txt.video.common.glide.load.model.ModelLoaderFactory;
import com.txt.video.common.glide.load.model.StringLoader;

import java.io.InputStream;

/**
 * A {@link ModelLoader} for translating {@link String} models, such as file paths or remote urls, into
 * {@link InputStream} data.
 */
public class StreamStringLoader extends StringLoader<InputStream> implements StreamModelLoader<String> {

    /**
     * The default factory for {@link com.txt.video.common.glide.load.model.stream.StreamStringLoader}s.
     */
    public static class Factory implements ModelLoaderFactory<String, InputStream> {
        @Override
        public ModelLoader<String, InputStream> build(Context context, GenericLoaderFactory factories) {
            return new com.txt.video.common.glide.load.model.stream.StreamStringLoader(factories.buildModelLoader(Uri.class, InputStream.class));
        }

        @Override
        public void teardown() {
            // Do nothing.
        }
    }

    public StreamStringLoader(Context context) {
        this(TxGlide.buildStreamModelLoader(Uri.class, context));
    }

    public StreamStringLoader(ModelLoader<Uri, InputStream> uriLoader) {
        super(uriLoader);
    }
}

package com.txt.video.common.glide.load.model.stream;

import android.content.Context;

import com.txt.video.common.glide.load.model.GenericLoaderFactory;
import com.txt.video.common.glide.load.model.GlideUrl;
import com.txt.video.common.glide.load.model.ModelLoader;
import com.txt.video.common.glide.load.model.ModelLoaderFactory;
import com.txt.video.common.glide.load.model.UrlLoader;

import java.io.InputStream;
import java.net.URL;

/**
 * A wrapper class that translates {@link URL} objects into {@link com.txt.video.common.glide.load.model.GlideUrl}
 * objects and then uses the wrapped {@link com.txt.video.common.glide.load.model.ModelLoader} for
 * {@link com.txt.video.common.glide.load.model.GlideUrl}s to load the {@link InputStream} data.
 */
public class StreamUrlLoader extends UrlLoader<InputStream> {

    /**
     * The default factory for {@link com.txt.video.common.glide.load.model.stream.StreamUrlLoader}s.
     */
    public static class Factory implements ModelLoaderFactory<URL, InputStream> {
        @Override
        public ModelLoader<URL, InputStream> build(Context context, GenericLoaderFactory factories) {
            return new com.txt.video.common.glide.load.model.stream.StreamUrlLoader(factories.buildModelLoader(GlideUrl.class, InputStream.class));
        }

        @Override
        public void teardown() {
            // Do nothing.
        }
    }

    public StreamUrlLoader(ModelLoader<GlideUrl, InputStream> glideUrlLoader) {
        super(glideUrlLoader);
    }
}

package com.txt.video.common.glide.load.model.stream;

import android.content.Context;

import com.txt.video.common.glide.load.data.DataFetcher;
import com.txt.video.common.glide.load.data.HttpUrlFetcher;
import com.txt.video.common.glide.load.model.GenericLoaderFactory;
import com.txt.video.common.glide.load.model.GlideUrl;
import com.txt.video.common.glide.load.model.ModelCache;
import com.txt.video.common.glide.load.model.ModelLoader;
import com.txt.video.common.glide.load.model.ModelLoaderFactory;

import java.io.InputStream;

/**
 * An {@link com.txt.video.common.glide.load.model.ModelLoader} for translating {@link com.txt.video.common.glide.load.model.GlideUrl}
 * (http/https URLS) into {@link InputStream} data.
 */
public class HttpUrlGlideUrlLoader implements StreamModelLoader<GlideUrl> {

    private final ModelCache<GlideUrl, GlideUrl> modelCache;

    /**
     * The default factory for {@link com.txt.video.common.glide.load.model.stream.HttpUrlGlideUrlLoader}s.
     */
    public static class Factory implements ModelLoaderFactory<GlideUrl, InputStream> {
        private final ModelCache<GlideUrl, GlideUrl> modelCache = new ModelCache<GlideUrl, GlideUrl>(500);

        @Override
        public ModelLoader<GlideUrl, InputStream> build(Context context, GenericLoaderFactory factories) {
            return new com.txt.video.common.glide.load.model.stream.HttpUrlGlideUrlLoader(modelCache);
        }

        @Override
        public void teardown() {
            // Do nothing.
        }
    }

    public HttpUrlGlideUrlLoader() {
        this(null);
    }

    public HttpUrlGlideUrlLoader(ModelCache<GlideUrl, GlideUrl> modelCache) {
        this.modelCache = modelCache;
    }

    @Override
    public DataFetcher<InputStream> getResourceFetcher(GlideUrl model, int width, int height) {
        // GlideUrls memoize parsed URLs so caching them saves a few object instantiations and time spent parsing urls.
        GlideUrl url = model;
        if (modelCache != null) {
            url = modelCache.get(model, 0, 0);
            if (url == null) {
                modelCache.put(model, 0, 0, model);
                url = model;
            }
        }
        return new HttpUrlFetcher(url);
    }
}

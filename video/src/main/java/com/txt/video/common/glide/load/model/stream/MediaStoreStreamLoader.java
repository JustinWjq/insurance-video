package com.txt.video.common.glide.load.model.stream;

import android.content.Context;
import android.net.Uri;

import com.txt.video.common.glide.load.data.DataFetcher;
import com.txt.video.common.glide.load.data.MediaStoreThumbFetcher;
import com.txt.video.common.glide.load.model.ModelLoader;

import java.io.InputStream;

/**
 * An {@link com.txt.video.common.glide.load.model.ModelLoader} that can use media store uris to open pre-generated thumbnails
 * from the media store using {@link android.provider.MediaStore.Images.Thumbnails} and
 * {@link android.provider.MediaStore.Video.Thumbnails} if the requested size is less than or equal to the media store
 * thumbnail size. If the given uri is not a media store uri or if the desired dimensions are too large,
 * it falls back to the wrapped {@link com.txt.video.common.glide.load.model.ModelLoader} to load the
 * {@link InputStream} data.
 */
public class MediaStoreStreamLoader implements StreamModelLoader<Uri> {
    private final Context context;
    private final ModelLoader<Uri, InputStream> uriLoader;

    public MediaStoreStreamLoader(Context context, ModelLoader<Uri, InputStream> uriLoader) {
        this.context = context;
        this.uriLoader = uriLoader;
    }

    @Override
    public DataFetcher<InputStream> getResourceFetcher(Uri model, int width, int height) {
        return new MediaStoreThumbFetcher(context, model, uriLoader.getResourceFetcher(model, width, height), width,
                height);
    }
}

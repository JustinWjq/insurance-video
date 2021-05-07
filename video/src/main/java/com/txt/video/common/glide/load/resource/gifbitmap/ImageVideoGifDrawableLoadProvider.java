package com.txt.video.common.glide.load.resource.gifbitmap;

import android.graphics.Bitmap;

import com.txt.video.common.glide.load.Encoder;
import com.txt.video.common.glide.load.ResourceDecoder;
import com.txt.video.common.glide.load.ResourceEncoder;
import com.txt.video.common.glide.load.engine.bitmap_recycle.BitmapPool;
import com.txt.video.common.glide.load.model.ImageVideoWrapper;
import com.txt.video.common.glide.load.resource.file.FileToStreamDecoder;
import com.txt.video.common.glide.load.resource.gif.GifDrawable;
import com.txt.video.common.glide.provider.DataLoadProvider;

import java.io.File;
import java.io.InputStream;

/**
 * An {@link com.txt.video.common.glide.provider.DataLoadProvider} that can load either an
 * {@link com.txt.video.common.glide.load.resource.gif.GifDrawable} or an {@link Bitmap} from either an
 * {@link InputStream} or an {@link android.os.ParcelFileDescriptor}.
 */
public class ImageVideoGifDrawableLoadProvider implements DataLoadProvider<ImageVideoWrapper, GifBitmapWrapper> {
    private final ResourceDecoder<File, GifBitmapWrapper> cacheDecoder;
    private final ResourceDecoder<ImageVideoWrapper, GifBitmapWrapper> sourceDecoder;
    private final ResourceEncoder<GifBitmapWrapper> encoder;
    private final Encoder<ImageVideoWrapper> sourceEncoder;

    public ImageVideoGifDrawableLoadProvider(DataLoadProvider<ImageVideoWrapper, Bitmap> bitmapProvider,
                                             DataLoadProvider<InputStream, GifDrawable> gifProvider, BitmapPool bitmapPool) {

        final GifBitmapWrapperResourceDecoder decoder = new GifBitmapWrapperResourceDecoder(
                bitmapProvider.getSourceDecoder(),
                gifProvider.getSourceDecoder(),
                bitmapPool
        );
        cacheDecoder = new FileToStreamDecoder<GifBitmapWrapper>(new GifBitmapWrapperStreamResourceDecoder(decoder));
        sourceDecoder = decoder;
        encoder = new GifBitmapWrapperResourceEncoder(bitmapProvider.getEncoder(), gifProvider.getEncoder());

        //TODO: what about the gif provider?
        sourceEncoder = bitmapProvider.getSourceEncoder();
    }

    @Override
    public ResourceDecoder<File, GifBitmapWrapper> getCacheDecoder() {
        return cacheDecoder;
    }

    @Override
    public ResourceDecoder<ImageVideoWrapper, GifBitmapWrapper> getSourceDecoder() {
        return sourceDecoder;
    }

    @Override
    public Encoder<ImageVideoWrapper> getSourceEncoder() {
        return sourceEncoder;
    }

    @Override
    public ResourceEncoder<GifBitmapWrapper> getEncoder() {
        return encoder;
    }
}

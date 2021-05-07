package com.txt.video.common.glide.load.model.file_descriptor;

import android.content.Context;
import android.net.Uri;
import android.os.ParcelFileDescriptor;

import com.txt.video.common.glide.TxGlide;
import com.txt.video.common.glide.load.model.GenericLoaderFactory;
import com.txt.video.common.glide.load.model.ModelLoader;
import com.txt.video.common.glide.load.model.ModelLoaderFactory;
import com.txt.video.common.glide.load.model.StringLoader;

/**
 * A {@link ModelLoader} For translating {@link String} models, such as file paths, into {@link ParcelFileDescriptor}
 * data.
 */
public class FileDescriptorStringLoader extends StringLoader<ParcelFileDescriptor>
        implements FileDescriptorModelLoader<String> {

    /**
     * The default factory for {@link com.txt.video.common.glide.load.model.file_descriptor.FileDescriptorStringLoader}s.
     */
    public static class Factory implements ModelLoaderFactory<String, ParcelFileDescriptor> {
        @Override
        public ModelLoader<String, ParcelFileDescriptor> build(Context context, GenericLoaderFactory factories) {
            return new com.txt.video.common.glide.load.model.file_descriptor.FileDescriptorStringLoader(factories.buildModelLoader(Uri.class, ParcelFileDescriptor.class));
        }

        @Override
        public void teardown() {
            // Do nothing.
        }
    }

    public FileDescriptorStringLoader(Context context) {
        this(TxGlide.buildFileDescriptorModelLoader(Uri.class, context));
    }

    public FileDescriptorStringLoader(ModelLoader<Uri, ParcelFileDescriptor> uriLoader) {
        super(uriLoader);
    }
}

package com.txt.video.common.glide.load.resource.gif;

import java.util.ArrayList;
import java.util.List;

/**
 * A header object containing the number of frames in an animated GIF image as well as basic metadata like width and
 * height that can be used to decode each individual frame of the GIF. Can be shared by one or more
 * {@link com.bumptech.glide.gifdecoder.GifDecoder}s to play the same animated GIF in multiple views.
 */
public class GifHeader {

    /** The "Netscape" loop count which means loop forever. */
    public static final int NETSCAPE_LOOP_COUNT_FOREVER = 0;
    /** Indicates that this header has no "Netscape" loop count. */
    public static final int NETSCAPE_LOOP_COUNT_DOES_NOT_EXIST = -1;

    int[] gct = null;
    int status = GifDecoder.STATUS_OK;
    int frameCount = 0;

    GifFrame currentFrame;
    List<GifFrame> frames = new ArrayList<GifFrame>();
    // Logical screen size.
    // Full image width.
    int width;
    // Full image height.
    int height;

    // 1 : global color table flag.
    boolean gctFlag;
    // 2-4 : color resolution.
    // 5 : gct sort flag.
    // 6-8 : gct size.
    int gctSize;
    // Background color index.
    int bgIndex;
    // Pixel aspect ratio.
    int pixelAspect;
    int bgColor;
    int loopCount = NETSCAPE_LOOP_COUNT_DOES_NOT_EXIST;

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public int getNumFrames() {
        return frameCount;
    }

    /**
     * Global status code of GIF data parsing.
     */
    public int getStatus() {
        return status;
    }
}

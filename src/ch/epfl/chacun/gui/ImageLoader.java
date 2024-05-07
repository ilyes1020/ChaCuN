package ch.epfl.chacun.gui;

import javafx.scene.image.Image;

import static java.util.FormatProcessor.FMT;

/**
 * Utility class to load images from the resources folder.
 *
 * @author Ilyes Rouibi (372420)
 * @author Weifeng Ding (379902)
 */
public final class ImageLoader {

    public static final int LARGE_TILE_PIXEL_SIZE = 512;
    public static final int LARGE_TILE_FIT_SIZE = 256;
    public static final int NORMAL_TILE_PIXEL_SIZE = 256;
    public static final int NORMAL_TILE_FIT_SIZE = 128;
    public static final int MARKER_PIXEL_SIZE = 96;
    public static final int MARKER_FIT_SIZE = 48;

    private ImageLoader() {}

    /**
     * Creates a JavaFx image for a normal-sized tile.
     * @param tileId    the tile's ID.
     * @return a JavaFx 256px image for a tile.
     */
    public static Image normalImageForTile(int tileId){
        System.out.println(FMT."/\{NORMAL_TILE_PIXEL_SIZE}/%02d\{tileId}.jpg");
        return new Image(FMT." /\{NORMAL_TILE_PIXEL_SIZE}/%02d\{tileId}.jpg");
    }

    /**
     * Creates a JavaFx image for a large-sized tile.
     * @param tileId    the tile's ID.
     * @return a JavaFx 512px image for a tile.
     */
    public static Image largeImageForTile(int tileId){
        System.out.println(FMT."/\{LARGE_TILE_PIXEL_SIZE}/%02d\{tileId}.jpg");
        return new Image(FMT." /\{LARGE_TILE_PIXEL_SIZE}/%02d\{tileId}.jpg");
    }
}

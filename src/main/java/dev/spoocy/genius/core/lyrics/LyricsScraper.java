package dev.spoocy.genius.core.lyrics;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class LyricsScraper {

    public static final String DEFAULT_LYRICS_CONTAINER = "Lyrics__Container-sc-68a46031-1 ibbPVY";
    public static final String DEFAULT_LYRICS_CONTAINER_HEADER = "LyricsHeader__Container-sc-6f4ef545-1 cORuWd";
    public static final String DEFAULT_TITLE_CONTAINER = "SongHeader-desktop__HiddenMask-sc-7a07005e-13 WOypk";

    private static String LYRICS_CONTAINER = DEFAULT_LYRICS_CONTAINER;
    private static String LYRICS_CONTAINER_HEADER = DEFAULT_LYRICS_CONTAINER_HEADER;
    private static String TITLE_CONTAINER = DEFAULT_TITLE_CONTAINER;

    /**
     * Get the class name used to search for lyrics container.
     *
     * @return the class name
     */
    public static String getLyricsContainer() {
        return LYRICS_CONTAINER;
    }

    /**
     * Set the class name used to search for lyrics container.
     * <p>
     * This will change the way the Library finds the lyrics and
     * might break its functionality if you don't know what you are doing.
     * <p>
     * This should only be used if the library cannot find the lyrics.
     *
     * @param className
     *          the class name to set
     */
    public static void setLyricsContainer(String className) {
        LYRICS_CONTAINER = className;
    }

    /**
     * Get the class name used to search for lyrics container header.
     *
     * @return the class name
     */
    public static String getLyricsContainerHeader() {
        return LYRICS_CONTAINER_HEADER;
    }

    /**
     * Set the class name used to search for lyrics container header.
     * <p>
     * This will change the way the Library finds the lyrics header and
     * might break its functionality if you don't know what you are doing.
     * <p>
     * This should only be used if the library cannot find the lyrics.
     *
     * @param className
     *          the class name to set
     */
    public static void setLyricsContainerHeader(String className) {
        LYRICS_CONTAINER_HEADER = className;
    }

    /**
     * Get the class name used to search for the title container.
     *
     * @return the class name
     */
    public static String getTitleContainer() {
        return TITLE_CONTAINER;
    }

    /**
     * Set the class name used to search for the title container.
     * <p>
     * This will change the way the Library finds the title and
     * might break its functionality if you don't know what you are doing.
     * <p>
     * This should only be used if the library cannot find the lyrics' title.
     *
     *
     * @param className
     *          the class name to set
     */
    public static void setTitleContainer(String className) {
        TITLE_CONTAINER = className;
    }



}

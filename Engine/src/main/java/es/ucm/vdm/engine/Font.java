package es.ucm.vdm.engine;

/**
 * Base class for the font functionality of the game. Contains shared attributes and functionality
 */
public interface Font {
    final String FONT_FILE = "Bungee-Regular.ttf";

    /**
     * Creates a platform specific instance of a font object with the supplied data
     * @param contents (String) string that we want to display
     * @param fontSize (int) size of the text
     * @param fontColor (int) color of the text, in hex format
     */
    public boolean initializeFont(String contents, int fontSize, int fontColor);

    public void render();

    public void setPosition(int x, int y);
}

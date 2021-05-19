package es.ucm.vdm.engine;

/**
 * Utility class used as a multiplatform container for color
 */
public class VDMColor {
    // internal color values
    public int _r, _g, _b, _a;

    /**
     * Empty constructor
     */
    public VDMColor() {
        _r = _g = _b = _a = 255;
    }

    /**
     * Class constructor with parameters
     * @param r (int) red value between 0 and 255
     * @param g (int) green value between 0 and 255
     * @param b (int) blue value between 0 and 255
     * @param a (int) alpha value between 0 and 255
     */
    public VDMColor (int r, int g, int b, int a) {
        _r = r;
        _g = g;
        _b = b;
        _a = a;
    }

    /**
     * used for getting white color
     * @return a new VDM Color object with the values for White
     */
    public VDMColor getWhite() {
        return new VDMColor(255, 255, 255, 255);
    }

    /**
     * used for getting red color
     * @return a new VDM Color object with the values for Red
     */
    public VDMColor getRed() {
        return new VDMColor(255, 20, 50, 255);
    }

    /**
     * used for getting the player color
     * @return a new VDM Color object with the values for the player color
     */
    public VDMColor getPlayerColor() {
        return new VDMColor(0, 136, 255, 255);
    }

    /**
     * used for getting the enemy color
     * @return a new VDM Color object with the values for the enemy color
     */
    public VDMColor getEnemyColor() {
        return new VDMColor(255, 0, 0, 255);
    }

    /**
     * used for getting the item color
     * @return a new VDM Color object with the values for the item color
     */
    public VDMColor getItemColor() {
        return new VDMColor(255, 242, 0, 255);
    }


}

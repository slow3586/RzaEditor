package rzaeditor.pageobjects.other;

import org.joml.Vector2i;
import rzaeditor.Drawing;
import rzaeditor.pageobjects.PageObjectComplex;

public class Resistor extends PageObjectComplex {
    public static Vector2i defaultSize = new Vector2i(4,2);
    public static final String defaultType = "Резистор";
    
    public Resistor(Vector2i p, Direction dir) {
        super(p,dir);
    }
    
    public static void drawPhantom(Vector2i pos) {
        Drawing.drawRectZoom(6,3,12,6);
    }
}

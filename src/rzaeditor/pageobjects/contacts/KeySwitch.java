package rzaeditor.pageobjects.contacts;

import java.awt.Color;
import org.joml.Vector2i;
import rzaeditor.Drawing;
import rzaeditor.pageobjects.PageObjectComplex;

public class KeySwitch extends PageObjectComplex {
    
    public static final Vector2i defaultSize = new Vector2i(6,4);
    public static final int defaultWireIntersectOffset = 2;
    public static final String defaultType = "Ключ";
    
    public KeySwitch(Vector2i p, Direction dir) {
        super(p, dir);
    }
    
    public static void drawPhantom(Vector2i pos) {
        Drawing.setLineType(Drawing.LineType.DASH);
        Drawing.drawLineZoom(9, 3, 9, 21);
        Drawing.drawLineZoom(18, 3, 18, 21);
        Drawing.drawLineZoom(27, 3, 27, 21);
        Drawing.setLineType(Drawing.LineType.SOLID);
        Drawing.fillDrawOvalZoom(6, 9, 6, 6);
        Drawing.fillDrawOvalZoom(24, 9, 6, 6);
    }
}

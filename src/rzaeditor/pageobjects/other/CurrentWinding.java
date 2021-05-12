package rzaeditor.pageobjects.other;

import org.joml.Vector2i;
import rzaeditor.Drawing;
import rzaeditor.pageobjects.PageObjectComplex;

public class CurrentWinding extends PageObjectComplex {
    public static Vector2i defaultSize = new Vector2i(4,2);
    public static final String defaultType = "Обмотка токовая";
    
    public CurrentWinding(Vector2i p, Direction dir) {
        super(p,dir);
    }
    
    public static void drawPhantom(Vector2i pos) {
        Drawing.drawArcZoom(6,2,6,8,0,180);
        Drawing.drawArcZoom(12,2,6,8,0,180);
    }
}

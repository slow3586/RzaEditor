package rzaeditor.pageobjects.other;

import org.joml.Vector2i;
import rzaeditor.Drawing;
import rzaeditor.pageobjects.PageObjectComplex;

public class VoltageWinding extends PageObjectComplex {
    public static Vector2i defaultSize = new Vector2i(4,2);
    public static final String defaultType = "Обмотка напряжения";
    
    public VoltageWinding(Vector2i p, Direction dir) {
        super(p,dir);
    }
    
    public static void drawPhantom(Vector2i pos) {
        Drawing.drawArcZoom(6,2,4,8,0,180);
        Drawing.drawArcZoom(10,2,4,8,0,180);
        Drawing.drawArcZoom(14,2,4,8,0,180);
    }
}

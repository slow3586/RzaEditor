package rzaeditor.pageobjects.other;

import org.joml.Vector2i;
import rzaeditor.Drawing;
import rzaeditor.pageobjects.PageObjectComplex;
import rzaeditor.pageobjects.WireIntersection;

public class Fuse extends PageObjectComplex {
    public static final Vector2i defaultSize = new Vector2i(4,2);
    public static final String defaultType = "Предохранитель";
    
    public Fuse(Vector2i pos, PageObjectComplex.Direction dir) {
        super(pos, dir);
    }
    
    public static void drawPhantom(Vector2i pos) {
        Drawing.drawRectZoom(6,3,12,6);
        Drawing.drawLineGrid(1, 1, 3, 1);
    }
}

package rzaeditor.pageobjects.other;

import org.joml.Vector2i;
import rzaeditor.Drawing;
import rzaeditor.pageobjects.PageObjectComplex;

public class Light extends PageObjectComplex {
    public static Vector2i defaultSize = new Vector2i(3,2);
    public static final String defaultType = "Лампочка";
    
    public Light(Vector2i p, PageObjectComplex.Direction dir) {
        super(p,dir);
    }
    
    public static void drawPhantom(Vector2i pos) {
        Drawing.drawOvalZoom(6, 3, 6, 6);
        Drawing.drawLineZoom(7, 4, 11, 8);
        Drawing.drawLineZoom(7, 8, 11, 4);
    }
}
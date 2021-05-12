package rzaeditor.pageobjects.other;

import org.joml.Vector2i;
import rzaeditor.Drawing;
import rzaeditor.pageobjects.PageObjectComplex;

public class Capacitor extends PageObjectComplex {
    public static Vector2i defaultSize = new Vector2i(3,2);
    public static final String defaultType = "Конденсатор";
    
    public Capacitor(Vector2i p, Direction dir) {
        super(p,dir);
    }
    
    public static void drawPhantom(Vector2i pos) {
        Drawing.drawLineGrid(1,0,1,2);
        Drawing.drawLineGrid(2,0,2,2);
    }
}

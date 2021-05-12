package rzaeditor.pageobjects.intersections;

import org.joml.Vector2i;
import rzaeditor.Drawing;
import rzaeditor.pageobjects.PageObjectComplex;

public class Cart1 extends PageObjectComplex {

    public static final Vector2i defaultSize = new Vector2i(3,2);
    //public static final boolean canSwitchDirection = true;
    
    public Cart1(Vector2i pos, Direction dir) {
        super(pos, dir);
    }
    
    public static void drawPhantom(Vector2i pos) {
        /*
        Drawing.drawLineGrid(0,1,1,0);
        Drawing.drawLineGrid(0,1,1,2);
        Drawing.drawLineGrid(3,1,2,0);
        Drawing.drawLineGrid(3,1,2,2);
        */
        Drawing.drawLineZoom(0, 6, 4, 0);
        Drawing.drawLineZoom(0, 6, 4, 12);
        Drawing.drawLineZoom(12, 6, 8, 0);
        Drawing.drawLineZoom(12, 6, 8, 12);
    }
    
    
}

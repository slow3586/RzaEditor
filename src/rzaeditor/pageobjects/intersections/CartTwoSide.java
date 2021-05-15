package rzaeditor.pageobjects.intersections;

import org.joml.Vector2i;
import rzaeditor.Drawing;
import rzaeditor.pageobjects.PageObjectComplex;

public class CartTwoSide extends PageObjectComplex {

    public static final Vector2i defaultSize = new Vector2i(4,2);
    public static final String defaultType = "Тележка 2";
    //public static final boolean canSwitchDirection = true;
    
    public CartTwoSide(Vector2i pos, Direction dir) {
        super(pos, dir);
    }
    
    public static void drawPhantom(Vector2i pos) {
        Drawing.drawLineZoom(6, 6, 10, 0);
        Drawing.drawLineZoom(6, 6, 10, 12);
        Drawing.drawLineZoom(18, 6, 14, 0);
        Drawing.drawLineZoom(18, 6, 14, 12);
    }
    
    
}

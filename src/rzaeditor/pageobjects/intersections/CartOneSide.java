package rzaeditor.pageobjects.intersections;

import org.joml.Vector2i;
import rzaeditor.Drawing;
import rzaeditor.pageobjects.PageObjectComplex;

public class CartOneSide extends PageObjectComplex {

    public static final Vector2i defaultSize = new Vector2i(4,2);
    public static final String defaultType = "Тележка 1";
    //public static final boolean canSwitchDirection = true;
    
    public CartOneSide(Vector2i pos, Direction dir) {
        super(pos, dir);
    }
    
    public static void drawPhantom(Vector2i pos) {
        Drawing.drawLineGrid(1,1,2,0);
        Drawing.drawLineGrid(1,1,2,2);
        Drawing.drawLineGrid(2,1,3,0);
        Drawing.drawLineGrid(2,1,3,2);
        Drawing.drawLineGrid(2,1,3,1);
    }
}

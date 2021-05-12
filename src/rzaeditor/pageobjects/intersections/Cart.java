package rzaeditor.pageobjects.intersections;

import org.joml.Vector2i;
import rzaeditor.Drawing;
import rzaeditor.pageobjects.PageObjectComplex;

public class Cart extends PageObjectComplex {

    public static final Vector2i defaultSize = new Vector2i(3,2);
    //public static final boolean canSwitchDirection = true;
    
    public Cart(Vector2i pos, Direction dir) {
        super(pos, dir);
    }
    
    
    
    public static void drawPhantom(Vector2i pos) {
        Drawing.drawLineGrid(0,1,1,0);
        Drawing.drawLineGrid(0,1,1,2);
        Drawing.drawLineGrid(1,1,2,0);
        Drawing.drawLineGrid(1,1,2,2);
    }
    
    
}

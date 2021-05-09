package rzaeditor.pageobjects;

import org.joml.Vector2i;
import rzaeditor.Drawing;

public class Ground extends PageObjectComplex {

    public static final Vector2i defaultSize = new Vector2i(2,2);
    public static final boolean canSwitchDirection = true;
    
    public Ground(Vector2i pos, Direction dir) {
        super(pos, dir);
    }
    
    public static void drawPhantom(Vector2i pos) {
        Drawing.drawLineGrid(0, 1, 1, 1);
        Drawing.drawLineZoom(6, 0, 6, 12);
        Drawing.drawLineZoom(9, 2, 9, 10);
        Drawing.drawLineZoom(12, 4, 12, 8);
    }

}

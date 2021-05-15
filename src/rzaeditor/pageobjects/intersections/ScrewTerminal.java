package rzaeditor.pageobjects.intersections;

import org.joml.Vector2i;
import rzaeditor.Drawing;
import rzaeditor.pageobjects.PageObjectComplex;
import rzaeditor.pageobjects.WireIntersection;

public class ScrewTerminal extends WireIntersection {

    public static final Vector2i defaultSize = new Vector2i(0,0);
    public static final String defaultType = "Клемма";
    //public static final boolean canSwitchDirection = true;
    
    public ScrewTerminal(Vector2i pos, PageObjectComplex.Direction dir) {
        super(pos, dir);
    }
    
    public static void drawPhantom(Vector2i pos) {
        Drawing.drawOvalZoom(0, 0, 6, 6);
        Drawing.drawLineZoom(0, 6, 6, 0);
    }
}

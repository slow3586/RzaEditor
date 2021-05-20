package rzaeditor.pageobjects.other;

import rzaeditor.pageobjects.source.CurrentSource;
import rzaeditor.pageobjects.WireIntersection;
import org.joml.Vector2i;
import rzaeditor.Drawing;

public class BusBar extends CurrentSource {

    public static final Vector2i defaultSize = new Vector2i(4,2);
    public static final String defaultType = "Шинка";
    
    public BusBar(Vector2i pos, Direction dir) {
        super(pos, dir);
        
        WireIntersection w0 = WireIntersection.getWI(2,2,this);
    }

    @Override
    public void addDefaultWireIntersects() {
    }
    
    public static void drawDefaultWireIntersectLines(Class c) {}
    
    public static void drawPhantom(Vector2i pos) {
        Drawing.drawLineGrid(2, 1, 2, 2);
        Drawing.drawRectGrid(0, 0, 4, 1);
        Drawing.drawOvalZoom(9,0,6,6);
    }

}

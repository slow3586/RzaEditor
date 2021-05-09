package rzaeditor.pageobjects;

import java.awt.Color;
import org.joml.Vector2i;
import org.joml.primitives.Rectanglei;
import rzaeditor.Drawing;
import rzaeditor.Logic;
import rzaeditor.Page;

public class RelayRT85 extends Relay {

    public static final Vector2i defaultSize = new Vector2i(3,4);
    
    public RelayRT85(Vector2i p, Direction dir) {
        super(p, dir);
        id = "РТ";
        WireIntersection w0 = WireIntersection.getWI(0,1,this); 
        WireIntersection w1 = WireIntersection.getWI(0,3,this); 
        WireIntersection w2 = WireIntersection.getWI(3,2,this);
        w0.addWireless(w2);
        w1.addWireless(w2);
        wireIntersections.add(w0);
        wireIntersections.add(w1);
        wireIntersections.add(w2);
    }
    
    @Override
    public String getType() {
        return "Реле РТ-85";
    }
    
    public static void drawPhantom(Vector2i pos) {
        Drawing.drawLineGrid(0,1,1,1);
        Drawing.drawLineGrid(2,2,3,2);
        Drawing.drawLineGrid(0,3,1,3);
        Drawing.drawRectGrid(1,0,1,4);
    }
}

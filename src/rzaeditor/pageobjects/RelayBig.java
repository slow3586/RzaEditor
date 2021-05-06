package rzaeditor.pageobjects;

import java.awt.Color;
import org.joml.Vector2i;
import org.joml.primitives.Rectanglei;
import rzaeditor.Drawing;
import rzaeditor.Logic;
import rzaeditor.Page;

public class RelayBig extends PageObject {

    public static final Vector2i size = new Vector2i(3,4);
    
    public RelayBig(Vector2i p, boolean rot) {
        pos = new Vector2i(p);
        rotated = rot;
        name = "Реле "+Page.current.wires.size();
        ID = "Реле "+Page.current.wires.size();
        type = "Реле";
        WireIntersection w0 = WireIntersection.getWI(Logic.swapIfTrue(0, 1, rot).add(pos)); 
        WireIntersection w1 = WireIntersection.getWI(Logic.swapIfTrue(0, 3, rot).add(pos)); 
        WireIntersection w2 = WireIntersection.getWI(Logic.swapIfTrue(3, 2, rot).add(pos)); 
        wireIntersections.add(w0);
        wireIntersections.add(w1);
        wireIntersections.add(w2);
    }

    @Override
    public void draw() {
        drawPhantom(pos, rotated);
        drawChildren();
    }
    
    public static void drawPhantom(Vector2i pos, boolean rot) {
        Drawing.setTranslateGrid(pos);
        if(rot){
            Drawing.setTranslateGrid(pos.x+size.x-1, pos.y);
            Drawing.setRot(90);
        }
        Drawing.drawLineGrid(0,1,1,1);
        Drawing.drawLineGrid(2,2,3,2);
        Drawing.drawLineGrid(0,3,1,3);
        Drawing.drawRectGrid(1,0,1,4);
    }

    @Override
    public void updatePageInteractions() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}

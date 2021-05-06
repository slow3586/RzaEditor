package rzaeditor.pageobjects;

import org.joml.Vector2i;
import rzaeditor.Drawing;
import rzaeditor.Logic;
import static rzaeditor.Logic.gridToScreen;
import static rzaeditor.Logic.posToScreen;
import rzaeditor.Page;
import static rzaeditor.pageobjects.Relay.size;

public class CurrentTransformer extends CurrentSource {

    public static final Vector2i size = new Vector2i(4,2);
    
    public CurrentTransformer(Vector2i p, boolean rot) {
        pos = new Vector2i(p);
        rotated = rot;
        name = "ТТ "+Page.current.wires.size();
        ID = "ТТ "+Page.current.wires.size();
        type = "ТТ";
        WireIntersection w0 = WireIntersection.getWI(Logic.swapIfTrue(0, 1, rot).add(pos)); 
        WireIntersection w1 = WireIntersection.getWI(Logic.swapIfTrue(4, 1, rot).add(pos)); 
        wireIntersections.add(w0);
        wireIntersections.add(w1);
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
        
        Drawing.drawArc(posToScreen(posToScreen(6), posToScreen(1)),
                new Vector2i((int)Math.round(1*Logic.zoomGridGap),  (int)Math.round(1*Logic.zoomGridGap)),
                0, 180);
        Drawing.drawArc(posToScreen(posToScreen(12), posToScreen(1)), 
                new Vector2i((int)Math.round(1*Logic.zoomGridGap),  (int)Math.round(1*Logic.zoomGridGap)),
                0, 180);
        
        Drawing.drawLineGrid(0,1,1,1);
        Drawing.drawLineGrid(3,1,4,1);
        
        Drawing.drawLine(posToScreen(posToScreen(4), posToScreen(4)),
                posToScreen(posToScreen(20), posToScreen(4)));
        
        Drawing.drawLine(Logic.gridToScreen(1, 1), 
                posToScreen(posToScreen(6), posToScreen(4)));
        Drawing.drawLine(Logic.gridToScreen(3, 1), 
                posToScreen(posToScreen(18), posToScreen(4)));
    }

    @Override
    public void updatePageInteractions() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}

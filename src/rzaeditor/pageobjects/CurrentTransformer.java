package rzaeditor.pageobjects;

import org.joml.Vector2i;
import rzaeditor.Drawing;
import rzaeditor.Logic;
import static rzaeditor.Logic.gridToScreen;
import static rzaeditor.Logic.posToScreen;
import rzaeditor.Page;

public class CurrentTransformer extends CurrentSource {

    public static final Vector2i defaultSize = new Vector2i(4,2);
    
    public CurrentTransformer(Vector2i p, boolean rot) {
        super(p, rot);
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
        PageObjectComplex.rotateCheck(pos, defaultSize, rot);
        
        Drawing.drawArc(posToScreen(6,1),
                new Vector2i((int)Math.round(1*Logic.zoomGridGap),  (int)Math.round(1*Logic.zoomGridGap)),
                0, 180);
        Drawing.drawArc(posToScreen(12,1), 
                new Vector2i((int)Math.round(1*Logic.zoomGridGap),  (int)Math.round(1*Logic.zoomGridGap)),
                0, 180);
        
        Drawing.drawLineGrid(0,1,1,1);
        Drawing.drawLineGrid(3,1,4,1);
        
        Drawing.drawLine(posToScreen(4,4),
                posToScreen(20,4));
        
        Drawing.drawLine(posToScreen(6,6), 
                posToScreen(6,4));
        Drawing.drawLine(posToScreen(18,6), 
                posToScreen(18,4));
    }

    @Override
    public void updatePageInteractions() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}

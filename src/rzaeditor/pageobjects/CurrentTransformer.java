package rzaeditor.pageobjects;

import org.joml.Vector2i;
import rzaeditor.Drawing;
import rzaeditor.Logic;
import static rzaeditor.Logic.gridToScreen;
import static rzaeditor.Logic.posToScreen;
import rzaeditor.Page;

public class CurrentTransformer extends CurrentSource {
    public static final Vector2i defaultSize = new Vector2i(4,2);
    
    public CurrentTransformer(Vector2i p, Direction dir) {
        super(p, dir);
        WireIntersection w0 = WireIntersection.getWI(0,1,this); 
        WireIntersection w1 = WireIntersection.getWI(4,1,this); 
        wireIntersections.add(w0);
        wireIntersections.add(w1);
    }
    
    @Override
    public String getType() {
        return "Трансформатор тока";
    }
    
    public static void drawPhantom(Vector2i pos) {
        Drawing.drawArc(posToScreen(6,1),
                new Vector2i((int)Math.round(1*Logic.zoomGridGap),  (int)Math.round(1*Logic.zoomGridGap)),
                0, 180);
        Drawing.drawArc(posToScreen(12,1), 
                new Vector2i((int)Math.round(1*Logic.zoomGridGap),  (int)Math.round(1*Logic.zoomGridGap)),
                0, 180);
        
        Drawing.drawLineGrid(0,1,1,1);
        Drawing.drawLineGrid(3,1,4,1);
        
        Drawing.drawLineZoom(4,4,20,4);
        
        Drawing.drawLineZoom(6,6,6,4);
        Drawing.drawLineZoom(18,6,18,4);
    }
}

package rzaeditor.pageobjects.source;

import rzaeditor.pageobjects.intersections.WireIntersection;
import org.joml.Vector2i;
import rzaeditor.Drawing;
import rzaeditor.Logic;
import static rzaeditor.Logic.gridToScreen;
import static rzaeditor.Logic.posToScreen;
import rzaeditor.Page;

public class CurrentTransformer extends CurrentSource {
    public static final Vector2i defaultSize = new Vector2i(4,2);
    public static final String defaultType = "Трансформатор тока";
    
    public CurrentTransformer(Vector2i p, Direction dir) {
        super(p, dir);
    }
    
    public static void drawPhantom(Vector2i pos) {
        Drawing.drawArc(posToScreen(6,1),
                new Vector2i((int)Math.round(1*Logic.zoomGridGap),  (int)Math.round(1*Logic.zoomGridGap)),
                0, 180);
        Drawing.drawArc(posToScreen(12,1), 
                new Vector2i((int)Math.round(1*Logic.zoomGridGap),  (int)Math.round(1*Logic.zoomGridGap)),
                0, 180);
        
        
        Drawing.drawLineZoom(4,4,20,4);
        
        Drawing.drawLineZoom(6,6,6,4);
        Drawing.drawLineZoom(18,6,18,4);
    }
}

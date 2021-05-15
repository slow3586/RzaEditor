package rzaeditor.pageobjects.contacts;

import org.joml.Vector2i;
import rzaeditor.Drawing;
import rzaeditor.Logic;
import static rzaeditor.Logic.posToScreen;
import rzaeditor.Page;
import rzaeditor.pageobjects.WireIntersection;

public class ContactClosedTimedAct extends Contact {
    public static final int defaultWireIntersectOffset = 0;
    public static final String defaultType = "Закрытый контакт (задержка срабатывания)";
    
    public ContactClosedTimedAct(Vector2i p, Direction dir) {
        super(p,dir);
    }
    
    public static void drawPhantom(Vector2i pos) {
        Drawing.drawLineGrid(0,0,1,0);
        Drawing.drawLineGrid(2,0,3,0);
        
        Drawing.drawLineZoom(6,0,13,4);
        Drawing.drawLineZoom(12,0,12,5);
        
        Drawing.drawLineZoom(8,1,8,8);
        Drawing.drawLineZoom(10,2,10,8);
        Drawing.drawArcZoom(6,6,6,3,180,180);
    }
    
}

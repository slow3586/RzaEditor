package rzaeditor.pageobjects.contacts;

import org.joml.Vector2i;
import rzaeditor.Drawing;
import rzaeditor.Logic;
import static rzaeditor.Logic.posToScreen;
import rzaeditor.Page;
import rzaeditor.pageobjects.intersections.WireIntersection;

public class ContactClosed extends Contact {
    public static final int defaultWireIntersectOffset = 0;
    public static final String defaultType = "Закрытый контакт";
    
    public ContactClosed(Vector2i p, Direction dir) {
        super(p,dir);
    }
    
    public static void drawPhantom(Vector2i pos) {
        Drawing.drawLineGrid(0,0,1,0);
        Drawing.drawLineGrid(2,0,3,0);
        
        Drawing.drawLineZoom(6,0,13,4);
        Drawing.drawLineZoom(12,0,12,5);
    }
    
}

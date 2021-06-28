package rzaeditor.pageobjects.contacts;

import org.joml.Vector2i;
import rzaeditor.Drawing;
import rzaeditor.Logic;
import static rzaeditor.Logic.posToScreen;
import rzaeditor.Page;
import rzaeditor.pageobjects.WireIntersection;

public class ContactClosed extends Contact {
    public static final int defaultWireIntersectOffset = 1;
    public static final String defaultType = "Закрытый контакт";
    
    public ContactClosed(Vector2i p, Direction dir) {
        super(p,dir);
    }
    
    public static void drawPhantom(Vector2i pos) {        
        Drawing.drawLineZoom(6,6,13,10);
        Drawing.drawLineZoom(12,6,12,11);
    }
    
}

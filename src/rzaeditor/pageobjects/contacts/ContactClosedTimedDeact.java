package rzaeditor.pageobjects.contacts;

import org.joml.Vector2i;
import rzaeditor.Drawing;
import rzaeditor.Logic;
import static rzaeditor.Logic.posToScreen;
import rzaeditor.Page;
import rzaeditor.pageobjects.WireIntersection;

public class ContactClosedTimedDeact extends Contact {
    public static final int defaultWireIntersectOffset = 0;
    public static final String defaultType = "Закрытый контакт (задержка возврата)";
    
    public ContactClosedTimedDeact(Vector2i p, Direction dir) {
        super(p,dir);
    }
    
    public static void drawPhantom(Vector2i pos) {
        
        
        Drawing.drawLineZoom(8,1,8,8);
        Drawing.drawLineZoom(10,2,10,8);
        Drawing.drawArcZoom(6,1,6,3,180,180);
    }
    
}

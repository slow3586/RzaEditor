package rzaeditor.pageobjects;

import org.joml.Vector2i;
import rzaeditor.Drawing;
import rzaeditor.Logic;
import static rzaeditor.Logic.posToScreen;
import rzaeditor.Page;

public class ContactClosed extends Contact {
    public static Vector2i defaultSize = new Vector2i(3,1);
    
    public ContactClosed(Vector2i p, Direction dir) {
        super(p,dir);
        WireIntersection w0 = WireIntersection.getWI(0,0,this); 
        WireIntersection w1 = WireIntersection.getWI(3,0,this); 
        w0.addWireless(w1);
        wireIntersections.add(w0);
        wireIntersections.add(w1);
    }
    
    @Override
    public String getType() {
        return "Закрытый контакт";
    }
    
    public static void drawPhantom(Vector2i pos) {
        Drawing.drawLineGrid(0,0,1,0);
        Drawing.drawLineGrid(2,0,3,0);
        
        Drawing.drawLineZoom(6,0,13,4);
        Drawing.drawLineZoom(12,0,12,5);
    }
    
}

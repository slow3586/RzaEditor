package rzaeditor.pageobjects;

import org.joml.Vector2i;
import rzaeditor.Drawing;
import rzaeditor.Logic;
import static rzaeditor.Logic.posToScreen;

public class ContactOpen extends Contact {
    public static Vector2i defaultSize = new Vector2i(3,1);
    
    public ContactOpen(Vector2i p, Direction dir) {
        super(p, dir);
        
        WireIntersection w0 = WireIntersection.getWI(0,1,this); 
        WireIntersection w1 = WireIntersection.getWI(3,1,this); 
        w0.addWireless(w1);
        wireIntersections.add(w0);
        wireIntersections.add(w1);
    }
    
    @Override
    public String getType() {
        return "Открытый контакт";
    }
    
    public static void drawPhantom(Vector2i pos) {
        Drawing.drawLineGrid(0,1,1,1);
        Drawing.drawLineGrid(2,1,3,1);
        
        Drawing.drawLineZoom(6,6,12,2);
    }

    @Override
    public void drawIDLabel() {
        Drawing.drawString(id, Logic.gridToScreen(getSize().x)/2-Drawing.getStringWidth(String.valueOf(id))/2, Logic.posToScreen(1));
    }
    
    @Override
    public void drawContactLabels() {
        Drawing.drawString(String.valueOf(contactId0), -Drawing.getStringWidth(String.valueOf(contactId0))/2, Logic.posToScreen(14));
        Drawing.drawString(String.valueOf(contactId1), Logic.gridToScreen(getSize().x)-Drawing.getStringWidth(String.valueOf(contactId1))/2, Logic.posToScreen(14));
    }
}

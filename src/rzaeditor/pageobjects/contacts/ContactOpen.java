package rzaeditor.pageobjects.contacts;

import org.joml.Vector2i;
import rzaeditor.Drawing;
import rzaeditor.Logic;
import rzaeditor.pageobjects.intersections.WireIntersection;
import static rzaeditor.Logic.posToScreen;

public class ContactOpen extends Contact {
    
    public static final String defaultType = "Открытый контакт";
    
    public ContactOpen(Vector2i p, Direction dir) {
        super(p, dir);
    }
    
    public static void drawPhantom(Vector2i pos) {
        Drawing.drawLineZoom(6,6,12,2);
    }

    @Override
    public void drawIDLabel() {
        Drawing.drawString(id, Logic.gridToScreen(size.x)/2-Drawing.getStringWidth(String.valueOf(id))/2, Logic.posToScreen(1));
    }
    
    @Override
    public void drawContactLabels() {
        Drawing.drawString(String.valueOf(contactId0), -Drawing.getStringWidth(String.valueOf(contactId0))/2, Logic.posToScreen(14));
        Drawing.drawString(String.valueOf(contactId1), Logic.gridToScreen(size.x)-Drawing.getStringWidth(String.valueOf(contactId1))/2, Logic.posToScreen(14));
    }
}

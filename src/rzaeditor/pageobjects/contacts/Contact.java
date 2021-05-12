package rzaeditor.pageobjects.contacts;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.joml.Vector2i;
import rzaeditor.Drawing;
import rzaeditor.InfoTable;
import rzaeditor.Logic;
import rzaeditor.Page;
import rzaeditor.pageobjects.PageObjectComplex;
import rzaeditor.pageobjects.relays.Relay;

abstract public class Contact extends PageObjectComplex {

    public int contactId0 = 1;
    public int contactId1 = 3;
    public boolean isOpen = true;
    public Relay relay = null;
    
    public Contact(Vector2i p, Direction dir) {
        super(p, dir);
    }
    
    public void activate(){
        isOpen=!isOpen;
    }

    @Override
    public void drawContactLabels() {
        super.drawContactLabels();
        
        Drawing.drawString(String.valueOf(contactId0), -Drawing.getStringWidth(String.valueOf(contactId0))/2, Logic.posToScreen(8));
        Drawing.drawString(String.valueOf(contactId1), Logic.gridToScreen(size.x)-Drawing.getStringWidth(String.valueOf(contactId1))/2, Logic.posToScreen(8));
    }
    
    @Override
    public void onSelect() {
        super.onSelect();
        
        InfoTable.addLineNameAssign("Реле", this, "relay");
    }

    @Override
    public void drawConnections() {
        super.drawConnections();
        drawConnection(relay);
    }

}

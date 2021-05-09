package rzaeditor.pageobjects;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.joml.Vector2i;
import rzaeditor.Drawing;
import rzaeditor.InfoTable;
import rzaeditor.Logic;
import rzaeditor.Page;

abstract public class Contact extends PageObjectComplex {

    int contactId0 = 1;
    int contactId1 = 3;
    boolean isOpen = true;
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
        Drawing.drawString(String.valueOf(contactId1), Logic.gridToScreen(getSize().x)-Drawing.getStringWidth(String.valueOf(contactId1))/2, Logic.posToScreen(8));
    }
    
    @Override
    public void onSelect() {
        super.onSelect();
        
        try {
            InfoTable.addLineNameAssign("Реле", this, getClass().getField("relay"));
        } catch (NoSuchFieldException | SecurityException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void drawConnections() {
        super.drawConnections();
        drawConnection(relay);
    }

}

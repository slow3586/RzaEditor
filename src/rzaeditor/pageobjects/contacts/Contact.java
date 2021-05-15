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

    public boolean isOpen = true;
    public Relay relay = null;
    
    public Contact(Vector2i p, Direction dir) {
        super(p, dir);
    }
    
    public void activate(){
        isOpen=!isOpen;
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

package rzaeditor.pageobjects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.joml.Vector2i;
import org.joml.primitives.Rectanglei;
import rzaeditor.DrawModeAssign;
import rzaeditor.Drawing;
import rzaeditor.InfoTable;
import rzaeditor.Logic;
import rzaeditor.MainFrame;
import rzaeditor.Page;

public class RelayRT40 extends Relay {

    public static final Vector2i defaultSize = new Vector2i(3,2);
    
    public int contactId0 = 2;
    public int contactId1 = 8;
    public ContactOpen contactOpen = null;
    public ContactClosed contactClosed = null;
    public final int contactOpenId0 = 1;
    public final int contactOpenId1 = 3;
    public final int contactClosedId0 = 5;
    public final int contactClosedId1 = 7;
    
    public RelayRT40(Vector2i p, Direction dir) {
        super(p, dir);
        id = "РТ";
        WireIntersection w0 = WireIntersection.getWI(0,1,this); 
        WireIntersection w1 = WireIntersection.getWI(3,1,this); 
        w0.addWireless(w1);
        Wire.checkAllWires();
        wireIntersections.add(w0);
        wireIntersections.add(w1);
    }

    @Override
    public String getType() {
        return "Реле РТ-40";
    }
    
    public static void drawPhantom(Vector2i pos) {
        Drawing.drawLineGrid(0,1,1,1);
        Drawing.drawLineGrid(2,1,3,1);
        Drawing.drawRectGrid(1,0,1,2);
    }

    @Override
    public void drawContactLabels() {
        super.drawContactLabels();
        
        Drawing.drawString(String.valueOf(contactId0), -Drawing.getStringWidth(String.valueOf(contactId0))/2, Logic.posToScreen(15));
        Drawing.drawString(String.valueOf(contactId1), Logic.gridToScreen(getSize().x)-Drawing.getStringWidth(String.valueOf(contactId1))/2, Logic.posToScreen(15));
    }

    @Override
    public void drawConnections() {
        super.drawConnections();
        drawConnection(contactClosed);
        drawConnection(contactOpen);
    }

    @Override
    public void onSelect() {
        super.onSelect();
        
        try {
            InfoTable.addLineNameAssign("Открытый контакт", this, getClass().getField("contactOpen"));
            InfoTable.addLineNameAssign("Закрытый контакт", this, getClass().getField("contactClosed"));
        } catch (NoSuchFieldException | SecurityException ex) {
            Logger.getLogger(RelayRT40.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void dataUpdated() {
        super.dataUpdated();
        if(contactOpen!=null){
            contactOpen.relay = this;
            contactOpen.id = id;
            contactOpen.contactId0=contactOpenId0;
            contactOpen.contactId1=contactOpenId1;
        }
        if(contactClosed!=null){
            contactClosed.relay = this;
            contactClosed.id = id;
            contactClosed.contactId0=contactClosedId0;
            contactClosed.contactId1=contactClosedId1;
        }
    }
}

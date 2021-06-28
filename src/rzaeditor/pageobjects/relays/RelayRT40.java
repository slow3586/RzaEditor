package rzaeditor.pageobjects.relays;

import rzaeditor.pageobjects.contacts.ContactClosed;
import rzaeditor.pageobjects.contacts.ContactOpen;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.joml.Vector2i;
import org.joml.primitives.Rectanglei;
import rzaeditor.drawmodes.DrawModeAssign;
import rzaeditor.Drawing;
import rzaeditor.InfoTable;
import rzaeditor.Logic;
import rzaeditor.MainFrame;
import rzaeditor.Page;
import rzaeditor.pageobjects.Wire;
import rzaeditor.pageobjects.WireIntersection;

public class RelayRT40 extends Relay {

    public static final Vector2i defaultSize = new Vector2i(3,2);
    public static final String defaultIDru = "РТ";
    public static final String defaultIDen = "KA";
    public static final String defaultType = "Реле РТ-40";
    public static final String defaultContactId0 = "2";
    public static final String defaultContactId1 = "8";
    public static final String contactOpenId0 = "1";
    public static final String contactOpenId1 = "3";
    public static final String contactClosedId0 = "5";
    public static final String contactClosedId1 = "7";
    
    public ContactOpen contactOpen = null;
    public ContactClosed contactClosed = null;
    
    public static final String[] fieldsToSave = new String[]{"contactOpen", "contactClosed"};
    
    public RelayRT40(Vector2i p, Direction dir) {
        super(p, dir);
    }
    
    public static void drawPhantom(Vector2i pos) {
        Relay.drawPhantom(pos);
    }

    @Override
    public void drawConnections() {
        super.drawConnections();
        drawConnection(contactClosed);
        drawConnection(contactOpen);
    }

    @Override
    public void delete() {
        super.delete();
        if(contactClosed!=null)
            contactClosed.relay = null;
        if(contactOpen!=null)
            contactOpen.relay = null;
    }
    
    @Override
    public void onSelect() {
        super.onSelect();
        InfoTable.addLineNameAssign("Открытый контакт", this, "contactOpen");
        InfoTable.addLineNameAssign("Закрытый контакт", this, "contactClosed");
    }

    @Override
    public void dataUpdated() {
        super.dataUpdated();
        if(contactOpen!=null){
            contactOpen.relay = this;
            contactOpen.id = id;
            contactOpen.setContactId0(contactOpenId0);
            contactOpen.setContactId1(contactOpenId0);
        }
        if(contactClosed!=null){
            contactClosed.relay = this;
            contactClosed.id = id;
            contactClosed.setContactId0(contactClosedId0);
            contactClosed.setContactId1(contactClosedId1);
        }
    }
}

package rzaeditor.pageobjects.relays;

import rzaeditor.pageobjects.contacts.Contact;
import java.util.HashSet;
import org.joml.Vector2i;
import rzaeditor.Drawing;
import rzaeditor.pageobjects.PageObjectComplex;

public class Relay extends PageObjectComplex {
    public static final Vector2i defaultSize = new Vector2i(3,2);
    public static final String defaultIDru = "K";
    public static final String defaultIDen = "K";
    public static final String defaultType = "Электромагнит";
    
    public Relay(Vector2i p, Direction dir) {
        super(p, dir);
    }
    
    public static void drawPhantom(Vector2i pos) {
        Drawing.drawRectGrid(1,0,1,2);
    }
}

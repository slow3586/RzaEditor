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
    
    public ContactOpen contactOpen = null;
    public ContactClosed contactClosed = null;
    
    public RelayRT40(Vector2i p, boolean rot) {
        super(p,rot);
        id = "РТ";
        WireIntersection w0 = WireIntersection.getWI(Logic.swapIfTrue(0, 1, rot).add(pos)); 
        WireIntersection w1 = WireIntersection.getWI(Logic.swapIfTrue(3, 1, rot).add(pos)); 
        w0.addWireless(w1);
        Wire.checkAllWires();
        wireIntersections.add(w0);
        wireIntersections.add(w1);
    }

    @Override
    public String getType() {
        return "Реле РТ-40";
    }
    
    public static void drawPhantom(Vector2i pos, boolean rot) {
        PageObjectComplex.rotateCheck(pos, defaultSize, rot);
        Drawing.drawLineGrid(0,1,1,1);
        Drawing.drawLineGrid(2,1,3,1);
        Drawing.drawRectGrid(1,0,1,2);
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
    public void updatePageInteractions() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}

package rzaeditor.pageobjects;

import org.joml.Vector2i;
import rzaeditor.Drawing;
import rzaeditor.Logic;
import static rzaeditor.Logic.posToScreen;

public class ContactOpen extends Contact {
    public static Vector2i defaultSize = new Vector2i(3,1);
    
    public ContactOpen(Vector2i p, boolean rot) {
        super(p, rot);
        
        WireIntersection w0 = WireIntersection.getWI(Logic.swapIfTrue(0, 1, rot).add(pos)); 
        WireIntersection w1 = WireIntersection.getWI(Logic.swapIfTrue(3, 1, rot).add(pos)); 
        wireIntersections.add(w0);
        wireIntersections.add(w1);
    }
    
    @Override
    public String getType() {
        return "Открытый контакт";
    }
    
    public static void drawPhantom(Vector2i pos, boolean rot) {
        PageObjectComplex.rotateCheck(pos, defaultSize, rot);
        Drawing.drawLineGrid(0,1,1,1);
        Drawing.drawLineGrid(2,1,3,1);
        
        Drawing.drawLine(posToScreen(6,6),
                posToScreen(12,2));
    }

    @Override
    public void updatePageInteractions() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}

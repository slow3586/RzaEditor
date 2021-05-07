package rzaeditor.pageobjects;

import org.joml.Vector2i;
import rzaeditor.Drawing;
import rzaeditor.Logic;
import static rzaeditor.Logic.posToScreen;
import rzaeditor.Page;

public class ContactClosed extends Contact {
    public static Vector2i defaultSize = new Vector2i(3,1);
    
    public ContactClosed(Vector2i p, boolean rot) {
        super(p,rot);
        WireIntersection w0 = WireIntersection.getWI(Logic.swapIfTrue(0, 0, rot).add(pos)); 
        WireIntersection w1 = WireIntersection.getWI(Logic.swapIfTrue(3, 0, rot).add(pos)); 
        wireIntersections.add(w0);
        wireIntersections.add(w1);
    }
    
    @Override
    public String getType() {
        return "Закрытый контакт";
    }
    
    public static void drawPhantom(Vector2i pos, boolean rot) {
        PageObjectComplex.rotateCheck(pos, defaultSize, rot);
        Drawing.drawLineGrid(0,0,1,0);
        Drawing.drawLineGrid(2,0,3,0);
        
        Drawing.drawLine(posToScreen(6,0),
                posToScreen(13,4));
        Drawing.drawLine(posToScreen(12,0),
                posToScreen(12,5));
    }

    @Override
    public void updatePageInteractions() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}

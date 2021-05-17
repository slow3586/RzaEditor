package rzaeditor.drawmodes;

import rzaeditor.pageobjects.Wire;
import java.awt.Color;
import org.joml.Vector2f;
import org.joml.Vector2i;
import rzaeditor.Drawing;
import rzaeditor.Logic;
import static rzaeditor.Logic.*;

public class DrawModeWire extends DrawMode {

    public static DrawModeWire imp = new DrawModeWire();

    @Override
    public void draw() {
        if(!Logic.isDragging) return;
        
        Vector2i s = new Vector2i(dragStart);
        Vector2i e = new Vector2i(dragEnd);
        if(Math.abs(dragVec.x) > Math.abs(dragVec.y))
            e.y=s.y;
        else
            e.x=s.x;
        
        Drawing.setColor(Color.RED);
        if(Wire.canBePlacedAt(s, e)){
            Drawing.setColor(Color.GREEN);
        }
        
        Drawing.setTranslatePagePos();
        Drawing.drawLine(Logic.gridToScreen(s), Logic.gridToScreen(e));
    }

    @Override
    public void mouseReleased() {
        Vector2i s = new Vector2i(dragStart);
        Vector2i e = new Vector2i(dragEnd);
        if(Math.abs(dragVec.x) > Math.abs(dragVec.y))
            e.y=s.y;
        else
            e.x=s.x;
        if(new Vector2i(e).sub(s).lengthSquared()==0) return;
        if(Wire.canBePlacedAt(s, e)){
            Wire.create(s, e, true);
        }
    }

}

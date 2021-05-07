package rzaeditor.pageobjects;

import java.awt.Color;
import org.joml.Vector2f;
import org.joml.Vector2i;
import rzaeditor.Drawing;
import rzaeditor.Logic;
import rzaeditor.Page;

public class PageLine extends Primitive {

    Vector2i start = new Vector2i();
    Vector2i end = new Vector2i();
    Color color = Color.BLACK;

    public PageLine(Vector2i pos) {
        super(pos);
    }
    
    public static PageLine create(Vector2i s, Vector2i e){
        PageLine pl = new PageLine(s);
        pl.start = new Vector2i(s);
        pl.end = new Vector2i(e);
        Logic.fixVectorPositions(pl.start, pl.end);
        pl.setSize(new Vector2i(pl.end).sub(pl.start));
        
        Page.current.primitives.add(pl);
        return pl;
    }
    
    public static boolean canBePlacedAt(Vector2i s, Vector2i e){
        Vector2i vec = new Vector2i(e).sub(s);
        
        if(vec.length()<=0) return false;
        
        for (Wire w1 : Page.current.wires) {
            if ((vec.y == 0)==w1.isHorizontal() && (w1.pointInside(s, 1) || w1.pointInside(e, 1))) {
                return false;
            }
        }
        
        return true;
    }
    
    @Override
    public void draw() {
        selectedCheck();
        Drawing.setStroke(2);
        Drawing.drawLine(Logic.gridToScreen(start), Logic.gridToScreen(end));
    }
}

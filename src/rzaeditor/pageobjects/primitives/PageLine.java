package rzaeditor.pageobjects.primitives;

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
    public static final String defaultType = "Линия";

    public PageLine(Vector2i pos) {
        super(pos);
    }
    
    public static PageLine create(Vector2i s, Vector2i e){
        PageLine pl = new PageLine(s);
        pl.start = new Vector2i(s);
        pl.end = new Vector2i(e);
        Logic.fixVectorPositions(pl.start, pl.end);
        pl.size = new Vector2i(pl.end).sub(pl.start);
        
        Page.current.objects.add(pl);
        return pl;
    }
    
    @Override
    public void draw() {
        super.draw();
        Drawing.setStrokeSize(2);
        Drawing.drawLineGrid(0,0, size.x, size.y);
    }
}
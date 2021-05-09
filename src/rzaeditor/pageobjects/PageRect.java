package rzaeditor.pageobjects;

import java.awt.Color;
import org.joml.Vector2f;
import org.joml.Vector2i;
import rzaeditor.Drawing;
import rzaeditor.Logic;
import static rzaeditor.Logic.zoomGridGap;
import rzaeditor.Page;

public class PageRect extends Primitive {
    public PageRect(Vector2i pos) {
        super(pos);
    }
    
    @Override
    public String getType() {
        return "Прямоугольник";
    }
    
    public static PageRect create(Vector2i pos, Vector2i size){
        PageRect pl = new PageRect(pos);
        pl.setSize(new Vector2i(size));
        
        Page.current.objects.add(pl);
        return pl;
    }

    @Override
    public void draw() {
        super.draw();
        Drawing.setStroke(2);
        Drawing.drawRect(0,0, getSize().x*zoomGridGap, getSize().y*zoomGridGap); 
    }
}

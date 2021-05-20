package rzaeditor.pageobjects.primitives;

import java.awt.Color;
import java.awt.Stroke;
import org.joml.Vector2f;
import org.joml.Vector2i;
import rzaeditor.Drawing;
import rzaeditor.Logic;
import static rzaeditor.Logic.zoomGridGap;
import rzaeditor.Page;

public class PageRect extends Primitive {
    public static final String defaultType = "Прямоугольник";
    
    public PageRect(Vector2i pos) {
        super(pos);
    }

    @Override
    public void draw() {
        super.draw();
        Drawing.setStrokeSize(2);
        Drawing.drawRect(0,0, size.x*zoomGridGap, size.y*zoomGridGap); 
    }
}

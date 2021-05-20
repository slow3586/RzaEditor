package rzaeditor.pageobjects.primitives;

import java.awt.Color;
import org.joml.Vector2f;
import org.joml.Vector2i;
import rzaeditor.Drawing;
import rzaeditor.Logic;
import rzaeditor.Page;

public class PageLine extends Primitive {
    public static final String defaultType = "Линия";

    public PageLine(Vector2i pos) {
        super(pos);
    }
    
    @Override
    public void draw() {
        super.draw();
        Drawing.setStrokeSize(2);
        Drawing.drawLineGrid(0,0, size.x, size.y);
    }
}

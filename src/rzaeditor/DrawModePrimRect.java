package rzaeditor;

import rzaeditor.pageobjects.PageLine;
import java.awt.Color;
import org.joml.Vector2i;
import static rzaeditor.Logic.dragEnd;
import static rzaeditor.Logic.dragStart;
import static rzaeditor.Logic.zoomGridGap;
import rzaeditor.pageobjects.PageRect;

public class DrawModePrimRect extends DrawMode {

    public static DrawModePrimRect imp = new DrawModePrimRect();
    public boolean lineGood = false;

    @Override
    public void mouseDrag() {
    }

    @Override
    public void mouseMove() {

    }

    @Override
    public void draw() {
       if(!Logic.isDragging) return;
        
        Drawing.setColor(Color.RED);

        Drawing.drawRect(Logic.gridToScreen(new Vector2i(Logic.dragRect.minX, Logic.dragRect.minY)),
                new Vector2i(Math.round(Logic.dragRect.lengthX()*zoomGridGap), Math.round(Logic.dragRect.lengthY()*zoomGridGap))); 
    }

    @Override
    public void mousePressed() {
    }

    @Override
    public void mouseReleased() {
        PageRect.create(new Vector2i(Logic.dragRect.minX, Logic.dragRect.minY), 
                new Vector2i(Logic.dragRect.lengthX(), Logic.dragRect.lengthY()));
    }

}

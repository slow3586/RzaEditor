package rzaeditor;

import rzaeditor.pageobjects.PageLine;
import java.awt.Color;
import static rzaeditor.Logic.dragEnd;
import static rzaeditor.Logic.dragStart;

public class DrawModePrimLine extends DrawMode {

    public static DrawModePrimLine imp = new DrawModePrimLine();
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
        
        Drawing.drawLine(Logic.gridToScreen(dragStart), Logic.gridToScreen(dragEnd)); 
    }

    @Override
    public void mousePressed() {
    }

    @Override
    public void mouseReleased() {
        PageLine.create(dragStart, dragEnd);
    }

}

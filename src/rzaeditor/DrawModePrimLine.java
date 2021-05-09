package rzaeditor;

import rzaeditor.pageobjects.PageLine;
import java.awt.Color;
import static rzaeditor.Logic.dragEnd;
import static rzaeditor.Logic.dragStart;

public class DrawModePrimLine extends DrawMode {

    public static DrawModePrimLine imp = new DrawModePrimLine();
    public boolean lineGood = false;

    @Override
    public void draw() {
       if(!Logic.isDragging) return;
        
        Drawing.setColor(Color.RED);
        Drawing.setTranslateGrid(0, 0);
        Drawing.drawLineGrid(dragStart, dragEnd); 
    }

    @Override
    public void mouseReleased() {
        PageLine.create(dragStart, dragEnd);
    }

}

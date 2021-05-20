package rzaeditor.drawmodes;

import rzaeditor.pageobjects.primitives.PageLine;
import java.awt.Color;
import rzaeditor.Drawing;
import rzaeditor.Logic;
import static rzaeditor.Logic.dragEnd;
import static rzaeditor.Logic.dragStart;
import static rzaeditor.Logic.dragStartFixed;
import rzaeditor.Page;

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
        PageLine l = new PageLine(dragStartFixed);
        l.size = Logic.dragVecFixed;
        Page.current.objects.add(l);
    }

}

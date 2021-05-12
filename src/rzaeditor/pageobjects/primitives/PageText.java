package rzaeditor.pageobjects.primitives;

import org.joml.Vector2i;
import rzaeditor.Drawing;
import static rzaeditor.Logic.zoomGridGap;
import rzaeditor.Page;
import rzaeditor.pageobjects.PageObjectBase;

public class PageText extends PageObjectBase {
public static final String defaultType = "Текст";
public String text = "";
    
    public PageText(Vector2i pos) {
        super(pos);
    }
    
    public static PageText create(Vector2i pos, Vector2i size){
        PageText pl = new PageText(pos);
        pl.size = new Vector2i(size);
        
        Page.current.objects.add(pl);
        return pl;
    }

    @Override
    public void draw() {
        super.draw();
        Drawing.drawRectGrid(0,0, size.x, size.y); 
        Vector2i s = Drawing.getStringHeight(text);
        Drawing.drawString(text, size.x/2 - s.x/2, size.y/2 - s.y/2);
    }
}

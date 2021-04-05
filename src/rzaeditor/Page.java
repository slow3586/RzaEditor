package rzaeditor;

import rzaeditor.pageobjects.PageObject;
import rzaeditor.pageobjects.Wire;
import rzaeditor.pageobjects.WireIntersection;
import java.util.HashSet;
import org.joml.Vector2i;
import org.joml.primitives.Rectanglei;

public class Page {
    
    public int gridGap = 6;
    public static Page current = newCircuitA3Page();
    
    public Vector2i titleSize;
    public Vector2i sizeNoBorder;
    public HashSet<PageObject> primitives = new HashSet<>();
    public HashSet<Wire> wires = new HashSet<>();
    public HashSet<WireIntersection> wireIntersections = new HashSet<>();
    public Vector2i pos= new Vector2i(0, 0);
    public Vector2i size;
    public Vector2i gridSize;
    public Rectanglei rect;
    public boolean resizable;
    public boolean hasBorder;
    public boolean hasUnsavedProgress = true;
    
    private Page(){
        
    }
    
    public void save(){
        hasUnsavedProgress = false;
    }
    
    public static Page newCircuitA3Page(){
        Page p = new Page();
        p.titleSize = new Vector2i(185, 55);
        p.sizeNoBorder = new Vector2i(420, 297);
        p.size = new Vector2i(395, 287);
        p.gridGap = 6;
        p.gridSize = new Vector2i(p.size.x / p.gridGap, p.size.y / p.gridGap);
        p.rect = new Rectanglei(-1, -1, p.gridSize.x, p.gridSize.y);
        p.resizable = false;
        p.hasBorder = true;
        return p;
    }
    
    public static Page newObjectPage(){
        Page p = new Page();
        p.titleSize = new Vector2i(0,0);
        p.sizeNoBorder = new Vector2i(0,0);
        p.size = new Vector2i(10,10);
        p.gridGap = 1;
        p.gridSize = new Vector2i(p.size.x / p.gridGap, p.size.y / p.gridGap);
        p.rect = new Rectanglei(-1, -1, p.gridSize.x, p.gridSize.y);
        p.resizable = true;
        p.hasBorder = false;
        return p;
    }

}

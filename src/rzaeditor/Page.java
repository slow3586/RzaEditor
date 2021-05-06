package rzaeditor;

import rzaeditor.pageobjects.PageObject;
import rzaeditor.pageobjects.Wire;
import rzaeditor.pageobjects.WireIntersection;
import java.util.HashSet;
import org.joml.Vector2i;
import org.joml.primitives.Rectanglei;

public class Page {
    
    //size of one cell in a diagram, related to relay size
    public static int gridGap = 6;
    //public static float gridGapFine = 0.5f;
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
    public boolean useFineGrid = false;
    public float cmPerCell;
    
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
        p.gridSize = new Vector2i(p.size.x / p.gridGap, p.size.y / p.gridGap);
        p.rect = new Rectanglei(-1, -1, p.gridSize.x, p.gridSize.y);
        p.resizable = false;
        p.hasBorder = true;
        p.cmPerCell = 6;
        return p;
    }
    
    public static Page newObjectPage(){
        Page p = new Page();
        p.titleSize = new Vector2i(0,0);
        p.sizeNoBorder = new Vector2i(0,0);
        p.size = new Vector2i(gridGap * 18*2, gridGap * 18*2);
        p.gridSize = new Vector2i(p.size.x / gridGap, p.size.y / gridGap);
        p.rect = new Rectanglei(-1, -1, p.gridSize.x, p.gridSize.y);
        p.resizable = true;
        p.hasBorder = false;
        p.useFineGrid = true;
        p.cmPerCell = 0.5f;
        return p;
    }

    /*
    public static Page fromText(String s){
        Page p = newObjectPage();
        
        String[] split = s.split("\n");
        for (int i = 0; i < split.length; i++) {
            String s0 = split[i];
            
            String[] split1 = s0.split("\t");
            String objClassName = split1[0];
            
            Class<? extends PageObject> c = null;
            try {
                c = Class.forName("rzaeditor.pageobjects."+objClassName).asSubclass(PageObject.class);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            
            String[] objArgs = new String[split1.length-1];
            for(int j=1; j<split1.length; j++){
                objArgs[j-1]=split1[j];
            }
            
            PageObject o = c.cast(PageObject.class);
            o.fromText(objArgs);
            if(o instanceof Wire){
                p.wires.add(o);
            }else if(o instanceof WireIntersection){
                p.wireIntersections.add(o);
            }
            p.primitives.add(o);
        }
        return p;
    }
    */  
}

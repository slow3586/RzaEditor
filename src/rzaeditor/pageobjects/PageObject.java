package rzaeditor.pageobjects;

import java.util.HashSet;
import org.joml.Vector2i;
import org.joml.primitives.Rectanglef;
import org.joml.primitives.Rectanglei;

public abstract class PageObject {
    Vector2i pos = new Vector2i();
    Rectanglei rectangle = new Rectanglei();
    String ID = "";
    String name = "";
    protected String type = "";
    public boolean selected = false;
    public PageObject parent = null;
    public HashSet<PageObject> children = new HashSet<>();
    
    abstract public void draw();
    abstract public void updatePageInteractions();
   // abstract boolean canBeMovedTo();
    
    public String getType(){
        return type;
    }
    
    abstract public PageObject fromText(String[] args);
    
}

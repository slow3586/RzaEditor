package rzaeditor.pageobjects;

import org.joml.Vector2i;

public abstract class Primitive extends PageObjectBase {

    public Primitive(Vector2i p) {
        super(p);
    }
    
    public void draw(){}
}

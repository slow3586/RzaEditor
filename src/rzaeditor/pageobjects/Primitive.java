package rzaeditor.pageobjects;

import org.joml.Vector2i;
import rzaeditor.Drawing;

public abstract class Primitive extends PageObjectBase {

    public Primitive(Vector2i p) {
        super(p);
    }
    
    public void draw(){
        super.draw();
        Drawing.setTranslateGrid(pos);
    }
}

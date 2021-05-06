package rzaeditor.pageobjects;

import java.awt.Color;
import org.joml.Vector2i;
import rzaeditor.Logic;
import rzaeditor.Page;

public class Relay extends PageObject {

    public static Relay create(Vector2i pos) {        
        Relay w = new Relay();
        
        w.name = "Реле "+Page.current.wires.size();
        w.ID = "Реле "+Page.current.wires.size();
        w.type = "Реле";
        WireIntersection w0 = WireIntersection.getWI(pos)
        children.add()
        
        return w;
    }
    
    @Override
    public void draw() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void updatePageInteractions() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public PageObject fromText(String[] args) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}

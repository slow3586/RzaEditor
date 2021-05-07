package rzaeditor.pageobjects;

import org.joml.Vector2i;

public class CurrentSource extends PageObjectComplex {

    float currentValue = 0;

    public CurrentSource(Vector2i p, boolean rot) {
        super(p, rot);
    }
    
    @Override
    public void draw() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void updatePageInteractions() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}

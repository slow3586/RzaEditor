package rzaeditor.pageobjects;

import org.joml.Vector2i;

public class CurrentSource extends PageObjectComplex {

    float currentValue = 0;

    public CurrentSource(Vector2i p, Direction dir) {
        super(p, dir);
    }
}

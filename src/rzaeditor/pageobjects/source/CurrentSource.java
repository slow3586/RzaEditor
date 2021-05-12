package rzaeditor.pageobjects.source;

import org.joml.Vector2i;
import rzaeditor.pageobjects.PageObjectComplex;

public class CurrentSource extends PageObjectComplex {

    float currentValue = 0;

    public CurrentSource(Vector2i p, Direction dir) {
        super(p, dir);
    }
}

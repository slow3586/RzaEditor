package rzaeditor.pageobjects.relays;

import rzaeditor.pageobjects.contacts.Contact;
import java.util.HashSet;
import org.joml.Vector2i;
import rzaeditor.pageobjects.PageObjectComplex;

public abstract class Relay extends PageObjectComplex {
    
    public Relay(Vector2i p, Direction dir) {
        super(p, dir);
    }
    
    public void activate(){
    }
}

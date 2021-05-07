package rzaeditor.pageobjects;

import java.util.HashSet;
import org.joml.Vector2i;

public class Relay extends PageObjectComplex {

    HashSet<Contact> basicContacts = new HashSet<>();
    
    public Relay(Vector2i p, boolean rot) {
        super(p, rot);
    }
    
    public void activate(){
        basicContacts.forEach((t) -> {
            t.activate();
        });
    }

    @Override
    public void updatePageInteractions() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}

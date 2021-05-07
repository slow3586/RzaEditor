package rzaeditor.pageobjects;

import org.joml.Vector2i;

abstract public class Contact extends PageObjectComplex {

    int contactId0 = 1;
    int contactId1 = 3;
    boolean isOpen = true;
    
    public Contact(Vector2i p, boolean rot) {
        super(p, rot);
    }
    
    public void activate(){
        isOpen=!isOpen;
    }

}

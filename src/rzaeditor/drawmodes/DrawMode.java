package rzaeditor.drawmodes;

import rzaeditor.Logic;
import static rzaeditor.Logic.ep;

public abstract class DrawMode {

    private static DrawMode current = DrawModeWire.imp;
    public String infoText = "";
    
    
    public static DrawMode getCurrent(){
        return current;
    }
    
    public static void setCurrent(DrawMode m){
        current.cleanup();
        current = m;
    }
    
    public void cleanup(){}
    public void mouseDrag(){}
    public void mouseMove(){}
    public void draw(){}
    public void mousePressed(){}
    public void mouseReleased(){} 
    public void keyboardEvent(){}
}

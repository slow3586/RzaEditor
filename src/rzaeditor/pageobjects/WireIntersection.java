package rzaeditor.pageobjects;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import org.joml.Vector2f;
import org.joml.Vector2i;
import rzaeditor.Drawing;
import rzaeditor.InfoTable;
import rzaeditor.Logic;
import rzaeditor.Page;
import rzaeditor.pageobjects.PageObjectBase;
import rzaeditor.pageobjects.PageObjectComplex;
import rzaeditor.pageobjects.PageObjectComplex.Direction;
import rzaeditor.pageobjects.Wire;

public class WireIntersection extends PageObjectBase{
    public static final String defaultType = "Связка";
    public HashSet<Wire> wires = new HashSet<>();
    //HashSet<WireIntersection> connected = new HashSet<>();
    public HashSet<WireIntersection> wireless = new HashSet<>();
    public HashSet<WireIntersection> voltageTo = new HashSet<>();
    public boolean on = true;
    public String textAbove = "";
    public String textBelow = "";
    public WIType wiType = WIType.NORMAL;
    public static final String[] fieldsToSave = new String[]{"textAbove", "textBelow", "wiType"};
            
    public static enum WIType{
        NORMAL,
        TERMINAL
    }

    private WireIntersection(Vector2i p) {
        super(p);
    }
    
    @Override
    public void moveTo(Vector2i p){
        WireIntersection n = getWIAt(p);
        if(n==null){
            pos = p;
        }else{
            n.wireless.addAll(wireless);
            //n.
        }
    }
    
    public static WireIntersection getWI(int x, int y, PageObjectComplex o) {
        return getWI(new Vector2i(x,y).add(o.pos.x, o.pos.y));
    }
    
    public static WireIntersection getWI(Vector2i p) {
        WireIntersection wi = getWIAt(p);
        if (wi == null) {
            wi = new WireIntersection(p);
        }
        return wi;
    }
    
    public void addWireless(WireIntersection i){
        wireless.add(i);
        i.wireless.add(this);
    }
    
    public void removeWireless(WireIntersection i){
        if(wireless.contains(i))
        wireless.remove(i);
        i.wireless.remove(this);
    }
    
    public void removeWire(Wire w){
        wires.remove(w);
        checkIsEmpty();
    }

    @Override
    public void onSelect() {
        super.onSelect();
        InfoTable.addLineText("Текст сверху", this, "textAbove");
        InfoTable.addLineText("Текст снизу", this, "textBelow");
        InfoTable.addLineOptions("Вид", this, "wiType", new String[]{"Обычная", "Клемма"}, new Object[]{WIType.NORMAL, WIType.TERMINAL});
    }
    
    public void draw() {
        
        int size = 3;
        
        selectedCheck();
        Drawing.setTranslateGrid(pos);
        Drawing.setFontSizeZoom(5);
        Drawing.drawStringZoomCentered(textAbove, 0, -6);
        Drawing.drawStringZoomCentered(textBelow, 0, 6);
        
        if(wiType==WIType.NORMAL)
            Drawing.fillOvalZoom(-2, -2, 4, 4);
        else if(wiType==WIType.TERMINAL){
            Drawing.setColor(Color.white);
            Drawing.fillOvalZoom(-2, -2, 4, 4);
            Drawing.setColor(Color.black);
            Drawing.drawOvalZoom(-2, -2, 4, 4);
            Drawing.drawLineZoom(1-3, 5-3, 5-3, 1-3);
        }
    }

    private static WireIntersection getWIAt(Vector2i p) {
        Optional<WireIntersection> m = Page.current.getWireIntersections().stream().filter((t) -> {
            System.out.println(p+" "+t.pos);
            return t.pos.equals(p);
        }).findFirst();
        return m.orElse(null);
    }

    public void checkIsEmpty() {
        if (wires.isEmpty() && wireless.isEmpty()) {
            delete();
        }
    }

    @Override
    public void delete() {
        if(wires.isEmpty() && wireless.isEmpty())
            super.delete();
    }
    
    
}

package rzaeditor.pageobjects;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.HashSet;
import java.util.Optional;
import org.joml.Vector2f;
import org.joml.Vector2i;
import rzaeditor.Drawing;
import rzaeditor.Logic;
import rzaeditor.Page;
import rzaeditor.pageobjects.PageObjectComplex.Direction;

public class WireIntersection extends PageObjectBase{
    HashSet<Wire> wires = new HashSet<>();
    //HashSet<WireIntersection> connected = new HashSet<>();
    HashSet<WireIntersection> wireless = new HashSet<>();
    HashSet<WireIntersection> voltageTo = new HashSet<>();
    boolean on = true;
    
    @Override
    public String getType() {
        return "Связка";
    }

    private WireIntersection(Vector2i p) {
        super(p);
        
        Page.current.objects.add(this);
    }
    
    public static WireIntersection getWI(int x, int y, PageObjectComplex o) {
        return getWI(Logic.swapIfTrue(x,y, o.direction==Direction.UP || o.direction==Direction.DOWN).add(o.pos.x, o.pos.y));
    }

    @Override
    public String save() {
        return null;
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
    
    public void removeWire(Wire w){
        wires.remove(w);
        checkIsEmpty();
    }

    public void draw() {
        
        int size = Math.round(3 * Logic.zoom);
        
        selectedCheck();
        Drawing.setTranslateGrid(pos);
        
        Drawing.fillOval(-size/2, -size/2, size, size);
    }

    private static WireIntersection getWIAt(Vector2i p) {
        HashSet<WireIntersection> s = new HashSet<>();
        s.addAll(Page.current.getWireIntersections());
        Page.current.objects.forEach((t) -> {
            if(!t.getClass().equals(PageObjectComplex.class)) return;
            
            PageObjectComplex o = (PageObjectComplex) t;
            s.addAll(o.wireIntersections);
        });
        Optional<WireIntersection> m = s.stream().filter((t) -> {
            return t.pos.equals(p);
        }).findFirst();
        return m.orElse(null);
    }

    public void checkIsEmpty() {
        if (wires.isEmpty() && wireless.isEmpty()) {
            delete();
        }
    }
}

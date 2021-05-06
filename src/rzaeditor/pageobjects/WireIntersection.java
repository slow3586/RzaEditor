package rzaeditor.pageobjects;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.util.HashSet;
import java.util.Optional;
import org.joml.Vector2f;
import org.joml.Vector2i;
import rzaeditor.Drawing;
import rzaeditor.Logic;
import rzaeditor.Page;

public class WireIntersection{

    Vector2i pos = new Vector2i();
    HashSet<Wire> wireIntersects = new HashSet<>();
    HashSet<WireIntersection> connected = new HashSet<>();
    HashSet<WireIntersection> connectedWireless = new HashSet<>();
    HashSet<WireIntersection> voltageTo = new HashSet<>();
    boolean on = true;

    private WireIntersection(Vector2i p) {
        pos = p;
        for (Wire wire : Page.current.wires) {
            if (wire.start.equals(pos)) {
                wire.startWI = this;
                wireIntersects.add(wire);
            }
            if (wire.end.equals(pos)) {
                wire.endWI = this;
                wireIntersects.add(wire);
            }
            if (wire.pointInside(pos, 1)) {
                wireIntersects.add(wire);
            }
        }
        if (!wireIntersects.isEmpty()) {
            Page.current.wireIntersections.add(this);
        }
    }

    public static WireIntersection getWI(Vector2i p) {
        WireIntersection wi = getWIAt(p);
        if (wi == null) {
            wi = new WireIntersection(p);
        }
        return wi;
    }

    public void draw() {
        int size = Math.round(3 * Logic.zoom);
        
        Drawing.setTranslateGrid(pos);
        
        Drawing.fillOval(-size/2, -size/2, size, size);
    }

    private static WireIntersection getWIAt(Vector2i p) {
        HashSet<WireIntersection> s = new HashSet<>();
        s.addAll(Page.current.wireIntersections);
        Page.current.objects.forEach((t) -> {
            s.addAll(t.wireIntersections);
        });
        Optional<WireIntersection> m = s.stream().filter((t) -> {
            return t.pos.equals(p);
        }).findFirst();
        return m.orElse(null);
    }

    public void checkIsEmpty() {
        if (wireIntersects.isEmpty()) {
            delete();
        }
    }

    public void delete() {
        Page.current.wireIntersections.remove(this);
    }
}

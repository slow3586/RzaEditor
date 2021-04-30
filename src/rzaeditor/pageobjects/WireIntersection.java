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

public class WireIntersection {

    Vector2i pos = new Vector2i();
    HashSet<Wire> wireIntersects = new HashSet<>();

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
        Vector2i t0 = Logic.gridToScreenCenter(pos);
        int size = Math.round(3 * Logic.zoom);
        
        Drawing.fillOval(t0.x - size/2, t0.y - size/2, size, size);
    }

    public static WireIntersection getWIAt(Vector2i p) {
        Optional<WireIntersection> m = Page.current.wireIntersections.stream().filter((t) -> {
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

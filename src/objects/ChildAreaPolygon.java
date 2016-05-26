//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package objects;

import java.awt.geom.Point2D;
import java.util.Vector;
import objects.AreaPolygon;
import objects.MotherAreaPolygon;

public class ChildAreaPolygon extends AreaPolygon {
    private MotherAreaPolygon motherAreaPolygon;
    private boolean isInMotherPolygon;

    public ChildAreaPolygon(Vector<Point2D> vectorList, MotherAreaPolygon motherAreaPolygon) {
        super(vectorList);
        this.motherAreaPolygon = motherAreaPolygon;
    }

    public MotherAreaPolygon getMotherAreaPolygon() {
        return this.motherAreaPolygon;
    }

    public void setMotherAreaPolygon(MotherAreaPolygon motherAreaPolygon) {
        this.motherAreaPolygon = motherAreaPolygon;
    }

    public boolean isInMotherPolygon() {
        return this.isInMotherPolygon;
    }

    public void setInMotherPolygon(boolean isInMotherPolygon) {
        this.isInMotherPolygon = isInMotherPolygon;
    }
}

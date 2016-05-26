//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package objects;

import java.awt.geom.Point2D;
import java.util.List;
import java.util.Vector;
import objects.AreaPolygon;
import objects.ChildAreaPolygon;

public class MotherAreaPolygon extends AreaPolygon {
    private List<ChildAreaPolygon> areaPolygonChildList;

    public MotherAreaPolygon(Vector<Point2D> vectorList) {
        super(vectorList);
    }

    public MotherAreaPolygon(Vector<Point2D> vectorList, List<ChildAreaPolygon> areaPolygonChildList) {
        super(vectorList);
        this.areaPolygonChildList = areaPolygonChildList;
    }

    public List<ChildAreaPolygon> getAreaPolygonChildList() {
        return this.areaPolygonChildList;
    }

    public void setAreaPolygonChildList(List<ChildAreaPolygon> areaPolygonChildList) {
        this.areaPolygonChildList = areaPolygonChildList;
    }
}

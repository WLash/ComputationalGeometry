package pra2;

import java.awt.geom.Point2D;
import java.util.List;
import java.util.Vector;

/**
 * Created by Wilhelm on 22.05.2016.
 */
public class AreaPolygon {

    Vector<Point2D> vectorPoints;
    AreaPolygon motherAreaPolygon;
    List<AreaPolygon> areaPolygonChildList;
    boolean isInMainArea;

    public List<AreaPolygon> getAreaPolygonChildList() {
        return areaPolygonChildList;
    }

    public void setAreaPolygonChildList(List<AreaPolygon> areaPolygonChildList) {
        this.areaPolygonChildList = areaPolygonChildList;
    }

    double area;

    public AreaPolygon(Vector<Point2D> vectorPoints) {
        this.vectorPoints = vectorPoints;
    }

    public Vector<Point2D> getVectorPoints() {
        return vectorPoints;
    }

    public void setVectorPoints(Vector<Point2D> vectorPoints) {
        this.vectorPoints = vectorPoints;
    }

    public AreaPolygon getMotherAreaPolygon() {
        return motherAreaPolygon;
    }

    public void setMotherAreaPolygon(AreaPolygon motherAreaPolygon) {
        this.motherAreaPolygon = motherAreaPolygon;
    }

    public boolean isInMainArea() {
        return isInMainArea;
    }

    public void setInMainArea(boolean inMainArea) {
        isInMainArea = inMainArea;
    }

    public double getArea() {
        return area;
    }

    public void setArea(double area) {
        this.area = area;
    }
}

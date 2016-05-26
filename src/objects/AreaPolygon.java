//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package objects;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.Vector;

public class AreaPolygon {
    private Vector<Point2D> vectorPoints;
    private double area;
    private Point2D leftestPoint;
    private Point2D highestPoint;

    public AreaPolygon(Vector<Point2D> vectorPoints) {
        this.vectorPoints = vectorPoints;
    }

    public Vector<Point2D> getVectorPoints() {
        return this.vectorPoints;
    }

    public void setVectorPoints(Vector<Point2D> vectorPoints) {
        this.vectorPoints = vectorPoints;
    }

    public double getArea() {
        return this.area;
    }

    public void setArea(double area) {
        this.area = area;
    }

    public boolean isOrientationCounterClockWise() {
        return this.getArea() < 0.0D;
    }

    public Point2D getLeftestPoint() {
        return this.leftestPoint;
    }

    public void setLeftestPoint(Point2D leftestPoint) {
        this.leftestPoint = leftestPoint;
    }

    public Point2D getHighestPoint() {
        return this.highestPoint;
    }

    public void setHighestPoint(Point2D highestPoint) {
        this.highestPoint = highestPoint;
    }

    public Point2D getPointOuterPolygon() {
        double valueX = this.getLeftestPoint().getX() - 1.0D;
        double valueY = this.getHighestPoint().getY() + 1.0D;
        Double point = new Double(valueX, valueY);
        return point;
    }
}

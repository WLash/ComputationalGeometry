package objects;

import util.ComGeoUtil;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

/**
 * Created by wlasc on 08.06.2016.
 */
public class CustomLine2D extends Line2D.Double {

    int id;
    double gradient;
    double yIntersect;
    Line2D neighborLineAfter;

    public CustomLine2D(int id, Line2D.Double line2D) {
        super(line2D.getP1(), line2D.getP2());
        this.id = id;
        this.gradient = ComGeoUtil.getGradient(this);
        this.yIntersect = ComGeoUtil.getYIntercept(gradient, this.getP1());
    }

    public double getGradient() {
        return gradient;
    }

    public void setGradient(double gradient) {
        this.gradient = gradient;
    }

    public double getyIntersect() {
        return yIntersect;
    }

    public void setyIntersect(double yIntersect) {
        this.yIntersect = yIntersect;
    }

    public double getActualY(double xIntersect){
        return gradient * xIntersect + yIntersect;
    }

    public Line2D getNeighborLineAfter() {
        return neighborLineAfter;
    }

    public void setNeighborLineAfter(Line2D neighborLineAfter) {
        this.neighborLineAfter = neighborLineAfter;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /**
     * Berechnet Schnittpunkt der Strecken. Gibt null zurück wenn kein Schnittpunkt besteht.
     * @param line
     * @return
     */
    public Point2D.Double intersectLines(CustomLine2D line)throws IllegalArgumentException
    {
        // Wegen der Lesbarkeit
        double x1 = this.getX1();
        double x2 = this.getX2();
        double x3 = line.getX1();
        double x4 = line.getX2();
        double y1 = this.getY1();
        double y2 = this.getY2();
        double y3 = line.getY1();
        double y4 = line.getY2();

        // Zaehler
        double zx = (x1 * y2 - y1 * x2)*(x3-x4) - (x1 - x2) * (x3 * y4 - y3 * x4);
        double zy = (x1 * y2 - y1 * x2)*(y3-y4) - (y1 - y2) * (x3 * y4 - y3 * x4);

        // Nenner
        double n = (x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4);

        // Koordinaten des Schnittpunktes
        double x = zx/n;
        double y = zy/n;


        // Test ob der Schnittpunkt auf den angebenen Strecken liegt oder außerhalb.
        if ( ((x3 >= x1 && x3 <= x2) || (x3 >=x2 && x3 <= x1) )
                || ((x4 >= x1 && x3 <= x2) || (x4 >=x2 && x3 <= x1) ))
        {

            return null;

        }
        return new Point2D.Double(x,y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CustomLine2D that = (CustomLine2D) o;

        return id == that.id;

    }

    @Override
    public int hashCode() {
        return id;
    }
}

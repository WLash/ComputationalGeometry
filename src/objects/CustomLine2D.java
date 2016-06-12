package objects;

import aufgabe3.Aufgabe3;
import org.apache.logging.log4j.Logger;
import util.ComGeoUtil;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.logging.LogManager;

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
     * @param m
     * @return
     */
    public Point2D.Double intersectLines(Line2D m)throws IllegalArgumentException {
        Line2D l = new Line2D.Double(this.getP1(), this.getP2());

        // Wegen der Lesbarkeit
        double x1 = l.getX1();
        double x2 = l.getX2();
        double x3 = m.getX1();
        double x4 = m.getX2();
        double y1 = l.getY1();
        double y2 = l.getY2();
        double y3 = m.getY1();
        double y4 = m.getY2();

        // Zaehler
        double zx = (x1 * y2 - y1 * x2)*(x3-x4) - (x1 - x2) * (x3 * y4 - y3 * x4);
        double zy = (x1 * y2 - y1 * x2)*(y3-y4) - (y1 - y2) * (x3 * y4 - y3 * x4);

        // Nenner
        double n = (x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4);

        // Koordinaten des Schnittpunktes
        double x = zx/n;
        double y = zy/n;

        if( (x >= x1 && x <= x2) && (x >= x3 && x <= x4) ){

            if(l.getP1().equals(m.getP1()) || l.getP1().equals(m.getP2()) || l.getP2().equals(m.getP1()) || l.getP2().equals(m.getP2())){
                return null;
            }

            // Vielleicht ist bei der Division durch n etwas schief gelaufen
            if (java.lang.Double.isNaN(x)& java.lang.Double.isNaN(y)) {


                throw new IllegalArgumentException("Schnittpunkt nicht eindeutig.");
            }

            Point2D.Double p = new Point2D.Double(x,y);

            return p;

        } else {
            return null;
        }

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

    // Test ob der Schnittpunkt auf den angebenen Strecken liegt oder außerhalb.
    //if ( ((x3 >= x1 && x3 <= x2) || (x3 >=x2 && x3 <= x1) )
      //      || ((x4 >= x1 && x3 <= x2) || (x4 >=x2 && x3 <= x1) ))
    //{

      //  return null;

    //}
}

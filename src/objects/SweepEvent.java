package objects;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

/**
 * Created by wlasc on 07.06.2016.
 */
public class SweepEvent implements Comparable<SweepEvent>{



    public enum SweepEventType {

        START           (1, "Startpunkt"),
        END             (2, "Endpunkt"),
        INTERSECT       (3, "Schnittpunkt");

        private int id;
        private String name;

        private SweepEventType(int id, String name){
            this.id = id;
            this.name = name;
        }
    }

    private SweepEventType sweepEventType;
    private Point2D point;

    public SweepEvent(){}

    public SweepEvent(SweepEventType sweepEventType, Point2D point) {
        this.sweepEventType = sweepEventType;
        this.point = point;
    }

    public Point2D getPoint() {
        return point;
    }

    public void setPoint(Point2D point) {
        this.point = point;
    }

    public SweepEventType getSweepEventType() {
        return sweepEventType;
    }

    public void setSweepEventType(SweepEventType sweepEventType) {
        this.sweepEventType = sweepEventType;
    }


    @Override
    public int compareTo(SweepEvent o) {
        Double thisX = this.getPoint().getX();
        Double oX = o.getPoint().getX();
        return thisX.compareTo(oX);
    }
}

package objects;

import java.awt.geom.Point2D;

/**
 * Created by wlasc on 07.06.2016.
 */
public class Event{


    private Point2D point;

    public Event(){}

    public Event(Point2D point) {
        this.point = point;
    }

    public Point2D getPoint() {
        return point;
    }

    public void setPoint(Point2D point) {
        this.point = point;
    }

}

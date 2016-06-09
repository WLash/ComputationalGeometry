package objects;


import java.awt.geom.Point2D;

/**
 * Created by wlasc on 08.06.2016.
 */
public class IntersectEvent extends SweepEvent {

    CustomLine2D line1;
    CustomLine2D line2;

    public IntersectEvent(CustomLine2D line1, CustomLine2D line2) {
        this.line1 = line1;
        this.line2 = line2;
    }

    public IntersectEvent(Point2D point, CustomLine2D line1, CustomLine2D line2) {
        super(SweepEventType.INTERSECT, point);
        this.line1 = line1;
        this.line2 = line2;
    }

}

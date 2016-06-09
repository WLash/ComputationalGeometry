package objects;


import java.awt.geom.Point2D;

/**
 * Created by wlasc on 08.06.2016.
 */
public class IntersectEvent extends Event {

    CustomLine2D line1;
    CustomLine2D line2;

    public IntersectEvent(CustomLine2D line1, CustomLine2D line2) {
        this.line1 = line1;
        this.line2 = line2;
    }

    public IntersectEvent(Point2D point, CustomLine2D line1, CustomLine2D line2) {
        super (point);
        this.line1 = line1;
        this.line2 = line2;
    }


    public CustomLine2D getLine1() {
        return line1;
    }

    public void setLine1(CustomLine2D line1) {
        this.line1 = line1;
    }

    public CustomLine2D getLine2() {
        return line2;
    }

    public void setLine2(CustomLine2D line2) {
        this.line2 = line2;
    }
}

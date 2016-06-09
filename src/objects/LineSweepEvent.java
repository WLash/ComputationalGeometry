package objects;

import java.awt.geom.Point2D;

/**
 * Created by wlasc on 08.06.2016.
 */
public class LineSweepEvent extends SweepEvent {


    private CustomLine2D line;

    public LineSweepEvent(SweepEventType sweepEventType, Point2D point, CustomLine2D line) {
        super(sweepEventType, point);
        this.line = line;
    }

    public CustomLine2D getLine() {
        return line;
    }

    public void setLine(CustomLine2D line) {
        this.line = line;
    }

}

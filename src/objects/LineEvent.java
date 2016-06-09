package objects;

import java.awt.geom.Point2D;

/**
 * Created by wlasc on 08.06.2016.
 */
public class LineEvent extends Event {


    private CustomLine2D line;
    private EventType eventType;

    public enum EventType {

        START           (1, "Startpunkt"),
        END             (2, "Endpunkt");

        private int id;
        private String name;

        private EventType(int id, String name){
            this.id = id;
            this.name = name;
        }
    }

    public LineEvent(EventType eventType, Point2D point, CustomLine2D line) {
        super(point);
        this.line = line;
        this.eventType = eventType;
    }

    public CustomLine2D getLine() {
        return line;
    }

    public void setLine(CustomLine2D line) {
        this.line = line;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }
}

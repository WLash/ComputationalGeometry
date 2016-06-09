package objects;

import javax.sound.sampled.LineEvent;

/**
 * Created by wlasc on 07.06.2016.
 */
public enum LineEventType {

    START           (1, "Startpunkt"),
    END             (2, "Endpunkt"),
    INTERSECTIOIN   (3, "Schnittpunkt");

    private int id;
    private String name;

    private LineEventType(int id, String name){
        this.id = id;
        this.name = name;
    }
}

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package objects;

import java.awt.geom.Point2D;
import objects.SubCountry;

public class Capital {
    String name;
    Point2D point;
    SubCountry subCountry;

    public Capital(String name, Point2D point) {
        this.name = name;
        this.point = point;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Point2D getPoint() {
        return this.point;
    }

    public void setPoint(Point2D point) {
        this.point = point;
    }

    public SubCountry getSubCountry() {
        return this.subCountry;
    }

    public void setSubCountry(SubCountry subCountry) {
        this.subCountry = subCountry;
    }
}

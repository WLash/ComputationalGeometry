//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package objects;

import java.util.List;
import objects.AreaPolygon;
import objects.Capital;

public class SubCountry {
    private String name;
    private Capital capital;
    private List<AreaPolygon> polygonlist;
    private double area;

    public SubCountry(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Capital getCapital() {
        return this.capital;
    }

    public void setCapital(Capital capital) {
        this.capital = capital;
    }

    public List<AreaPolygon> getPolygonlist() {
        return this.polygonlist;
    }

    public void setPolygonlist(List<AreaPolygon> polygonlist) {
        this.polygonlist = polygonlist;
    }

    public double getArea() {
        return this.area;
    }

    public void setArea(double area) {
        this.area = area;
    }
}

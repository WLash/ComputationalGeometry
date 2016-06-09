//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package util;

import aufgabe1.Aufgabe1;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;
import objects.AreaPolygon;
import objects.CustomLine2D;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ComGeoUtil {
    private static Logger log = LogManager.getLogger(Aufgabe1.class);

    public ComGeoUtil() {
    }

    public static int ccw(Line2D q, Point2D p) {
        Vector<Double> vectorLine = calculateVector(q.getP1(), q.getP2());
        Vector<Double> vectorPoint = calculateVector(q.getP1(), p);
        byte retValue = 0;
        double crossProduct = (vectorLine.get(0)) * (vectorPoint.get(1)) - (vectorLine.get(1)) * (vectorPoint.get(0));
        if(crossProduct < 0.0) {
            retValue = -1;
        } else if(crossProduct == 0.0) {
            retValue = 0;
        } else if(crossProduct > 0.0) {
            retValue = 1;
        }

        return retValue;
    }

    public static Vector<Double> calculateVector(Point2D p1, Point2D p2) {
        Vector<Double> vector = new Vector();
        vector.add(p2.getX() - p1.getX());
        vector.add(p2.getY() - p1.getY());
        return vector;
    }

    public static boolean pointInPolygon(AreaPolygon polygon, Point2D point) {
        Point2D pointOuterPolygon = polygon.getPointOuterPolygon();
        Line2D.Double checkLine = new Line2D.Double(point, pointOuterPolygon);
        int crossCount = 0;
        Vector<Point2D> vector = polygon.getVectorPoints();

        for(int pointInPolygon = 0; pointInPolygon < vector.size() - 1; ++pointInPolygon) {
            Point2D p = vector.get(pointInPolygon);
            Point2D pa = vector.get(pointInPolygon + 1);
            Line2D.Double linePolygon = new Line2D.Double(p, pa);
            int ccwCheckLinePoint1 = ccw(checkLine, linePolygon.getP1());
            int ccwCheckLinePoint2 = ccw(checkLine, linePolygon.getP2());
            int ccwPolygonLinePoint1 = ccw(linePolygon, checkLine.getP1());
            int ccwPolygonLinePoint2 = ccw(linePolygon, checkLine.getP2());
            int ccwCheckLine = ccwCheckLinePoint1 * ccwCheckLinePoint2;
            int ccwPolygonLine = ccwPolygonLinePoint1 * ccwPolygonLinePoint2;
            boolean areLinesCrossing = ccwCheckLine <= 0 && ccwPolygonLine <= 0;
            log.debug(checkLine.getP1() + " " + checkLine.getP2());
            log.debug(linePolygon.getP1() + " " + checkLine.getP2());
            if(areLinesCrossing) {
                ++crossCount;
            }
        }

        boolean isPointInPolygon = crossCount % 2 > 0 && crossCount != 0;
        return isPointInPolygon;
    }

    public static Vector<Line2D.Double> readLines(String path){
        Vector vector = new Vector();


        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(path));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            log.error("Pfad zur Datei ist ungültig!");
        }
        String line;
        try {
            while((line = br.readLine()) != null) {
                String[] val = line.split(" ");

                Line2D.Double strecke = new Line2D.Double(Double.valueOf(val[0]), Double.valueOf(val[1]), Double.valueOf(val[2]), Double.valueOf(val[3]));
                vector.add(strecke);
            }
        } catch (IOException e) {
            e.printStackTrace();
            log.error("Fehler beim Einlesen der Datei!");
        }

        return vector;

    }

    public static double getGradient(Line2D line){
        return getGradient(line.getP1(), line.getP2());
    }

    public static double getGradient(Point2D p1, Point2D p2){
        double m = (p2.getY() - p1.getY() ) / (p2.getX() - p1.getX());
        return m;
    }


    public static double getYIntercept(double gradient, Point2D p){
        double t = p.getY() - gradient * p.getX();
        return t;
    }
  /*  y2 - y1      -4 - (-8)
    m  =  —————————  =  —————————  =  -4/3
    x2 - x1        -1 - 2

    b  =  y2 - m·x2  =  -4 - (-4/3)·(-1) = -16/3

    Gesuchte Funktion:

    f(x) = -4/3·x - 16/3*/

}

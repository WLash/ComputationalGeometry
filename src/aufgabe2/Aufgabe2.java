//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package aufgabe2;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import objects.AreaPolygon;
import objects.Capital;
import objects.ChildAreaPolygon;
import objects.MotherAreaPolygon;
import objects.SubCountry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.util.StringBuilders;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import util.ComGeoUtil;

public class Aufgabe2 {
    private static Logger log = LogManager.getLogger(Aufgabe2.class);

    public Aufgabe2() {
    }

    public static void start() throws Exception {
        try {
            File file = new File("src/aufgabe2/svg/deutschland.svg");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            FileWriter fw = new FileWriter("src/aufgabe2/results/ergebnis" + cal.get(Calendar.YEAR) +
                    cal.get(Calendar.MONTH) + cal.get(Calendar.DAY_OF_MONTH) + "_" + System.currentTimeMillis() + ".txt");
            BufferedWriter bw = new BufferedWriter(fw);
            doc.getDocumentElement().normalize();
            log.debug("Root element :" + doc.getDocumentElement().getNodeName());
            NodeList nList = doc.getElementsByTagName("path");
            log.debug("----------------------------");
            ArrayList<SubCountry> subCountryList = new ArrayList();

            for(int temp = 0; temp < nList.getLength(); ++temp) {
                Node nNode = nList.item(temp);
                log.debug("\nCurrent Element :" + nNode.getNodeName());
                if(nNode.getNodeType() == 1) {
                    Element eElement = (Element)nNode;
                    String id = eElement.getAttribute("id");
                    log.debug("id : " + id);
                    log.debug("fill: " + eElement.getAttribute("fill"));
                    String capitalPointValueXString = eElement.getAttribute("sodipodi:cx");
                    String capitalPointValueYString = eElement.getAttribute("sodipodi:cy");
                    String pathString = eElement.getAttribute("d");

                    if(!pathString.isEmpty()) {
                        SubCountry bundesland = new SubCountry(id);
                        ArrayList<AreaPolygon> areaPolygonList = pathStringToAreaPolygonList(pathString);
                        log.debug("Anzahl Flächen: " + areaPolygonList.size());

                        for(int i = 0; i < areaPolygonList.size(); i++) {
                            AreaPolygon areaPolygon = areaPolygonList.get(i);
                            calcAreaOfPolygon(areaPolygon);
                        }

                        bundesland.setPolygonlist(areaPolygonList);
                        double allArea = 0.0;


                        for(AreaPolygon polygon : areaPolygonList){

                            ChildAreaPolygon childAreaPolygon = null;
                            if(polygon instanceof ChildAreaPolygon) {
                                childAreaPolygon = (ChildAreaPolygon)polygon;
                                boolean pointInPolygon = ComGeoUtil.pointInPolygon(childAreaPolygon.getMotherAreaPolygon(), childAreaPolygon.getVectorPoints().get(0));
                                childAreaPolygon.setInMotherPolygon(pointInPolygon);
                            }

                            if(childAreaPolygon != null && childAreaPolygon.isInMotherPolygon()) {
                                allArea -= StrictMath.abs(polygon.getArea());
                            } else {
                                allArea += StrictMath.abs(polygon.getArea());
                            }
                        }
                        bundesland.setArea(allArea);

                        for(AreaPolygon polygon : areaPolygonList){
                            log.debug("Fläche Polygon: " + polygon.getArea());
                        }

                        log.debug("Gesamtfläche: " + allArea);
                        bw.write(bundesland.getName() + ":" + "\n");
                        bw.write("    Fläche: " + bundesland.getArea() + "\n");
                        subCountryList.add(bundesland);
                        log.debug("Bundesland " + bundesland.getName() + " zur Liste hinzugefügt." + subCountryList.size());
                    } else if(!capitalPointValueXString.isEmpty() && !capitalPointValueYString.isEmpty()) {
                        Point2D.Double point = new Point2D.Double(Double.valueOf(capitalPointValueXString), Double.valueOf(capitalPointValueYString));
                        Capital capital = new Capital(id, point);


                        for(SubCountry bundesland : subCountryList) {

                            for(AreaPolygon polygon : bundesland.getPolygonlist()){
                                boolean isCapitalInSubCountry = ComGeoUtil.pointInPolygon(polygon, capital.getPoint());
                                if(isCapitalInSubCountry) {
                                    bundesland.setCapital(capital);
                                    capital.setSubCountry(bundesland);
                                    break;
                                }
                            }
                        }
                        bw.write(capital.getName() + " -> " + capital.getSubCountry().getName() + "\n");
                    }
                }
            }

            bw.close();
        } catch (Exception var24) {
            var24.printStackTrace();
        }

    }

    private static void calcAreaOfPolygon(AreaPolygon currentPolygon) {
        double area = 0.0D;
        Vector<Point2D> vector = currentPolygon.getVectorPoints();

        for(int i = 0; i < vector.size(); ++i) {
            boolean first = i == 0;
            boolean last = i == vector.size() - 1;
            Point2D p = vector.elementAt(i);
            Point2D pb;
            if(first) {
                pb = vector.elementAt(vector.size() - 1);
            } else {
                pb = vector.elementAt(i - 1);
            }

            Point2D pa;
            if(last) {
                pa = vector.elementAt(0);
            } else {
                pa = vector.elementAt(i + 1);
            }

            area += p.getY() * (pb.getX() - pa.getX()) / 2.0D;
            if(currentPolygon.getLeftestPoint() == null || currentPolygon.getLeftestPoint().getX() > p.getX()) {
                currentPolygon.setLeftestPoint(p);
            }

            if(currentPolygon.getHighestPoint() == null || currentPolygon.getHighestPoint().getY() < p.getY()) {
                currentPolygon.setHighestPoint(p);
            }
        }
        currentPolygon.setArea(area);
    }

    private static ArrayList<AreaPolygon> pathStringToAreaPolygonList(String pathString) {
        ArrayList resultList = new ArrayList();
        String[] areasOfBundesland = pathString.substring(1).split("z");

        for(int i = 0; i < areasOfBundesland.length - 1; ++i) {
            String childAreaPolygonList = areasOfBundesland[i];
            if(i > 0) {
                childAreaPolygonList = childAreaPolygonList.substring(1);
            }

            String[] coordLines = childAreaPolygonList.split(" ");
            Vector<Point2D> vector = new Vector();
            Point2D pointBefore = null;

            for(int j = 0; j < coordLines.length; j++) {
                boolean isLast = j == coordLines.length - 1;

                String coordString = coordLines[j];

                if(coordLines[j].contains("H")){
                    String[] hStringArray = coordString.split("H");
                    coordString = hStringArray[0];
                    Point2D hPoint1 = coordsLineToPoint(coordString, pointBefore, isLast);
                    vector.add(hPoint1);
                    Point2D hPoint2 = new Point2D.Double(Double.valueOf(hStringArray[1]), hPoint1.getY());
                    vector.add(hPoint2);
                } else {
                    Point2D point = coordsLineToPoint(coordString, pointBefore, isLast);
                    if (point != null) {
                        vector.add(point);
                        pointBefore = point;
                    }
                }
            }

            log.debug("Anzahl Punkte Polygon: " + vector.size());
            AreaPolygon areaPolygon;
            if(!resultList.isEmpty()) {
                areaPolygon = new ChildAreaPolygon(vector, (MotherAreaPolygon)resultList.get(0));
            } else {
                areaPolygon = new MotherAreaPolygon(vector);
            }

            resultList.add(areaPolygon);
        }

        MotherAreaPolygon motherAreaPolygon = (MotherAreaPolygon)resultList.get(0);
        List<ChildAreaPolygon> childAreaPolygonList = (ArrayList)resultList.clone();
        childAreaPolygonList.remove(0);
        motherAreaPolygon.setAreaPolygonChildList(childAreaPolygonList);
        return resultList;
    }

    private static Point2D coordsLineToPoint(String coordsLine, Point2D pointBefore, boolean isLast) {
        Point2D.Double point = null;
        if(!coordsLine.isEmpty()) {
            boolean startsWithL = coordsLine.startsWith("L");
            String[] coordsStringArray = coordsLine.substring(1).split(",");

            double valueX = Double.valueOf(coordsStringArray[0]);
            double valueY = Double.valueOf(coordsStringArray[1]);
            if(!isLast && pointBefore != null && !startsWithL) {
                valueX += pointBefore.getX();
                valueY += pointBefore.getY();
            }

            point = new Point2D.Double(valueX, valueY);
        }

        return point;
    }
}

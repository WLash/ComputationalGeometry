package aufgabe3;

import objects.CustomLine2D;
import objects.IntersectEvent;
import objects.LineSweepEvent;
import objects.SweepEvent;
import objects.exceptions.BadDataException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import util.ComGeoUtil;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.*;

/**
 * Created by wlasc on 07.06.2016.
 */
public class Aufgabe3 {

    private static Logger log = LogManager.getLogger(Aufgabe3.class);

    public static void start(){

        Vector<Line2D.Double> line2DVector = ComGeoUtil.readLines("src/aufgabe3/coords/simple_test.dat");

        try {
            List<SweepEvent> eventList = generateSortedEventList(line2DVector);

            List<CustomLine2D> lineOrder = new ArrayList<>();

            List<Point2D> intersectSet = new ArrayList<>();

            while(!eventList.isEmpty()){
                SweepEvent event = eventList.get(0);

                log.debug(event.getPoint().getX());

                if(event instanceof  LineSweepEvent) {

                    LineSweepEvent lineSweepEvent = (LineSweepEvent) event;
                    if(lineSweepEvent.getSweepEventType().equals(SweepEvent.SweepEventType.START)){
                        lineOrder.add(lineSweepEvent.getLine());
                    }

                    if(event.getSweepEventType().equals(SweepEvent.SweepEventType.END)) {
                        lineOrder.remove(lineSweepEvent.getLine());
                    }

                    if (lineOrder.size() > 1 ) {
                        //sortLineOrder(lineOrder, event.getPoint().getX()); //TODO: händisch sortieren und nur bei Schnittpunkt ändern

                        for (int i = 1; i < lineOrder.size(); i++) {

                            if (i != lineOrder.size() - 1) {
                                CustomLine2D customLine = lineOrder.get(i);
                                CustomLine2D customLineAfter = lineOrder.get(i + 1);

                                customLine.setNeighborLineAfter(customLineAfter);

                                Point2D intersect = customLine.intersectLines(customLine);
                                if (intersect != null) {
                                    intersectSet.add(intersect);
                                    SweepEvent intersectEvent = new IntersectEvent(intersect, customLine, customLineAfter);
                                    for (int j = 1; i < eventList.size(); i++) {
                                        SweepEvent sweepEvent = eventList.get(j);
                                        if (sweepEvent.getPoint().getX() >= intersect.getX()) {
                                            eventList.add(j, intersectEvent);
                                        }
                                    }
                                }


                            }
                        }


                    }
                }

                if(event instanceof IntersectEvent){

                }





                eventList.remove(0);
            }

            log.debug("Anzahl Schnittpunkte: " );




        } catch (Exception e) {
            log.error(e.getMessage());
        }


    }

    private static List<CustomLine2D> sortLineOrder(List<CustomLine2D> orderList, double xIntersect){
        Collections.sort(orderList, new Comparator<Object>() {
            @Override
            public int compare(Object a1, Object a2) {
                CustomLine2D line1 = (CustomLine2D) a1;
                CustomLine2D line2 = (CustomLine2D) a2;

                Double y1 = line1.calculateY(xIntersect);
                Double y2 = line2.calculateY(xIntersect);
                return y1.compareTo(y2);
            }
        });

        return orderList;
    }

    private static List<SweepEvent> generateSortedEventList(Vector<Line2D.Double> line2DVector) throws BadDataException {

        ArrayList<SweepEvent> arrayList = new ArrayList<>();
        int idCounter = 0;
        for(Line2D.Double line : line2DVector){

            Point2D p1 = line.getP1();
            Point2D p2 = line.getP2();

            if(p1.equals(p2)) throw new BadDataException("Beide Punkte der Strecke sind identisch!");
            else if(p1.getX() == p2.getX()) throw new BadDataException("Die Strecke verläuft parallel zur Y-Achse");

            idCounter = idCounter+2;
            if(p1.getX() < p2.getX()){
                arrayList.add(new LineSweepEvent(SweepEvent.SweepEventType.START, p1, new CustomLine2D(idCounter-1, line)));
                arrayList.add(new LineSweepEvent(SweepEvent.SweepEventType.END, p2, new CustomLine2D(idCounter, line)));

            } else {
                arrayList.add(new LineSweepEvent(SweepEvent.SweepEventType.START, p2, new CustomLine2D(idCounter-1, line)));
                arrayList.add(new LineSweepEvent(SweepEvent.SweepEventType.END, p1, new CustomLine2D(idCounter, line)));
            }
        }
        Collections.sort(arrayList);
        return arrayList;
    }




}

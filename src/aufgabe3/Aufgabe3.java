package aufgabe3;

import objects.CustomLine2D;
import objects.IntersectEvent;
import objects.LineEvent;
import objects.Event;
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

    public static void start() throws BadDataException {

        Vector<Line2D.Double> line2DVector = ComGeoUtil.readLines("src/aufgabe3/coords/simple_test.dat");

            List<Event> eventQueue = generateSortedEventList(line2DVector);

            List<CustomLine2D> sweepLineOrder = new ArrayList<>();

            List<Point2D> intersectSet = new ArrayList<>();

            while(!eventQueue.isEmpty()){
                Event event = eventQueue.get(0);

                log.debug(event.getPoint().getX());

                if(event instanceof LineEvent) {

                    LineEvent lineEvent = (LineEvent) event;
                    if(lineEvent.getEventType().equals(LineEvent.EventType.START)){
                        handleStartEvent(sweepLineOrder, eventQueue, lineEvent);

                    }

                    if(lineEvent.getEventType().equals(LineEvent.EventType.END)) {
                        handleEndEvent(sweepLineOrder, eventQueue, lineEvent);
                    }
                } else if(event instanceof IntersectEvent){
                    sweepLineOrder.indexOf(event);

                    IntersectEvent intersectEvent = (IntersectEvent) event;

                    int indexOfLine1 = sweepLineOrder.indexOf(intersectEvent.getLine1());
                    int indexOfLine2 = sweepLineOrder.indexOf(intersectEvent.getLine2());


                    Collections.swap(sweepLineOrder, indexOfLine1, indexOfLine2);
                }





                eventQueue.remove(0);
            }

            log.debug("Anzahl Schnittpunkte: " );





    }



    private static List<CustomLine2D> sortLineOrder(List<CustomLine2D> orderList, double xIntersect){
        Collections.sort(orderList, new Comparator<Object>() {
            @Override
            public int compare(Object a1, Object a2) {
                CustomLine2D line1 = (CustomLine2D) a1;
                CustomLine2D line2 = (CustomLine2D) a2;

                Double y1 = line1.getActualY(xIntersect);
                Double y2 = line2.getActualY(xIntersect);
                return y1.compareTo(y2);
            }
        });

        return orderList;
    }

    private static List<Event> generateSortedEventList(Vector<Line2D.Double> line2DVector) throws BadDataException {

        ArrayList<Event> arrayList = new ArrayList<>();
        int idCounter = 0;
        for(Line2D.Double line : line2DVector){

            Point2D p1 = line.getP1();
            Point2D p2 = line.getP2();

            if(p1.equals(p2)) throw new BadDataException("Beide Punkte der Strecke sind identisch!");
            else if(p1.getX() == p2.getX()) throw new BadDataException("Die Strecke verläuft parallel zur Y-Achse");

            idCounter = idCounter+2;
            if(p1.getX() < p2.getX()){
                arrayList.add(new LineEvent(LineEvent.EventType.START, p1, new CustomLine2D(idCounter-1, line)));
                arrayList.add(new LineEvent(LineEvent.EventType.END, p2, new CustomLine2D(idCounter, line)));

            } else {
                Line2D.Double swappedLine = new Line2D.Double(p2, p1);
                arrayList.add(new LineEvent(LineEvent.EventType.START, p2, new CustomLine2D(idCounter-1, swappedLine)));
                arrayList.add(new LineEvent(LineEvent.EventType.END, p1, new CustomLine2D(idCounter, swappedLine)));
            }
        }
        Collections.sort(arrayList, (o1, o2) -> {
            Double double1 = o1.getPoint().getX();
            Double double2 = o2.getPoint().getX();
            return double1.compareTo(double2);
        });
        return arrayList;
    }

    /**
     *
     * @param sweepLineOrder
     * @param line
     * @return Stelle an der die line einsortiert wurde
     */
    private static int addLineToSweepLine(List<CustomLine2D> sweepLineOrder, CustomLine2D line){
        int placeToSortIn = sweepLineOrder.size()-1;
        if(sweepLineOrder.isEmpty()){
            sweepLineOrder.add(line);
        } else {

            for(int i = 0; i < sweepLineOrder.size(); i++){
                double itLineY = sweepLineOrder.get(i).getActualY(line.getP1().getX());
                double lineY = line.getP1().getY();
                if(lineY <= itLineY){
                    placeToSortIn = i;
                    break;
                }
            }

            sweepLineOrder.add(placeToSortIn, line);
        }
        return placeToSortIn;
    }

    private static int addEventToQueue(List<Event> eventQueue, Event event){
        int placeToSortIn = eventQueue.size()-1;
        for (int i = 1; i < eventQueue.size(); i++) {
            Event sweepEvent = eventQueue.get(i);
            if (sweepEvent.getPoint().getX() >= event.getPoint().getX()) {
                placeToSortIn = i;
                break;
            }
        }

        eventQueue.add(placeToSortIn, event);
        return placeToSortIn;
    }

    private static void handleStartEvent(List<CustomLine2D> sweepLineOrder, List<Event> eventQueue, LineEvent lineEvent){
        CustomLine2D line = lineEvent.getLine();
        int placeInList = addLineToSweepLine(sweepLineOrder, line);


        if(placeInList>0){
            CustomLine2D lineAbove = sweepLineOrder.get(placeInList - 1);
            Point2D intersectWithAbove = lineAbove.intersectLines(lineEvent.getLine());
            if(intersectWithAbove != null){
                Event intersectEvent = new IntersectEvent(intersectWithAbove, lineAbove, line);
                addEventToQueue(eventQueue, intersectEvent);
            }
        }

        if(placeInList < sweepLineOrder.size()-1){
            CustomLine2D lineBelow = sweepLineOrder.get(placeInList + 1);
            Point2D intersectWithBelow = lineBelow.intersectLines(lineEvent.getLine());
            if(intersectWithBelow != null){
                Event intersectEvent = new IntersectEvent(intersectWithBelow, line, lineBelow);
                addEventToQueue(eventQueue, intersectEvent);
            }
        }
    }

    private static void handleEndEvent(List<CustomLine2D> sweepLineOrder, List<Event> eventQueue, LineEvent lineEvent) {
        int placeInSweepLine = sweepLineOrder.indexOf(lineEvent.getLine());
        sweepLineOrder.remove(placeInSweepLine);

        if(placeInSweepLine > 0 && placeInSweepLine < sweepLineOrder.size()-1){
            CustomLine2D lineAbove = sweepLineOrder.get(placeInSweepLine-1);
            CustomLine2D lineBelow = sweepLineOrder.get(placeInSweepLine + 1);

            Point2D intersect = lineAbove.intersectLines(lineBelow);
            if(intersect != null){
                Event intersectEvent = new IntersectEvent(intersect, lineAbove, lineBelow);
                addEventToQueue(eventQueue, intersectEvent);
            }
        }


    }


}

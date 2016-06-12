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
        long startTime = System.currentTimeMillis();
        Vector<Line2D.Double> line2DVector = ComGeoUtil.readLines("src/aufgabe3/coords/s_1000_10.dat");

            List<Event> eventQueue = generateSortedEventList(line2DVector);


            List<CustomLine2D> sweepLineOrder = new ArrayList<>();

            List<Point2D> intersectSet = new ArrayList<>();

            while(!eventQueue.isEmpty()){
                Event event = eventQueue.get(0);
                log.debug("___________________________________________");


                if(event instanceof LineEvent) {

                    log.debug("LineEventX: " + event.getPoint().getX());

                    LineEvent lineEvent = (LineEvent) event;
                    CustomLine2D line = lineEvent.getLine();
                    log.debug("LineID: " + lineEvent.getLine().getId());
                    if(lineEvent.getEventType().equals(LineEvent.EventType.START)){
                        handleStartEvent(sweepLineOrder, eventQueue, line);

                    }

                    if(lineEvent.getEventType().equals(LineEvent.EventType.END)) {
                        handleEndEvent(sweepLineOrder, eventQueue, line);
                    }
                } else if(event instanceof IntersectEvent){
                    log.debug("IntersectEventX: " + event.getPoint().getX());
                    if(!intersectSet.contains(event.getPoint())) {
                        sweepLineOrder.indexOf(event);

                        IntersectEvent intersectEvent = (IntersectEvent) event;
                        intersectSet.add(intersectEvent.getPoint());

                        CustomLine2D line1 = intersectEvent.getLine1();
                        CustomLine2D line2 = intersectEvent.getLine2();

                        handleIntersectEvent(sweepLineOrder, eventQueue, line1, line2);
                    }
                }
                eventQueue.remove(0);
            }

            long endTime = System.currentTimeMillis();
            log.error("Anzahl Schnittpunkte: " + intersectSet.size());
            log.error("Zeit: " + (endTime-startTime) + " ms");




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

        ArrayList<Event> eventList = new ArrayList<>();
        int idCounter = 0;
        for(Line2D.Double line : line2DVector){
            idCounter++;
            Point2D p1 = line.getP1();
            Point2D p2 = line.getP2();

            if(p1.equals(p2) || p1.getX() == p2.getX()) {
                log.error("Beide Punkte der Strecke sind identisch!");
            }
            else if(p1.getX() == p2.getX()) log.error("Die Strecke verläuft parallel zur Y-Achse");



            if(p1.getX() < p2.getX()){
                eventList.add(new LineEvent(LineEvent.EventType.START, p1, new CustomLine2D(idCounter, line)));
                eventList.add(new LineEvent(LineEvent.EventType.END, p2, new CustomLine2D(idCounter, line)));

            } else {
                Line2D.Double swappedLine = new Line2D.Double(p2, p1);
                eventList.add(new LineEvent(LineEvent.EventType.START, p2, new CustomLine2D(idCounter, swappedLine)));
                eventList.add(new LineEvent(LineEvent.EventType.END, p1, new CustomLine2D(idCounter, swappedLine)));
            }
        }
        Collections.sort(eventList, (o1, o2) -> {
            Double double1 = o1.getPoint().getX();
            Double double2 = o2.getPoint().getX();
            return double1.compareTo(double2);
        });
        return eventList;
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

    private static void handleStartEvent(List<CustomLine2D> sweepLineOrder, List<Event> eventQueue, CustomLine2D customLine2D){
        log.debug("handleStartEvent");
        CustomLine2D line = customLine2D;
        int placeInList = addLineToSweepLine(sweepLineOrder, line);


        if(placeInList>0){
            CustomLine2D lineAbove = sweepLineOrder.get(placeInList - 1);
            Point2D intersectWithAbove = lineAbove.intersectLines(customLine2D);
            log.debug("intersect: " + intersectWithAbove);
            if(intersectWithAbove != null){
                Event intersectEvent = new IntersectEvent(intersectWithAbove, lineAbove, line);
                addEventToQueue(eventQueue, intersectEvent);
            }

            if(placeInList < sweepLineOrder.size()-1){
                CustomLine2D lineBelow = sweepLineOrder.get(placeInList + 1);
                Point2D intersectWithBelow = lineBelow.intersectLines(customLine2D);
                log.debug("intersect: " + intersectWithBelow);
                if(intersectWithBelow != null){
                    Event intersectEvent = new IntersectEvent(intersectWithBelow, line, lineBelow);
                    addEventToQueue(eventQueue, intersectEvent);
                }
            }
        }


    }

    private static void handleEndEvent(List<CustomLine2D> sweepLineOrder, List<Event> eventQueue, CustomLine2D customLine2D) {
        log.debug("handleEndEvent");
        int placeInSweepLine = sweepLineOrder.indexOf(customLine2D);

        sweepLineOrder.remove(placeInSweepLine);
        log.debug("placeInSweepLine: " + placeInSweepLine);
        log.debug("sweepLineSize: " + sweepLineOrder.size());
        if(placeInSweepLine > 0 && placeInSweepLine < sweepLineOrder.size()-2){
            CustomLine2D lineAbove = sweepLineOrder.get(placeInSweepLine-1);
            CustomLine2D lineBelow = sweepLineOrder.get(placeInSweepLine + 1);

            Point2D intersect = lineAbove.intersectLines(lineBelow);
            log.debug("intersect: " + intersect);
            if(intersect != null){
                Event intersectEvent = new IntersectEvent(intersect, lineAbove, lineBelow);
                addEventToQueue(eventQueue, intersectEvent);
            }
        }


    }

    private static void handleIntersectEvent(List<CustomLine2D> sweepLineOrder, List<Event> eventQueue, CustomLine2D line1, CustomLine2D line2) {
        log.debug("handleIntersectEvent");
        int indexOfLine1 = sweepLineOrder.indexOf(line1);
        int indexOfLine2 = indexOfLine1+1;

        log.debug("indexes for swap: " + indexOfLine1 + " " + indexOfLine2);

        Collections.swap(sweepLineOrder, indexOfLine1, indexOfLine2);

        indexOfLine1 = indexOfLine2;
        indexOfLine2 = indexOfLine1-1;

        log.debug("indexes after swap: " + indexOfLine1 + " " + indexOfLine2);

        if(indexOfLine2-1 >= 0 ){
            CustomLine2D lineAboveIntersectLines = sweepLineOrder.get(indexOfLine2-1);
            log.debug(indexOfLine1-1);

            log.debug("line2: " + line2.getP1() + " " + line2.getP2());
            log.debug("lineAboveIntersectLines: " + lineAboveIntersectLines.getP1() + " " + lineAboveIntersectLines.getP2());

            Point2D intersectAbove = line2.intersectLines(lineAboveIntersectLines);
            log.debug("intersectAbove: " + intersectAbove);

            if(intersectAbove != null){
                Event intersectAboveEvent = new IntersectEvent(intersectAbove, lineAboveIntersectLines, line2);
                addEventToQueue(eventQueue, intersectAboveEvent);

            }

            if(indexOfLine1+1 < sweepLineOrder.size()-2) {
                CustomLine2D lineBelowIntersectLines = sweepLineOrder.get(indexOfLine1 + 1);
                Point2D intersectBelow = line1.intersectLines(lineBelowIntersectLines);
                log.debug("intersectBelow: " + intersectBelow);
                if (intersectBelow != null) {
                    Event intersectBelowEvent = new IntersectEvent(intersectBelow, line1, lineBelowIntersectLines);
                    addEventToQueue(eventQueue, intersectBelowEvent);
                }
            }
        }

    }


}

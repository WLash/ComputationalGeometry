package pra2;

import java.awt.geom.Point2D;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import pra1.Pra1;


public class Pra2 {

	private static Logger log = LogManager.getLogger(Pra1.class);

	public static void start() throws Exception {
		try {

			File fXmlFile = new File("src/pra2/svg/deutschland2.svg");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);

			// optional, but recommended
			// read this -
			// http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
			doc.getDocumentElement().normalize();

			log.debug("Root element :" + doc.getDocumentElement().getNodeName());

			NodeList nList = doc.getElementsByTagName("path");

			log.debug("----------------------------");

			for (int temp = 0; temp < nList.getLength(); temp++) {

				Node nNode = nList.item(temp);

				log.debug("\nCurrent Element :" + nNode.getNodeName());

				if (nNode.getNodeType() == Node.ELEMENT_NODE) {

					Element eElement = (Element) nNode;

					String bundesland = eElement.getAttribute("id");
					log.error("id : " + bundesland);
					log.debug("fill: " + eElement.getAttribute("fill"));
					String pathString = eElement.getAttribute("d");
					if(!pathString.isEmpty()) {
						List<AreaPolygon> allPolygonsOfSubCountry = pathStringToAreaPolygonList(pathString);
						double allArea = 0;

						for(int i = 0; i < allPolygonsOfSubCountry.size(); i++){
							AreaPolygon currentPolygon = allPolygonsOfSubCountry.get(i);
							calcAreaOfPolygon(currentPolygon);
							allArea += currentPolygon.getArea();
							log.error("Gesamtflache der Datei = " + allArea);
						}

					}
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/*private static boolean isPointInPolygon(Point2D point, Vector<Point2D> currentPolygon) {


	}*/



	private static void calcAreaOfPolygon(AreaPolygon currentPolygon) {
	//Calculate the Area of a Polygon
		double area = 0;
        Vector<Point2D> vector = currentPolygon.getVectorPoints();
        boolean isMotherPolygon = currentPolygon.getAreaPolygonChildList() != null;
        List<Point2D> firstPointsOfChildPolygonList = new ArrayList<>();

        if(isMotherPolygon){
            for(AreaPolygon child : currentPolygon.getAreaPolygonChildList()){
                firstPointsOfChildPolygonList.add(child.getVectorPoints().get(0));
            }
        }


		for(int i = 0 ; i < vector.size() ; i ++){
			boolean first = i == 0;
			boolean last =	i == vector.size()-1;

			Point2D p = vector.elementAt(i);
			//point before
			Point2D pb;
			//point after
			Point2D pa;

			//first case
			if(first){
				pb = vector.elementAt(vector.size()-1);
			} else {
				pb = vector.elementAt(i-1);
			}

			//last case
			if(last) {
				pa = vector.elementAt(0);
			} else {
				pa = vector.elementAt(i+1);
			}
			area += p.getY() * (pb.getX() - pa.getX())/2;

            //Point in Polygon
            if(isMotherPolygon){
                for(Point2D point : firstPointsOfChildPolygonList){

                }
            }
		}
		currentPolygon.setArea(area);
	}


	private static List<AreaPolygon> pathStringToAreaPolygonList(String pathString){
		List<AreaPolygon> resultList = new ArrayList<>();


		String[] areasOfBundesland = pathString.substring(1).split("z");
		for(int i = 0; i < areasOfBundesland.length-1; i++){
			String areaPathString = areasOfBundesland[i];
			if (i>0){
				areaPathString = areaPathString.substring(1);
			}
			String[] coordLines = areaPathString.split(" ");
			Vector<Point2D> vector = new Vector<>();

			Point2D pointBefore = null;
			for (int j = 0; j < coordLines.length; j++) {

				boolean isLast = j==coordLines.length-1;

				Point2D point = coordsLineToPoint(coordLines[j], pointBefore, isLast);
				vector.add(point);
				pointBefore = point;

			}
			log.error("Anzahl Punkte Polygon: " + vector.size());

            AreaPolygon areaPolygon = new AreaPolygon(vector);

            if(!resultList.isEmpty()){
                areaPolygon.setMotherAreaPolygon(resultList.get(0));
            }
            resultList.add(areaPolygon);
		}

        if(resultList.size()>1){
            AreaPolygon motherAreaPolygon = resultList.get(0);
            List<AreaPolygon> childAreaPolygonList = resultList;
            childAreaPolygonList.remove(0);
            motherAreaPolygon.setAreaPolygonChildList(childAreaPolygonList);
        }
		return resultList;
	}

	private static Point2D coordsLineToPoint(String coordsLine, Point2D pointBefore, boolean isLast){
		boolean startsWithL = coordsLine.startsWith("L");
		String[] coordsStringArray = coordsLine.substring(1).split(",");
		double valueX = Float.valueOf(coordsStringArray[0]);
		double valueY = Float.valueOf(coordsStringArray[1]);

		if(!isLast && pointBefore != null && !startsWithL){
			valueX = valueX + pointBefore.getX();
			valueY = valueY + pointBefore.getY();
		}
		Point2D point = new Point2D.Double(valueX, valueY);
		return point;
	}

}

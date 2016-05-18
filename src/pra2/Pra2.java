package pra2;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
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

			File fXmlFile = new File("src/pra2/svg/deutschland.svg");
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
					log.debug("id : " + bundesland);
					log.debug("fill: " + eElement.getAttribute("fill"));
					String pathString = eElement.getAttribute("d");
					if(!pathString.isEmpty()) {
						List<Vector<Point2D>> paths = pathStringToArrayList(pathString);
						int vectorCounter = 0;
						for(Vector<Point2D> vector : paths){
							vectorCounter++;
							log.debug("Vector " + vectorCounter + " von Bundesland " + bundesland + ": ");

							for(Point2D point : vector){
								log.debug(point);
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static List<Vector<Point2D>> pathStringToArrayList(String pathString){
		List<Vector<Point2D>> resultList = new ArrayList<>();


		String[] areasOfBundesland = pathString.substring(1).split("z");

		for(int i = 0; i < areasOfBundesland.length; i++){

			String[] coordLines = areasOfBundesland[i].split(" ");
			Vector<Point2D> vector = new Vector<>();
			for (int j = 0; j < coordLines.length; j++) {
				if(!coordLines[j].isEmpty()) {
					Point2D point = coordsLineToPoint(coordLines[j].substring(1));
					vector.add(point);
				}
			}
			resultList.add(vector);
		}

		return resultList;
	}

	private static Point2D coordsLineToPoint(String coordsLine){
		String[] coordsStringArray = coordsLine.split(",");
		Point2D point = new Point2D.Float(Float.valueOf(coordsStringArray[0]), Float.valueOf(coordsStringArray[1]));
		return point;
	}

}

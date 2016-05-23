package pra1;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Vector;

public class Pra1 {

	private static Logger log = LogManager.getLogger(Pra1.class);

	public static void start() throws Exception {

		long startTime = System.currentTimeMillis();
		Vector<Line2D> data = readData();
		System.out.println("Start . Size: " + data.size());

		int crossCount = 0;
		int comparison = 0;

		for(int i = 0 ; i < data.size() ; i++){
			for(int j = i+1 ; j < data.size(); j++){
				log.debug("---------------Vergleich Strecke " + (i+1) + " mit Strecke " + (j+1) + "--------------------------------");
				Line2D line1 = data.get(i);
				Line2D line2 = data.get(j);
				//wenn alle ccw == 0 sind, liegen alle Punkte auf einem imagin�ren Strahl. Pr�fe ob f�r Line 2 einer der beiden X werte innerhalb des DeltaX von Linie 1 Liegen

				int line1First =  ccw(line1, line2.getP2());
				int line1Second = ccw(line1, line2.getP1());

				int line2First = ccw(line2, line1.getP2());
				int line2Second = ccw(line2, line1.getP1());


				int ccwLine1 = line1First * line1Second;
				int ccwLine2 = line2First * line2Second;


				log.debug("CCW von Strecke " + (i + 1) + " mit Punkt 1 von Strecke " + (j + 1) + ": " + line1First);
				log.debug("CCW von Strecke " + (i+1) + " mit Punkt 2 von Strecke " + (j+1) + ": " + line1Second);

				log.debug("CCW von Strecke " + (j+1) + " mit Punkt 1 von Strecke " + (i+1) + ": " + line2First);
				log.debug("CCW von Strecke " + (j+1) + " mit Punkt 2 von Strecke " + (i+1) + ": " + line2Second);

				/* Sonderfall, wenn beide Strecken auf der selben gerade liegen */
				if(line1First == 0 && line1Second == 0 && line2First == 0 && line2Second == 0){
					log.debug("Sonderfall: beide Strecken auf einer Gerade");

					double line1X1 = line1.getX1() <= line1.getX2() ? line1.getX1() : line1.getX2();
					double line1X2 = line1.getX1() >= line1.getX2() ? line1.getX1() : line1.getX2();

					double line2X1 = line2.getX1() <= line2.getX2() ? line2.getX1() : line2.getX2();
					double line2X2 = line2.getX1() >= line2.getX2() ? line2.getX1() : line2.getX2();

					if( (line2X1 >= line1X1 && line2X1 <= line1X2)
							|| (line2X2 >= line1X1 && line2X2 <= line1X2)){
						crossCount++;
						log.debug("!!!!!!!!!!!!!!!!! SCHNITT "+ crossCount + " !!!!!!!!!!!!!!");
					}
					break;
				}

				if (ccwLine1 <= 0 && ccwLine2 <= 0){
					crossCount++;
					log.debug("!!!!!!!!!!!!!!!!! SCHNITT "+ crossCount + " !!!!!!!!!!!!!!");
				} else {
					log.debug("!!!!!!!!!!!! KEIN SCHNITT !!!!!!!!!!!!!!");
				}

				comparison ++;
			}
		}

		long endTime = System.currentTimeMillis();
		long duration = endTime - startTime;

		System.out.println("Anzahl sich schneidender Strecken: " + crossCount);
		System.out.println("Anzahl vergleiche: " + comparison);
		System.out.println("Dauer: " + duration +  " ms");
	}

	private static int ccw(Line2D q, Point2D p) {
		Vector<Double> vectorLine = calculateVector(q.getP1(), q.getP2());
		Vector<Double> vectorPoint = calculateVector(q.getP1(), p);
		int retValue = 0;

		double crossProduct = vectorLine.get(0) * vectorPoint.get(1) - vectorLine.get(1) * vectorPoint.get(0);
		if (crossProduct < 0){
			retValue = -1;
		} else if (crossProduct == 0) {
			retValue = 0;
		} else if (crossProduct > 0) {
			retValue = 1;
		}

		return retValue;
	}

	private static Vector<Double> calculateVector(Point2D p1, Point2D p2){
		Vector<Double> vector = new Vector<>();
		vector.add(p2.getX() - p1.getX());
		vector.add(p2.getY() - p1.getY());

		return vector;
	}
	

	private static Vector<Line2D> readData() throws Exception{
		Vector<Line2D> data = new Vector<>();
		try (BufferedReader br = new BufferedReader(new FileReader("src/pra1/coords/data_10cuts.dat"))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		       	String [] val = line.split(" ");
				Line2D strecke = new Line2D.Double(Double.parseDouble(val[0]),Double.parseDouble(val[1]), Double.parseDouble(val[2]),Double.parseDouble(val[3]));
		       	data.add(strecke);

		    }
		}
		    catch(Exception e)
		    {
		    	throw e;
		    }
			return data;	
		}
		
	
	
}

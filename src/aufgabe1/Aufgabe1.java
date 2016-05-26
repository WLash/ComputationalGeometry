//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package aufgabe1;

import java.awt.geom.Line2D;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Vector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import util.ComGeoUtil;

public class Aufgabe1 {
    private static Logger log = LogManager.getLogger(Aufgabe1.class);

    public static void start() throws Exception {
        long startTime = System.currentTimeMillis();
        Vector<Line2D> data = readData();
        System.out.println("Start . Size: " + data.size());
        int crossCount = 0;
        int comparison = 0;

        for(int i = 0; i< data.size(); i++) {
            for(int j = i + 1; j < data.size(); ++j) {
                log.debug("---------------Vergleich Strecke " + (i + 1) + " mit Strecke " + (j + 1) + "--------------------------------");
                Line2D line1 = data.get(i);
                Line2D line2 = data.get(j);
                int line1First = ComGeoUtil.ccw(line1, line2.getP2());
                int line1Second = ComGeoUtil.ccw(line1, line2.getP1());
                int line2First = ComGeoUtil.ccw(line2, line1.getP2());
                int line2Second = ComGeoUtil.ccw(line2, line1.getP1());
                int ccwLine1 = line1First * line1Second;
                int ccwLine2 = line2First * line2Second;
                log.debug("CCW von Strecke " + (i + 1) + " mit Punkt 1 von Strecke " + (j + 1) + ": " + line1First);
                log.debug("CCW von Strecke " + (i + 1) + " mit Punkt 2 von Strecke " + (j + 1) + ": " + line1Second);
                log.debug("CCW von Strecke " + (j + 1) + " mit Punkt 1 von Strecke " + (i + 1) + ": " + line2First);
                log.debug("CCW von Strecke " + (j + 1) + " mit Punkt 2 von Strecke " + (i + 1) + ": " + line2Second);
                if(line1First == 0 && line1Second == 0 && line2First == 0 && line2Second == 0) {
                    log.debug("Sonderfall: beide Strecken auf einer Gerade");
                    double line1X1 = line1.getX1() <= line1.getX2()?line1.getX1():line1.getX2();
                    double line1X2 = line1.getX1() >= line1.getX2()?line1.getX1():line1.getX2();
                    double line2X1 = line2.getX1() <= line2.getX2()?line2.getX1():line2.getX2();
                    double line2X2 = line2.getX1() >= line2.getX2()?line2.getX1():line2.getX2();
                    if(line2X1 >= line1X1 && line2X1 <= line1X2 || line2X2 >= line1X1 && line2X2 <= line1X2) {
                        ++crossCount;
                        log.debug("!!!!!!!!!!!!!!!!! SCHNITT " + crossCount + " !!!!!!!!!!!!!!");
                    }
                    break;
                }

                if(ccwLine1 <= 0 && ccwLine2 <= 0) {
                    ++crossCount;
                    log.debug("!!!!!!!!!!!!!!!!! SCHNITT " + crossCount + " !!!!!!!!!!!!!!");
                } else {
                    log.debug("!!!!!!!!!!!! KEIN SCHNITT !!!!!!!!!!!!!!");
                }

                ++comparison;
            }
        }

        long currentTime = System.currentTimeMillis();
        long time = currentTime - startTime;
        System.out.println("Anzahl sich schneidender Strecken: " + crossCount);
        System.out.println("Anzahl vergleiche: " + comparison);
        System.out.println("Dauer: " + time + " ms");
    }

    private static Vector<Line2D> readData() throws Exception {
        Vector vector = new Vector();

        try {
            BufferedReader br = new BufferedReader(new FileReader("src/aufgabe1/coords/data_10cuts.dat"));
            String line;
            while((line = br.readLine()) != null) {
                String[] val = line.split(" ");

                Line2D strecke = new Line2D.Double(Double.valueOf(val[0]), Double.valueOf(val[1]), Double.valueOf(val[2]), Double.valueOf(val[3]));
                vector.add(strecke);
            }

            return vector;
        } catch (Exception var16) {
            throw var16;
        }
    }
}

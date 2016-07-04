package aufgabe4;

public class Aufgabe4 {

	public static void start(){
		
		ProcessBuilder pb = new ProcessBuilder("saucy", "input.saucy");

		// send standard output to a file
		pb.redirectOutput(new File("output.saucy"));
		// merge standard error with standard output
		pb.redirectErrorStream(true);

		Process p = pb.start();
		
		String[] cmd = { "cmd", "/C", "saucy input.saucy > output.saucy" }; 
	}
}

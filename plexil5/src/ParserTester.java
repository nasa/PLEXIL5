import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.nianet.plexil.parser.ParseException;
import org.nianet.plexil.parser.Plexilite;


public class ParserTester {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws ParseException 
	 */
	public static void main(String[] args) throws IOException, ParseException {
		BufferedReader br = new BufferedReader(new FileReader("ejemplo.txt"));
		String str = "";
		while(br.ready()){
			str+= " " + br.readLine();
		}
		br.close();
		Plexilite.parseState(str);
	}

}

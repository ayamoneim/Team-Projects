import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

import org.apache.log4j.Logger;

public class Schema {
	private Logger log = Logger.getLogger(Schema.class.getName());

	public void schemaCreating(ArrayList<Column> columns, String tablepath,
			String tablename, String rowname) {
		try {
			File myFile = new File(tablepath);
			myFile.createNewFile();

			FileWriter write = new FileWriter(tablepath);
			write.write("<?xml version=\"1.0\"?>\n");
			write.write("<xs:schema xmlns:xs=\"http://www.w3.org/2001/XMLSchema\">\n");
			write.write("<xs:element name=" + "\"" + tablename + "\">\n");
			write.write("\t<xs:complexType>\n");
			write.write("\t\t<xs:sequence>\n");

			write.write("\t\t\t<xs:element name=" + "\"" + rowname + "\">\n");
			write.write("\t\t\t\t<xs:complexType>\n");
			write.write("\t\t\t\t\t<xs:sequence>\n");
			for (int i = 0; i < columns.size(); i++) {
				write.write("\t\t\t\t\t\t<xs:element name=" + "\""
						+ columns.get(i).getColName() + "\"" + " type=\"xs:"
						+ columns.get(i).getdataType().toLowerCase() + "\"/>\n");

			}

			write.write("\t\t\t\t\t</xs:sequence>\n");
			write.write("\t\t\t\t</xs:complexType>\n");
			write.write("\t\t\t</xs:element>\n");

			write.write("\t\t</xs:sequence>\n");
			write.write("\t</xs:complexType>\n");
			write.write("</xs:element>\n");
			write.flush();
			write.close();
		} catch (Exception e) {
			log.error(e.getMessage());
		}

	}

	public ArrayList<Column> schemaParsing(String tablepath) {
		ArrayList<Column> ret = new ArrayList<Column>();
		try {
			File myFile = new File(tablepath);
			FileReader read = new FileReader(myFile);
			BufferedReader br = new BufferedReader(read);
			int counter = 0;
			while (br.ready()) {
				String line = br.readLine();
				counter++;
				if (counter < 9) {
					continue;
				}
				if (line.contains("sequence")) {
					break;
				}

				// processing
				line = line.trim();
				String[] nametype = line.split("\\\"");
				String name = nametype[1];
				String type = nametype[3].substring(3);
				if (type.contains("(")) {
					Column temp = new Column(name, type.split("\\(")[0],
							Integer.parseInt(type.split("\\(")[1].replaceAll(
									"\\)", "")));
					ret.add(temp);
				} else {
					Column temp = new Column(name, type, 0);
					ret.add(temp);
				}
			}
			br.close();
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
		return ret;
	}
}

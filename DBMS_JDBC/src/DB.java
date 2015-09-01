
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class DB implements Database {

	private static String DataBase = "";
	
	public int counter = 0;

	public DB() {
		try {
			File f = new File("existDataBase.txt");
			if (f.exists()) {
				BufferedReader bf = new BufferedReader(new FileReader(f));
				while (bf.ready()) {
					DataBase = bf.readLine();
				}
			}
		} catch (Exception e) {

		}
	}

	private Validator Validate = new Validator();

	/*
	 * (non-Javadoc)
	 * 
	 * @see Database#createDatabase(java.lang.String)
	 * 
	 * create new Database(new Folder) to create tables(xml files) in it later
	 */
	@Override
	public String createDatabase(String databaseName) {
		counter = 0;
			try {
				File f = new File("existDataBase.txt");
				if (!f.exists()) {
					f.createNewFile();
				}
				PrintWriter write = new PrintWriter(new FileOutputStream(
						"existDataBase.txt"));
				write.write(databaseName);
				write.close();
			} catch (Exception e) {

			}
			File dir = new File(databaseName);
			if (!dir.exists()) {
				dir.mkdir();
				DataBase = databaseName;
				return dbms.Con_DB;
			}
			else {
				DataBase = databaseName;
				return dbms.DB_ALREADY_EXISTS ; 
			}

			
		}

	public void updateFile(Document dom, String TableName) {

		TransformerFactory transformerFactory = TransformerFactory
				.newInstance();
		Transformer transformer;
		try {

			transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(dom);
			StreamResult result = new StreamResult(DataBase + '\\'
					+ TableName.toLowerCase() + ".xml");
			// StreamResult result = new StreamResult(DataBase + '\\' +
			// TableName
			// + ".xml");
			transformer.transform(source, result);

		} catch (TransformerException e) {
			e.printStackTrace();
		}
	}

	/*
	 * Build a Dom Tree of an XML File
	 */
	public Document buildDom(InputStream is) {

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = null;
		try {
			db = dbf.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		Document dom = null;
		try {
			dom = db.parse(is);
		} catch (SAXException | IOException e) {
			e.printStackTrace();
		}
		return dom;

	}

	private LinkedList<Integer> checkCondition(Condition con, NodeList rows) {
		LinkedList<Integer> correctRows = new LinkedList<Integer>();
		int index = 0;
		for (int i = 0; i < rows.getLength(); i++) {
			if (rows.item(i) instanceof Element) {

				if (rows.item(i).hasChildNodes()) {
					NodeList columns = rows.item(i).getChildNodes();
					for (int j = 0; j < columns.getLength(); j++) {
						if (columns.item(j) instanceof Element) {
							Node curr = columns.item(j);
							if (curr.getNodeName().toLowerCase()
									.equals(con.getColName().toLowerCase())) {
								if (conditionMatch(curr.getTextContent(),
										con.getOperator(), con.getValue())) {
									correctRows.add(index);
								}
							}
						}
					}
				}
				index++;
			}
		}

		return correctRows;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see Database#update(java.util.ArrayList, java.util.ArrayList,
	 * java.lang.String, Condition)
	 * 
	 * Updates table entries with new values and save them to the database
	 */
	public String update(ArrayList<String> columnNames,
			ArrayList<String> values, String TableName, Condition con) {
		counter = 0;
		if (DataBase == "")
			return dbms.DB_NOT_FOUND;

		InputStream is;
		try {
			is = new FileInputStream(DataBase + '\\' + TableName.toLowerCase()
					+ ".xml");
		} catch (FileNotFoundException e) {
			return dbms.TABLE_NOT_FOUND;
		}

		if (!Validate.Validate_Parameters(columnNames, DataBase + '\\'
				+ TableName.toLowerCase() + ".xsd"))
			return dbms.COLUMN_NOT_FOUND;
		if (!Validate
				.Validate_DataTypes(columnNames, values,
						TableName.toLowerCase(),
						DataBase + '\\' + TableName.toLowerCase() + ".xsd",
						this, false))
			return dbms.COLUMN_TYPE_MISMATCH;

		if (con != null) {
			ArrayList<String> temp0 = new ArrayList<String>();
			temp0.add(con.getColName());
			ArrayList<String> temp1 = new ArrayList<String>();
			temp1.add(con.getValue());

			if (!Validate.Validate_Parameters(temp0, DataBase + '\\'
					+ TableName.toLowerCase() + ".xsd"))
				return dbms.COLUMN_NOT_FOUND;

			if (!Validate.Validate_DataTypes(temp0, temp1,
					TableName.toLowerCase(),
					DataBase + '\\' + TableName.toLowerCase() + ".xsd", this,
					true))
				return dbms.COLUMN_TYPE_MISMATCH;

		}
		Document dom = buildDom(is);

		NodeList rows = dom.getDocumentElement().getChildNodes();

		if (checkCondition(con, rows).size() == 0)
			return dbms.NOT_MATCH_CRITERIA;

		for (int i = 0; i < rows.getLength(); i++) {
			if (rows.item(i).getNodeName().equals("#text"))
				continue;
			boolean flag = false;
			NodeList rowdata = rows.item(i).getChildNodes();

			for (int j = 0; j < rowdata.getLength(); j++) {
				if (rowdata.item(j).getNodeName().equals("#text"))
					continue;

				if (rowdata.item(j).getNodeName().toLowerCase()
						.equals(con.getColName().toLowerCase())
						&& conditionMatch(rowdata.item(j).getTextContent(),
								con.getOperator(), con.getValue())) {
					// this row match the condition and must be updated
					flag = true;
				}
			}
			if (flag) {
				counter++;
				for (int j = 0; j < columnNames.size(); j++) {
					for (int q = 0; q < rowdata.getLength(); q++) {
						if (rowdata.item(q).getNodeName().toLowerCase()
								.equals(columnNames.get(j).toLowerCase())) {
							rowdata.item(q).setTextContent(
									(String) values.get(j));
							break;
						}
					}
				}
			}
		}
		updateFile(dom, TableName.toLowerCase());

		return dbms.Con_Update;
	}

	public boolean conditionMatch(String oper1, String oper, String oper2) {

		if (oper1.matches("-?\\d+(\\.\\d+)?")
				&& oper2.matches("-?\\d+(\\.\\d+)?")) {
			int op1 = Integer.parseInt(oper1);
			int op2 = Integer.parseInt(oper2);
			if (oper.equals("<")) {
				return (op1 < op2);
			}
			if (oper.equals(">")) {
				return (op1 > op2);
			}
			if (oper.equals("=")) {
				return (op1 == op2);
			}
		} else {
			return (oper1.equals(oper2));
		}
		return false;
	}

	// helping method for select methods
	private String appendRow(NodeList columns, ArrayList<String> orderingColumns) {
		StringBuilder temp = new StringBuilder();
		for (int k = 0; k < columns.getLength(); k++) {
			if (columns.item(k).getNodeType() != 3
					&& columns.item(k) instanceof Element) {
				temp.append(columns.item(k).getTextContent());
				if (k <= columns.getLength() - 1) {
					temp.append('*');

				}
			}
		}
		return temp.toString();
	}

	// helping method for select methods
	private String appendRowWithExceptions(NodeList columns,
			ArrayList<String> columnNames, ArrayList<String> orderingColumns) {
		StringBuilder temp = new StringBuilder();
		int index = 0;
		for (int k = 0; k < columns.getLength(); k++) {
			if (columns.item(k).getNodeType() != 3
					&& columns.item(k) instanceof Element
					&& index<columnNames.size() && columns.item(k).getNodeName().toLowerCase()
							.equals(columnNames.get(index).toLowerCase())) {
				index++;
				temp.append(columns.item(k).getTextContent());
				if (index <= columnNames.size() - 1) {
					temp.append('*');
				}
			}
			
		}
		temp.append('*');
		return temp.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see Database#delete(java.lang.String, Condition) Delete any row
	 * statisfying a given condition
	 */
	@Override
	public String delete(String TableName, Condition con) {
		if (DataBase == "")
			return dbms.DB_NOT_FOUND;
		counter = 0;
		InputStream is;
		try {
			is = new FileInputStream(DataBase + '\\' + TableName.toLowerCase()
					+ ".xml");
		} catch (FileNotFoundException e) {
			return dbms.TABLE_NOT_FOUND;
		}

		if (con != null) {
			ArrayList<String> temp0 = new ArrayList<String>();
			temp0.add(con.getColName());
			ArrayList<String> temp1 = new ArrayList<String>();
			temp1.add(con.getValue());

			if (!Validate.Validate_Parameters(temp0, DataBase + '\\'
					+ TableName.toLowerCase() + ".xsd"))
				return dbms.COLUMN_NOT_FOUND;

			if (!Validate.Validate_DataTypes(temp0, temp1,
					TableName.toLowerCase(),
					DataBase + '\\' + TableName.toLowerCase() + ".xsd", this,
					true))
				return dbms.COLUMN_TYPE_MISMATCH;

		}
		Document doc = buildDom(is);
		Element root = doc.getDocumentElement();
		NodeList rows = root.getChildNodes();

		if (con == null) {

			while (rows.getLength() != 0) {
				root.removeChild(rows.item(0));
				counter++;
			}

		}

		else {
			LinkedList<Integer> rowsD = checkCondition(con, rows);
			counter = rowsD.size();
			if (rowsD.size() == 0)
				return dbms.NOT_MATCH_CRITERIA;

			for (int i = 0; i < rowsD.size(); i++) {
				int x = rowsD.get(i) - i;
				rowsD.set(i, x);
			}
			for (int i = 0; i < rows.getLength(); i++) {
				if (!(rows.item(i) instanceof Element))
					root.removeChild(rows.item(i));

			}
			for (int i = 0; i < rowsD.size(); i++) {
				int index = rowsD.get(i);
				root.removeChild(rows.item(index));
			}
		}
		updateFile(doc, TableName.toLowerCase());

		return dbms.Con_Delete;
	}

	// helping method for select methods
	private int[] getIndicesArray(ArrayList<String> orderingColumns,
			ArrayList<String> columnNames) {
		int[] indices = new int[orderingColumns.size()];

		for (int i = 0; i < orderingColumns.size(); i++) {
			for (int j = 0; j < columnNames.size(); j++) {
				if (orderingColumns.get(i).toLowerCase()
						.equals(columnNames.get(j).toLowerCase())) {
					indices[i] = j;
					break;
				}
			}
		}
		return indices;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see Database#selectColumn(java.util.ArrayList, java.lang.String,
	 * Condition, java.util.ArrayList, boolean)
	 * 
	 * This method handles the following SQL statements with/without condition :
	 * SELECT * FROM table_name ORDER BY column_name,column_name ASC|DESC;
	 * SELECT * FROM table_name;
	 */
	@Override
	public String selectAll(String TableName, Condition con,
			ArrayList<String> orderingColumns, boolean DESC) throws Exception {
		if (DataBase == "")
			return dbms.DB_NOT_FOUND;

		InputStream is;
		try {

			is = new FileInputStream(DataBase
					+ '\\' + TableName.toLowerCase() + ".xml");
		} catch (FileNotFoundException e) {
			return dbms.TABLE_NOT_FOUND;
		}

		if (orderingColumns != null
				&& !Validate.Validate_Parameters(orderingColumns, DataBase
						+ '\\' + TableName.toLowerCase() + ".xsd"))
			return dbms.COLUMN_NOT_FOUND;

		if (con != null) {
			ArrayList<String> temp0 = new ArrayList<String>();
			temp0.add(con.getColName());
			ArrayList<String> temp1 = new ArrayList<String>();
			temp1.add(con.getValue());

			if (!Validate.Validate_Parameters(temp0, DataBase + '\\'
					+ TableName.toLowerCase() + ".xsd"))
				return dbms.COLUMN_NOT_FOUND;

			if (!Validate.Validate_DataTypes(temp0, temp1,
					TableName.toLowerCase(),
					DataBase + '\\' + TableName.toLowerCase() + ".xsd", this,
					true))
				return dbms.COLUMN_TYPE_MISMATCH;

		}
		Document document = buildDom(is);

		// This string will contain all table contents to be returned later
		StringBuilder requestedItems = new StringBuilder();

		Element root = document.getDocumentElement();

		NodeList rows = root.getChildNodes();

		LinkedList<Integer> correctRows = null;
		int index = 0;
		if (con != null) {
			correctRows = checkCondition(con, rows);
		}

		boolean isFound = false;
		int k = 0;
		for (int i = 0; i < rows.getLength(); i++) {
			if (rows.item(i) instanceof Element) {
				boolean in = false;
				if (con != null && index < correctRows.size()
						&& correctRows.get(index) == k) {
					NodeList columns = rows.item(i).getChildNodes();
					requestedItems.append(appendRow(columns, orderingColumns));
					in = true;
					isFound = true;
					index++;
				} else if (con == null) {
					NodeList columns = rows.item(i).getChildNodes();
					requestedItems.append(appendRow(columns, orderingColumns));
					isFound = true;
				}
				if (in || con == null) {
					requestedItems.append('\n');
				}
				k++;

			}

		}

		StringBuilder temp = new StringBuilder();
		for (int i = 0; i < requestedItems.length() - 1; i++)
			temp.append(requestedItems.charAt(i));
		requestedItems = temp;
		if (!isFound)
			return dbms.NOT_MATCH_CRITERIA;
		if (orderingColumns == null || orderingColumns.isEmpty()) {
			return requestedItems.toString();
		}
		Schema schema = new Schema();
		ArrayList<Column> tableColumns = schema.schemaParsing(
				DataBase + '\\' + TableName.toLowerCase() + ".xsd"
				);
		ArrayList<String> columnNames = new ArrayList<String>();

		for (int i = 0; i < tableColumns.size(); i++) {
			columnNames.add(tableColumns.get(i).getColName());
		}

		requestedItems = getRequestedItemsByOrder(requestedItems,
				getIndicesArray(orderingColumns, columnNames), DESC);
		return requestedItems.toString();
	}

	// helping method for select methods
	private StringBuilder getRequestedItemsByOrder(
			StringBuilder requestedItems, int[] indices, boolean DESC) {
		String[][] table = parseString(requestedItems.toString(), indices, DESC);
		StringBuilder newRequestedItems = new StringBuilder();
		newRequestedItems = buildString(table);
		return newRequestedItems;
	}

	// helping method for select methods
	private StringBuilder buildString(String[][] table) {
		StringBuilder temp = new StringBuilder();
		for (int i = 0; i < table.length; i++) {
			for (int j = 0; j < table[i].length; j++) {
				temp.append(table[i][j]);
				if (j <= table[i].length - 1)
					temp.append('*');
			}
			if (i < table.length - 1)
				temp.append('\n');
		}
		return temp;
	}

	// helping method for select methods
	private String[][] parseString(String string, int[] indices, boolean DESC) {
		String[] line = string.split("\\n");
		int len = line[0].split("\\*").length;
		String[][] table = new String[line.length][len];

		for (int i = 0; i < table.length; i++) {
			String[] temp = line[i].split("\\*");
			
			for (int j = 0; j < table[i].length; j++) {
				table[i][j] = temp[j];
			}
		}

		sortLexicographically(table, indices, DESC);

		return table;
	}

	/*
	 * This method sorts the selected table from the database accorring to the
	 * given columns through Bubble sort manipulation
	 */
	private void sortLexicographically(String[][] table, int[] indices,
			boolean DESC) {
		for (int a = 0; a < indices.length; a++) {
			for (int b = 0; b < table.length - 1; b++) {
				for (int c = 0; c < table.length - b - 1; c++) {

					String type = getDataType(table[c][indices[a]]);

					if (!type.equals("string")) {

						if (DESC
								&& ((type.equals("integer") && ((a == 0 && Integer
										.parseInt(table[c][indices[a]]) < Integer
										.parseInt(table[c + 1][indices[a]])) || (a > 0
										&& Integer
												.parseInt(table[c][indices[a]]) < Integer
												.parseInt(table[c + 1][indices[a]]) && table[c][indices[a - 1]]
										.compareTo(table[c + 1][indices[a - 1]]) == 0))) || (type
										.equals("double") && ((a == 0 && Double
										.parseDouble(table[c][indices[a]]) < Double
										.parseDouble(table[c + 1][indices[a]])) || (a > 0
										&& Double
												.parseDouble(table[c][indices[a]]) < Double
												.parseDouble(table[c + 1][indices[a]]) && table[c][indices[a - 1]]
										.compareTo(table[c + 1][indices[a - 1]]) == 0))))) {
							for (int i = 0; i < table[c].length; i++) {
								String temp = table[c][i];
								table[c][i] = table[c + 1][i];
								table[c + 1][i] = temp;
							}

						}
						if (!DESC
								&& ((type.equals("integer") && ((a == 0 && Integer
										.parseInt(table[c][indices[a]]) > Integer
										.parseInt(table[c + 1][indices[a]])) || (a > 0
										&& Integer
												.parseInt(table[c][indices[a]]) > Integer
												.parseInt(table[c + 1][indices[a]]) && table[c][indices[a - 1]]
										.compareTo(table[c + 1][indices[a - 1]]) == 0))) || (type
										.equals("double") && ((a == 0 && Double
										.parseDouble(table[c][indices[a]]) > Double
										.parseDouble(table[c + 1][indices[a]])) || (a > 0
										&& Double
												.parseDouble(table[c][indices[a]]) > Double
												.parseDouble(table[c + 1][indices[a]]) && table[c][indices[a - 1]]
										.compareTo(table[c + 1][indices[a - 1]]) == 0))))) {
							for (int i = 0; i < table[c].length; i++) {
								String temp = table[c][i];
								table[c][i] = table[c + 1][i];
								table[c + 1][i] = temp;
							}
						}
					} else {
						if ((a == 0 && DESC && table[c][indices[a]]
								.compareTo(table[c + 1][indices[a]]) < 0)
								|| (DESC
										&& a > 0
										&& table[c][indices[a - 1]]
												.compareTo(table[c + 1][indices[a - 1]]) == 0 && table[c][indices[a]]
										.compareTo(table[c + 1][indices[a]]) < 0)) {
							for (int i = 0; i < table[c].length; i++) {
								String temp = table[c][i];
								table[c][i] = table[c + 1][i];
								table[c + 1][i] = temp;
							}

						}
						if ((a == 0 && !DESC && table[c][indices[a]]
								.compareTo(table[c + 1][indices[a]]) > 0)
								|| (!DESC
										&& a > 0
										&& table[c][indices[a - 1]]
												.compareTo(table[c + 1][indices[a - 1]]) == 0 && table[c][indices[a]]
										.compareTo(table[c + 1][indices[a]]) > 0)) {

							for (int i = 0; i < table[c].length; i++) {
								String temp = table[c][i];
								table[c][i] = table[c + 1][i];
								table[c + 1][i] = temp;
							}

						}
					}
				}
			}
		}

	}

	/*
	 * Returns true if the given string is Integer false otherwise
	 */
	public String getDataType(String string) {

		if (string.matches("([ ]*)((-?+)(\\+{0,1}+))([ ]*)([0-9]+)([ ]*)"))
			return "integer";

		if (string
				.matches("([ ]*)((-?+)(\\+{0,1}+))([ ]*)([0-9]+)([ 0-9 ]*)([ ]*)((\\.)([ ]*[0-9]+))?+([ ]*)([ 0-9 ]*)([ ]*)")) {
			return "double";
		}

		if(string.toLowerCase().matches("\\ *\\{(\\ *'?\\w+'?,?)+\\}"))
			return "array";
		return "string";
	}

	/*
	 * Arranges the user's query according to the arrangement of columns in
	 * schema file
	 */
	void arrangeColumnNames(ArrayList<String> columnNames, String TableName) {
		Schema schema = new Schema();
		ArrayList<Column> tableColumns = schema.schemaParsing(DataBase + '\\'
				+ TableName.toLowerCase() + ".xsd");
		int[] temp = new int[columnNames.size()];
		for (int i = 0; i < columnNames.size(); i++) {
			for (int j = 0; j < tableColumns.size(); j++) {
				if (tableColumns.get(j).getColName().toLowerCase()
						.equals(columnNames.get(i).toLowerCase())) {
					temp[i] = j;
					break;
				}
			}
		}

		for (int c = 0; c < (temp.length - 1); c++) {
			for (int d = 0; d < temp.length - c - 1; d++) {
				if (temp[d] > temp[d + 1]) {
					int swap = temp[d];
					temp[d] = temp[d + 1];
					temp[d + 1] = swap;
					String s = columnNames.get(d);
					columnNames.set(d, columnNames.get(d + 1));
					columnNames.set(d + 1, s);
				}

			}
		}

	}

	/*
	 * Arranges the user's query according to the arrangement of columns in
	 * schema file for insert into statement
	 */
	void arrangeColumnNamesWithValues(ArrayList<String> columnNames,
			ArrayList<String> values, String TableName) {
		Schema schema = new Schema();
		ArrayList<Column> tableColumns = schema.schemaParsing(DataBase + '\\'
				+ TableName.toLowerCase() + ".xsd");
		int[] temp = new int[columnNames.size()];
		for (int i = 0; i < columnNames.size(); i++) {
			for (int j = 0; j < tableColumns.size(); j++) {
				if (tableColumns.get(j).getColName().toLowerCase()
						.equals(columnNames.get(i).toLowerCase())) {
					temp[i] = j;
					break;
				}
			}
		}

		for (int c = 0; c < (temp.length - 1); c++) {
			for (int d = 0; d < temp.length - c - 1; d++) {
				if (temp[d] > temp[d + 1]) {
					int swap = temp[d];
					temp[d] = temp[d + 1];
					temp[d + 1] = swap;
					String s = columnNames.get(d);
					columnNames.set(d, columnNames.get(d + 1));
					columnNames.set(d + 1, s);
					s = values.get(d);
					values.set(d, values.get(d + 1));
					values.set(d + 1, s);
				}

			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see Database#selectColumn(java.util.ArrayList, java.lang.String,
	 * Condition, java.util.ArrayList, boolean)
	 * 
	 * This method handles the following SQL statements with/without condition :
	 * SELECT column_name,column_name FROM table_name ORDER BY
	 * column_name,column_name ASC|DESC; SELECT column_name,column_name FROM
	 * table_name; SELECT DISTINCT column_name,column_name FROM table_name;
	 */
	@Override
	public String selectColumn(ArrayList<String> columnNames, String TableName,
			Condition con, ArrayList<String> orderingColumns, boolean DESC) {
		if (DataBase == "")
			return dbms.DB_NOT_FOUND;

		InputStream is;
		try {
			is = new FileInputStream(DataBase + '\\' + TableName.toLowerCase()
					+ ".xml");
		} catch (FileNotFoundException e) {
			return dbms.TABLE_NOT_FOUND;
		}

		if (orderingColumns != null
				&& !Validate.Validate_Parameters(orderingColumns, DataBase
						+ '\\' + TableName.toLowerCase() + ".xsd"))
			return dbms.COLUMN_NOT_FOUND;
		if (orderingColumns != null
				&& !Validate.Validate_Parameters(orderingColumns, DataBase
						+ '\\' + TableName.toLowerCase() + ".xsd"))
			return dbms.COLUMN_NOT_FOUND;

		if (con != null) {
			ArrayList<String> temp0 = new ArrayList<String>();
			temp0.add(con.getColName());
			ArrayList<String> temp1 = new ArrayList<String>();
			temp1.add(con.getValue());
			System.out.println(con.getValue());
			if (!Validate.Validate_Parameters(temp0, DataBase + '\\'
					+ TableName.toLowerCase() + ".xsd"))
				return dbms.COLUMN_NOT_FOUND;

			if (!Validate.Validate_DataTypes(temp0, temp1,
					TableName.toLowerCase(),
					DataBase + '\\' + TableName.toLowerCase() + ".xsd", this,
					true))
				return dbms.COLUMN_TYPE_MISMATCH;

		}
		Document document = buildDom(is);

		// This string will contain all table contents to be returned later
		StringBuilder requestedItems = new StringBuilder();

		Element root = document.getDocumentElement();

		NodeList rows = root.getChildNodes();

		LinkedList<Integer> correctRows = null;
		int index = 0;
		if (con != null) {
			correctRows = checkCondition(con, rows);
		}
		int k = 0;

		boolean isFound = false;
		for (int i = 0; i < rows.getLength(); i++) {
			if (rows.item(i) instanceof Element) {
				boolean in = false;

				if (con != null && index < correctRows.size()
						&& correctRows.get(index) == k) {

					index++;
					NodeList columns = rows.item(i).getChildNodes();
					requestedItems.append(appendRowWithExceptions(columns,
							columnNames, orderingColumns));
					in = true;
					isFound = true;
				} else if (con == null) {
					NodeList columns = rows.item(i).getChildNodes();
					requestedItems.append(appendRowWithExceptions(columns,
							columnNames, orderingColumns));
					isFound = true;
				}
				if (in || con == null) {
					requestedItems.append('\n');
				}
				k++;
			}
		}

		if (!isFound)
			return dbms.NOT_MATCH_CRITERIA;
		if (orderingColumns == null || orderingColumns.isEmpty())
			return requestedItems.toString();

		requestedItems = getRequestedItemsByOrder(requestedItems,
				getIndicesArray(orderingColumns, columnNames), DESC);
		return requestedItems.toString();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see Database#selectColumn(java.util.ArrayList, java.lang.String,
	 * Condition, java.util.ArrayList, boolean)
	 * 
	 * This method creates an xml file with its schema file stating all table
	 * basic info through method schemaCreating(....) class Schema
	 */
	@Override
	public String createTable(ArrayList<Column> myColumns, String TableName) {
		if (DataBase == "")
			return dbms.DB_NOT_FOUND;
		File myFile = new File(DataBase + '\\' + TableName.toLowerCase()
				+ ".xml");

		try {
			if (!myFile.createNewFile())
				return dbms.TABLE_ALREADY_EXISTS;
			else {
				XMLOutputFactory factory = XMLOutputFactory.newInstance();
				try {
					// XMLStreamWriter writer = factory
					// .createXMLStreamWriter(new FileWriter(DataBase
					// + '\\' + TableName + ".xml"));
					XMLStreamWriter writer = factory
							.createXMLStreamWriter(new FileWriter(DataBase
									+ '\\' + TableName.toLowerCase() + ".xml"));
					writer.writeStartDocument();
					writer.writeStartElement(TableName.toLowerCase());

					writer.writeEndElement();
					writer.writeEndDocument();
					writer.flush();
					writer.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		/* Schema creation */
		Schema sch = new Schema();

		sch.schemaCreating(myColumns, DataBase + '\\' + TableName.toLowerCase()
				+ ".xsd", TableName.toLowerCase(), "Row");
		counter = myColumns.size();
		return dbms.Con_Table;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see Database#insert(java.util.ArrayList, java.util.ArrayList,
	 * java.lang.String)
	 * 
	 * This method inserts a row into a table having given values
	 */
	@Override
	public String insert(ArrayList<String> columnNames,
			ArrayList<String> values, String TableName) {
		if (DataBase == "")
			return dbms.DB_NOT_FOUND;
		InputStream is;
		try {
			is = new FileInputStream(DataBase + '\\' + TableName.toLowerCase()
					+ ".xml");
		} catch (FileNotFoundException e) {
			return dbms.TABLE_NOT_FOUND;
		}
		if (columnNames != null) {
			if (!Validate.Validate_Parameters(columnNames, DataBase + '\\'
					+ TableName.toLowerCase() + ".xsd"))
				return dbms.COLUMN_NOT_FOUND;

			if (!Validate.Validate_DataTypes(columnNames, values,
					TableName.toLowerCase(),
					DataBase + '\\' + TableName.toLowerCase() + ".xsd", this,
					false))
				return dbms.COLUMN_TYPE_MISMATCH;
		}

		Document doc = buildDom(is);
		Element root = doc.getDocumentElement();
		NodeList rows = root.getChildNodes();

		Schema sch = new Schema();
		ArrayList<Column> schCol = sch.schemaParsing(DataBase + '\\'
				+ TableName.toLowerCase() + ".xsd");

		if (columnNames == null || columnNames.size() == 0) {
			columnNames = new ArrayList<String>();
			for (int i = 0; i < schCol.size(); i++) {
				columnNames.add(schCol.get(i).getColName());
			}
		} else if (columnNames.size() < schCol.size()) {

			for (int i = 0; i < schCol.size(); i++) {
				// if (columnNames.contains(schCol.get(i).getColName()))
				// continue;

				boolean flag = false;
				for (int j = 0; j < columnNames.size(); j++) {
					if (columnNames.get(j).toLowerCase()
							.equals(schCol.get(i).getColName().toLowerCase())) {
						flag = true;
						break;
					}
				}
				if (flag)
					continue;

				columnNames.add(schCol.get(i).getColName());
				values.add("null");
			}
		}

		arrangeColumnNamesWithValues(columnNames, values,
				TableName.toLowerCase());

		Element newRow = doc.createElement("Row");// should have a row name

		for (int i = 0; i < columnNames.size(); i++) {
			Element n = doc.createElement(columnNames.get(i));
			n.setTextContent(values.get(i));
			newRow.appendChild(n);
		}

		root.appendChild(newRow);
		updateFile(doc, TableName.toLowerCase());
		counter = 1;
		return dbms.Con_insert;

	}

}

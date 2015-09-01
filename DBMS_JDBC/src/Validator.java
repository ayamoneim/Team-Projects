import java.util.ArrayList;
import java.util.LinkedList;

public class Validator {

	// SCHEMA

	/*
	 * validates if the columns in the user's query exist in the table through
	 * schema file
	 */
	public boolean Validate_Parameters(ArrayList<String> columnNames,
			String TablePath) {

		Schema schema = new Schema();
		ArrayList<Column> tableColumns = schema.schemaParsing(TablePath);
		ArrayList<String> temp = new ArrayList<String>();

		for (int i = 0; i < tableColumns.size(); i++) {
			temp.add(tableColumns.get(i).getColName().toLowerCase());
		}

		for (int i = 0; i < columnNames.size(); i++) {
			if (!temp.contains(columnNames.get(i).toLowerCase())) {
				return false;
			}
		}
		return true;
	}

	public boolean Validate_DataTypes(ArrayList<String> columnNames,
			ArrayList<String> values, String TableName, String TablePath, DB b,
			boolean isCondition) {

		Schema sch = new Schema();
		ArrayList<Column> cols = sch.schemaParsing(TablePath);

		if (isCondition) {
			String type = "";
			String colName = columnNames.get(0);
			for (int i = 0; i < cols.size(); i++) {
				if (cols.get(i).getColName().toLowerCase()
						.equals(colName.toLowerCase())) {
					type = cols.get(i).getdataType();
					break;
				}
			}
			String value = values.get(0);
			// integer
			if (value.matches("([ ]*)((-?+)(\\+{0,1}+))([ ]*)([0-9]+)([ ]*)")) {
				if (type.equals("integer") || type.equals("tinyint")
						|| type.equals("smallint") || type.equals("bigint"))
					return true;
				return false;
			}

			// float
			if (value
					.matches("([ ]*)((-?+)(\\+{0,1}+))([ ]*)([0-9]+)([ 0-9 ]*)([ ]*)((\\.)([ ]*[0-9]+))?+([ ]*)([ 0-9 ]*)([ ]*)")) {
				if (type.equals("float") || type.equals("double")
						|| type.equals("real"))
					return true;
				return false;
			}

			// string
			if (!type.equals("varchar") && !type.equals("longvarchar")
					&& !type.equals("char")) {
				return false;
			}
			return true;
		}
		b.arrangeColumnNamesWithValues(columnNames, values, TableName);
		for (int i = 0; i < cols.size(); i++) {
			for (int j = 0; j < columnNames.size(); j++) {
				if (cols.get(i).getColName().equals(columnNames.get(j))
						&& b.getDataType(values.get(j)).equals("integer")
						&& (!cols.get(i).getdataType().equals("integer")
								&& !cols.get(i).getdataType().equals("tinyint")
								&& !cols.get(i).getdataType()
										.equals("smallint")
								&& !cols.get(i).getdataType().equals("int") && !cols
								.get(i).getdataType().equals("bigint"))) {
					return false;
				}
				if (cols.get(i).getColName().equals(columnNames.get(j))
						&& b.getDataType(values.get(j)).equals("double")
						&& (!cols.get(i).getdataType().equals("real")
								&& !cols.get(i).getdataType().equals("float") && !cols
								.get(i).getdataType().equals("double"))) {
					return false;
				}

				if (cols.get(i).getColName().equals(columnNames.get(j))
						&& b.getDataType(values.get(j)).equals("array")) {
					return validateArray(cols.get(i).getdataType(),
							values.get(j));

				}

				if (cols.get(i).getColName().equals(columnNames.get(j))
						&& b.getDataType(values.get(j)).equals("string")
						&& (!cols.get(i).getdataType().equals("varchar")
								&& !cols.get(i).getdataType()
										.equals("longvarchar") && !cols.get(i)
								.getdataType().equals("char"))) {
					return false;

				}
			}
		}

		return true;
	}

	public boolean validateArray(String dataType, String array) {
		String[] contents = array.split("[ \\,\\}\\{\\']");
		// String[] properties = dataType.split("[ \\]\\[]");

		LinkedList<String> l = new LinkedList<String>();
		for (int i = 0; i < contents.length; i++) {
			String s = contents[i].trim();
			if (s.length() != 0) {
				l.add(contents[i]);
			}
		}

		// if (l.size() > Integer.parseInt(properties[2])) {
		// return false;
		// }
		if (dataType.toLowerCase().contains("integer")) {

			for (int i = 0; i < l.size(); i++) {
				try {
					Integer.parseInt(l.get(i));
				} catch (NumberFormatException E) {
					return false;
				}
			}
		}
		return true;
	}

	// Commands
	public boolean Validate_Expression(String input) {
		input = input.toLowerCase();
		input = input.trim();
		if (input.charAt(0) == 'u')
			return IsUpdateValid(input);
		if (input.charAt(0) == 's')
			return IsSelectValid(input);
		if (input.charAt(0) == 'i')
			return IsInsertValid(input);
		if (input.charAt(0) == 'd') {
			return IsDeleteValid(input);
		}
		if (input.charAt(0) == 'c')
			return IsCreateValid(input);
		return false;
	}

	private boolean IsUpdateValid(String inp) {
		return inp
				.toLowerCase()
				.matches(
						"^(update)\\ +\\w+\\ +(set)\\ +([_a-z][a-z0-9_]*\\ *=\\ *(('.*')|([0-9]+))\\ *,\\ *)*([_a-z][a-z0-9_]*\\ *=\\ *(('.*'\\ *)|([0-9]+\\ +)))+where\\ +([_a-z][a-z0-9_]*\\ *=\\ *(('?.*'?)|([0-9]+)))\\ *;?");
	}

	private boolean IsSelectValid(String inp) {
		return inp
				.toLowerCase()
				.matches(
						"^(select)((\\ *\\*\\ *))from\\ +\\w+\\ *(order\\ +by\\ +((\\w+,)*\\w+)\\ +(asc|desc))?\\ *(where\\ +\\w+\\ *((>)|(<)|(=)|(>=)|(<=)|(==))\\ *('?.*'?))?\\ *;?")
				|| inp.toLowerCase()
						.matches(
								"^(select)\\ +(distinct)?\\ *\\(?\\ *((\\w+\\ *,\\ *)*\\ *\\w+)\\ *\\)?\\ *from\\ +\\w+\\ *(order\\ +by\\ +((\\w+\\ *,\\ *)*\\ *\\w+)\\ +(asc|desc))?\\ *(where\\ +\\w+\\ *((>)|(<)|(=)|(>=)|(<=)|(==))\\ *('?.*'?))?\\ *;?");
	}

	private boolean IsInsertValid(String inp) {
		return inp
				.toLowerCase()
				.matches(
						"^(insert)\\ +(into)\\ +\\w+\\ *\\(\\ *((\\w+\\ *,\\ *)*\\ *\\w+)\\ *\\)\\ *values\\ *\\(\\ *(.*)\\ *\\)\\ *;?");

	}

	private boolean IsDeleteValid(String inp) {
		return inp
				.toLowerCase()
				.matches(
						"^(delete)\\ *((\\ *\\*\\ *))?from\\ +\\w+\\ *(where\\ +\\w+\\ *((>)|(<)|(=)|(>=)|(<=)|(==))\\ *('?.*'?))?\\ *;?");
	}

	private boolean IsCreateValid(String inp) {
		if (inp.toLowerCase().matches(
				"^(create|use)\\ +(database)\\ +[_a-z][a-z0-9_]*\\ *;?"))
			return true;
		return inp
				.toLowerCase()
				.matches(
						"^(create)\\ +(table)\\ +\\w+\\ *\\(\\ *(\\w+\\ +((varchar\\([0-9]+\\))|(integer)|(tinyint)|(smallint)|(int)|real|float|double|bigint|long|(integer\\ +array\\[[0-9]+\\])|(varchar\\ +array\\[[0-9]+\\])|(char\\([0-9]+\\))|(longvarchar\\([0-9]+\\)))\\ *,\\ *)*((\\w+\\ +((varchar\\([0-9]+\\))|(integer)|(tinyint)|(smallint)|(int)|real|float|double|bigint|long|(integer\\ +array\\[[0-9]+\\])|(varchar\\ +array\\[[0-9]+\\])|(char\\([0-9]+\\))|(longvarchar\\([0-9]+\\)))))\\ *\\)\\ *;?");
	}
}
//  ((varchar\\([0-9]+\\))|(integer)|(tinyint)|(smallint)|(int)|real|float|double|bigint|long|(integer array\\[[0-9]+\\])|(varchar array\\[[0-9]+\\])|(char\\([0-9]+\\))|(longvarchar\\([0-9]+\\)))
import java.util.ArrayList;

import org.apache.log4j.Logger;

public class Parser {
	public DB onFocus = new DB();
	public String TableName;
	private Logger log = Logger.getLogger(ConnectionIM.class.getName());

	public String perform(String command) throws Exception {
		String original = new String(command);
		command = wellFormatted(command);
		String[] parts = command.split(" ");
		Condition con = getCon(parts, original);
		TableName = getTableName(parts);

		if (parts[0].toUpperCase().equals("SELECT")) {
			log.info("Executing Select Statement");
			if (onFocus == null)
				return dbms.TABLE_NOT_FOUND;
			if (parts[1].equals("*")) {
				return onFocus.selectAll(TableName, con,
						orderingColumns(parts), original.toUpperCase()
								.contains("DESC"));
			} else if (parts[1].toUpperCase().equals("DISTINCT")) {
				ArrayList<String> coluNames = new ArrayList<String>();
				for (int i = 2; i < parts.length
						&& !parts[i].toUpperCase().equals("FROM"); i++)
					coluNames.add(parts[i]);
				return onFocus.selectColumn(coluNames, TableName, con,
						orderingColumns(parts), original.toUpperCase()
								.contains("DESC"));
			} else {
				ArrayList<String> coluNames = new ArrayList<String>();
				for (int i = 1; i < parts.length
						&& !parts[i].toString().equals("FROM"); i++)
					coluNames.add(parts[i]);
				return onFocus.selectColumn(coluNames, TableName, con,
						orderingColumns(parts), original.toUpperCase()
								.contains("DESC"));
			}
		} else if (parts[0].toUpperCase().equals("CREATE")) {
			if (parts[1].toUpperCase().equals("DATABASE")) {
				log.info("Executing Create Database Statement");
				onFocus = new DB();
				return onFocus.createDatabase(parts[2]);
			} else if (parts[1].toUpperCase().equals("TABLE")) {
				log.info("Executing Create Table Statement");
				parts = command.replaceAll(" ARRAY", "ARRAY")
						.replaceAll(" Array", "Array")
						.replaceAll(" array", "array").split(" ");
				if (onFocus == null)
					return dbms.DB_NOT_FOUND;

				return onFocus.createTable(getColumnsData(parts), parts[2]);
			}
		} else if (parts[0].toUpperCase().equals("DELETE")) {
			log.info("Executing Delete Statement");
			if (onFocus == null)
				return dbms.TABLE_NOT_FOUND;
			return onFocus.delete(TableName, con);

		} else if (parts[0].toUpperCase().equals("INSERT")) {
			log.info("Executing Insert Statement");
			if (onFocus == null)
				return dbms.TABLE_NOT_FOUND;
			ArrayList<String> columns = getBetweenBrackets(original
					.substring(original.toUpperCase().indexOf("VALUES") + 6));
			if (parts[3].equals("("))
				return onFocus.insert(
						getBetweenBrackets(original.substring(
								original.indexOf("("), original.indexOf(")"))),
						columns, parts[2]);
			else
				return onFocus.insert(null, columns, parts[2]);
		} else if (parts[0].toUpperCase().equals("UPDATE")) {
			log.info("Executing update Statement");
			if (onFocus == null)
				return dbms.TABLE_NOT_FOUND;
			ArrayList<String> ColuNames = new ArrayList<String>(), values = new ArrayList<String>();
			getData(ColuNames, values, original);
			return onFocus.update(ColuNames, values, TableName, con);
		}
		return dbms.PARSING_ERROR;
	}

	private Condition getCon(String parts[], String d) {
		if (d.toUpperCase().contains("WHERE")) {
			d = d.substring(d.toUpperCase().indexOf("WHERE") + 6).trim();
			if (d.contains("'")) {
				d = d.substring(d.indexOf("'"));
				d.replaceAll("[';]", "");
			} else
				d = null;
		}

		for (int i = 3; i < parts.length; i++)
			if (parts[i].toUpperCase().equals("WHERE")) {
				return new Condition(parts[i + 1], parts[i + 2],
						d == null ? parts[i + 3] : d.replaceAll("[';]", ""));
			}

		return null;
	}

	private String getTableName(String parts[]) {
		for (int i = 0; i < parts.length; i++)
			if (parts[i].toLowerCase().equals("FROM".toLowerCase())
					|| parts[i].toUpperCase().equals("INTO")
					|| parts[i].toUpperCase().equals("UPDATE"))
				return parts[i + 1];
		return null;
	}

	public ArrayList<Column> getColumnsData(String parts[]) {

		ArrayList<Column> myColumns = new ArrayList<Column>();
		for (int i = 4; i < parts.length && !parts[i].equals(")"); i += 5)
			if (parts[i + 1].toLowerCase().equals("int")
					|| parts[i + 1].toLowerCase().equals("integer")
					|| parts[i + 1].toLowerCase().equals("long")
					|| parts[i + 1].toLowerCase().equals("float")
					|| parts[i + 1].toLowerCase().equals("double")
					|| parts[i + 1].toLowerCase().equals("real")
					|| parts[i + 1].toLowerCase().equals("smallint")
					|| parts[i + 1].toLowerCase().equals("bigint")
					|| parts[i + 1].toLowerCase().equals("tinyint")) {
				myColumns.add(new Column(parts[i], parts[i + 1], 255));
				i -= 3;
			} else {
				myColumns.add(new Column(parts[i], parts[i + 1], Integer
						.parseInt(parts[i + 3].trim())));
			}
		return myColumns;
	}

	private ArrayList<String> getBetweenBrackets(String original) {

		original = original.replaceAll("[\\(\\)\\;]", "");
		original = original.replaceAll("\\'( )*\\,( )*\\'", ",").trim();
		if (original.charAt(0) == '\'')
			original = original.substring(1, original.length() - 1);
		String[] parts = original.split(",");
		ArrayList<String> Values = new ArrayList<String>();
		for (int i = 0; i < parts.length; i++)
			if (parts[i].charAt(0) == '{') {
				String temp = parts[i++];
				for (; i < parts.length; i++) {
					temp = temp.concat("','" + parts[i]);
					if (parts[i].charAt(parts[i].length() - 1) == '}')
						break;
				}
				Values.add(temp.trim());
			} else {
				Values.add(parts[i].replaceAll("'", "").trim());
			}

		return Values.size() == 0 ? null : Values;
	}

	private void getData(ArrayList<String> names, ArrayList<String> values,
			String p) {
		int i = p.length();
		if (p.toLowerCase().contains("where"))
			i = p.toLowerCase().indexOf("where");
		String temp = p.substring(p.toLowerCase().indexOf("set") + 3, i).trim();
		String[] pe = temp.split("( )*,( )*");
		for (int j = 0; j < pe.length; j++) {
			String[] pe2 = pe[j].split("( )*=( )*");
			names.add(pe2[0].trim());
			if (pe2[1].charAt(0) == '{') {
				temp = pe2[1];
				for (i = j + 1; i < pe.length; i++) {
					temp = temp.concat("," + pe[i]);
					if (pe[i].charAt(pe[i].length() - 1) == '}')
						break;
				}
				j = i;
				values.add(temp.trim());
			} else
				values.add(pe2[1].trim().substring(1, pe2[1].length() - 1));
		}
	}

	private String wellFormatted(String command) {
		command = command.replaceAll(",", " ");
		command = command.replaceAll("=", " = ");
		command = command.replaceAll(">", " > ");
		command = command.replaceAll("<", " < ");
		command = command.replaceAll("\\)", " \\) ");
		command = command.replaceAll("\\(", " \\( ");
		command = command.replaceAll("\\]", " \\) ");
		command = command.replaceAll("\\[", " \\( ");
		command = command.replaceAll("[\t;]", "");
		return command.replaceAll("^ +| +$|( )+", "$1");
	}

	private ArrayList<String> orderingColumns(String[] parts) {
		ArrayList<String> res = null;
		for (int i = 5; i < parts.length; i++) {
			if (parts[i].toUpperCase().toUpperCase().equals("BY")) {
				res = new ArrayList<String>();
				for (int j = i + 1; j < parts.length
						&& !parts[j].toUpperCase().equals("DESC")
						&& !parts[j].toUpperCase().equals("ASC"); j++)
					res.add(parts[j]);
				break;
			}
		}
		return res;
	}
}

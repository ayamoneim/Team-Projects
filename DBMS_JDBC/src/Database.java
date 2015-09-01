

import java.util.ArrayList;


public interface Database {
	public String createDatabase(String databaseName);

	/*
	 * if there is no condition then con == null;
	 */
	public String selectAll(String TableName, Condition con,
			ArrayList<String> orderingColumns, boolean DESC)throws Exception;

	public String selectColumn(ArrayList<String> columnNames, String TableName,
			Condition con, ArrayList<String> orderingColus, boolean DESC);

	public String createTable(ArrayList<Column> myColumns, String TableName);

	public String delete(String TableName, Condition con);

	public String insert(ArrayList<String> columnNames, ArrayList<String> values,
			String TableName);

	/*
	 * if ColumnNames equals null then it is the first case of insertion;
	 */

	public String update(ArrayList<String> columnNames, ArrayList<String> values,
			String TableName, Condition con);
}
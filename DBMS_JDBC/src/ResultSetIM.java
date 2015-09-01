import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

import org.apache.log4j.Logger;

public class ResultSetIM implements ResultSet {

	public ArrayList<ArrayList<String>> rows = null;
	public ArrayList<String> columnNames = null;
	public ArrayList<String> columnTypes = null;
	private String tablename;
	private Logger log = Logger.getLogger(ResultSetIM.class.getName());
	boolean closed = false;
	int pointer = 0; // indicating that it points to a null place

	private ConnectionIM con;
	private Statement statement;

	// the first row index is one not zero

	public ResultSetIM(ArrayList<ArrayList<String>> rows,
			ArrayList<String> columnNames, ArrayList<String> columnTypes,
			Statement statement, String table) {
		this.rows = rows;
		this.columnNames = columnNames;
		this.columnTypes = columnTypes;
		this.statement = statement;
		this.tablename = table;
	}

	public int getSize() {
		return this.columnNames.size();
	}

	@Override
	public boolean absolute(int row) throws SQLException {
		if (closed) {
			log.error("Accessing Closed ResultSet");
			throw new SQLException("Accessing Closed ResultSet");
		}
		if (rows.size() == 0) {
			return false; // need to check
		}
		if (row < 0) {
			row *= -1;
			if (row > rows.size()) {
				pointer = 0;
			} else {
				pointer = rows.size() - row + 1;
			}
		} else if (row > 0) {
			if (row > rows.size()) {
				pointer = rows.size() + 1; // indicating after last
			} else {
				pointer = row;
			}
		} else if (row == 0) {
			pointer = row; // indicating before first
		}
		if (pointer > 0 && pointer <= rows.size()) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void afterLast() throws SQLException {
		if (closed) {
			log.error("Accessing Closed ResultSet");
			throw new SQLException("Accessing Closed ResultSet");
		}
		if (rows.size() == 0) {
			return;
		}
		pointer = rows.size() + 1;
	}

	@Override
	public void beforeFirst() throws SQLException {
		if (closed) {
			log.error("Accessing Closed ResultSet");
			throw new SQLException("Accessing Closed ResultSet");
		}
		if (rows.size() == 0) {
			return;
		}
		pointer = 0;
	}

	@Override
	public void close() throws SQLException {
		if (closed)
			return;
		closed = true;
		pointer = 0;
		columnNames = null;
		columnTypes = null;
		rows = null;
		statement.close();
	}

	@Override
	public int findColumn(String columnLabel) throws SQLException {
		if (closed) {
			log.error("Accessing Closed ResultSet");
			throw new SQLException("Accessing Closed ResultSet");
		}
		int index = -1;
		for (int i = 0; i < columnNames.size(); i++) {
			if (columnNames.get(i).equals(columnLabel)) {
				index = i;
				return index;
			}
		}
		log.error("Column Not Found");
		throw new SQLException("Column Not Found");
	}

	@Override
	public boolean first() throws SQLException {
		if (closed) {
			log.error("Accessing Closed ResultSet");
			throw new SQLException("Accessing Closed ResultSet");
		}
		if (rows.size() == 0) {
			return false;
		} else {
			absolute(1);
			return true;
		}
	}

	@Override
	public Array getArray(int columnIndex) throws SQLException {
		if (closed) {
			log.error("Accessing Closed ResultSet");
			throw new SQLException("Accessing Closed ResultSet");
		}
		if (columnIndex < 0 || columnIndex >= this.columnNames.size()) {
			log.error("Column Index Out OF Range");
			throw new SQLException("Column Index Out OF Range");
		}
		if (pointer - 1 < 0 || pointer - 1 >= rows.size()
				|| rows.get(pointer - 1).get(columnIndex).isEmpty()) {
			log.error("ResultSet pointer undefined");
			throw new SQLException("ResultSet pointer undefined");
		}

		if (rows.get(pointer - 1).get(columnIndex).equals("null"))
			return null;
		String type = columnTypes.get(columnIndex).split(" ")[0];
		array arr = new array(rows.get(pointer - 1).get(columnIndex), type);
		return arr;
	}

	@Override
	public Array getArray(String columnLabel) throws SQLException {
		return getArray(findColumn(columnLabel));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * Retrieves the value of the designated column in the current row of this
	 * ResultSet object as a boolean in the Java programming language. If the
	 * designated column has a datatype of CHAR or VARCHAR and contains a "0" or
	 * has a datatype of BIT, TINYINT, SMALLINT, INTEGER or BIGINT and contains
	 * a 0, a value of false is returned. If the designated column has a
	 * datatype of CHAR or VARCHAR and contains a "1" or has a datatype of BIT,
	 * TINYINT, SMALLINT, INTEGER or BIGINT and contains a 1, a value of true is
	 * returned
	 */
	@Override
	public boolean getBoolean(int columnIndex) throws SQLException {

		// this method is called on a closed result set
		if (closed) {
			log.error("Accessing Closed ResultSet");
			throw new SQLException("Accessing Closed ResultSet");
		}
		// columnIndex not valid
		if (columnIndex < 0 || columnIndex >= this.columnNames.size()) {
			log.error("Column Index Out OF Range");
			throw new SQLException("Column Index Out OF Range");
		}

		if (columnTypes.get(columnIndex).toLowerCase().equals("char")
				|| columnTypes.get(columnIndex).toLowerCase().equals("varchar")
				|| columnTypes.get(columnIndex).toLowerCase().equals("bit")
				|| columnTypes.get(columnIndex).toLowerCase().equals("tinyint")
				|| columnTypes.get(columnIndex).toLowerCase()
						.equals("smallint")
				|| columnTypes.get(columnIndex).toLowerCase().equals("integer")
				|| columnTypes.get(columnIndex).toLowerCase().equals("bigint")) {

			String s = getString(columnIndex);

			if (s != null && s.equals("0")) {
				return false;
			} else if (s != null && s.equals("1")) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean getBoolean(String columnLabel) throws SQLException {
		// TODO Auto-generated method stub
		return getBoolean(findColumn(columnLabel));
	}

	/*
	 * 
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getDate(int)
	 * 
	 * Retrieves the value of the designated column in the current row of this
	 * ResultSet object as a java.sql.Date object in the Java programming
	 * language.
	 */
	@Override
	public Date getDate(int columnIndex) throws SQLException {
		// this method is called on a closed result set
		if (closed) {
			log.error("Accessing Closed ResultSet");
			throw new SQLException("Accessing Closed ResultSet");
		}
		// columnIndex not valid
		if (columnIndex < 0 || columnIndex >= columnNames.size()) {
			log.error("Column Index Out OF Range");
			throw new SQLException("Column Index Out OF Range");
		}

		return null;
	}

	@Override
	public Date getDate(String columnLabel) throws SQLException {
		// TODO Auto-generated method stub
		return getDate(findColumn(columnLabel));
	}

	@Override
	public Date getDate(int columnIndex, Calendar cal) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Date getDate(String columnLabel, Calendar cal) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc) Retrieves the value of the designated column in the current
	 * row of this ResultSet object as a double in the Java programming language
	 */
	@Override
	public double getDouble(int columnIndex) throws SQLException {

		// this method is called on a closed result set
		if (closed) {
			log.error("Accessing Closed ResultSet");
			throw new SQLException("Accessing Closed ResultSet");
		}
		// columnIndex not valid
		if (columnIndex < 0 || columnIndex >= columnNames.size()) {
			log.error("Column Index Out OF Range");
			throw new SQLException("Column Index Out OF Range");
		}

		String S = getString(columnIndex);

		if (S != null) {
			if (S.length() == 0) {
				return 0;
			}
			try {
				return Double.valueOf(S).doubleValue();
			} catch (NumberFormatException E) {
				log.error("Bad Format For Double" + S + "' in column "
						+ columnIndex + "(" + columnNames.get(columnIndex)
						+ ").");
				throw new java.sql.SQLException("Bad Format For Double" + S
						+ "' in column " + columnIndex + "("
						+ columnNames.get(columnIndex) + ").");
			}
		}
		return 0;
	}

	@Override
	public double getDouble(String columnLabel) throws SQLException {
		// TODO Auto-generated method stub
		return getDouble(findColumn(columnLabel));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getFetchDirection()
	 * 
	 * Retrieves the fetch direction for this ResultSet object.
	 */
	@Override
	public int getFetchDirection() throws SQLException {
		// TODO Auto-generated method stub
		return this.FETCH_UNKNOWN;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getFloat(int)
	 * 
	 * Retrieves the value of the designated column in the current row of this
	 * ResultSet object as a float in the Java programming language.
	 */

	@Override
	public float getFloat(int columnIndex) throws SQLException {

		// this method is called on a closed result set
		if (closed) {
			log.error("Accessing Closed ResultSet");
			throw new SQLException("Accessing Closed ResultSet");
		}
		// columnIndex not valid
		if (columnIndex < 0 || columnIndex >= columnNames.size()) {
			log.error("Column Index Out OF Range");
			throw new SQLException("Column Index Out OF Range");
		}

		String S = getString(columnIndex);

		if (S != null) {
			if (S.length() == 0) {
				return 0;
			}
			try {
				return Float.valueOf(S).floatValue();
			} catch (NumberFormatException E) {
				log.error("Bad Format For Float " + S + " in column "
						+ columnIndex + "(" + columnNames.get(columnIndex)
						+ ").");
				throw new java.sql.SQLException("Bad Format For Float " + S
						+ " in column " + columnIndex + "("
						+ columnNames.get(columnIndex) + ").");
			}
		}
		return 0;
	}

	@Override
	public float getFloat(String columnLabel) throws SQLException {
		// TODO Auto-generated method stub
		return getFloat(findColumn(columnLabel));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getInt(int)
	 * 
	 * Retrieves the value of the designated column in the current row of this
	 * ResultSet object as an int in the Java programming language.
	 */
	@Override
	public int getInt(int columnIndex) throws SQLException {

		// this method is called on a closed result set
		if (closed) {
			log.error("Accessing Closed ResultSet");
			throw new SQLException("Accessing Closed ResultSet");
		}
		// columnIndex not valid
		if (columnIndex < 0 || columnIndex >= columnNames.size()) {
			log.error("Column Index Out OF Range");
			throw new SQLException("Column Index Out OF Range");
		}

		String S = getString(columnIndex);

		if (S != null) {
			if (S.length() == 0) {
				return 0;
			}
			try {
				return Integer.parseInt(S);
			} catch (NumberFormatException E) {
				log.error("Bad Integer Format" + S + "' in column"
						+ columnIndex + "(" + columnNames.get(columnIndex)
						+ ").");
				throw new java.sql.SQLException("Bad Integer Format" + S
						+ "' in column" + columnIndex + "("
						+ columnNames.get(columnIndex) + ").");
			}
		}
		return 0;
	}

	@Override
	public int getInt(String columnLabel) throws SQLException {
		// TODO Auto-generated method stub
		return getInt(findColumn(columnLabel));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getLong(java.lang.String) Retrieves the value of
	 * the designated column in the current row of this ResultSet object as a
	 * long in the Java programming language.
	 */
	@Override
	public long getLong(int columnIndex) throws SQLException {

		// this method is called on a closed result set
		if (closed) {
			log.error("Accessing Closed ResultSet");
			throw new SQLException("Accessing Closed ResultSet");
		}
		// columnIndex not valid
		if (columnIndex < 0 || columnIndex >= columnNames.size()) {
			log.error("Column Index Out OF Range");
			throw new SQLException("Column Index Out OF Range");
		}
		String s = getString(columnIndex);
		if (s != null) {
			if (s.length() == 0)
				return 0;

			try {
				return Long.parseLong(s);
			} catch (NumberFormatException E) {
				log.error("Bad Long Format" + s + "' in column" + columnIndex
						+ "(" + columnNames.get(columnIndex) + ").");
				throw new java.sql.SQLException("Bad Long Format" + s
						+ "' in column" + columnIndex + "("
						+ columnNames.get(columnIndex) + ").");
			}
		}
		return 0;
	}

	@Override
	public long getLong(String columnLabel) throws SQLException {
		// TODO Auto-generated method stub
		return getLong(findColumn(columnLabel));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getMetaData()
	 * 
	 * Retrieves the number, types and properties of this ResultSet object's
	 * columns.
	 */
	@Override
	public ResultSetMetaData getMetaData() throws SQLException {
		// this method is called on a closed result set

		if (closed) {
			log.error("Accessing Closed ResultSet");
			throw new SQLException("Accessing Closed ResultSet");
		}
		return new ResultSetMetaDataIM(columnNames, columnTypes, tablename);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getObject(int) Gets the value of the designated
	 * column in the current row of this ResultSet object as an Object in the
	 * Java programming language.
	 * 
	 * This method will return the value of the given column as a Java object.
	 * The type of the Java object will be the default Java object type
	 * corresponding to the column's SQL type, following the mapping for
	 * built-in types specified in the JDBC specification. If the value is an
	 * SQL NULL, the driver returns a Java null.
	 */
	@Override
	public Object getObject(int columnIndex) throws SQLException {
		// this method is called on a closed result set
		if (closed) {
			log.error("Accsess Closed ResultSet");
			throw new SQLException("Accsess Closed ResultSet");
		}
		// columnIndex not valid

		if (columnIndex < 0 || columnIndex >= this.columnNames.size()) {
			log.error("Column Index Out OF Range");
			System.out.println();
			throw new SQLException("Column Index Out OF Range");
		}

		
		if (pointer - 1 < 0 ) {
			log.error("ResultSet pointer undefined");
			System.out.println();
			throw new SQLException("ResultSet pointer undefined");
		}

		if (rows.get(pointer - 1).get(columnIndex).equals("null"))
			return "null";
		if (columnTypes
				.get(columnIndex)
				.toLowerCase()
				.matches("(integer|varchar[0-9]*)\\ +array\\ *\\[[0-9]+\\]\\ *"))
			return getArray(columnIndex);

		switch (columnTypes.get(columnIndex).toLowerCase()) {
		case "bit":
			return new Boolean(getBoolean(columnIndex));
		case "tinyint":
		case "smallint":
		case "integer":
		case "int":
			return new Integer(getInt(columnIndex));
		case "long":
		case "bigint":
			return new Long(getLong(columnIndex));
		case "real":
		case "float":
			return new Float(getFloat(columnIndex));
		case "double":
			return new Double(getDouble(columnIndex));
		case "char":
		case "varchar":
		case "longvarchar":
			return getString(columnIndex);
		case "date":
			return getDate(columnIndex);
		default: {
			throw new SQLException("Undefined Data Type");
		}

		}
	}

	@Override
	public Object getObject(String columnLabel) throws SQLException {
		// TODO Auto-generated method stub
		return getObject(findColumn(columnLabel));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getStatement() Retrieves the Statement object
	 * that produced this ResultSet object. If the result set was generated some
	 * other way, such as by a DatabaseMetaData method, this method may return
	 * null
	 */
	@Override
	public Statement getStatement() throws SQLException {
		if (closed) {
			log.error("Accessing Closed ResultSet");
			throw new SQLException("Accessing Closed ResultSet");
		}
		return statement;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.ResultSet#getString(int) Retrieves the value of the
	 * designated column in the current row of this ResultSet object as a String
	 * in the Java programming language.
	 */
	@Override
	public String getString(int columnIndex) throws SQLException {

		if (closed) {
			log.error("Accessing Closed ResultSet");
			throw new SQLException("Accessing Closed ResultSet");
		}

		if (rows == null) {
			log.error("No ResultSet Generated");

			throw new SQLException("No ResultSet Generated");
		}// this method is called on a closed result set

		// columnIndex not valid
		if (columnIndex < 0 || columnIndex >= columnNames.size()) {
			log.error("Column Index Out OF Range");
			throw new SQLException("Column Index Out OF Range");
		}
		if (pointer == -1 || pointer == 0 || pointer == rows.size() + 1) {
			log.error("Index Out Of bounds");
			throw new SQLException("Index Out Of bounds");
		}
		return new String(rows.get(pointer - 1).get(columnIndex));

	}

	@Override
	public String getString(String columnLabel) throws SQLException {
		return getString(findColumn(columnLabel));
	}

	@Override
	public boolean isAfterLast() throws SQLException {
		if (closed) {
			log.error("Accessing Closed ResultSet");
			throw new SQLException("Accessing Closed ResultSet");
		}
		if (rows.size() == 0) {
			return false;
		}
		return (pointer == rows.size() + 1);
	}

	@Override
	public boolean isBeforeFirst() throws SQLException {
		if (closed) {
			log.error("Accessing Closed ResultSet");
			throw new SQLException("Accessing Closed ResultSet");
		}
		if (rows.size() == 0) {
			return false;
		}
		return (pointer == 0);
	}

	@Override
	public boolean isClosed() throws SQLException {
		return closed;
	}

	@Override
	public boolean isFirst() throws SQLException {
		if (closed) {
			log.error("Accessing Closed ResultSet");
			throw new SQLException("Accessing Closed ResultSet");
		}
		if (rows.size() == 0) {
			return false;
		}
		return (pointer == 1);
	}

	@Override
	public boolean isLast() throws SQLException {
		if (closed) {
			log.error("Accessing Closed ResultSet");
			throw new SQLException("Accessing Closed ResultSet");
		}
		if (rows.size() == 0) {
			return false;
		}
		return (pointer == rows.size());
	}

	@Override
	public boolean last() throws SQLException {
		if (closed) {
			log.error("Accessing Closed ResultSet");
			throw new SQLException("Accessing Closed ResultSet");
		}
		if (rows.size() == 0) {
			return false;
		} else {
			pointer = rows.size();
			return true;
		}
	}

	@Override
	public boolean next() throws SQLException {
		if (closed) {
			log.error("Accessing Closed ResultSet");
			throw new SQLException("Accessing Closed ResultSet");
		} else if (rows.size() == 0) {
			return false;
		} else if (pointer == rows.size()) {
			pointer++;
			return false;
		} else if (pointer == rows.size() + 1) {
			return false;
		} else {
			pointer++;
			return true;
		}
	}

	@Override
	public boolean previous() throws SQLException {
		if (closed) {
			log.error("Accessing Closed ResultSet");
			throw new SQLException("Accessing Closed ResultSet");
		} else if (rows.size() == 0) {
			return false;
		} else if (pointer == 1) {
			pointer--;
			return false;
		} else if (pointer == 0) {
			return false;
		} else {
			pointer--;
			return true;
		}
	}

	// no need to implement these methods
	@Override
	public void moveToCurrentRow() throws SQLException {

	}

	@Override
	public void moveToInsertRow() throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void refreshRow() throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean relative(int rows) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean rowDeleted() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean rowInserted() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean rowUpdated() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void updateArray(int columnIndex, Array x) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateArray(String columnLabel, Array x) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateAsciiStream(int columnIndex, InputStream x)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateAsciiStream(String columnLabel, InputStream x)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateAsciiStream(int columnIndex, InputStream x, int length)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateAsciiStream(String columnLabel, InputStream x, int length)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateAsciiStream(int columnIndex, InputStream x, long length)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateAsciiStream(String columnLabel, InputStream x, long length)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateBigDecimal(int columnIndex, BigDecimal x)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateBigDecimal(String columnLabel, BigDecimal x)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateBinaryStream(int columnIndex, InputStream x)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateBinaryStream(String columnLabel, InputStream x)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateBinaryStream(int columnIndex, InputStream x, int length)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateBinaryStream(String columnLabel, InputStream x, int length)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateBinaryStream(int columnIndex, InputStream x, long length)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateBinaryStream(String columnLabel, InputStream x,
			long length) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateBlob(int columnIndex, Blob x) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateBlob(String columnLabel, Blob x) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateBlob(int columnIndex, InputStream inputStream)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateBlob(String columnLabel, InputStream inputStream)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateBlob(int columnIndex, InputStream inputStream, long length)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateBlob(String columnLabel, InputStream inputStream,
			long length) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateBoolean(int columnIndex, boolean x) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateBoolean(String columnLabel, boolean x)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateByte(int columnIndex, byte x) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateByte(String columnLabel, byte x) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateBytes(int columnIndex, byte[] x) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateBytes(String columnLabel, byte[] x) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateCharacterStream(int columnIndex, Reader x)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateCharacterStream(String columnLabel, Reader reader)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateCharacterStream(int columnIndex, Reader x, int length)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateCharacterStream(String columnLabel, Reader reader,
			int length) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateCharacterStream(int columnIndex, Reader x, long length)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateCharacterStream(String columnLabel, Reader reader,
			long length) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateClob(int columnIndex, Clob x) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateClob(String columnLabel, Clob x) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateClob(int columnIndex, Reader reader) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateClob(String columnLabel, Reader reader)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateClob(int columnIndex, Reader reader, long length)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateClob(String columnLabel, Reader reader, long length)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateDate(int columnIndex, Date x) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateDate(String columnLabel, Date x) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateDouble(int columnIndex, double x) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateDouble(String columnLabel, double x) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateFloat(int columnIndex, float x) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateFloat(String columnLabel, float x) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateInt(int columnIndex, int x) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateInt(String columnLabel, int x) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateLong(int columnIndex, long x) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateLong(String columnLabel, long x) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateNCharacterStream(int columnIndex, Reader x)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateNCharacterStream(String columnLabel, Reader reader)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateNCharacterStream(int columnIndex, Reader x, long length)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateNCharacterStream(String columnLabel, Reader reader,
			long length) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateNClob(int columnIndex, NClob nClob) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateNClob(String columnLabel, NClob nClob)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateNClob(int columnIndex, Reader reader) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateNClob(String columnLabel, Reader reader)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateNClob(int columnIndex, Reader reader, long length)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateNClob(String columnLabel, Reader reader, long length)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateNString(int columnIndex, String nString)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateNString(String columnLabel, String nString)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateNull(int columnIndex) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateNull(String columnLabel) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateObject(int columnIndex, Object x) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateObject(String columnLabel, Object x) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateObject(int columnIndex, Object x, int scaleOrLength)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateObject(String columnLabel, Object x, int scaleOrLength)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateRef(int columnIndex, Ref x) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateRef(String columnLabel, Ref x) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateRow() throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateRowId(int columnIndex, RowId x) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateRowId(String columnLabel, RowId x) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateSQLXML(int columnIndex, SQLXML xmlObject)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateSQLXML(String columnLabel, SQLXML xmlObject)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateShort(int columnIndex, short x) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateShort(String columnLabel, short x) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateString(int columnIndex, String x) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateString(String columnLabel, String x) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateTime(int columnIndex, Time x) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateTime(String columnLabel, Time x) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateTimestamp(int columnIndex, Timestamp x)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateTimestamp(String columnLabel, Timestamp x)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean wasNull() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setFetchDirection(int direction) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setFetchSize(int rows) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void insertRow() throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return false;
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		return null;
	}

	@Override
	public void cancelRowUpdates() throws SQLException {

	}

	@Override
	public void clearWarnings() throws SQLException {

	}

	@Override
	public void deleteRow() throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public InputStream getAsciiStream(int columnIndex) throws SQLException {
		return null;
	}

	@Override
	public InputStream getAsciiStream(String columnLabel) throws SQLException {
		return null;
	}

	@Override
	public BigDecimal getBigDecimal(int columnIndex) throws SQLException {
		return null;
	}

	@Override
	public BigDecimal getBigDecimal(String columnLabel) throws SQLException {
		return null;
	}

	@Override
	public BigDecimal getBigDecimal(int columnIndex, int scale)
			throws SQLException {
		return null;
	}

	@Override
	public BigDecimal getBigDecimal(String columnLabel, int scale)
			throws SQLException {
		return null;
	}

	@Override
	public InputStream getBinaryStream(int columnIndex) throws SQLException {
		return null;
	}

	@Override
	public InputStream getBinaryStream(String columnLabel) throws SQLException {
		return null;
	}

	@Override
	public Blob getBlob(int columnIndex) throws SQLException {
		return null;
	}

	@Override
	public Blob getBlob(String columnLabel) throws SQLException {
		return null;
	}

	@Override
	public byte getByte(int columnIndex) throws SQLException {
		return 0;
	}

	@Override
	public byte getByte(String columnLabel) throws SQLException {
		return 0;
	}

	@Override
	public byte[] getBytes(int columnIndex) throws SQLException {
		return null;
	}

	@Override
	public byte[] getBytes(String columnLabel) throws SQLException {
		return null;
	}

	@Override
	public Reader getCharacterStream(int columnIndex) throws SQLException {
		return null;
	}

	@Override
	public Reader getCharacterStream(String columnLabel) throws SQLException {
		return null;
	}

	@Override
	public Clob getClob(int columnIndex) throws SQLException {
		return null;
	}

	@Override
	public Clob getClob(String columnLabel) throws SQLException {
		return null;
	}

	@Override
	public int getConcurrency() throws SQLException {
		return 0;
	}

	@Override
	public String getCursorName() throws SQLException {
		return null;
	}

	@Override
	public int getFetchSize() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getHoldability() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Reader getNCharacterStream(int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Reader getNCharacterStream(String columnLabel) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NClob getNClob(int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NClob getNClob(String columnLabel) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getNString(int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getNString(String columnLabel) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getObject(int columnIndex, Map<String, Class<?>> map)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getObject(String columnLabel, Map<String, Class<?>> map)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T getObject(int columnIndex, Class<T> type) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T getObject(String columnLabel, Class<T> type)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Ref getRef(int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Ref getRef(String columnLabel) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getRow() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public RowId getRowId(int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RowId getRowId(String columnLabel) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SQLXML getSQLXML(int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SQLXML getSQLXML(String columnLabel) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public short getShort(int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public short getShort(String columnLabel) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Time getTime(int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Time getTime(String columnLabel) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Time getTime(int columnIndex, Calendar cal) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Time getTime(String columnLabel, Calendar cal) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Timestamp getTimestamp(int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Timestamp getTimestamp(String columnLabel) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Timestamp getTimestamp(int columnIndex, Calendar cal)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Timestamp getTimestamp(String columnLabel, Calendar cal)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getType() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public URL getURL(int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public URL getURL(String columnLabel) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InputStream getUnicodeStream(int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InputStream getUnicodeStream(String columnLabel) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SQLWarning getWarnings() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

}

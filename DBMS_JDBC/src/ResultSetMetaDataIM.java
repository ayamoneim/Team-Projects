import java.sql.*;
import java.util.ArrayList;

import org.apache.log4j.Logger;

public class ResultSetMetaDataIM implements ResultSetMetaData {
	private String table_name;
	private ArrayList<String> columns_types;
	private ArrayList<String> columns_names;
	private Logger log = Logger.getLogger(ResultSetMetaDataIM.class.getName());

	public ResultSetMetaDataIM(ArrayList<String> columnsNames,
			ArrayList<String> columnTypes, String TableName) {
		columns_names = columnsNames;
		columns_types = columnTypes;
		table_name = TableName;
	}

	/************** the required **************/
	@Override
	public int getColumnCount() throws SQLException {
		if (columns_names == null) {
			log.error("There is no column_names");
			throw new SQLException("There is no column_names");
		}
		return columns_names.size();
	}

	@Override
	public String getColumnLabel(int column) throws SQLException {
		return getColumnName(column);
	}

	@Override
	public String getColumnName(int column) throws SQLException {
		column--;
		if (column < -1 || column >= columns_names.size()) {
			log.error("Index is out of bound");
			throw new SQLException("Index is out of bound");
		}

		return columns_names.get(column);
	}

	@Override
	public int getColumnType(int column) throws SQLException {
		column--;
		if (column < -1 || column >= columns_names.size()) {
			log.error("Index is out of bound");
			throw new SQLException("Index is out of bound");
		}
		if (columns_types.get(column).equals("int")
				|| columns_types.get(column).equals("integer"))
			return Types.INTEGER;
		if (columns_types.get(column).toLowerCase().equals("varchar"))
			return Types.VARCHAR;
		if (columns_types.get(column).toLowerCase().equals("float"))
			return Types.FLOAT;
		if (columns_types.get(column).toLowerCase().equals("double"))
			return Types.DOUBLE;
		if (columns_types.get(column).toLowerCase().contains("array"))
			return Types.ARRAY;
		if (columns_types.get(column).toLowerCase().equals("boolean"))
			return Types.BOOLEAN;
		if (columns_types.get(column).toLowerCase().equals("char"))
			return Types.CHAR;
		if (columns_types.get(column).toLowerCase().equals("null"))
			return Types.NULL;
		if (columns_types.get(column).toLowerCase().equals("date"))
			return Types.DATE;
		if (columns_types.get(column).toLowerCase().equals("bit"))
			return Types.BIT;
		if (columns_types.get(column).toLowerCase().equals("smallint"))
			return Types.SMALLINT;
		if (columns_types.get(column).toLowerCase().equals("bigint"))
			return Types.BIGINT;
		if (columns_types.get(column).toLowerCase().equals("tinyint"))
			return Types.TINYINT;
		if (columns_types.get(column).toLowerCase().equals("real"))
			return Types.REAL;
		log.error("Type mismatch");
		throw new SQLException("Type mismatch");
	}

	@Override
	public String getTableName(int column) throws SQLException {
		if (column < -1 || column >= columns_names.size()) {
			log.error("Index is out of bound");
			throw new SQLException("Index is out of bound");
		}
		return table_name;
	}

	@Override
	public boolean isAutoIncrement(int column) throws SQLException {
		return true;
	}

	@Override
	public int isNullable(int column) throws SQLException {
		return ResultSetMetaData.columnNullable;
	}

	@Override
	public boolean isReadOnly(int column) throws SQLException {
		return false;
	}

	@Override
	public boolean isSearchable(int column) throws SQLException {

		return true;
	}

	@Override
	public boolean isWritable(int column) throws SQLException {

		return true;
	}

	/******************************************/
	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCatalogName(int column) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getColumnClassName(int column) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getColumnDisplaySize(int column) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getPrecision(int column) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getScale(int column) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getSchemaName(int column) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isCaseSensitive(int column) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isCurrency(int column) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isDefinitelyWritable(int column) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSigned(int column) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getColumnTypeName(int column) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

}

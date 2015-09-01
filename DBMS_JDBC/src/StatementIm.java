import java.sql.*;
import java.util.Vector;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.log4j.Logger;

public class StatementIm implements Statement{
	
	private static final int INF = -1;
	
	private Vector<String> Batches = new Vector<String>();
	private String Database;
	private ResultSetIM ResSet = new ResultSetIM(null, null, null, null, null);
	private int timelimit = INF;
	private ConnectionIM connection;
	private DBMSystem DBMSS;
	private Logger log = Logger.getLogger(StatementIm.class.getName());

	public StatementIm(String DB, ConnectionIM con) throws Exception
	{
		connection = con;
		Database = DB;
		DBMSS = new DBMSystem();
		DBMSS.input("CREATE DATABASE " + DB + ";");
	}
	
	class Task implements Callable<String> {
		String sql;
		Task(String s){sql = s;}
	    @Override
	    public String call() throws Exception {
	    	return DBMSS.input(sql);
	    }
	}
	
	@Override
	public void addBatch(String sql) throws SQLException {
		if(Database == ""){
			log.error("No Defined Current Database");
			throw new SQLException("No Defined Current Database");
		}
		Batches.add(sql);
	}
	
	@Override
	public void clearBatch() throws SQLException {
		if(Database == ""){
			log.error("No Defined Current Database");
			throw new SQLException("No Defined Current Database");
		}
		Batches.clear();
	}
	
	@Override
	public void close() throws SQLException {
		if(Database == "")
			return;
		Database = "";
		ResSet.close();
		connection.close();
	}
	
	@Override
	public boolean execute(String sql) throws SQLException {
		if(Database == ""){
			log.error("No Defined Current Database");
			throw new SQLException("No Defined Current Database");
		}
		if(sql.trim().substring(0, 6).toUpperCase().equals("SELECT"))
		{
			executeQuery(sql);
			return true;
		}
		else executeUpdate(sql);
		return false;
	}
	
	@Override
	public int[] executeBatch() throws SQLException {
		if(Database == ""){
			log.error("No Defined Current Database");
			throw new SQLException("No Defined Current Database");
		}
		int[] arr = new int[Batches.size()];
		for(int i=0;i<Batches.size();i++)
		{
			if(Batches.elementAt(i).trim().substring(0, 6).toUpperCase().equals("SELECT"))
			{
				log.error(new BatchUpdateException());
				throw new BatchUpdateException();
			}
			executeUpdate(Batches.elementAt(i));
			arr[i] = DBMSS.parser.onFocus.counter;
		}
		return arr;
	}
	
	@Override
	public ResultSet executeQuery(final String sql) throws SQLException {
		if(Database == ""){
			log.error("No Defined Current Database");
			throw new SQLException("No Defined Current Database");
		}		
		if(!sql.trim().substring(0, 6).toUpperCase().equals("SELECT"))
		{
			log.error(new SQLException());
			throw new SQLException();
		}
		String res = "";
		if(timelimit == INF)
		{
			try {
				res = DBMSS.input(sql);
			} catch (Exception e) {
				log.error(e.getMessage());
				throw new SQLException();
			}
		}
		else
		{
			ExecutorService executor = Executors.newSingleThreadExecutor();
			Future<String> future = executor.submit(new Task(sql));
			try {
	        	try {
					res = future.get(timelimit, TimeUnit.SECONDS);
				} catch (InterruptedException | ExecutionException e) {
					
				}
	        } catch (TimeoutException e) {
	        	log.error("Query Execution TimeOut");
	        	throw new SQLTimeoutException("Query Execution TimeOut");
	        }
	        executor.shutdownNow();
		}
		if(res.equals(dbms.PARSING_ERROR) || res.equals(dbms.TABLE_NOT_FOUND) || res.equals(dbms.TABLE_ALREADY_EXISTS) || res.equals(dbms.COLUMN_NOT_FOUND) || res.equals(dbms.COLUMN_TYPE_MISMATCH) || res.equals(dbms.NOT_MATCH_CRITERIA) || res.equals(dbms.DB_NOT_FOUND))
		{
			log.error(res);
        	throw new SQLException(res);
		}
		ArrayList<ArrayList<String> > rows = new ArrayList<ArrayList<String> >();
		String parse = "";
		for(int i=0;i<res.length();i++)
		{
			ArrayList<String> tempo = new ArrayList<String>();
			while(i < res.length())
			{
				if(res.charAt(i) == '\n') break;
				if(res.charAt(i) == '*')
				{
					tempo.add(parse);parse = "";
				}
				else parse+=res.charAt(i);
				i++;
			}
			rows.add(tempo);
		}
		ArrayList<String> colnames = new ArrayList<String>(),coltypes = new ArrayList<String>();
		Schema s = new Schema();
		ArrayList<Column> t = s.schemaParsing(Database + "\\" + DBMSS.parser.TableName + ".xsd");
		for (int i = 0; i < t.size(); i++) {
			colnames.add(t.get(i).getColName());
			coltypes.add(t.get(i).getdataType());
		}
		return ResSet = new ResultSetIM(rows, colnames, coltypes, this, DBMSS.parser.TableName);
	}
	
	@Override
	public int executeUpdate(final String sql) throws SQLException {
		if(Database == ""){
			log.error("No Defined Current Database");
			throw new SQLException("No Defined Current Database");
		}
		if(sql.trim().substring(0, 6).toUpperCase().equals("SELECT"))
		{
			log.error(new SQLException());
			throw new SQLException();
		}
		String res = "";
		if(timelimit == INF)
		{
			try {
				res = DBMSS.input(sql);
			} catch (Exception e) {
				log.error(e.getMessage());
				throw new SQLException();
			}
		}
		else
		{
			ExecutorService executor = Executors.newSingleThreadExecutor();
			Future<String> future = executor.submit(new Task(sql));
			try {
	        	try {
					res = future.get(timelimit, TimeUnit.SECONDS);
				} catch (InterruptedException | ExecutionException e) {
					
				}
	        } catch (TimeoutException e) {
	        	log.error("Query Execution TimeOut");
	        	throw new SQLTimeoutException("Query Execution TimeOut");
	        }
	        executor.shutdownNow();
		}
		if(res.equals(dbms.PARSING_ERROR) || res.equals(dbms.TABLE_NOT_FOUND) || res.equals(dbms.TABLE_ALREADY_EXISTS) || res.equals(dbms.COLUMN_NOT_FOUND) || res.equals(dbms.COLUMN_TYPE_MISMATCH) || res.equals(dbms.NOT_MATCH_CRITERIA) || res.equals(dbms.DB_NOT_FOUND)){
			log.error(res);
			throw new SQLException(res);
		}
		return 0;
	}
	
	@Override
	public int getQueryTimeout() throws SQLException {
		if(Database == ""){
			log.error("No Defined Current Database");
			throw new SQLException("No Defined Current Database");
		}
		if(timelimit == INF)
			return 0;
		return timelimit;
	}
	
	@Override
	public void setQueryTimeout(int seconds) throws SQLException {
		if(Database == "" || seconds<0){
			log.error("No Defined Current Database");
			throw new SQLException("No Defined Current Database");
		}
		if(seconds == 0)
			timelimit = INF;
		else timelimit = seconds;
	}
	
	@Override
	public Connection getConnection() throws SQLException {
		if(Database == ""){
			log.error("No Defined Current Database");
			throw new SQLException("No Defined Current Database");
		}
		return connection;
	}
	
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
	public void cancel() throws SQLException {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void clearWarnings() throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void closeOnCompletion() throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean execute(String sql, int autoGeneratedKeys)
			throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean execute(String sql, int[] columnIndexes) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean execute(String sql, String[] columnNames)
			throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int executeUpdate(String sql, int autoGeneratedKeys)
			throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int executeUpdate(String sql, int[] columnIndexes)
			throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int executeUpdate(String sql, String[] columnNames)
			throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getFetchDirection() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getFetchSize() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ResultSet getGeneratedKeys() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getMaxFieldSize() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getMaxRows() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean getMoreResults() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean getMoreResults(int current) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ResultSet getResultSet() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getResultSetConcurrency() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getResultSetHoldability() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getResultSetType() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getUpdateCount() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public SQLWarning getWarnings() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isCloseOnCompletion() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isClosed() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isPoolable() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setCursorName(String name) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setEscapeProcessing(boolean enable) throws SQLException {
		// TODO Auto-generated method stub
		
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
	public void setMaxFieldSize(int max) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setMaxRows(int max) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPoolable(boolean poolable) throws SQLException {
		// TODO Auto-generated method stub
		
	}

}

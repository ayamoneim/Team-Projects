


public interface dbms {

	public static final String TABLE_NOT_FOUND = "This Table doesn't exists in database";// in commands retrieving file
	public static final String COLUMN_NOT_FOUND = "This column doesn't exists in this table";// schema check in commands 
	public static final String TABLE_ALREADY_EXISTS = "This Table already exists";// creating table
	public static final String PARSING_ERROR = "bad formated input";// parsing
	public static final String DB_NOT_FOUND = "No database exists";// commands
	public static final String COLUMN_TYPE_MISMATCH = "Entered value doesn't match column type";// commands type mismatch
	public static final String Con_DB = "DB created";// command done
	public static final String Con_Table = "Table created";// command done
	public static final String Con_insert = "insertion Complete";// command done
	public static final String Con_Delete = "Row/s deleted";// command done
	public static final String Con_Update = "Row/s Updated";// command done
	public static final String NOT_MATCH_CRITERIA = "no row exists with this criteria";// check condition returns nth
	public static final String DB_ALREADY_EXISTS= "Data Base Already Exist" ;
	/**
	 * This function will take the input String like select * from table_name
	 * and return the results in String
	 * @throws Exception 
	 * */
	public String input(String input) throws Exception;
	
	
}

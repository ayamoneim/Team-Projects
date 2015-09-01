import java.sql.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;


public class testResultSet {

	public static void main(String[]args) throws Exception{
		

		ArrayList<ArrayList<String> > rows = new ArrayList<ArrayList<String> >();
		String tablename  = "hamada" ; 
		ArrayList<String > columnames = new ArrayList <String>()  ; 
		ArrayList <String > columntypes = new ArrayList < String>() ; 
//		ResultSetIM res = new ResultSetIM(rows , columnames , columntypes , s , tablename  ) ;
		StatementIm s = new StatementIm ("hamada",new ConnectionIM ("hamada") ) ; 
		ResultSetIM res = (ResultSetIM) s.executeQuery("select * from Persons");
		res.absolute(0);
		while(res.next()){
			System.out.println(res.getInt(0));
			System.out.println(res.getString(1));
			System.out.println(res.getString(2));
			System.out.println(res.getString(3));
			System.out.println(res.getString(4));
			System.out.println(res.getFloat(5));
//			System.out.println(res.getArray(6));
			System.out.println("****************************************");
		}
		
	}
	
}

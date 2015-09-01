////import java.util.concurrent.Callable;
////import java.util.concurrent.ExecutorService;
////import java.util.concurrent.Executors;
////import java.util.concurrent.Future;
////import java.util.concurrent.TimeUnit;
////import java.util.concurrent.TimeoutException;
////
////
////
////
public class test {
////    public static void main(String[] args) throws Exception {
////    	
////    	
////    	
//////        ExecutorService executor = Executors.newSingleThreadExecutor();
//////        Future<String> future = executor.submit(new Task());
//////
//////        try {
//////            System.out.println("Started..");
//////            System.out.println(future.get(3, TimeUnit.SECONDS));
//////            System.out.println("Finished!");
//////        } catch (TimeoutException e) {
//////            System.out.println("Terminated!");
//////        }
//////
//////        executor.shutdownNow();
////    }
////}
////
////class Task implements Callable<String> {
////    @Override
////    public String call() throws Exception {
////        return "Ready!";
////    }
////}
//
//import java.sql.SQLException;
//import java.util.ArrayList;
//
//public class test {
////	public static void main(String[] args) throws SQLException {
////	
////		Validator v = new Validator();
////		System.out.println(v.Validate_Expression("SELECT columnname,columnname FROM tablename;"));
////		System.out.println(v.Validate_Expression("SELECT * FROM table_name;"));
////		System.out.println(v.Validate_Expression("SELECT DISTINCT column_name,column_name FROM table_name;"));
////		System.out.println(v.Validate_Expression("SELECT column_name,column_name FROM table_name WHERE column_name < value;"));
////		System.out.println(v.Validate_Expression("SELECT * FROM Customers WHERE Country='Mexico';"));
////		System.out.println(v.Validate_Expression("SELECT * FROM Customers WHERE CustomerID=1;"));
////		System.out.println(v.Validate_Expression("SELECT column_name,column_name FROM table_name ORDER BY column_name,column_name ASC;"));
////		System.out.println(v.Validate_Expression("INSERT INTO Customers (CustomerName,ContactName,Address,City,PostalCode,Country) VALUES ('Cardinal','{Tom b,'2'}','Skagen','Stavanger','4006','Norway');"));
////		System.out.println(v.Validate_Expression("UPDATE Customers SET ContactName='Alfred Schmidt', City='Hamburg' WHERE CustomerName='{Alfreds ,Futterkiste}';"));
////		System.out.println(v.Validate_Expression("DELETE FROM Customers WHERE CustomerName='Alfreds Futterkiste' ;"));
////		System.out.println(v.Validate_Expression("DELETE * FROM table_name;"));
////
////	}
//	
//	
public static void main(String[] args) throws Exception {
DB test = new DB();
Parser p = new Parser();
System.out.println(p.perform("Select * from persons order by personid desc"));
//////////
//Condition con = new Condition("PersonID",">","1");
//ArrayList<String> a = new ArrayList<String>();
//ArrayList<String> a0 = new ArrayList<String>();
//a0.add("array");
//////////a0.add("LastName");
//////////a0.add("FirstName");
////////a0.add("address");
////a0.add("city");
//////////a.add("address");
////////a.add("PersonID");
//////////a.add("FirstName");
//a.add("{0,0,0}");
//////////a0.add("6");
////////////a0.add("null");
////////////a0.add("moneim");
////////////a0.add("dsfsfd");
//////////a0.add("dsfsdfa");
////////////test.parseSchema();
////////////System.out.println(test.insert(a,a0,"Persons"));
//////////Integer x = 0;
//////////System.out.println(x.getClass().getName());
////////////System.out.println(test.delete("Persons", con));
//////////(String TableName, Condition con,
//////////	ArrayList<String> orderingColumns, boolean DESC)
//////////int[]arr = test.getIndicesArray(a, a0);
////////int[]arr = {3};
//////////System.out.println(arr.length);
//////////for(int i = 0;i<arr.length;i++)
//////////System.out.println(arr[i]);
//////////test.parseString("1*Mohamed*Ali*11 street*Tanta\n1*Mohamed*Tamer*11 street*Alexandria\n3*Ahmed*Mohsen*12 street*Cairo\n4*Bassem*Yasser*43 street*Banha", arr, true);
////////
//
//System.out.println(test.update(a0, a, "Persons", con));
////System.out.println(test.selectColumn(a0,"Persons", null,a,true));
////////////
//////////System.out.println("********************************************");
//////////System.out.println(test.selectColumn(a0,"Persons", null,a,false));
}
//
}

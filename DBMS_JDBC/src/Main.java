import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.Properties;

import org.apache.log4j.Logger;

public class Main {

	public static void main(String[] args) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		String url = "jdbc:DBMS:default";
		Connection con;
		Statement stmt;
		Logger log = Logger.getLogger(Main.class.getName());
		try {
			Class.forName("DriverIM");
		} catch (java.lang.ClassNotFoundException e) {
			System.err.print("ClassNotFoundException: ");
			System.err.println(e.getMessage());
		}
		Properties p = null;
		try {
			DriverIM e = new DriverIM();
			DriverManager.registerDriver(e);
			do {
				String tempo;
				System.out.print("Enter username: ");
				tempo = in.readLine();
				System.out.print("Enter password: ");
				p = new Properties();
				p.put("username", tempo);
				p.put("password", in.readLine());
				try {
					con = DriverManager.getConnection(url, p);
				} catch (SQLException sqlEx) {
					log.error(sqlEx.getMessage());
					continue;
				}
				break;
			} while (true);
			System.out.println("\n-------------------------------------------");
			stmt = con.createStatement();
			String query;
			while (true) {
				System.out.print("SQL>");
				query = in.readLine().trim();
				if (query == null)
					continue;
				String firstW = query.trim();
				if (query.contains(" "))
					firstW = query.substring(0, query.indexOf(" "));
				if (firstW.toLowerCase().equals("use"))
					try {
						con = DriverManager.getConnection("Jdbc:DBMS:"
								+ query.substring(3).trim()
										.replaceAll(";", " ").trim(), p);
						stmt = con.createStatement();
					} catch (SQLException ee) {
						log.error(ee.getMessage());
						continue;
					}
				else if (firstW.toLowerCase().equals("select")) {
					try {
						ResultSetIM res = (ResultSetIM) stmt
								.executeQuery(query);
						res.absolute(0);
						int sz = res.rows.size() == 0 ? 0 : res.rows.get(0)
								.size();
						while (res.next()) {
							for (int i = 0; i < sz; i++) {
								if (res.columnTypes.get(i).contains("array")) {
									array a = new array(
											res.rows.get(res.pointer - 1)
													.get(i), res.columnTypes
													.get(i).split(" ")[0]);
									if (!res.getObject(i).equals("")) {
										System.out.print(a.getArray() + " | ");
									}
								} else {
									if (!res.getObject(i).equals("")) {
										System.out.print(res.getObject(i)
												+ " | ");
									}
								}
							}
							System.out.println();
						}
					} catch (SQLException ee) {
						log.error(ee.getMessage());
						System.out.println();
						System.err.println("SQLException: " + ee.getMessage());
						continue;
					}
				} else if (firstW.toLowerCase().equals("create")) {
					try {
						System.out.println(stmt.execute(query));
						String[] str = query.split(" ");
						if (!str[1].toLowerCase().equals("table")) {
							query = query.trim().substring(query.indexOf(" "))
									.trim();
							con = DriverManager.getConnection("Jdbc:DBMS:"
									+ query.substring(query.indexOf(" "))
											.replaceAll(";", " ").trim(), p);
							stmt = con.createStatement();
						}
					} catch (SQLException ee) {
						log.error(ee.getMessage());
						System.err.println("SQLException: " + ee.getMessage());
						continue;
					}
				} else if (query.trim().toLowerCase().equals("end")) {
					con.close();
					stmt.close();
					break;
				} else {
					try {
						System.out.println(stmt.executeUpdate(query));
					} catch (SQLException ee) {
						log.error(ee.getMessage());
						continue;
					}
				}
			}

		} catch (SQLException ex) {
			log.error(ex.getMessage());
			System.err.println("SQLException: " + ex.getMessage());
		}
	}
}

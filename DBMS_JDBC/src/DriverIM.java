
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;
import java.util.logging.Logger;

public class DriverIM implements Driver {

	/************** the required **************/
	@Override
	public boolean acceptsURL(String url) throws SQLException {

		String[] str = url.split(":");
		File dir = new File(str[2]);
		if (!dir.exists()) {
			return false;
		}
		return true;
	}

	@Override
	public Connection connect(String url, Properties info) throws SQLException {

		if (!acceptsURL(url))
			throw new SQLException("data base doesn't exist.");

		String user = info.getProperty("username");
		String pass = info.getProperty("password");

		if (!checkUser(user, pass))
			throw new SQLException("incorrect username or password.");

		String[] str = url.split(":");

		return new ConnectionIM(str[2]);

	}

	@Override
	public DriverPropertyInfo[] getPropertyInfo(String url, Properties info)
			throws SQLException {

		DriverPropertyInfo username = new DriverPropertyInfo("username",
				info.getProperty("username"));
		username.description = "username required to enter database.";
		username.required = true;

		DriverPropertyInfo pass = new DriverPropertyInfo("password",
				info.getProperty("password"));
		pass.description = "password required to enter database.";
		pass.required = true;

		String[] str = url.split(":");

		DriverPropertyInfo databases = new DriverPropertyInfo("databases",
				str[2]);
		databases.description = "database selected for connection.";
		databases.required = true;

		try {
			databases.choices = dbs();
		} catch (Exception e) {
			e.printStackTrace();
		}

		DriverPropertyInfo[] prop = new DriverPropertyInfo[3];
		prop[0] = username;
		prop[1] = pass;
		prop[2] = databases;

		return prop;
	}

	private String[] dbs() throws Exception {
		BufferedReader br = new BufferedReader(new FileReader("databases.txt"));
		int n = Integer.parseInt(br.readLine());
		String[] dbs = new String[n];
		for (int i = 0; i < n; i++)
			dbs[i] = br.readLine();
		br.close();
		return dbs;
	}

	private boolean checkUser(String user, String pass) {

		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader("users.txt"));
			int n = 0;
			n = Integer.parseInt(br.readLine());
			for (int i = 0; i < n; i++) {
				String[] in = null;
				in = br.readLine().split(",");
				// TODO Auto-generated catch block
				if (user.equals(in[0]) && pass.equals(in[1])) return true;
			}
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Can't find the file");
		}

		return false;
	}

	/******************************************/
	@Override
	public int getMajorVersion() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getMinorVersion() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean jdbcCompliant() {
		// TODO Auto-generated method stub
		return false;
	}

}

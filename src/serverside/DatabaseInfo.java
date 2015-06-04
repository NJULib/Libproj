package serverside;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class DatabaseInfo {

	private String dbDriver;
	private String dbURL;
	private String dbUser;
	private String dbPassword;

	public DatabaseInfo() {
		Properties properties = new Properties();
		try {
			//InputStream inputstream = getClass().getResourceAsStream("servInfo.txt");
			InputStream inputstream = new FileInputStream("dbInfo.txt");
			properties.load(inputstream);
			if (inputstream != null) {
				inputstream.close();
			}
		} catch (FileNotFoundException e1) {
			System.out.println("û�ҵ� dbInfo.txt �ļ�!");
		} catch (IOException e2) {
			System.out.println("I/O Error!");
		}
		dbDriver = properties.getProperty("dbdriver");
		dbURL = properties.getProperty("dburl");
		dbUser = properties.getProperty("dbuser");
		dbPassword = properties.getProperty("dbpwd");
	}

	public String getDbDriver() {
		return dbDriver;
	}

	public String getDbURL() {
		return dbURL;
	}

	public String getDbUser() {
		return dbUser;
	}

	public String getDbPassword() {
		return dbPassword;
	}
}

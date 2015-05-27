package serverside;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 用于读连接数据库时所用到的有关参数，如
 * 数据库的驱动程序、数据库所在主机的URL、用于连接数据库的用户名、密码
 */
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
			System.out.println("没找到 dbInfo.txt 文件!");
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

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
		dbDriver = "sun.jdbc.odbc.JdbcOdbcDriver";
		dbURL = "jdbc:odbc:library";
		dbUser = "book";
		dbPassword = "book";
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

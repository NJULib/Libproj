package clientside;

import java.util.Properties;
import java.io.*;

public class ServerInfo {

	private String serverHost = "";
	private int serverPort = 0;

	public ServerInfo() {
		Properties properties = new Properties();
		try {
			InputStream inputstream = new FileInputStream("servInfo.txt");
			properties.load(inputstream);
			if (inputstream != null) {
				inputstream.close();
			}
		} catch (FileNotFoundException e1) {
			System.out.println("没找到 servInfo.txt 文件!");
		} catch (IOException e2) {
			System.out.println("磁盘 I/O 出错!");
		}
		serverHost = properties.getProperty("host");
		serverPort = Integer.valueOf(properties.getProperty("port"));
	}

	public String getHost() {
		return serverHost;
	}

	public int getPort() {
		return serverPort;
	}

}

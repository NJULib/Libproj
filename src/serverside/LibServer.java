package serverside;

import util.LibProtocals;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import util.*;

/**
 * 应用程序服务器端主类，启动服务器并等待客户的连接 
 */
@SuppressWarnings("all")
public class LibServer implements LibProtocals {
	protected ServerSocket serverSocket;
	protected LibDataAccessor libDataAccessor;
	protected boolean done;
	protected Socket clientSocket = null;
	protected LibOpHandler libOpHandler = null;

	public LibServer(int thePort) {
		done = false;
		try {
			log("启动服务器 " + thePort);
			serverSocket = new ServerSocket(thePort);
			libDataAccessor = new LibDataAccessor();
			log("服务器准备就绪!");
		} catch (IOException e) {
			log(e);
			System.exit(1);
		}
		while (!done) {
			try {
				log("服务器正等待请求...");
				clientSocket = serverSocket.accept();
				String clientHostName = clientSocket.getInetAddress()
						.getHostName();
				log("收到连接: " + clientHostName);
				libOpHandler = new LibOpHandler(clientSocket, libDataAccessor);
				libOpHandler.start();
			} catch (IOException e2) {
				log(e2);
			}
		}
	}

	protected void log(Object msg) {
		System.out.println(CurrDateTime.currDateTime() + "LibServer类: " + msg);
	}

	public static void main(String[] args) {
		LibServer theLibServer = null;
		int port = 6666; // 设置默认启动端口号
		if (args.length == 1) {
			port = Integer.parseInt(args[0]);
		}
		theLibServer = new LibServer(port);
	}
}

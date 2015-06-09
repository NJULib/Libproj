package serverside;

import util.LibProtocals;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import util.*;

/**
 * Ӧ�ó�������������࣬�������������ȴ��ͻ������� 
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
			log("���������� " + thePort);
			serverSocket = new ServerSocket(thePort);
			libDataAccessor = new LibDataAccessor();
			log("������׼������!");
		} catch (IOException e) {
			log(e);
			System.exit(1);
		}
		while (!done) {
			try {
				log("���������ȴ�����...");
				clientSocket = serverSocket.accept();
				String clientHostName = clientSocket.getInetAddress()
						.getHostName();
				log("�յ�����: " + clientHostName);
				libOpHandler = new LibOpHandler(clientSocket, libDataAccessor);
				libOpHandler.start();
			} catch (IOException e2) {
				log(e2);
			}
		}
	}

	protected void log(Object msg) {
		System.out.println(CurrDateTime.currDateTime() + "LibServer��: " + msg);
	}

	public static void main(String[] args) {
		LibServer theLibServer = null;
		int port = 6666; // ����Ĭ�������˿ں�
		if (args.length == 1) {
			port = Integer.parseInt(args[0]);
		}
		theLibServer = new LibServer(port);
	}
}

import java.io.*;

import javax.net.ssl.*;

public class SSUtingServer {
	static boolean first = true; // �ּ���

	public static void main(String[] args) {
		final String runRoot = "C:\\Users\\hgKim\\Documents\\College\\3Grade\\1stSemester\\NetworkProgramming\\SSUting\\bin\\";

		SSLServerSocketFactory sslServerFactory = null;
		SSLServerSocket sslServerSocket = null;
		SSLSocket sslSocket = null;

		User user = new User();

		ServerThread thread = null;
		int count = 1;

		if (args.length != 1) {
			System.out.println("Usage: Classname Port");
			System.exit(1);
		}

		int sPort = Integer.parseInt(args[0]);
		String ksName = runRoot + ".keystore/SSUtingServerKey";

		try {
			WhisperServer whisperServer = new WhisperServer("localhost");

			System.setProperty("javax.net.ssl.keyStore", ksName);
			System.setProperty("javax.net.ssl.keyStorePassword", "20150283");

			sslServerFactory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();

			sslServerSocket = (SSLServerSocket) sslServerFactory.createServerSocket(sPort);

			while (true) {
				sslSocket = (SSLSocket) sslServerSocket.accept();

				thread = new ServerThread(user, sslSocket, whisperServer);
				thread.start();

				if (first) {
					while (first) {
						System.out.println("�� ���� ��...");
						Thread.sleep(1500);
					}
					System.out.println("�� ���� �Ϸ�!");
					System.out.println(user.clientmap.keySet().toString() + "���� ������� ����̽��ϴ�.\n");
					System.out.println("ä�ù� ���� �ο� : 1");
				}
			}

		} catch (SSLException se) {
			System.out.println("SSL problem, exit~");
			try {
				sslServerSocket.close();
				sslSocket.close();
			} catch (IOException i) {
			}
		} catch (Exception e) {
			System.out.println("What?? exit~");
			try {
				sslServerSocket.close();
				sslSocket.close();
			} catch (IOException i) {
			}
		}
	}
}
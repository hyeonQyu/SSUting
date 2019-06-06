import java.io.*;

import javax.net.ssl.*;

public class SSUtingServer {
	static boolean first = true;

	public static void main(String[] args) {
		final String runRoot = "D:\\대학 자료\\3-1\\네트워크 프로그래밍\\SSUting\\bin\\";

		SSLServerSocketFactory sslServerFactory = null;
		SSLServerSocket sslServerSocket = null;
		SSLSocket sslSocket = null;

		User user = new User();

		ServerThread thread = null;

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
						System.out.println("방 만들기 중...");
						Thread.sleep(1500);
					}
					System.out.println("방 만듬");
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
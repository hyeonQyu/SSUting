import java.io.*;

import javax.net.ssl.*;

public class SSUtingServer {
	static boolean first = true; // 주선자

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
						System.out.println("방 생성 중...");
						Thread.sleep(1500);
					}
					System.out.println("방 생성 완료!");
					System.out.println(user.clientmap.keySet().toString() + "님이 사랑방을 만드셨습니다.\n");
					System.out.println("채팅방 참여 인원 : 1");
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
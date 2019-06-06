import java.io.*;

import javax.net.ssl.*;

public class SSUtingServer {
	static int sPort;
	
	public static void main(String[] args) {
		final String runRoot = "C:\\Users\\hgKim\\Documents\\College\\3Grade\\1stSemester\\NetworkProgramming\\SSUting\\bin\\"; // root change : your system
																				// root
		SSLServerSocketFactory sslServerFactory = null;
		SSLServerSocket sslServerSocket = null;
		SSLSocket sslSocket = null;

		User user = new User();
		
		ServerThread thread = null;
		
		if (args.length != 1) {
			System.out.println("Usage: Classname Port");
			System.exit(1);
		}
		sPort = Integer.parseInt(args[0]);

		String ksName = runRoot + ".keystore/SSUtingServerKey";
		
		try {
			 WhisperServer whisperServer = new WhisperServer("localhost");
					 
			 System.setProperty("javax.net.ssl.keyStore",ksName);
	         System.setProperty("javax.net.ssl.keyStorePassword", "20150283");

	         sslServerFactory = (SSLServerSocketFactory)SSLServerSocketFactory.getDefault();

	         sslServerSocket = (SSLServerSocket) sslServerFactory.createServerSocket(sPort);

			while (true) {
				
				sslSocket = (SSLSocket) sslServerSocket.accept();

				thread = new ServerThread(user, sslSocket, whisperServer);
				thread.start();
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
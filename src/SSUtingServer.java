import java.io.*;
import java.security.*;
import javax.net.ssl.*;

public class SSUtingServer {

	public static void main(String[] args) {

		final KeyStore ks;
		final KeyManagerFactory kmf;
		final SSLContext sc;

		final String runRoot = "D:\\대학 자료\\3-1\\네트워크 프로그래밍\\SSUting\\bin\\"; // root change : your system
																				// root
		SSLServerSocketFactory ssf = null;
		SSLServerSocket serverSocket = null;
		SSLSocket sslSocket = null;

		User user = new User();
		
		if (args.length != 2) {
			System.out.println("Usage: Classname Port and Key");
			System.exit(1);
		}
		int sPort = Integer.parseInt(args[0]);
		String key = args[1];

		String ksName = runRoot + ".keystore/SSUtingServerKey";

		char keyStorePass[] = key.toCharArray();

		char keyPass[] = key.toCharArray();

		try {
			ks = KeyStore.getInstance("JKS");
			ks.load(new FileInputStream(ksName), keyStorePass);

			kmf = KeyManagerFactory.getInstance("SunX509");
			kmf.init(ks, keyPass);

			sc = SSLContext.getInstance("TLS");

			sc.init(kmf.getKeyManagers(), null, null);
			ssf = sc.getServerSocketFactory();

			serverSocket = (SSLServerSocket) ssf.createServerSocket(sPort);
			
			// printServerSocketInfo(s);

			while (true) {
				
				sslSocket = (SSLSocket) serverSocket.accept();
				// printSocketInfo(c);

				Thread serverThread = new Thread(new ServerThread(user, sslSocket));
				serverThread.start();
			}

		} catch (SSLException se) {
			System.out.println("SSL problem, exit~");
			try {
				serverSocket.close();
				sslSocket.close();
			} catch (IOException i) {
			}
		} catch (Exception e) {
			System.out.println("What?? exit~");
			try {
				serverSocket.close();
				sslSocket.close();
			} catch (IOException i) {
			}
		}
	}

	private static void printSocketInfo(SSLSocket s) {
		System.out.println("Socket class: " + s.getClass());
		System.out.println("   Remote address = " + s.getInetAddress().toString());
		System.out.println("   Remote port = " + s.getPort());
		System.out.println("   Local socket address = " + s.getLocalSocketAddress().toString());
		System.out.println("   Local address = " + s.getLocalAddress().toString());
		System.out.println("   Local port = " + s.getLocalPort());
		System.out.println("   Need client authentication = " + s.getNeedClientAuth());
		SSLSession ss = s.getSession();
		System.out.println("   Cipher suite = " + ss.getCipherSuite());
		System.out.println("   Protocol = " + ss.getProtocol());
	}

	private static void printServerSocketInfo(SSLServerSocket s) {
		System.out.println("Server socket class: " + s.getClass());
		System.out.println("   Server address = " + s.getInetAddress().toString());
		System.out.println("   Server port = " + s.getLocalPort());
		System.out.println("   Need client authentication = " + s.getNeedClientAuth());
		System.out.println("   Want client authentication = " + s.getWantClientAuth());
		System.out.println("   Use client mode = " + s.getUseClientMode());
	}
}
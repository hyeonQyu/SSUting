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
		SSLServerSocket s = null;
		SSLSocket c = null;

		User user = new User();

		Thread serverThread[] = new Thread[10];
		int count = 0;

		if (args.length != 1) {
			System.out.println("Usage: Classname Port");
			System.exit(1);
		}
		int sPort = Integer.parseInt(args[0]);

		String ksName = runRoot + ".keystore/SSUtingServerKey";

		char keyStorePass[] = "20150283".toCharArray();

		char keyPass[] = "20150283".toCharArray();

		try {
			ks = KeyStore.getInstance("JKS");
			ks.load(new FileInputStream(ksName), keyStorePass);

			kmf = KeyManagerFactory.getInstance("SunX509");
			kmf.init(ks, keyPass);

			sc = SSLContext.getInstance("TLS");
			sc.init(kmf.getKeyManagers(), null, null);

			while (true) {
				ssf = sc.getServerSocketFactory();
				s = (SSLServerSocket) ssf.createServerSocket(sPort);
				printServerSocketInfo(s);

				c = (SSLSocket) s.accept();
				printSocketInfo(c);

				serverThread[count] = new Thread(new ServerThread(user, c));
			}
			// s.close();
			// c.close();
		} catch (SSLException se) {
			System.out.println("SSL problem, exit~");
			try {
				s.close();
				c.close();
			} catch (IOException i) {
			}
		} catch (Exception e) {
			System.out.println("What?? exit~");
			try {
				s.close();
				c.close();
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
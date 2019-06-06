
import java.io.*;
import javax.net.ssl.*;

public class SSUtingClient {

	public static void main(String[] args) {

		DataInputStream in = null;
		DataOutputStream out = null;
		BufferedReader in2 = null;
		// PrintStream out = System.out;

		SSLSocketFactory f = null;
		SSLSocket c = null;

		String sServer = "";
		int sPort = -1;
		String key = "";

		if (args.length != 3) {
			System.out.println("Usage: Classname ServerName securePort and Key");
			System.exit(1);
		}
		sServer = args[0];
		sPort = Integer.parseInt(args[1]);
		key = args[2];

		try {
			System.setProperty("javax.net.ssl.trustStore", "trustedCerts");
			System.setProperty("javax.net.ssl.trustStorePassword", key);

			f = (SSLSocketFactory) SSLSocketFactory.getDefault();
			c = (SSLSocket) f.createSocket(sServer, sPort);

			String[] supported = c.getSupportedCipherSuites();
			c.setEnabledCipherSuites(supported);
			//printSocketInfo(c);

			c.startHandshake();
			// ------------------------------

			in = new DataInputStream(c.getInputStream());
			in2 = new BufferedReader(new InputStreamReader(System.in));
			out = new DataOutputStream(c.getOutputStream());

			// ä�ÿ� ��� �� �г����� �Է¹���
			System.out.print("�г����� �Է����ּ��� : ");
			String data = in2.readLine();

			// ������ �г����� ����
			out.writeUTF(data);
			// ����ڰ� ä�� ������ �Է� �� ������ �����ϱ� ���� ������ ���� �� ����
			Thread th = new Thread(new ClientThread(out, data));
			th.start();

			// Ŭ���̾�Ʈ�� ���� ������� �����κ��� ������ �о���̴� �͸� �ݺ�.
			while (true) {
				String str2 = in.readUTF();
				System.out.println(str2);
			}
		} catch (IOException io) {
		}
		try {
			c.close();
		} catch (IOException ie) {
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
}
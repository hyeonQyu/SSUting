
import java.io.*;
import javax.net.ssl.*;

public class SSUtingClient {

	public static void main(String[] args) {

		DataInputStream in = null;
		DataOutputStream out = null;
		BufferedReader in2 = null;
		// PrintStream out = System.out;

		SSLSocketFactory sslFactory = null;
		SSLSocket sslSocket = null;

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

			sslFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
			sslSocket = (SSLSocket) sslFactory.createSocket(sServer, sPort);

			String[] supported = sslSocket.getSupportedCipherSuites();
			sslSocket.setEnabledCipherSuites(supported);

			sslSocket.startHandshake();

			in = new DataInputStream(sslSocket.getInputStream());
			in2 = new BufferedReader(new InputStreamReader(System.in));
			out = new DataOutputStream(sslSocket.getOutputStream());

			// ä�ÿ� ��� �� �г����� �Է¹���
			System.out.print("�г����� �Է����ּ��� : ");
			String data = in2.readLine();

			// ������ �г����� ����
			out.writeUTF(data);
			
			// ����ڰ� ä�� ������ �Է� �� ������ �����ϱ� ���� ������ ���� �� ����
			ClientThread thread = new ClientThread(out, data);
			thread.start();

			// Ŭ���̾�Ʈ�� ���� ������� �����κ��� ������ �о���̴� �͸� �ݺ�.
			while (true) {
				String str2 = in.readUTF();

				if (!data.equals(str2.substring(0, data.length()))) {
					System.out.println(str2);
				}
			}
		} catch (IOException io) {
		}
		try {
			sslSocket.close();
		} catch (Exception e) {
		}

	}
}
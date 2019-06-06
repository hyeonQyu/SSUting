
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

			// 채팅에 사용 할 닉네임을 입력받음
			System.out.print("닉네임을 입력해주세요 : ");
			String data = in2.readLine();

			// 서버로 닉네임을 전송
			out.writeUTF(data);
			// 사용자가 채팅 내용을 입력 및 서버로 전송하기 위한 쓰레드 생성 및 시작
			Thread th = new Thread(new ClientThread(out, data));
			th.start();

			// 클라이언트의 메인 쓰레드는 서버로부터 데이터 읽어들이는 것만 반복.
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
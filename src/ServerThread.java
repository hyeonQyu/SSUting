import java.io.DataInputStream;
import javax.net.ssl.SSLSocket;

public class ServerThread extends Thread {
	SSLSocket sslSocket;

	DataInputStream in;

	String name; // �߽���

	User user = new User();

	WhisperServer whisperServer;

	public ServerThread(User user, SSLSocket sslSocket, WhisperServer whisperServer) throws Exception {
		this.user = user;
		this.sslSocket = sslSocket;

		this.whisperServer = whisperServer;
	}

	@Override
	public void run() {
		try {
			// ������ Client�κ��� �����͸� �о���̱� ���� DataInputStream ����
			in = new DataInputStream(this.sslSocket.getInputStream()); // ���� ����ڷκ��� �г����� �о����

			this.name = in.readUTF(); // ����� �߰�
			user.addClient(name, this.sslSocket);

			if (SSUtingServer.first) {
				SSUtingServer.first = false;
			}

			while (true) {
				// �������� Ŭ���̾�Ʈ���� �޼��� ����
				String msg = in.readUTF();
				user.sendMsg(msg, name);
			}
		} catch (Exception e) {
			user.removeClient(this.name);
		}
	}
}
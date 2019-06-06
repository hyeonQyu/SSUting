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
			if (SSUtingServer.first) {
				name = "(����)" + name;
				SSUtingServer.first = false;
			}
			user.addClient(name, this.sslSocket);
			
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
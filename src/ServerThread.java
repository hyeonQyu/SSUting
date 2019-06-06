import java.io.DataInputStream;

import javax.net.ssl.SSLSocket;

public class ServerThread implements Runnable {
	SSLSocket sslSocket;

	DataInputStream in;

	String name;
	User user = new User();

	public ServerThread(User user, SSLSocket sslSocket) throws Exception {
		this.user = user;
		this.sslSocket = sslSocket;

		// ������ Client�κ��� �����͸� �о���̱� ���� DataInputStream ����
		in = new DataInputStream(this.sslSocket.getInputStream()); // ���� ����ڷκ��� �г����� �о����
		this.name = in.readUTF(); // ����� �߰����ݴϴ�.
		user.addClient(name, this.sslSocket);

	}

	@Override
	public void run() {
		try {
			while (true) { // �������� Ŭ���̾�Ʈ���� �޼��� ����
				String msg = in.readUTF();

				if (msg.substring(0, 2).equals("/w")) { // �ӼӸ�
					String afterCmd = msg.substring(2);
					String[] words = afterCmd.split(" ");

				} else { // �ӼӸ��� �ƴ϶�� ��ü ����
					user.sendMsg(msg, name);
				}
			}
		} catch (Exception e) {
			user.removeClient(this.name);
		}

	}
}
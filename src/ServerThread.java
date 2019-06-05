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
		in = new DataInputStream(sslSocket.getInputStream());
		// ���� ����ڷκ��� �г����� �о����
		this.name = in.readUTF();
		// ����� �߰����ݴϴ�.
		user.addClient(name, sslSocket);
	}

	@Override
	public void run() {
		try {

			while (true) {
				String msg = in.readUTF();
				user.sendMsg(msg, name);
			}
		} catch (Exception e) {

			user.removeClient(this.name);
		}
	}

}
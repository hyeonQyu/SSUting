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

		// 접속한 Client로부터 데이터를 읽어들이기 위한 DataInputStream 생성
		in = new DataInputStream(sslSocket.getInputStream());
		// 최초 사용자로부터 닉네임을 읽어들임
		this.name = in.readUTF();
		// 사용자 추가해줍니다.
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
import java.io.DataInputStream;
import javax.net.ssl.SSLSocket;

public class ServerThread extends Thread {
	SSLSocket sslSocket;

	DataInputStream in;

	String name; // 발신자

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
			// 접속한 Client로부터 데이터를 읽어들이기 위한 DataInputStream 생성
			in = new DataInputStream(this.sslSocket.getInputStream()); // 최초 사용자로부터 닉네임을 읽어들임

			this.name = in.readUTF(); // 사용자 추가
			if (SSUtingServer.first) {
				name = "(방장)" + name;
				SSUtingServer.first = false;
			}
			user.addClient(name, this.sslSocket);
			
			while (true) {			
				// 서버에서 클라이언트에게 메세지 전송
                String msg = in.readUTF();
                user.sendMsg(msg, name);
			}
		} catch (Exception e) {
			user.removeClient(this.name);
		}
	}
}
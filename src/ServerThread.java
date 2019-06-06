import java.io.DataInputStream;
import javax.net.ssl.SSLSocket;

public class ServerThread extends Thread {
	SSLSocket sslSocket;

	DataInputStream in;

	String name;	// 발신자
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
			user.addClient(name, this.sslSocket);
			
			while (true) {
				// 서버에서 클라이언트에게 메세지 전송
                String msg = in.readUTF();
                
                if(msg.substring(0, 2).equals("/w")) {
                	// 귓속말
                	String[] words = msg.split(" ");
                	// 수신자
                	String receiver = words[1];
                	
                	// 메세지
                	String message = "";
                	for(int i = 2; i < words.length; i++) {
                		message += words[i];
                		message += " ";
                	}
                	
                	// 발신자와 수신자에게만 귓속말 전송
                	whisperServer.sendWhisper(name, receiver, message);
                }
                else if(msg.equals("quit")) {
                	user.removeClient(this.name);
                }
                else {
                	// 귓속말이 아니라면 전체 전송
                    user.sendMsg(msg , name);	
                }
			}
		} catch (Exception e) {
			user.removeClient(this.name);
		}
	}
}
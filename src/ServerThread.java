import java.io.DataInputStream;
import javax.net.ssl.SSLSocket;

public class ServerThread extends Thread {
	SSLSocket sslSocket;

	DataInputStream in;

	String name;	// �߽���
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
			
			while (true) {
				// �������� Ŭ���̾�Ʈ���� �޼��� ����
                String msg = in.readUTF();
                
                if(msg.substring(0, 2).equals("/w")) {
                	// �ӼӸ�
                	String[] words = msg.split(" ");
                	// ������
                	String receiver = words[1];
                	
                	// �޼���
                	String message = "";
                	for(int i = 2; i < words.length; i++) {
                		message += words[i];
                		message += " ";
                	}
                	
                	// �߽��ڿ� �����ڿ��Ը� �ӼӸ� ����
                	whisperServer.sendWhisper(name, receiver, message);
                }
                else if(msg.equals("quit")) {
                	user.removeClient(this.name);
                }
                else {
                	// �ӼӸ��� �ƴ϶�� ��ü ����
                    user.sendMsg(msg , name);	
                }
			}
		} catch (Exception e) {
			user.removeClient(this.name);
		}
	}
}
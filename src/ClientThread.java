import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;

public class ClientThread extends Thread {
	DataOutputStream out;
	BufferedReader in2 = new BufferedReader(new InputStreamReader(System.in));

	String name; // 발신자
	WhisperClient whisperClient;

	public ClientThread(String sServer, DataOutputStream out, String name) {
		this.out = out;
		this.name = name;

		whisperClient = new WhisperClient(sServer);
	}

	public void run() {
		while (true) {
			try {
				String msg = in2.readLine(); // 키보드로부터 입력을 받음

				// 귓속말
				if (msg.substring(0, 2).equals("/w")) {
					String[] words = msg.split(" ");
					// 수신자
					String receiver = words[1];

					// 메세지
					String message = "";
					for (int i = 2; i < words.length; i++) {
						message += words[i];
						message += " ";
					}

					// 발신자와 수신자에게만 귓속말 전송(RMI서버로 전송)
					whisperClient.sendWhisper(name, receiver, message);
				} else {
					// SSL서버로 전송
					out.writeUTF(msg);
				}
			} catch (Exception e) {
			}
		}
	}
}
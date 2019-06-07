import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;

public class ClientThread extends Thread {
	DataOutputStream out;
	BufferedReader in2 = new BufferedReader(new InputStreamReader(System.in));

	String name; // �߽���
	WhisperClient whisperClient;

	public ClientThread(String sServer, DataOutputStream out, String name) {
		this.out = out;
		this.name = name;

		whisperClient = new WhisperClient(sServer);
	}

	public void run() {
		while (true) {
			try {
				String msg = in2.readLine(); // Ű����κ��� �Է��� ����

				// �ӼӸ�
				if (msg.substring(0, 2).equals("/w")) {
					String[] words = msg.split(" ");
					// ������
					String receiver = words[1];

					// �޼���
					String message = "";
					for (int i = 2; i < words.length; i++) {
						message += words[i];
						message += " ";
					}

					// �߽��ڿ� �����ڿ��Ը� �ӼӸ� ����(RMI������ ����)
					whisperClient.sendWhisper(name, receiver, message);
				} else {
					// SSL������ ����
					out.writeUTF(msg);
				}
			} catch (Exception e) {
			}
		}
	}
}
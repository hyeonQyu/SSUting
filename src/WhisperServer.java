import java.rmi.Naming;

public class WhisperServer {

	private Whisper whisper;

	public WhisperServer(String server) {
		try {
			// 1:1��ȭ ��ü ����
			whisper = new WhisperImpl();
			Naming.rebind("rmi://" + server + ":1099/WhisperService", whisper);
			System.out.println("Server Created");
		} catch (Exception e) {
			System.out.print("Trouble: " + e);
		}
	}

}

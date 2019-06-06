import java.rmi.Naming;

public class WhisperServer {
	
	private Whisper whisper;
	
	public WhisperServer(String server) {
		try {
			// 1:1��ȭ ��ü ����
			whisper = new WhisperImpl();
			Naming.rebind("rmi://" + server + ":1099/WhisperService", whisper);
			System.out.println("rmi���� �������");
		} catch(Exception e) {
			System.out.print("Trouble: " + e);
		}
	}
	
	public void sendWhisper(String from, String to, String message) {
		try {
			whisper.sendWhisper(from, to, message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}

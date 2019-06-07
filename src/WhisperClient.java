import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class WhisperClient {

	private Whisper whisper;

	public WhisperClient(String server) {
		try {
			whisper = (Whisper) Naming.lookup("rmi://" + server + "/WhisperService");
		} catch (MalformedURLException mue) {
			System.out.println("MalformedURLException: " + mue);
		} catch (RemoteException re) {
			System.out.println("RemoteException: " + re);
		} catch (NotBoundException nbe) {
			System.out.println("NotBoundException: " + nbe);
		}
	}

	public void sendWhisper(String from, String to, String message) throws Exception {
		whisper.sendWhisper(from, to, message);
	}
}

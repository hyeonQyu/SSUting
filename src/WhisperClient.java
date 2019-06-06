import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class WhisperClient {
	
	//public Whisper whisper;
	
	public WhisperClient(String server) {
		try {
			Whisper whisper = (Whisper) Naming.lookup("rmi://" + server + "/WhisperService");
			System.out.println(whisper.getPartner());
		}
		catch(MalformedURLException mue) {
			System.out.println("MalformedURLException: " + mue);
		}
		catch(RemoteException re) {
			System.out.println("RemoteException: " + re);
		}
		catch(NotBoundException nbe) {
			System.out.println("NotBoundException: " + nbe);
		}
	}
}

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class WhisperClient {
	
	//public Whisper whisper;
	
	public WhisperClient(String server) {
		try {
			//Whisper whisper = Naming.lookup("rmi://" + server + "/WhisperService");
			Whisper whisper = (Whisper)LocateRegistry.getRegistry("localhost", SSUtingServer.sPort + 10).lookup("rmi://" + server + "/WhisperService");
			System.out.println(whisper.getPartner());
		}
/*		catch(MalformedURLException mue) {
			System.out.println("MalformedURLException: " + mue);
		}*/
		catch(RemoteException re) {
			System.out.println("RemoteException: " + re);
		}
		catch(NotBoundException nbe) {
			System.out.println("NotBoundException: " + nbe);
		}
	}
}

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Whisper extends Remote {
	public String getPartner() throws RemoteException;
	public void sendWhisper(String from, String to, String message) throws Exception;
}

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class WhisperImpl extends UnicastRemoteObject implements Whisper {
   public WhisperImpl() throws RemoteException {
      super();
   }

   @Override
   public String getPartner() throws RemoteException {
      // TODO Auto-generated method stub
      return "getPartner()";
   }
}
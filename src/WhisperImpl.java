import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Iterator;

public class WhisperImpl extends UnicastRemoteObject implements Whisper {
	public WhisperImpl() throws RemoteException {
		super();
	}

	@Override
	public void sendWhisper(String from, String to, String message) throws Exception {
		// TODO Auto-generated method stub
		Iterator iterator = User.clientmap.keySet().iterator();
		while (iterator.hasNext()) {
			String clientName = iterator.next().toString();
			
			// 발신자에게 전송할 메시지
			if (clientName.equals(from)) {
				String tempS;
				Iterator tempI = User.clientmap.keySet().iterator();
				while (tempI.hasNext()) {
					tempS = tempI.next().toString();
					if (tempS.equals(to) && !clientName.equals(to)) {
						User.clientmap.get(clientName).writeUTF("[" + to + "]" + "님에게 : " + message);
						break;
					}
				}
			}
			// 수신자에게 전송할 메시지
			else if (clientName.equals(to)) {
				User.clientmap.get(clientName).writeUTF("[" + from + "]" + "님으로부터 : " + message);
			}
		}

	}
}

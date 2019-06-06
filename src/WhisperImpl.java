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
			String clientName = (String) iterator.next();

			// �߽��ڿ��� ������ �޽���
			if (clientName.equals(from)) {
				User.clientmap.get(clientName).writeUTF("[" + to + "]" + "�Կ��� : " + message);
			}
			// �����ڿ��� ������ �޽���
			else if (clientName.equals(to)) {
				User.clientmap.get(clientName).writeUTF("[" + from + "]" + "�����κ��� : " + message);
			}
		}
	}
}

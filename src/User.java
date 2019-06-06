import java.io.DataOutputStream;
import java.util.HashMap;
import java.util.Iterator;

import javax.net.ssl.SSLSocket;

public class User {

	public static HashMap<String, DataOutputStream> clientmap = new HashMap<String, DataOutputStream>();

	public synchronized void addClient(String name, SSLSocket socket) {
		try {
			sendMsg(name + " 님이 입장하셨습니다.");
			clientmap.put(name, new DataOutputStream(socket.getOutputStream()));
			System.out.println("채팅 참여 인원 : " + clientmap.size());
		} catch (Exception e) {
		}

	}

	public synchronized void removeClient(String name) {
		try {
			clientmap.remove(name);
			sendMsg(name + " 님이 퇴장하셨습니다.");
			System.out.println("채팅 참여 인원 : " + clientmap.size());
		} catch (Exception e) {
		}
	}

	public synchronized void sendMsg(String msg) throws Exception {
		Iterator iterator = clientmap.keySet().iterator();
		while (iterator.hasNext()) {
			String clientname = (String) iterator.next();
			clientmap.get(clientname).writeUTF(msg);
		}
	}

	public synchronized void sendMsg(String msg, String name) throws Exception {
		String printName = name;
		Iterator iterator = clientmap.keySet().iterator();
		while (iterator.hasNext()) {
			String clientname = (String) iterator.next();
			if (printName.substring(0, 4).equals("(방장)")) {
				printName = printName.substring(5);
			}
			clientmap.get(clientname).writeUTF(printName + " : " + msg);
		}
	}
}
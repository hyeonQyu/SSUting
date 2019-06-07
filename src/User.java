import java.io.DataOutputStream;
import java.util.HashMap;
import java.util.Iterator;

import javax.net.ssl.SSLSocket;

public class User {

	public static HashMap<String, DataOutputStream> clientmap = new HashMap<String, DataOutputStream>();

	public synchronized void addClient(String name, SSLSocket socket) {
		try {
			int cnt = 0;
			sendMsg("[" + name + "]" + " 님이 입장하셨습니다.");
			clientmap.put(name, new DataOutputStream(socket.getOutputStream()));
			if (!SSUtingServer.first) {
				System.out.println("------------------------------------------");
				System.out.println("사랑방 참여 인원 : " + clientmap.size());
				Iterator iterator = clientmap.keySet().iterator();
				while (iterator.hasNext()) {
					System.out.println(++cnt + " : " + iterator.next().toString());
				}
				System.out.println("------------------------------------------");
			}
		} catch (Exception e) {
		}

	}

	public synchronized void removeClient(String name) {
		try {
			int cnt = 0;
			clientmap.remove(name);
			
			if (clientmap.isEmpty()) {
				SSUtingServer.first = true;
			}
			sendMsg("[" + name + "]" + " 님이 퇴장하셨습니다.");
			System.out.println("------------------------------------------");
			System.out.println("사랑방 참여 인원 : " + clientmap.size());
			Iterator iterator = clientmap.keySet().iterator();
			while (iterator.hasNext()) {
				System.out.println(++cnt + " : " + iterator.next().toString());
			}
			System.out.println("------------------------------------------");
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
		Iterator iterator = clientmap.keySet().iterator();
		if (msg.contains("시리야~")) {
			int cnt = 0;
			clientmap.get(name).writeUTF("------------------------------------------");
			clientmap.get(name).writeUTF("사랑방 참여 인원 : " + clientmap.size());
			while (iterator.hasNext()) {
				clientmap.get(name).writeUTF(++cnt + " : " + iterator.next().toString());
			}
			clientmap.get(name).writeUTF("------------------------------------------");
		} else {
			while (iterator.hasNext()) {
				String clientname = (String) iterator.next();
				clientmap.get(clientname).writeUTF(name + " : " + msg);
			}
		}
	}
}
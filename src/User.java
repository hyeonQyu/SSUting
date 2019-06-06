import java.io.DataOutputStream;
import java.util.HashMap;
import java.util.Iterator;

import javax.net.ssl.SSLSocket;

public class User {

	public static HashMap<String, DataOutputStream> clientmap = new HashMap<String, DataOutputStream>();

	public synchronized void addClient(String name, SSLSocket socket) {
		try {
			int cnt = 0;
			sendMsg("[" + name + "]" + " ���� �����ϼ̽��ϴ�.");
			clientmap.put(name, new DataOutputStream(socket.getOutputStream()));
			if (!SSUtingServer.first) {
				System.out.println("------------------------------------------");
				System.out.println("����� ���� �ο� : " + clientmap.size());
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
			sendMsg("[" + name + "]" + " ���� �����ϼ̽��ϴ�.");
			System.out.println("------------------------------------------");
			System.out.println("����� ���� �ο� : " + clientmap.size());
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
		while (iterator.hasNext()) {
			String clientname = (String) iterator.next();
			clientmap.get(clientname).writeUTF(name + " : " + msg);
		}
	}
}
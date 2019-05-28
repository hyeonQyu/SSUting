import java.io.IOException;
import java.nio.*;
import java.nio.channels.*;
import java.nio.charset.*;
import java.util.*;

public class NIOMultiClientThread extends Thread {
	private NIOMultiClient mc;
	private Charset charset;
	private CharsetDecoder decoder;

	public NIOMultiClientThread(NIOMultiClient mc) {
		this.mc = mc;
		charset = Charset.forName("KSC5601");
		decoder = charset.newDecoder();
	}

	public void run() {
		String message = null;
		String[] receivedMsg = null;
		Selector selector = mc.getSelector();
		boolean isStop = false;
		while (!isStop) {
			try {
				selector.select();// client의 selector
				Iterator iterator = selector.selectedKeys().iterator();// next를 사용하기위해서 iterator를 만듬
				while (iterator.hasNext()) {
					SelectionKey key = (SelectionKey) iterator.next();
					if (key.isReadable()) {
						message = read(key);
					}
					iterator.remove();
				}
				receivedMsg = message.split("#");// split는 #을 기준으로 나눔, []로 받으므로 차례로 들어감
			} catch (Exception e) {
				e.printStackTrace();
				isStop = true;
			}
			System.out.println(receivedMsg[0] + "," + receivedMsg[1]);
			if (receivedMsg[1].equals("exit")) {
				if (receivedMsg[0].equals(mc.getId())) {// 두 개의 조건이 맞으면 클라이언트(mc)를 내보냄
					mc.exit();
				} else {
					mc.getJta().append(receivedMsg[0] + "님이 종료하셨습니다" + System.getProperty("line.separator"));
					mc.getJta().setCaretPosition(mc.getJta().getDocument().getLength());
				}
			} else {
				mc.getJta().append(receivedMsg[0] + " : " + receivedMsg[1] + System.getProperty("line.separator"));
				// mc.getJta().setCaretPosition(mc.getJta().getDocument().getLength());
			}
		}
	}

	public String read(SelectionKey key) {
		SocketChannel sc = (SocketChannel) key.channel();// 채널의 정보를 소켓채널로
		ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
		try {
			sc.read(buffer);// buffer에 소켓의 정보를 넣음
		} catch (IOException e) {
			e.printStackTrace();
			try {
				sc.close();
			} catch (IOException ioe) {
			}
		}
		System.out.println(buffer);
		buffer.flip();
		String message = null;
		try {
			message = decoder.decode(buffer).toString();
		} catch (CharacterCodingException e) {
			e.printStackTrace();
		}
		System.out.println("message : " + message);
		return message;
	}
}
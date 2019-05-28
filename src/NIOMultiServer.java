import java.nio.*;
import java.nio.channels.*;
import java.util.*;
import java.io.*;
import java.net.*;

public class NIOMultiServer {
	private String host = "localhost";
	private int port = 3000;
	private Selector selector;
	private ServerSocketChannel serverChannel;
	private ServerSocket serverSocket;
	private ArrayList list = new ArrayList();

	public NIOMultiServer() {
		try { // 채널을 관리해주는 것이 셀렉터
			selector = Selector.open(); // 셀렉터를 연다
			serverChannel = ServerSocketChannel.open();// 서버채널을 연다
			serverChannel.configureBlocking(false);// 블로킹되지 않도록 세팅
			serverSocket = serverChannel.socket();// 채널의 소켓과 서버소켓을 내부적으로 연결
			InetSocketAddress isa = new InetSocketAddress(host, port);// 아이넷 주소 생성
			serverSocket.bind(isa);// 소켓과 어드레스를 연결
			serverChannel.register(selector, SelectionKey.OP_ACCEPT);// 채널을 셀렉터에 등록
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		readyServer();
	}

	public void readyServer() {
		try {
			while (true) {
				System.out.println("요청을 기다리는 중...");
				selector.select();
				Iterator iterator = selector.selectedKeys().iterator();
				while (iterator.hasNext()) {
					SelectionKey key = (SelectionKey) iterator.next();
					if (key.isAcceptable()) {
						accept(key);
					} else if (key.isReadable()) {
						read(key);
					}
					iterator.remove();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void accept(SelectionKey key) {
		ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
		SocketChannel socketChannel = null;
		try {
			socketChannel = serverChannel.accept();
			if (socketChannel == null)
				return;
			socketChannel.configureBlocking(false);
			socketChannel.register(selector, SelectionKey.OP_READ);
			list.add(socketChannel);
		} catch (Exception ioe) {
			ioe.printStackTrace();
		}
	}

	public void read(SelectionKey key) {
		SocketChannel socketChannel = (SocketChannel) key.channel();
		ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
		try {
			int read = socketChannel.read(buffer);
		} catch (IOException ioe) {
			try {
				socketChannel.close();
			} catch (IOException _ioe) {
			}
			list.remove(socketChannel);
		}
		try {
			broadCasting(buffer);
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		buffer.clear();
	}

	public void broadCasting(ByteBuffer buffer) throws IOException {
		buffer.flip();
		for (int i = 0; i < list.size(); i++) {
			SocketChannel sc = (SocketChannel) list.get(i);
			sc.write(buffer);
			buffer.rewind();
		}
	}

	public static void main(String[] args) {
		new NIOMultiServer();
	}
}
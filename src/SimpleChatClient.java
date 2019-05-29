import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.*;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Scanner;

public class SimpleChatClient {

	private static final String HOST = "localhost";
	private static final int PORT = 1333;

	private static FileHandler fileHandler;
	private static Logger logger = Logger.getLogger("net.daum.javacafe");

	private Selector selector = null;
	private SocketChannel sc = null;

	private Charset charset = null;
	private CharsetDecoder decoder = null;
	
	private String userName;

	public SimpleChatClient() {
		charset = Charset.forName("EUC-KR");
		decoder = charset.newDecoder();
		
		System.out.print("이름을 입력하세요 >> ");
		Scanner scanner = new Scanner(System.in);
		userName = scanner.nextLine();
	}

	public void initServer() {
		try {
			selector = Selector.open();
			
			// 소켓채널 생성
			sc = SocketChannel.open(new InetSocketAddress(HOST, PORT));
			sc.configureBlocking(false);
			
			sc.register(selector, SelectionKey.OP_READ);
			broadcast(userName + "님이 입장했습니다.");
			
		} catch(IOException e) {
			log(Level.WARNING, "SimpleChatClient.initServer()", e);
		}
	}
	
	private void broadcast(String str) {
		ByteBuffer buffer = ByteBuffer.allocateDirect(50);
		
		buffer.put(str.getBytes());
		buffer.flip();
		try {
			sc.write(buffer);
		} catch(Exception e) {
			System.out.println("broadcast 문제 발생");
		}
	}
	
	public void startServer() {
		startWriter();
		startReader();
	}
	
	private void startWriter() {
//		info("Writer is started..");
		Thread t = new MyThread(sc);
		t.start();
	}
	
	private void startReader() {
//		info("Reader is started..");
		try {
			while(true) {
//				info("요청을 기다리는 중..");
				selector.select();
				
				Iterator it = selector.selectedKeys().iterator();
				while(it.hasNext()) {
					SelectionKey key = (SelectionKey) it.next();
					if(key.isReadable()) {
						read(key);
					}
					it.remove();
				}
			}
		} catch(Exception e) {
			log(Level.WARNING, "SimpleChatClient.startServer()", e);
		}
	}
	
	private void read(SelectionKey key) {
		// SelectionKey로부터 소켓채널을 얻어옴
		SocketChannel sc = (SocketChannel) key.channel();
		// 바이트버퍼 생성
		ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
		int read = 0;
		try {
			// 요청한 클라이언트의 소켓채널로부터 데이터를 읽어들임
			read = sc.read(buffer);
//			info(read + "byte를 읽었습니다.");
		} catch(IOException e) {
			try {
				sc.close();
			} catch(IOException e1) {
				
			}
		}
		
		buffer.flip();
		
		String data = "";
		try {
			data = decoder.decode(buffer).toString();
		} catch(CharacterCodingException e) {
			log(Level.WARNING, "SimpleChatClient.read()", e);
		}
		
		System.out.println(data);
		
		// 버퍼 메모리 해제
		clearBuffer(buffer);
	}
	
	private void clearBuffer(ByteBuffer buffer) {
		if(buffer != null) {
			buffer.clear();
			buffer = null;
		}
	}
	
	public void initLog() {
		try {
			fileHandler = new FileHandler("SimpleChatClient.log");
		} catch(IOException e) {
			logger.addHandler(fileHandler);
			logger.setLevel(Level.ALL);
		}
	}
	
	public void log(Level level, String msg, Throwable error) {
		logger.log(level, msg, error);
	}
	
	public void info(String msg) {
		logger.info(msg);
	}
	
	class MyThread extends Thread {
		
		private SocketChannel sc = null;
		
		public MyThread(SocketChannel sc) {
			this.sc = sc;
		}
		
		public void run() {
			ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
			try {
				while(!Thread.currentThread().isInterrupted()) {
					buffer.clear();
					BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
					String	message = userName + " : " + in.readLine();
					
					// ctrl + c 혹은 quit이라고 입력
					if(message.equals(userName + " : null") || message.equals(userName + " : quit")) {
						message = userName + "님이 나갔습니다.";
					}
					
					buffer.put(message.getBytes());
					buffer.flip();
					
					sc.write(buffer);
					if(message.equals(userName + "님이 나갔습니다."))
						System.exit(0);
				}
			} catch(Exception e) {
				
			} finally {
				clearBuffer(buffer);
			}
		}
		
	}
	
	public static void main(String[] args) {
		SimpleChatClient scc = new SimpleChatClient();
		scc.initLog();
		scc.initServer();
		scc.startServer();
	}
	
}

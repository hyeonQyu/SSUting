import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Vector;

public class NioServer {
    private Selector selector = null;
    private ServerSocketChannel serverSocketChannel = null;
    private ServerSocket serverSocket = null;

    private Vector room = new Vector();

    public void initServer() {
        try {
            selector = Selector.open();

            // Create ServerSocket Channel
            serverSocketChannel = ServerSocketChannel.open();

            // 비 블록킹 모드로 설정한다.
            serverSocketChannel.configureBlocking(false);

            // 서버소켓 채널과 연결된 서버소켓을 가져��다.
            serverSocket = serverSocketChannel.socket();

            // 주어진 파라미터에 해당��는 주소다. 포트로 서버소켓을 바인드한다.
            InetSocketAddress isa = new InetSocketAddress("localhost", 9999);
            serverSocket.bind(isa);

            // 서버소켓 채널을 셀렉터에 등록한다.
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void startServer() {
        System.out.println("Server is Started....");
        try {
            while (true) {
                selector.select(); // 셀렉터�� select() 메소드로 준비된 이벤트가 있는지 확인한다.

                Iterator it = selector.selectedKeys().iterator();

                while (it.hasNext()) {
                    SelectionKey key = (SelectionKey) it.next();

                    if (key.isAcceptable()) {
                        accept(key);
                    } else if (key.isReadable()) {
                        read(key);
                    }
                    it.remove();
                } // end of while(iterator)
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void accept(SelectionKey key) {
        ServerSocketChannel server = (ServerSocketChannel) key.channel();

        try {
            // 서버소켓 채널�� accept() 메소드로 서버소켓을 생성한다.
            SocketChannel sc = server.accept();
            // 생성된 소켓채널을 비 블록킹과 읽기 모드로 셀렉터에 등록한다.

            if (sc == null)
                return;

            sc.configureBlocking(false);
            sc.register(selector, SelectionKey.OP_READ);

            room.add(sc); // 접속자 추가
            System.out.println(sc.toString() + "클라이언트가 접속했습니다.");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void read(SelectionKey key) {

        // SelectionKey로부터 소켓채널을 얻어��다.
        SocketChannel sc = (SocketChannel) key.channel();
        // ByteBuffer를 생성한다.
        ByteBuffer buffer = ByteBuffer.allocateDirect(1024);

        try {
            // 요청한 클라이언트�� 소켓채널로부터 데이터를 읽어들인다.
            int read = sc.read(buffer);
        } catch (IOException ex) {
            try {
                sc.close();
            } catch (IOException e) {
            }

            room.remove(sc);
            ex.printStackTrace();
        }

        try {
            broadcast(buffer);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        if (buffer != null) {
            buffer.clear();
            buffer = null;
        }
    }

    private void broadcast(ByteBuffer buffer) throws IOException {
        buffer.flip();
        Iterator iter = room.iterator();
        while (iter.hasNext()) 
        {
            SocketChannel sc = (SocketChannel) iter.next();

            if (sc != null) {
                sc.write(buffer);
                buffer.rewind();
            }
        }
    }
}
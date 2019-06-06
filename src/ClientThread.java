import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;

public class ClientThread extends Thread {
    DataOutputStream out;
    BufferedReader in2 = new BufferedReader(new InputStreamReader(System.in));
    
    String name;	
    WhisperClient whisperClient;
    
    public ClientThread(DataOutputStream out, String name)
    {
        this.out = out;
        this.name = name;
        
        whisperClient = new WhisperClient("localhost");
    }
    
    public void run()
    {     
        while(true)
        {      
            try
            {
                String msg = in2.readLine();    //키보드로부터 입력을 받음
                out.writeUTF(msg);                //서버로 전송
            }catch(Exception e) {}
        }
    }
}
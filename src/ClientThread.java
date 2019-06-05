import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;

public class ClientThread implements Runnable{
    DataOutputStream out;
    BufferedReader in2 = new BufferedReader(new InputStreamReader(System.in));
    
    String name;
    WhisperClient whisperClient;
    
    public ClientThread(DataOutputStream out, String name)
    {
        this.out = out;
        this.name = name;
    }
    
    public void run()
    {
        whisperClient = new WhisperClient("localhost", name);
        
        while(true)
        {      
            try
            {
                String msg = in2.readLine();    //Ű����κ��� �Է��� ����
                out.writeUTF(msg);                //������ ����
                System.out.println("Send");
            }catch(Exception e) {}
        }
    }
}
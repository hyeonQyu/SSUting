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
                String msg = in2.readLine();    //Ű����κ��� �Է��� ����
                out.writeUTF(msg);                //������ ����
            }catch(Exception e) {}
        }
    }
}
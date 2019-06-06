import java.rmi.Naming;

public class WhisperServer {
   
   public WhisperServer(String server) {
      try {
         // 1:1대화 객체 생성                                      
         Whisper whisper = new WhisperImpl();
         Naming.rebind("rmi://" + server + ":1099/WhisperService", whisper);
         System.out.println("rmi서버 만들어짐");
      } catch(Exception e) {
         System.out.print("Trouble: " + e);
      }
   }
   
}
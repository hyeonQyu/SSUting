import java.rmi.Naming;

public class WhisperServer {
   
   public WhisperServer(String server) {
      try {
         // 1:1��ȭ ��ü ����                                        Calculator c = new CalculatorImpl();
         Whisper whisper = new WhisperImpl();
         Naming.rebind("rmi://" + server + ":1099/WhisperService", whisper);
         System.out.println("rmi���� �������");
      } catch(Exception e) {
         System.out.print("Trouble: " + e);
      }
   }
   
}
package test;
import java.net.*;
import java.util.Random;
import java.io.*;
 
public class EchoClient {
       public static void run(){
             try{
                   
                    // 1. 서버의 IP와 서버의 동작 포트 값(10001)을 인자로 넣어 socket 생성

	          	   InetAddress ipaddress = InetAddress.getByName("localhost");
	          	   System.out.println(ipaddress+" 에 연결");
	          	   Socket sock = new Socket(ipaddress, 13461);
                    BufferedReader keyboard =
                           new BufferedReader(new InputStreamReader(System.in));
                   
                    // 2. 생성된 Socket으로부터 InputStream과 OutputStream을 구함
                    OutputStream out = sock.getOutputStream();
                    InputStream in = sock.getInputStream();
                   
                    // 3. InputStream은 BufferedReader 형식으로 변환
                    //    OutputStream은 PrintWriter 형식으로 변환
                    PrintWriter pw = new PrintWriter(new OutputStreamWriter(out));
                   
                    // 4. 키보드로부터 한 줄씩 입력받는 BufferedReader 객체 생성
                    BufferedReader br = new BufferedReader(new InputStreamReader(in));
                   
                    String line = null;

                    ReadLineThread rt;
                    rt = new ReadLineThread(br,sock);
                    Thread t = new Thread(rt);
                    t.start();
                    Random r = new Random();
                    pw.println("LOGIN:"+"[DUMMYID]"+r.nextInt(100)+":ASDF:ASDF");
                    pw.flush();
                    // 5. 키보드로부터 한 줄을 입력받음
                    while((line = keyboard.readLine()) != null){
                           if(line.equals("quit")) break;
                           if(t.getState()==Thread.State.WAITING) {
                        	   t.notify();
                        	   System.out.println(ipaddress+" 에 연결");
                        	   sock = new Socket(ipaddress, 13461);
                               out = sock.getOutputStream();
                               in = sock.getInputStream();
                               pw = new PrintWriter(new OutputStreamWriter(out));
                               br = new BufferedReader(new InputStreamReader(in));
                           }
                           // 6. PrintWriter에 있는 println() 메소드를 이용해 서버에게 전송
                           pw.println(line);
                           pw.flush();
                          
                           // 7. 서버가 다시 반환하는 문자열을 BufferedReader에 있는
                           //    readLine()을 이용해서 읽어들임
                    }
                    
                    t.wait();
                    pw.close();
                    br.close();
                    sock.close();
             }catch(Exception e){
            	 e.printStackTrace();
             }
       }
}
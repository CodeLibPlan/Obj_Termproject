
import java.util.Scanner;
import network_client.BackgroundListener;
import network_client.BackgroundSender;
import network_client.CallbackEvent;
import network_client.Connection;
import network_client.DummyListener;
import network_client.DummySender;
import network_client.LinkedQueue;
import network_client.NetworkListener;
import network_client.NetworkManager;
import network_client.NetworkSender;

public class Main_OOP_client {

	//static
	public static void main(String[] args) {
		Main_OOP_client main = new Main_OOP_client();
		main.run();
	}
	
	//non-static
	private boolean loginned;
	private Scanner scanner;
	
	
	public void run() {
		loginned = false;
		NetworkManager.setChatEvent(new ReceiveChatCallback());
		NetworkManager.setDrawEvent(new ReceiveDrawCallback());
		NetworkManager.setFileEvent(new ReceiveFileCallback());
		NetworkManager.setLoginEvent(new ReceiveLoginCallback());
		NetworkManager.setUserListEvent(new ReceiveUserListCallback());
		NetworkManager.init();
		scanner = new Scanner(System.in);
		
		auth();
		while(true) {
			NetworkManager.chat(scanner.nextLine());
			NetworkManager.draw(scanner.nextLine());
			NetworkManager.file(scanner.nextLine());
		}
	}
	public boolean auth() {
		scanner = new Scanner(System.in);
		while(!this.loginned) {
			NetworkManager.login(scanner.nextLine());
		}
		return true;
	}
	
	
	class ReceiveChatCallback implements CallbackEvent<String[]>{
		//CallbackEvent�� ����Ͽ�
		//run �޼��带 �������̵��ϸ� run�޼��� �ȿ� �ִ� �ڵ尡 �޽��� ���� �� �۵��Ѵ�.
		//������ ������� �۵��ϴ� �Ϻ� UI ���� �ڵ尡 �۵����� ����!
		@Override
		public void run(String[] input) {
			System.out.println(" chat arrived! : " + input[0] + " : "+input[1]);//Handler, Callback, Thread
		}
		
	}class ReceiveDrawCallback implements CallbackEvent<String[]>{
		//CallbackEvent�� ����Ͽ�
		//run �޼��带 �������̵��ϸ� run�޼��� �ȿ� �ִ� �ڵ尡 �޽��� ���� �� �۵��Ѵ�.
		//������ ������� �۵��ϴ� �Ϻ� UI ���� �ڵ尡 �۵����� ����!
		@Override
		public void run(String[] input) {
			System.out.println(" draw arrived! : " + input[0] + " : "+input[1]);//Handler, Callback, Thread
		}
		
	}class ReceiveFileCallback implements CallbackEvent<String[]>{
		//CallbackEvent�� ����Ͽ�
		//run �޼��带 �������̵��ϸ� run�޼��� �ȿ� �ִ� �ڵ尡 �޽��� ���� �� �۵��Ѵ�.
		//������ ������� �۵��ϴ� �Ϻ� UI ���� �ڵ尡 �۵����� ����!
		@Override
		public void run(String[] input) {
			System.out.println(" file arrived! : " + input[0] + " : "+input[1]);//Handler, Callback, Thread
		}
		
	}class ReceiveLoginCallback implements CallbackEvent<String[]>{
		//CallbackEvent�� ����Ͽ�
		//run �޼��带 �������̵��ϸ� run�޼��� �ȿ� �ִ� �ڵ尡 �޽��� ���� �� �۵��Ѵ�.
		//������ ������� �۵��ϴ� �Ϻ� UI ���� �ڵ尡 �۵����� ����!
		@Override
		public void run(String[] input) {
			System.out.println(" Login arrived! : " + input[0] + " : "+input[1]);//Handler, Callback, Thread
		}
		
	}class ReceiveUserListCallback implements CallbackEvent<String[]>{
		//CallbackEvent�� ����Ͽ�
		//run �޼��带 �������̵��ϸ� run�޼��� �ȿ� �ִ� �ڵ尡 �޽��� ���� �� �۵��Ѵ�.
		//������ ������� �۵��ϴ� �Ϻ� UI ���� �ڵ尡 �۵����� ����!
		@Override
		public void run(String[] input) {
			System.out.println(" userlist arrived! " + input[0] + " : "+input[1]);//Handler, Callback, Thread
			loginned = true;
		}
		
	}
}

package network_client;

import message.MsgLinker;

public abstract class NetworkManager {
	//private instance variables
	private static CallbackEvent<String[]> chatEvent = new DefaultCallback();
	private static CallbackEvent<String[]> drawEvent = new DefaultCallback();
	private static CallbackEvent<String[]> fileEvent = new DefaultCallback();
	private static CallbackEvent<String[]> loginEvent = new DefaultCallback();
	private static CallbackEvent<String[]> userListEvent = new DefaultCallback();

	private static Connection connection;
	private static BackgroundListener listener;
	private static BackgroundSender sender;

	// public Getter & Setter
	// if client catches message, it works with each callback
	public static void setChatEvent(CallbackEvent<String[]> newChatEvent) {//init message callback
		NetworkManager.chatEvent = newChatEvent;
	}
	public static void setDrawEvent(CallbackEvent newDrawEvent) {//init draw callback.
		NetworkManager.drawEvent = newDrawEvent;
	}
	public static void setFileEvent(CallbackEvent newFileEvent) {// init file callback.
		NetworkManager.fileEvent = newFileEvent;
	}
	public static void setLoginEvent(CallbackEvent newLoginEvent) {// init Login callback.
		NetworkManager.loginEvent = newLoginEvent;
	}
	public static void setUserListEvent(CallbackEvent newUserListEvent) {// init newUserList callback.
		NetworkManager.userListEvent = newUserListEvent;
	}
	//inner class
	private static class CallbackForThreeMessage implements CallbackEvent<String>{//NetworkListener에 삽입할 클래스
		
		@Override
		public void run(String input) {
			String[] chat = MsgLinker.msgRead(MsgLinker.MSGTOKEN, input);
			String[] draw = MsgLinker.msgRead(MsgLinker.DRAWTOKEN, input);
			String[] file = MsgLinker.msgRead(MsgLinker.FILETOKEN, input);
			String[] login = MsgLinker.msgRead(MsgLinker.LOGINTOKEN, input);
			String[] userlist = MsgLinker.msgRead(MsgLinker.USERLISTTOKEN, input);
			if(chat != null) {//on chat msg received
				NetworkManager.chatEvent.run(chat);
			}
			else if(draw != null) {//on draw msg received
				NetworkManager.drawEvent.run(draw);
			}
			else if(file != null) {//on file msg received
				/*StringBuffer sbuffer = new StringBuffer();
				String[] splitted = input.split(""+NetworkManager.FILETOKEN);
				for(int i = 1;i<splitted.length-1;i++) {
					sbuffer.append(splitted[i]);
					if(i+1<splitted.length-1) {
						break;
					}
					sbuffer.append(NetworkManager.FILETOKEN);
				}*/
				NetworkManager.fileEvent.run(file);
			}
			else if(login != null) {//on login msg received
				NetworkManager.loginEvent.run(login);
			}
			else if(userlist != null) {//on Loginned
				NetworkManager.userListEvent.run(userlist);
			}
		}
	}
	private static class DefaultCallback implements CallbackEvent<String[]>{//Callback Event를 따로 설정하지 않았을 때

		@Override
		public void run(String[] input) {
			System.out.println("기능이 설정되지 않았습니다. : "+input[1]);
		}
		
	}
	//private method
	public static void init() {//이 함수를 호출하여 연결을 시작
		NetworkManager.connection = new Connection("localhost",13461);//커넥션 객체. 서버 주소 및 포트를 넣는다. 지금은 가짜 주소
		NetworkManager.listener = new NetworkListener(connection, new CallbackForThreeMessage());//메시지 리스너 클래스에 넣는다.
		NetworkManager.sender = new NetworkSender(connection);
		listener.start();
	}
	public static void login(String authinfo) {//로그인 보내기 : 각 닉네임, 방이름, 방 비밀번호마다 콜론(:)으로 구분
		NetworkManager.sender.send(MsgLinker.msgBuild(MsgLinker.LOGINTOKEN, authinfo));
	}
	public static void chat(String chat) {//메시지 보내기 : 스트링만 넣어주면 알아서 처리
		NetworkManager.sender.send(MsgLinker.msgBuild(MsgLinker.MSGTOKEN, chat));
	}
	public static void draw(String draw) {//드로잉 데이터 보내기 : 스트링만 넣어주면 알아서 처리.
		NetworkManager.sender.send(MsgLinker.msgBuild(MsgLinker.DRAWTOKEN, draw));
	}
	public static void file(String file) {//파일 보내기 : 스트링만 넣어주면 알아서 처리.
		NetworkManager.sender.send(MsgLinker.msgBuild(MsgLinker.FILETOKEN, file));
	}
	public static void userList() {//유저 리스트 요청 : 이 함수를 호출하면 유저리스트를 불러옴.
		NetworkManager.sender.send(MsgLinker.msgBuild(MsgLinker.USERLISTTOKEN, ""));
	}
}

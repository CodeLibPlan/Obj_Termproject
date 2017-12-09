package org.androidtown.palette_sliding;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.net.Socket;

import network_client.CallbackEvent;
import network_client.NetworkManager;

/*
public class MainActivity extends AppCompatActivity {


    //non-static
    public boolean loginned;
    public String ID;
    public String RoomName;
    private EditText et;

    public static Handler mainHandler = null;
    public static LinearLayout addTBHere;
    public static MainActivity runningActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mainHandler = new Handler();
        addTBHere = (LinearLayout) findViewById(R.id.addheretextbox);
        runningActivity = this;

        NetworkManager.setLoginEvent(new ReceiveLoginCallback());
        NetworkManager.setUserListEvent(new ReceiveUserListCallback());
        NetworkManager.init();
        et = (EditText) findViewById(R.id.editText);
        Button bt = (Button) findViewById(R.id.button);
        bt.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view){
                if(UserInfoContainer.logined){
                    sendmsg();
                }
                else
                    auth();
            }
        });

    }
    public void auth() {
        if(!UserInfoContainer.logined) {
            Log.d("asdjfklsajgklsakdf" , this.et.getText().toString());
            NetworkManager.login(this.et.getText().toString());
            et.setText("");
        }
    }
    public void sendmsg(){
        NetworkManager.chat(this.et.getText().toString());
        NetworkManager.file(this.et.getText().toString());
        NetworkManager.draw(this.et.getText().toString());
    }
    public static class ReceiveLoginCallback implements CallbackEvent<String[]>{
        //CallbackEvent를 상속하여
        //run 메서드를 오버라이드하면 run메서드 안에 있는 코드가 메시지 수신 시 작동한다.
        //스레드 방식으로 작동하니 일부 UI 관련 코드가 작동하지 않음!
        @Override
        public void run(final String[] input) {
            Log.d(" Login arrived! : " , input[0] + " : "+input[1]);//Handler, Callback, Thread
            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    TextView tv = new TextView(MainActivity.runningActivity);
                    tv.setText(input[0] + " / " + input[1]);
                    addTBHere.addView(tv);
                }
            });
        }

    }public static class ReceiveUserListCallback implements CallbackEvent<String[]>{
        //CallbackEvent를 상속하여
        //run 메서드를 오버라이드하면 run메서드 안에 있는 코드가 메시지 수신 시 작동한다.
        //스레드 방식으로 작동하니 일부 UI 관련 코드가 작동하지 않음!
        @Override
        public void run(final String[] input) {
            Log.d(" userlist arrived! ",input[0] + " : " + input[1]);//Handler, Callback, Thread

            UserInfoContainer.logined = true;
            System.out.println("logined");
            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    TextView tv = new TextView(MainActivity.runningActivity);
                    tv.setText(input[0] + " / " + input[1]);
                    addTBHere.addView(tv);

                }
            });

        }

    }
}*/
public class MainActivity extends AppCompatActivity {

    public static Handler mHandler = null;
    public static String username;
    public boolean loginned;
    public String ID;
    public String RoomName;
    private Socket socket;
    String login[] = new String[3];
    Button log,bSuccess;
    EditText et_room, et_id, et_pw;
    String sR, sI, sP;
    public static MainActivity runningActivity;
    public MainActivity(){}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mHandler = new Handler();
        runningActivity = this;

        //  this.run();
        NetworkManager.setLoginEvent(new ReceiveLoginCallback());
        NetworkManager.setUserListEvent(new ReceiveUserListCallback());
        NetworkManager.init();

        et_room = (EditText) findViewById(R.id.etRoomName);
        et_id = (EditText) findViewById(R.id.etUserName);
        et_pw = (EditText) findViewById(R.id.etPassword);

        log = (Button) findViewById(R.id.bLogin);
        String room = et_room.toString();
        String id = et_id.toString();
        String pw = et_pw.toString();

        log.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
              /*  switch (view.getId()) {
                    case R.id.bLogin:
                        sR = et_room.getText().toString();
                        sI = et_id.getText().toString();
                        username = sI;
                        sP = et_pw.getText().toString();
                        for(int i=0; i<sR.length() || i<sI.length() || i<sP.length(); i++){
                            if(sR.charAt(i)==';'||sR.charAt(i)==','||sI.charAt(i)==';'||sI.charAt(i)==','||sP.charAt(i)==';'||sP.charAt(i)==','){
                                Toast.makeText(getApplicationContext(), "특수문자를 빼고 입력해주십시오.",Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                        //         NetworkManager.login(sI + ":" + sR + ":" + sP);
                        //         UserReceiveMessageCallback u_callback = new UserReceiveMessageCallback();
                        login[0] = sI;
                        login[1] = sR;
                        login[2] = sP;
//                        NetworkManager.userList();
//                        TextView temp = (TextView)findViewById(R.id.temp);
//                        temp.setText(login[0]+login[1]+login[2]);
//                        temp.setVisibility(View.GONE);





                }*/
                auth();
            }

        });

    }

    public void auth() {
        if(!UserInfoContainer.logined) {
            String room =  this.et_room.getText().toString();
            String name =  this.et_id.getText().toString();
            String pw =  this.et_pw.getText().toString();
            Log.d(this.getClass().getName(), "auth method start");
            NetworkManager.login(name+":"+room+":"+pw);//name:room:pw
            this.username = name;

        }
    }
    public void change(){
        Intent intent = new Intent(this, DrawActivity.class);
        startActivity(intent);
    }
    public void sendmsg(){
        NetworkManager.chat(this.et_room.getText().toString());
        NetworkManager.file(this.et_room.getText().toString());
        NetworkManager.draw(this.et_room.getText().toString());
    }
    public static class ReceiveLoginCallback implements CallbackEvent<String[]>{
        //CallbackEvent를 상속하여
        //run 메서드를 오버라이드하면 run메서드 안에 있는 코드가 메시지 수신 시 작동한다.
        //스레드 방식으로 작동하니 일부 UI 관련 코드가 작동하지 않음!
        @Override
        public void run(final String[] input) {
            Log.d(" Login arrived! : " , input[0] + " : "+input[1]);//Handler, Callback, Thread
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    TextView tv = new TextView(MainActivity.runningActivity);
                    tv.setText(input[0] + " / " + input[1]);

                    //    addTBHere.addView(tv);
                }
            });
        }

    }
    public static class ReceiveUserListCallback implements CallbackEvent<String[]> {
        //CallbackEvent를 상속하여
        //run 메서드를 오버라이드하면 run메서드 안에 있는 코드가 메시지 수신 시 작동한다.
        //스레드 방식으로 작동하니 일부 UI 관련 코드가 작동하지 않음!
        public ReceiveUserListCallback()
        {
        }//end C IntentHandler(C C)

        @Override
        public void run(final String[] input) {

            Log.d(" userlist arrived! ", input[0] + " : " + input[1]);//Handler, Callback, Thread

            UserInfoContainer.logined = true;
            System.out.println("logined");
            MainActivity.runningActivity.change();

            mHandler.post(new Runnable() {
//                @Override
                public void run() {

                }
            });

        }


//
//    public void run() {
//        ReceiveMessageCallback callback = new ReceiveMessageCallback();
//        NetworkManager.setLoginEvent(callback);
//        NetworkManager.init();
//        UserReceiveMessageCallback u_callback = new UserReceiveMessageCallback();
//        NetworkManager.setUserListEvent(u_callback);
//    }
//
//
//    class ReceiveMessageCallback implements CallbackEvent<String[]> {
//        //CallbackEvent를 상속하여
//        //run 메서드를 오버라이드하면 run메서드 안에 있는 코드가 메시지 수신 시 작동한다.
//        //스레드 방식으로 작동하니 일부 UI 관련 코드가 작동하지 않음!
//        @Override
//        public void run(String[] input) {
//            Log.d(this.getClass().getName()," message arrived! : "+input[0]);
//        }
//
//    }
//    class UserReceiveMessageCallback implements CallbackEvent<String[]> {
//        //CallbackEvent를 상속하여
//        //run 메서드를 오버라이드하면 run메서드 안에 있는 코드가 메시지 수신 시 작동한다.
//        //스레드 방식으로 작동하니 일부 UI 관련 코드가 작동하지 않음!
//        @Override
//        public void run(String[] input) {
//          //  Log.e("asdf","asdf");
//
//            Log.d(this.getClass().getName()," message arrived! : "+input[0]);
//
//
//        }
//    }
    }
}
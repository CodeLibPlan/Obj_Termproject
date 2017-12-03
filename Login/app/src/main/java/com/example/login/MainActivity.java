package com.example.login;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.net.Socket;

/**
 * Created by 전혜민 on 2017-11-26.
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Handler mHandler;

    private Socket socket;

    Button log;
    EditText et_room, et_id, et_pw;
    String sR, sI, sP;
    Connection connection = new Connection("newscrap.iptime.org", 18080);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mHandler = new Handler();

        et_room = (EditText) findViewById(R.id.etRoomName);
        et_id = (EditText) findViewById(R.id.etUserName);
        et_pw = (EditText) findViewById(R.id.etPassword);

        log = (Button) findViewById(R.id.bLogin);

        String room = et_room.toString();
        String id = et_id.toString();
        String pw = et_pw.toString();


        log.setOnClickListener(this);



    }

    public void run() {
        ReceiveMessageCallback callback = new ReceiveMessageCallback();//객체를 만들어서,
        BackgroundListener listener = new DummyListener(connection, callback);//메시지 리스너 클래스에 넣는다.

        BackgroundSender sender = new DummySender(connection);//메시지 보내는 클래스
        listener.start();//메시지 받기 시작
        callback.run(listener.toString());
        sender.send("message");//메시지 보내기

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bLogin:
                sR = et_room.getText().toString();
                sI = et_id.getText().toString();
                sP = et_pw.getText().toString();
                for(int i=0; i<sR.length() || i<sI.length() || i<sP.length(); i++){
                    if(sR.charAt(i)==';'||sR.charAt(i)==','||sI.charAt(i)==';'||sI.charAt(i)==','||sP.charAt(i)==';'||sP.charAt(i)==','){
                        Toast.makeText(getApplicationContext(), "특수문자를 빼고 입력해주십시오.",Toast.LENGTH_SHORT).show();
                        return;
                    }
                        }

                BackgroundSender bg_sender = new DummySender(connection);
                bg_sender.send("LOGIN:" + sI + ":" + sR + ":" + sP);
                ReceiveMessageCallback callback = new ReceiveMessageCallback();
                BackgroundListener listener = new DummyListener(connection, callback);
                listener.start();
                callback.run(listener.toString());



        }
    }

    class ReceiveMessageCallback implements CallbackEvent{
        //CallbackEvent를 상속하여
        //run 메서드를 오버라이드하면 run메서드 안에 있는 코드가 메시지 수신 시 작동한다.
        //스레드 방식으로 작동하니 일부 UI 관련 코드가 작동하지 않음!
        @Override
        public void run(String input) {
            //  Log.d(this.getClass().getName()," message arrived! : "+input);

        }

    }
}
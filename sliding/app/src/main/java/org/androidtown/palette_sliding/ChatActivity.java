package org.androidtown.palette_sliding;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.content.ContentValues.TAG;

/**
 * Created by chm31 on 2017-11-26.
 */

public class ChatActivity extends AppCompatActivity {
    Connection connection = new Connection("newscrap.iptime.org", 18080);//커넥션 객체. 서버 주소 및 포트를 넣는다. 지금은 가짜 주소
    ListView m_ListView;
    ChatAdapter m_Adapter;
    EditText etText;
    Button btnSend;
    TextView textView;
    long mNow;
    Date mDate;
    private String html = "";
    private int port = 18080; // PORT번호
    private Socket socket;
    private Handler mHandler;
    protected void onStop() {
        super.onStop();
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    BackgroundSender bg_sender = new DummySender(connection);
    SimpleDateFormat mFormat = new SimpleDateFormat("YYYY년 MM월 dd일 EEE ");
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        // 커스텀 어댑터 생성
        m_Adapter = new ChatAdapter();
        etText = (EditText) findViewById(R.id.editText1);
        btnSend = (Button) findViewById(R.id.button1);
        // Xml에서 추가한 ListView 연결
        m_ListView = (ListView) findViewById(R.id.listView1);
        textView = (TextView) findViewById(R.id.text);
        // ListView에 어댑터 연결
        m_ListView.setAdapter(m_Adapter);
        mHandler = new Handler();
        etText = (EditText) findViewById(R.id.editText1);

        btnSend.setOnClickListener(new Button.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onClick(View view) {
                String stText = etText.getText().toString();
                if (stText.equals("") || stText.isEmpty()) {
                    Toast.makeText(ChatActivity.this, "내용을 입력해 주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    run_send();
                }
            }
        });

        long now = System.currentTimeMillis();

        mDate = new Date(now);
        String date = null;
        String temp = date;
        date = getDate();
        m_Adapter.add(getDate(), 2);

        run_recv();
        ImageButton home_main = (ImageButton)findViewById(R.id.home_main);
        ImageButton chat_main = (ImageButton)findViewById(R.id.chat_main);
        ImageButton image_main = (ImageButton)findViewById(R.id.image_main);

        chat_main.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                startActivity(intent);
            }
        });
        image_main.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getApplicationContext(),PhotoActivity.class);
                startActivity(intent);

            }
        });
        home_main.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);

            }
        });
    }

    private String getDate() {
        mNow = System.currentTimeMillis();
        mDate = new Date(mNow);

        return mFormat.format(mDate);
    }

    private void refresh(String inputValue, int _str) {
        m_Adapter.add(inputValue, _str);
        m_Adapter.notifyDataSetChanged();
    }

    public void getChat() {//여기서 받아서`
        Log.d(TAG, "getChat() 호출됨");


    }
    public void run_recv() {
        ReceiveMessageCallback rc = new ReceiveMessageCallback();

        Connection connection = new Connection("newscrap.iptime.org",18080);//커넥션 객체. 서버 주소 및 포트를 넣는다. 지금은 가짜 주소
        ReceiveMessageCallback callback = new ReceiveMessageCallback();//객체를 만들어서,
        BackgroundListener listener = new DummyListener(connection, callback);//메시지 리스너 클래스에 넣는다.
        BackgroundSender sender = new DummySender(connection);//메시지 보내는 클래스
        listener.start();//메시지 받기 시작
        rc.run(listener.toString());
        sender.send("message");//메시지 보내기

    }
    public void run_send(){
        Connection connection = new Connection("newscrap.iptime.org",18080);//커넥션 객체. 서버 주소 및 포트를 넣는다. 지금은 가짜 주소
        ReceiveMessageCallback callback = new ReceiveMessageCallback();//객체를 만들어서,
        BackgroundSender sender = new DummySender(connection);//메시지 보내는 클래스
        etText = (EditText) findViewById(R.id.editText1);
        String str = etText.getText().toString();
        bg_sender.send("chat$#" + str);
        etText.setText("");
        refresh(str, 1);
    }

    class ReceiveMessageCallback implements CallbackEvent {
        //CallbackEvent를 상속하여
        //run 메서드를 오버라이드하면 run메서드 안에 있는 코드가 메시지 수신 시 작동한다.
        //스레드 방식으로 작동하니 일부 UI 관련 코드가 작동하지 않음!
        @Override
        public void run(String input) {
            Log.d(this.getClass().getName(), "message arrived!" + input);
            m_Adapter.add(input, 0);
        }

    }
}

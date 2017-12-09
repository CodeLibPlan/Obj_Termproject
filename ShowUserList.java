package org.androidtown.palette_sliding;

/**
 * Created by chm31 on 2017-12-09.
 */

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import network_client.CallbackEvent;
import network_client.NetworkManager;

import static android.content.ContentValues.TAG;

/**
 * Created by chm31 on 2017-11-26.
 */

public class ShowUserList extends AppCompatActivity {
    ListView m_ListView;
    UserAdapter m_Adapter;
    TextView textView;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_list);

        m_Adapter = new UserAdapter();
        m_ListView = (ListView) findViewById(R.id.listView1);
        textView = (TextView) findViewById(R.id.text);
        m_ListView.setAdapter(m_Adapter);
//        mHandler = new Handler();

        run_recv();
    }
    private void refresh(String inputValue, int _str) {
        m_Adapter.add(inputValue, _str);
        m_Adapter.notifyDataSetChanged();
    }

    public void getUser() {//여기서 받아서`
        Log.d(TAG, "getUser() 호출됨");


    }
    public void run_recv() {
        ReceiveMessageCallback callback = new ReceiveMessageCallback();//객체를 만들어서,
        NetworkManager.setChatEvent(callback);
    }


    class ReceiveMessageCallback implements CallbackEvent<String[]> {
        //CallbackEvent를 상속하여
        //run 메서드를 오버라이드하면 run메서드 안에 있는 코드가 메시지 수신 시 작동한다.
        //스레드 방식으로 작동하니 일부 UI 관련 코드가 작동하지 않음!
        @Override
        public void run(String[] input) {
            Log.d(this.getClass().getName(), "userlist arrived!" + input);
            m_Adapter.add(input[1], 0);
            refresh(input[1],0);
        }

    }
}

package org.androidtown.palette_sliding;

import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import network_client.CallbackEvent;
import network_client.NetworkManager;

public class DrawActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,ColorPickerDialog.OnColorChangedListener{
    private BackPressCloseHandler backPressCloseHandler;
    public static final boolean D = true; // 디버그 모드
    // private static final String TAG = "print2Activity"; // 로그 남기기위한 선언
    private static final int COLOR_MENU_ID = Menu.FIRST; // 첫번째 메뉴
    private static final int EMBOSS_MENU_ID = Menu.FIRST + 1; // 두번째 메뉴
    private static final int BLUR_MENU_ID = Menu.FIRST + 2; // 세번째 메뉴
    private static final int ERASE_MENU_ID = Menu.FIRST + 3; // 네번째 메뉴
    //private static final int SRCATOP_MENU_ID = Menu.FIRST + 4; // 다섯번째 메뉴
    private static final int ALL_ERASE_NENU_ID = Menu.FIRST + 4;
    private signView sV; // 그려질 signView뷰영역
    ListView m_ListView;
    UserAdapter m_user_Adapter;
    TextView u_text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sV = (signView) findViewById(R.id.signView);

        backPressCloseHandler = new BackPressCloseHandler(this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ImageButton home_main = (ImageButton)findViewById(R.id.home_main);
        ImageButton chat_main = (ImageButton)findViewById(R.id.chat_main);
        ImageButton image_main = (ImageButton)findViewById(R.id.image_main);

        chat_main.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
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
                Intent intent = new Intent(getApplicationContext(),DrawActivity.class);
                startActivity(intent);

            }
        });
    }

    private void saveView(View view) {

        Bitmap b = Bitmap.createBitmap(view.getWidth(), view.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        view.draw(c);
        FileOutputStream fos = null;

        // 날짜 포맷
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMddHHmmss");
        Date date = new Date(System.currentTimeMillis());

        // 파일 이름 형식 지정
        String name = "gapps_" + dateFormat.format(date) + ".jpg";

        // 저장위치는 /mnt/sdcard/Android/data/com.glbapps.paint2/file
        File imgFile = new File(getExternalFilesDir(null), name);
        try {
            fos = new FileOutputStream(imgFile);
            if (fos != null) {
                b.compress(Bitmap.CompressFormat.JPEG, 85, fos);// 압축률85%
                fos.close();
            }
        } catch (Exception e) {
            if (D)
                Log.e(getClass().getSimpleName(), "Exception: " + e.toString());
        }

        if (D)
            Log.e(getClass().getSimpleName(), "저장" + name);
        //Toast.makeText(this, "저장완료:" + name, 0);
        Toast.makeText(this, "저장완료:" + name, Toast.LENGTH_SHORT);// 0: LENGTH_SHORT
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);

        } else {
            super.onBackPressed();
            backPressCloseHandler.onBackPressed();

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {super.onCreateOptionsMenu(menu);

        menu.add(0, COLOR_MENU_ID, 0, "색상").setShortcut('3', 'c');
        menu.add(0, EMBOSS_MENU_ID, 0, "파스칼").setShortcut('4', 's');
        menu.add(0, BLUR_MENU_ID, 0, "흐림").setShortcut('5', 'z');
        menu.add(0, ERASE_MENU_ID, 0, "지우개").setShortcut('5', 'z');
        menu.add(0, ALL_ERASE_NENU_ID, 0, "모두지우기").setShortcut('5', 'z');

        /****
         * Is this the mechanism to extend with filter effects? Intent intent =
         * new Intent(null, getIntent().getData());
         * intent.addCategory(Intent.CATEGORY_ALTERNATIVE);
         * menu.addIntentOptions( Menu.ALTERNATIVE, 0, new ComponentName(this,
         * NotesList.class), null, intent, 0, null);
         *****/
        return true;

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
            Log.d(this.getClass().getName(), "message arrived!" + input);
            m_user_Adapter.add(input[1], 0);
            refresh(input[1],0);
        }

    }
    private void refresh(String inputValue, int _str) {
        m_user_Adapter.add(inputValue, _str);
        m_user_Adapter.notifyDataSetChanged();
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        //    setContentView(R.layout.nav_header_main);
    /*    NetworkManager.init();
        run_recv();
            // 커스텀 어댑터 생성
        m_user_Adapter = new UserAdapter();
            // Xml에서 추가한 ListView 연결
        m_ListView = (ListView) findViewById(R.id.listView1);
        u_text = (TextView) findViewById(R.id.text);
            // ListView에 어댑터 연결
        menu.add((CharSequence) u_text.toString());
        m_ListView.setAdapter(m_user_Adapter);

//        mHandler = new Handler();
*/

   return true;
    }
    public void colorChanged(int color) {
        sV.colorChanged(color);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        sV.mPaint.setStrokeWidth(2); // 굵기 초기화
        sV.mPaint.setXfermode(null);// 지우개 초기화
        sV.mPaint.setAlpha(0xFF);

        switch (item.getItemId()) {
            case COLOR_MENU_ID:
                new ColorPickerDialog(this, this, sV.mPaint.getColor()).show();
                return true;
            case EMBOSS_MENU_ID: // 토글
                if (sV.mPaint.getMaskFilter() != sV.mEmboss) {
                    sV.mPaint.setMaskFilter(sV.mEmboss);
                } else {
                    sV.mPaint.setMaskFilter(null); // 없앰
                }
                return true;
            case BLUR_MENU_ID: // 토글
                if (sV.mPaint.getMaskFilter() != sV.mBlur) {
                    sV.mPaint.setMaskFilter(sV.mBlur);
                } else {
                    sV.mPaint.setMaskFilter(null); // 없앰
                }
                return true;
            case ERASE_MENU_ID:
                sV.mPaint.setStrokeWidth(12); // 굵기 변경
                // 사실 지우개라기보단. 마스크 처럼 선위에 투명선을 그리는것과 같다.
                sV.mPaint
                        .setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));// 지우개
                return true;
            case ALL_ERASE_NENU_ID:

                sV.mPaint.setStrokeWidth(2000);
                sV.mPaint
                        .setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));// 지우개
                return true;


            /*case SRCATOP_MENU_ID:
                sV.mPaint.setXfermode(new PorterDuffXfermode(
                        PorterDuff.Mode.SRC_ATOP));// 겹쳐그리기
                sV.mPaint.setAlpha(0x80);
                return true;*/
        }
        return super.onOptionsItemSelected(item);

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentManager manager = getFragmentManager();
        if(id == R.id.show_list){

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }




}

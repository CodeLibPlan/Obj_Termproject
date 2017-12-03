package drawpaper.example.com.paint;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Date;
import java.text.SimpleDateFormat;

public class Paint2Activity extends AppCompatActivity implements
    ColorPickerDialog.OnColorChangedListener, View.OnClickListener{

    /** Called when the activity is first created. */
    public static final boolean D = true; // 디버그 모드
    // private static final String TAG = "print2Activity"; // 로그 남기기위한 선언
    private static final int COLOR_MENU_ID = Menu.FIRST; // 첫번째 메뉴
    private static final int EMBOSS_MENU_ID = Menu.FIRST + 1; // 두번째 메뉴
    private static final int BLUR_MENU_ID = Menu.FIRST + 2; // 세번째 메뉴
    private static final int ERASE_MENU_ID = Menu.FIRST + 3; // 네번째 메뉴
    //private static final int SRCATOP_MENU_ID = Menu.FIRST + 4; // 다섯번째 메뉴
    private static final int ALL_ERASE_NENU_ID = Menu.FIRST + 4;
    private signView sV; // 그려질 signView뷰영역
    signView canvas;// ;
    private Button btn_save; // 저장 버튼

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        sV = (signView) findViewById(R.id.signView);
        btn_save = (Button) findViewById(R.id.btn_save);
        // Button -> btn_save -> Activity -> implements OnClickListener -> this
        // -> view
        btn_save.setOnClickListener(this);
    }

    // OnClickListener-> this -> view
    public void onClick(View v) {
        // 모든 클릭이벤트 처리
        switch (v.getId()) {
            case R.id.btn_save: // 저장버튼
                if (sV != null)
                    saveView(sV);

            default:
        }


    }

    // 저장 루틴
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

    // 메뉴 생성
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

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
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        return true;
    }

    // 메뉴 아이템 선택시
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
    // 색상 변경
    public void colorChanged(int color) {
        sV.colorChanged(color);
    }






}

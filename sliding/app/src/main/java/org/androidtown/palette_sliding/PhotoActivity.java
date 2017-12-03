package org.androidtown.palette_sliding;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.Socket;

/**
 * Created by chm31 on 2017-11-26.
 */

public class PhotoActivity  extends AppCompatActivity implements View.OnClickListener {
    Connection connection = new Connection("newscrap.iptime.org",18080);//커넥션 객체. 서버 주소 및 포트를 넣는다. 지금은 가짜 주소


    private String html = "";
    private Handler mHandler;

    private Socket socket;

    private static final int RESULT_LOAD_IMAGE = 1;

    ImageView imageToUpload, downloadedImage;
    Button bUploadImage, bDownloadImage;
    EditText uploadImageName, downloadImageName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        mHandler = new Handler();

        imageToUpload = (ImageView) findViewById(R.id.imageToUpload);
        downloadedImage = (ImageView) findViewById(R.id.downloadedImage);

        bUploadImage = (Button) findViewById(R.id.bUploadImage);
        bDownloadImage = (Button) findViewById(R.id.bDownloadImage);

        uploadImageName = (EditText) findViewById(R.id.etUploadName);
        downloadImageName = (EditText) findViewById(R.id.etDownloadName);

        imageToUpload.setOnClickListener(this);
        bUploadImage.setOnClickListener(this);
        bDownloadImage.setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case  R.id.imageToUpload:
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent,RESULT_LOAD_IMAGE);
                break;
            case R.id.bUploadImage:
                Bitmap image = ((BitmapDrawable)imageToUpload.getDrawable()).getBitmap();
                new UploadImage(image, uploadImageName.getText().toString()).execute();
                break;
            case R.id.bDownloadImage:
                new DownloadImage(downloadImageName.getText().toString()).execute();

                break;
        }
    }
    private class UploadImage extends AsyncTask<Void, Void, Void> {
        Bitmap image;
        String name;

        public UploadImage(Bitmap image, String name){
            this.image = image;
            this.name = name;
        }
        protected void onPostExecute(Void aVoid){
            super.onPostExecute(aVoid);
            Toast.makeText(getApplicationContext(), "Image Uploaded", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
            String encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(),Base64.DEFAULT);

            BackgroundSender bg_sender = new DummySender(connection);
            bg_sender.send(encodedImage);
            run();

            return null;
        }
    }
    //@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                Uri selectedImage = data.getData();
                imageToUpload.setImageURI(selectedImage);
                Toast.makeText(getApplicationContext(), "Picture selected",Toast.LENGTH_SHORT).show();
            }

        }

    }
    private class DownloadImage extends AsyncTask<Void, Void, Bitmap> {
        String name;

        public DownloadImage(String name) {
            this.name = name;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (bitmap != null) {
                downloadedImage.setImageBitmap(bitmap);
                String path = "sdcard/DICM/" + ".png";
                try {
                    FileOutputStream stream = new FileOutputStream(path);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                Toast.makeText(getApplicationContext(), "Image Downloaded", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            class ReceiveMessageCallbackFile implements CallbackEvent {
                String d_image;

                ReceiveMessageCallbackFile() {
                }

                @Override
                public void run(String input) {
                    String token = "image:%^[*]^[*]^";
                    d_image = input.split(token)[1];

                }
            }

            Bitmap imgBitmap = null;
            BufferedInputStream bis = null;
            HttpURLConnection connection = null;
            ReceiveMessageCallbackFile rc = new ReceiveMessageCallbackFile();
            try {
                int nSize = rc.d_image.length();
                Bitmap saveImg = BitmapFactory.decodeByteArray(rc.d_image.getBytes(), 0, rc.d_image.length());
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (bis != null) {
                    try {
                        bis.close();
                    } catch (IOException e) {

                    }
                }
                if (connection != null) {
                    connection.disconnect();
                }
            }
            return imgBitmap;
        }
    }
        public void run() {
        ReceiveMessageCallback callback = new ReceiveMessageCallback();//객체를 만들어서,
        BackgroundListener listener = new DummyListener(connection, callback);//메시지 리스너 클래스에 넣는다.

        BackgroundSender sender = new DummySender(connection);//메시지 보내는 클래스
        listener.start();//메시지 받기 시작
        callback.run(listener.toString());
        sender.send("message");//메시지 보내기

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




package org.androidtown.palette_sliding;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import message.MsgLinker;
import network_client.CallbackEvent;
import network_client.NetworkManager;

/**
 * Created by chm31 on 2017-11-26.
 */

public class PhotoActivity  extends AppCompatActivity implements View.OnClickListener {

    private static final int RESULT_LOAD_IMAGE = 1;
    public String encodedImage;
    Bitmap saveImg;
    ImageView imageToUpload, downloadedImage;
    Button bUploadImage, bDownloadImage;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        NetworkManager.init();
        CallbackForThreeMessage callback = new CallbackForThreeMessage();
        NetworkManager.setDrawEvent(callback);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        imageToUpload = (ImageView) findViewById(R.id.imageToUpload);
        downloadedImage = (ImageView) findViewById(R.id.downloadedImage);

        bUploadImage = (Button) findViewById(R.id.bUploadImage);
        bDownloadImage = (Button) findViewById(R.id.bDownloadImage);

        imageToUpload.setOnClickListener(this);
        bUploadImage.setOnClickListener(this);
        bDownloadImage.setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case  R.id.imageToUpload:
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent,RESULT_LOAD_IMAGE);
                break;
            case R.id.bUploadImage:
                Bitmap image = ((BitmapDrawable)imageToUpload.getDrawable()).getBitmap();
                new UploadImage(image).execute();
                downloadedImage.setImageBitmap(saveImg);
                break;
            case R.id.bDownloadImage:
                new DownloadImage().execute();
                break;
        }
    }
    private class UploadImage extends AsyncTask<Void, Void, Void> {
        Bitmap image;


        public UploadImage(Bitmap image){
            this.image = image;
        }
        protected void onPostExecute(Void aVoid){
            super.onPostExecute(aVoid);
            Toast.makeText(getApplicationContext(), "Image Uploaded", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
            encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(),Base64.DEFAULT);


            /*
            ReceiveMessageCallback callback = new ReceiveMessageCallback();
            BackgroundListener listener = new NetworkListener(connection,callback);
            BackgroundSender bg_sender = new NetworkSender(connection);
            bg_sender.send(encodedImage);

            listener.start();
            callback.run(listener.toString());
            */
            NetworkManager.file(encodedImage);
            return null;
        }
    }
    private class DownloadImage extends AsyncTask<Void, Void, Bitmap> {

        public DownloadImage() {

        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (bitmap != null) {

                String path = "./"+".png";
                try {
                    FileOutputStream stream = new FileOutputStream(path);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream);
                    sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_FINISHED+Uri.parse("file://"+stream)));
                    Toast.makeText(getApplicationContext(), "Image Downloaded", Toast.LENGTH_SHORT).show();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            }
        }

        @Override
        protected Bitmap doInBackground(Void... params) {

            /*Bitmap imgBitmap = null;
            BufferedInputStream bis = null;

            try {
                imgBitmap = saveImg;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (bis != null) {
                    try {
                        bis.close();
                    } catch (IOException e) {

                    }
                }
            }*/
            return saveImg;
        }
    }

    private class CallbackForThreeMessage implements CallbackEvent<String[]> {//NetworkListener???쎌엯???대옒??

        @Override
        public void run(String[] input) {
            String[] file = MsgLinker.msgRead(MsgLinker.FILETOKEN, input[1]);

            if(file != null) {//on file msg received
            /*StringBuffer sbuffer = new StringBuffer();
            String[] splitted = input.split(""+NetworkManager.FILETOKEN);
            for(int i = 1;i<splitted.length-1;i++) {
               sbuffer.append(splitted[i]);
               if(i+1<splitted.length-1) {
                  break;
               }
               sbuffer.append(NetworkManager.FILETOKEN);
            }*/
                byte[] decoded = Base64.decode(input[1],Base64.NO_WRAP);
                saveImg = BitmapFactory.decodeByteArray(decoded, 0, decoded.length);
            }
        }
    }
}


package com.example.mario.xiaoyou;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Personname extends Activity {

    private TextView textView;
    private Button button;
    private ImageView imageView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personname);
        init();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        final Think ne = new Think();
        int a = ne.geta();
        if (a != 1){
            Intent intent = new Intent(Personname.this,UnPersonname.class);
            startActivity(intent);
        }
        SharedPreferences pref = getSharedPreferences("name",MODE_PRIVATE);
        String name = pref.getString("name","");
        textView.setText(name);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ne.a(-1);
                finish();
            }
        });

        Savebitmap savebitmap = new Savebitmap();
        Bitmap bitmap = savebitmap.getBitmap();
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        } else {
            new Jsonpost().execute(name);
        }
    }

    public class Jsonpost extends AsyncTask<String ,Void ,String> {
        @Override
        protected String doInBackground(String... params) {

            String result = "";
            String a ="";
            HttpURLConnection urlConnection;

            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("name",params[0]);
                a = jsonObject.toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {

                URL url = new URL("http://40.84.62.67:80/touxiang1");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setUseCaches(false);
                urlConnection.setInstanceFollowRedirects(true);
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.connect();

                DataOutputStream dataOutputStream = new DataOutputStream(urlConnection.getOutputStream());
                byte[] content = a.getBytes("utf-8");
                dataOutputStream.write(content,0,content.length);
                dataOutputStream.flush();
                dataOutputStream.close();

                if(urlConnection.getResponseCode()==HttpURLConnection.HTTP_OK){
                    InputStream inputStream = urlConnection.getInputStream();
                    BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                    String str;
                    StringBuilder buffer = new StringBuilder();
                    while((str = br.readLine()) != null){
                        buffer.append(str);
                    }
                    inputStream.close();
                    br.close();
                    result = buffer.toString();
                }

                urlConnection.getInputStream();
                urlConnection.disconnect();

            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            if (s != null) {
                Bitmap bitmap = null;
                try {
                    byte[] bitmapArray;
                    bitmapArray = Base64.decode(s, Base64.DEFAULT);
                    bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0,
                            bitmapArray.length);

                    Savebitmap savebitmap = new Savebitmap();
                    savebitmap.setBitmap(bitmap);
                    imageView.setImageBitmap(bitmap);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void load(View v){
        startActivity(new Intent(Personname.this,SelectPicPopupWindow.class));
    }

    private void init() {
        textView = (TextView) findViewById(R.id.name);
        button = (Button) findViewById(R.id.personnamequit);
        imageView = (ImageView) findViewById(R.id.touxiang);
    }
}

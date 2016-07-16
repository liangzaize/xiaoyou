package com.example.mario.xiaoyou;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class UnPersonname extends AppCompatActivity {

    private EditText user,passwd;
    private Button a1,a2;
    private String result;
    private TextView t;

    private class Jsonpost extends AsyncTask<String ,Void ,String>{

        @Override
        protected String doInBackground(String... params){
            HttpURLConnection urlConnection;
            URL url;
            try {
                url = new URL("http://40.84.62.67:80/zhuce");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setUseCaches(false);
                urlConnection.setInstanceFollowRedirects(true);
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.connect();

                DataOutputStream dataOutputStream = new DataOutputStream(urlConnection.getOutputStream());
                byte[] content = params[0].getBytes("utf-8");
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
                    Log.d("======",result);
                }

                urlConnection.getInputStream();
                urlConnection.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            } return result;
        }

        @Override
        protected void onPostExecute(String result){

            if(result != null){
            String port = "";
            String returnname = "";

                try{
                    JSONObject parentjson = new JSONObject(result);
                    port = parentjson.getString("Port");
                    returnname = parentjson.getString("Returnname");
                } catch (Exception e){
                    e.printStackTrace();
                }

            if (port.equals("yes")) {
                Think add = new Think();
                add.a(1);
                SharedPreferences.Editor editor = getSharedPreferences("name",MODE_PRIVATE).edit();
                editor.putString("name",returnname);
                editor.apply();

                new Jsonpost1().execute(returnname);

                Toast.makeText(UnPersonname.this,"登陆成功",Toast.LENGTH_SHORT).show();
                finish();
                overridePendingTransition(R.anim.in_from_middle, R.anim.out_to_out);
            }
            else if(result.equals("error")){
                t.setText("账号/密码错误");
            }
            else if(result.equals("error1")){
                t.setText("该账号已注册");
            }
            }
            else {
                t.setText("请检查网络连接");
            }
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.unpersonname);
        init();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        //注册
        a1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            String mess = petToJson("post");
                String a1 = user.getText().toString();
                if(a1.length()<6){
                    t.setText("账号长度太短");
                }else {
                    new Jsonpost().execute(mess);
                }

            }
    });
        //登录
        a2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            String mess = petToJson("get");
                new Jsonpost().execute(mess);

            }
        });
}
    @Override
    public void onBackPressed() {
// 这里处理逻辑代码，大家注意：该方法仅适用于2.0或更新版的sdk
        finish();
        overridePendingTransition(R.anim.in_from_middle, R.anim.out_to_out);
    }

    private String petToJson(String post) {
        String JsonResult = "";
        String a1 = user.getText().toString();
        String a2 = passwd.getText().toString();

            try {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("username", a1);
                jsonObj.put("password", a2);
                jsonObj.put("post", post);
                JsonResult = jsonObj.toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return JsonResult;
    }

    private class Jsonpost1 extends AsyncTask<String ,Void ,String> {

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
                    bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
                    Savebitmap savebitmap = new Savebitmap();
                    savebitmap.setBitmap(bitmap);
//                    Intent intent = new Intent(action);
//                    intent.putExtra("data", bitmap);
//                    sendBroadcast(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            super.onPostExecute(s);
        }
    }

    private void init() {
        user = (EditText) findViewById(R.id.editText);
        passwd = (EditText) findViewById(R.id.editText2);
        a1 = (Button) findViewById(R.id.button);
        a2 = (Button) findViewById(R.id.button2);
        t = (TextView) findViewById(R.id.tites);
    }
}



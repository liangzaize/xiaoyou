package com.example.mario.xiaoyou;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;

public class Person extends Activity {
    private EditText editText1, editText2;
    private Button button;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.person_layour);
        init();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String send = dataToJson();
                new Getsend().execute(send);

            }
        });
    }

    private String dataToJson() {
        String result = "";
        SimpleDateFormat formatter = new SimpleDateFormat ("yyyyMMddHHmmss");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String str = formatter.format(curDate);
        String title = editText1.getText().toString();
        String maint = editText2.getText().toString();
        SharedPreferences pref = getSharedPreferences("name", MODE_PRIVATE);
        String name = pref.getString("name", "");

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("title", title);
            jsonObject.put("maint", maint);
            jsonObject.put("name", name);
            jsonObject.put("time",str);
            result = jsonObject.toString();

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    private class Getsend extends AsyncTask<String, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            Toast.makeText(Person.this, "正在分享", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Boolean doInBackground(String... params) {

            try {
                URL url = new URL("http://40.84.62.67:80/login");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setUseCaches(false);
                httpURLConnection.setInstanceFollowRedirects(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setRequestProperty("Content-Type", "application/json");
                httpURLConnection.connect();

                DataOutputStream dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(dataOutputStream));
                bw.write(params[0]);
                bw.flush();
                httpURLConnection.getInputStream();
                dataOutputStream.close();
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (aBoolean) {
                Toast.makeText(Person.this, "分享成功", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(Person.this, "连接服务器失败，请检查网络", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void init() {
        editText1 = (EditText) findViewById(R.id.submittitle);
        editText2 = (EditText) findViewById(R.id.submitmain);
        button = (Button) findViewById(R.id.submit);
    }
}
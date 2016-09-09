package com.example.mario.xiaoyou;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class Reply extends AppCompatActivity {

    private TextView name,title,speak;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reply_layout);
        init();
        Intent intent = getIntent();
        String forname = intent.getStringExtra("name");
        String fortitle = intent.getStringExtra("title");
        String fortime = intent.getStringExtra("time");
        name.setText(forname);
        title.setText(fortitle);
        new JsonTask().execute(forname,fortime);

    }

    private class JsonTask extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... params) {

            String finalJson="";
            try {
                URL url = new URL("http://40.84.62.67:80/replyofwenzhang");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setConnectTimeout(3000);
                connection.setDoOutput(true);
                connection.setDoInput(true);
                connection.setRequestMethod("POST");
                connection.setUseCaches(false);
                connection.setInstanceFollowRedirects(true);
                connection.setRequestProperty("Content-type", "application/json");
                connection.connect();

                DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(dataOutputStream));

                JSONObject jsonObject = new JSONObject();

                try {
                    jsonObject.put("name", params[0]);
                    jsonObject.put("time", params[1]);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String result = jsonObject.toString();
                bw.write(result);
                bw.flush();
                connection.getInputStream();
                dataOutputStream.close();
                bw.close();

                InputStream stream = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
                StringBuilder buffer = new StringBuilder();
                String line = "";
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }
                finalJson = buffer.toString();

            } catch (IOException e) {
                e.printStackTrace();
            }

            return finalJson;
        }

        @Override
        protected void onPostExecute(String data) {
            if (data != null) {
                speak.setText(data);
            }
        }
    }
    private void init(){
        name = (TextView)findViewById(R.id.replyname);
        title = (TextView)findViewById(R.id.replytitle);
        speak = (TextView)findViewById(R.id.replywenzhang);
    }
}

package com.example.mario.xiaoyou;

import android.content.Context;
import android.content.Intent;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

public class AndroidJSInterface
{
        private Context context;

        public AndroidJSInterface(Context context) {
            this.context = context;
        }
        @JavascriptInterface
        public void goActivity() {
            context.startActivity(new Intent(context, MainActivity.class));
            Toast.makeText(context, "发帖成功，请刷新", Toast.LENGTH_SHORT).show();
        }
}

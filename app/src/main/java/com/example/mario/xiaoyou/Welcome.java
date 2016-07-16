package com.example.mario.xiaoyou;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.os.Handler;

public class Welcome extends Activity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.welcome);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                Intent intent = new Intent(Welcome.this,MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.in_from_out, R.anim.out_to_middle);
                finish();
            }
        }, 1000);
   }
}


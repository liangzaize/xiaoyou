package com.example.mario.xiaoyou;

import android.graphics.Bitmap;

public class Savebitmap {

    public static Bitmap bitmap = null;

    public void setBitmap(Bitmap a){
        bitmap = a;
    }

    public Bitmap getBitmap(){
        return bitmap;
    }
}

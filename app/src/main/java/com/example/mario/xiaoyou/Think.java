package com.example.mario.xiaoyou;

import android.util.Log;

public class Think {
    public static int login = 0;

    public int a (int change) {
//0是未登录，1是已登录
        login += change;
        Log.d("-----------",Integer.toString(login));
        return login ;
    }

    public int geta(){
        return login;
    }
}

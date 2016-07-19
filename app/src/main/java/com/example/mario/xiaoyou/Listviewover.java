package com.example.mario.xiaoyou;

import android.graphics.Bitmap;

public class Listviewover {

    public Listviewover(String name, String title, Bitmap imag){
        this.name = name;
        this.title = title;
        this.imag = imag;

    }
    private Bitmap imag;  //头像
    private String name;//发表者名字
    private String title;//标题

    public Bitmap getImag() {
        return imag;
    }

    public void setImag(Bitmap imag) {
        this.imag = imag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
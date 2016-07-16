package com.example.mario.xiaoyou;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class MyAdapter extends BaseAdapter {

    private List<Listviewover> mData;//定义数据。
    private LayoutInflater mInflater;//定义Inflater,加载我们自定义的布局。

    public MyAdapter(LayoutInflater inflater,List<Listviewover> data){
        mInflater = inflater;
        mData = data;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertview, ViewGroup viewGroup) {
        //获得ListView中的view
        View listviewover = mInflater.inflate(R.layout.listviewover,null);
        //获得学生对象
        Listviewover student = mData.get(position);
        //获得自定义布局中每一个控件的对象。
        ImageView imagePhoto = (ImageView) listviewover.findViewById(R.id.image_photo);
        TextView name = (TextView) listviewover.findViewById(R.id.textview_name);
        TextView title = (TextView) listviewover.findViewById(R.id.textview_hobby);
        //将数据一一添加到自定义的布局中。
        if(student.getImag() != null) {
            imagePhoto.setImageBitmap(student.getImag());
        }else{
            imagePhoto.setImageResource(R.drawable.go);
        }
        name.setText(student.getName());
        title.setText(student.getTitle());
        return listviewover ;
    }
}
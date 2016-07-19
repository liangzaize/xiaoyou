package com.example.mario.xiaoyou;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

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
        //创建ViewHolder的对象。
        ViewHolder viewHolder = null;
        //获得Item位置上的数据。
        Listviewover student = mData.get(position);
        //convertview 优化
        if(convertview == null){
            convertview = mInflater.inflate(R.layout.listviewover,null);
            viewHolder = new ViewHolder();
            viewHolder.imagePhoto = (ImageView) convertview.findViewById(R.id.image_photo);
            viewHolder.name = (TextView) convertview.findViewById(R.id.textview_name);
            viewHolder.title = (TextView) convertview.findViewById(R.id.textview_hobby);
            //convertview为空时，ViewHolder将显示在ListView中的数据通过findViewById获取到。
            convertview.setTag(viewHolder);
        }else{
        //convertview不为空时，直接获取ViewHolder的Tag即可。
        viewHolder = (ViewHolder) convertview.getTag();
        }
        if(student.getImag() != null) {
            viewHolder.imagePhoto.setImageBitmap(student.getImag());
        }else{
            viewHolder.imagePhoto.setImageResource(R.drawable.go);
        }
        viewHolder.name.setText(student.getName());
        viewHolder.title.setText(student.getTitle());

        return convertview ;
    }

    /*
     *ViewHolder内部类
     */
    class ViewHolder{
        TextView name;
        TextView title;
        ImageView imagePhoto;
    }
}
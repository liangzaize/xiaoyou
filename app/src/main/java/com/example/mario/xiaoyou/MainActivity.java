package com.example.mario.xiaoyou;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, AbsListView.OnScrollListener {

    private View leftButton, upview,moreView,header;
    private ImageView imageView, menuButton;
    private AnimationDrawable animationDrawable;
    private ListView listView;
    private SwipeRefreshLayout swipeLayout;
    private List<Listviewover> amData;
    private TextView tx, bt;
    int numtorefresh = 1,touchSlop = 10,MaxNum,statusBarHeight,lastVisibleIndex;
    private AnimatorSet backAnimatorSet, hideAnimatorSet;
    private String biaozhi;
    private MyAdapter adapter;
    private String[] sArray1 , sArray , sArray3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MaxNum = 10;
        moreView = getLayoutInflater().inflate(R.layout.moredata, null);
        init();
        tx.setVisibility(View.GONE);
        amData = new ArrayList<>();
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        upview.measure(w, h);
        int height = upview.getMeasuredHeight();

        touchSlop = (int) (ViewConfiguration.get(MainActivity.this).getScaledTouchSlop() * 0.9);
        //为ListView添加一个Header，这个Header与ToolBar一样高。这样我们可以正确的看到列表中的第一个元素而不被遮住。
        header = new View(MainActivity.this);
        header.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height));
        header.setBackgroundColor(Color.parseColor("#00000000"));
        listView.addHeaderView(header);
        listView.setOnTouchListener(onTouchListener);
        listView.setOnScrollListener(onScrollListener);
        listView.setOnItemClickListener(this);
        listView.setOnScrollListener(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        new JSONTask().execute(numtorefresh, 2);
        imageView.setImageResource(R.drawable.animationlist);
        animationDrawable = (AnimationDrawable) imageView.getDrawable();
        animationDrawable.start();


        //动画刷新
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeLayout.setRefreshing(false);
                        new JSONTask().execute(1, 2);
                        imageView.setImageResource(R.drawable.animationlist);
                        animationDrawable = (AnimationDrawable) imageView.getDrawable();
                        animationDrawable.start();
                        Savebitmap savebitmap = new Savebitmap();
                        Bitmap bitmap = savebitmap.getBitmap();
                        Think ai = new Think();
                        if (bitmap != null && ai.geta() == 1) {
                            menuButton.setImageBitmap(bitmap);
                        } else {
                            menuButton.setImageResource(R.drawable.go);
                        }
                    }
                }, 100);
            }
        });

        //下按钮监控
        leftButton.setOnClickListener(new View.OnClickListener() {
            int pan;

            @Override
            public void onClick(View v) {
                Think a = new Think();
                pan = a.a(0);
                if (pan == 1) {
                    startActivity(new Intent(MainActivity.this, Person.class));
                } else {
                    Intent intent = new Intent(MainActivity.this, UnPersonname.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.in_from_out, R.anim.out_to_middle);
                }
            }
        });
        //右上角按钮监控
        menuButton.setOnClickListener(new View.OnClickListener() {
            int pan;

            @Override
            public void onClick(View v) {
                Think a = new Think();
                pan = a.a(0);
                if (pan == 1) {
                    Intent intent = new Intent(MainActivity.this, Personname.class);
                    View sharedView = menuButton;
                    String transitionName = getString(R.string.square_blue_name);
                    ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, sharedView, transitionName);
                    startActivity(intent, transitionActivityOptions.toBundle());
                } else {
                    Intent intent = new Intent(MainActivity.this, UnPersonname.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.in_from_out, R.anim.out_to_middle);
                }
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(MainActivity.this, Reply.class);
        int count = position - 1;
        String title = sArray[count];
        String name = sArray1[count];
        String time = sArray3[count];
        intent.putExtra("name", name);
        intent.putExtra("title", title);
        intent.putExtra("time",time);
        startActivity(intent);
    }

    private void animateBack() {
        //先清除其他动画
        if (hideAnimatorSet != null && hideAnimatorSet.isRunning()) {
            hideAnimatorSet.cancel();
        }
        if (backAnimatorSet != null && backAnimatorSet.isRunning()) {
            //如果这个动画已经在运行了，就不管它
        } else {
            backAnimatorSet = new AnimatorSet();
            //下面两句是将头尾元素放回初始位置。
            ObjectAnimator headerAnimator = ObjectAnimator.ofFloat(upview, "translationY", upview.getTranslationY(), 0f);
            ObjectAnimator footerAnimator = ObjectAnimator.ofFloat(leftButton, "translationY", leftButton.getTranslationY(), 0f);
            ArrayList<Animator> animators = new ArrayList<>();
            animators.add(headerAnimator);
            animators.add(footerAnimator);
            backAnimatorSet.setDuration(300);
            backAnimatorSet.playTogether(animators);
            backAnimatorSet.start();
        }
    }

    private void animateHide() {
        //先清除其他动画
        if (backAnimatorSet != null && backAnimatorSet.isRunning()) {
            backAnimatorSet.cancel();
        }
        if (hideAnimatorSet != null && hideAnimatorSet.isRunning()) {
            //如果这个动画已经在运行了，就不管它
        } else {
            hideAnimatorSet = new AnimatorSet();
            ObjectAnimator headerAnimator = ObjectAnimator.ofFloat(upview, "translationY", upview.getTranslationY(), -upview.getHeight() + statusBarHeight);//将ToolBar隐藏到上面
            ObjectAnimator footerAnimator = ObjectAnimator.ofFloat(leftButton, "translationY", leftButton.getTranslationY(), leftButton.getHeight() + 60);//将Button隐藏到下面
            ArrayList<Animator> animators = new ArrayList<>();
            animators.add(headerAnimator);
            animators.add(footerAnimator);
            hideAnimatorSet.setDuration(200);
            hideAnimatorSet.playTogether(animators);
            hideAnimatorSet.start();
        }
    }

    View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        float lastY = 0f;
        float currentY = 0f;
        //下面两个表示滑动的方向，大于0表示向下滑动，小于0表示向上滑动，等于0表示未滑动
        int lastDirection = 0;
        int currentDirection = 0;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    lastY = event.getY();
                    currentY = event.getY();
                    currentDirection = 0;
                    lastDirection = 0;
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (listView.getFirstVisiblePosition() > 0) {
                        //只有在listView.getFirstVisiblePosition()>0的时候才判断是否进行显隐动画。因为listView.getFirstVisiblePosition()==0时，
                        //ToolBar——也就是头部元素必须是可见的，如果这时候隐藏了起来，那么占位置用了headerview就被用户发现了
                        //但是当用户将列表向下拉露出列表的headerview的时候，应该要让头尾元素再次出现才对——这个判断写在了后面onScrollListener里面……
                        float tmpCurrentY = event.getY();
                        if (Math.abs(tmpCurrentY - lastY) > touchSlop) {//滑动距离大于touchslop时才进行判断
                            currentY = tmpCurrentY;
                            currentDirection = (int) (currentY - lastY);
                            if (lastDirection != currentDirection) {
                                //如果与上次方向不同，则执行显/隐动画
                                if (currentDirection < 0) {
                                    animateHide();
                                } else {
                                    animateBack();
                                }
                            }
                            lastY = currentY;
                        }
                    }
                    break;
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
                    //手指抬起的时候要把currentDirection设置为0，这样下次不管向哪拉，都与当前的不同（其实在ACTION_DOWN里写了之后这里就用不着了……）
                    currentDirection = 0;
                    lastDirection = 0;
                    break;
            }
            return false;
        }
    };

    AbsListView.OnScrollListener onScrollListener = new AbsListView.OnScrollListener() {

        //这个Listener其实是用来对付当用户的手离开列表后列表仍然在滑动的情况，也就是SCROLL_STATE_FLING

        int lastPosition = 0;//上次滚动到的第一个可见元素在listview里的位置——firstVisibleItem
        int state = SCROLL_STATE_IDLE;

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            //记录当前列表状态
            state = scrollState;
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if (firstVisibleItem == 0) {
                animateBack();
            }
            if (firstVisibleItem > 0) {
                if (firstVisibleItem > lastPosition && state == SCROLL_STATE_FLING) {
                    //如果上次的位置小于当前位置，那么隐藏头尾元素
                    animateHide();
                }

                //================================
                if (firstVisibleItem < lastPosition && state == SCROLL_STATE_FLING) {
                    //如果上次的位置大于当前位置，那么显示头尾元素，其实本例中，这个if没用
                    //如果是滑动ListView触发的，那么，animateBack()肯定已经执行过了，所以没有必要
                    //如果是点击按钮啥的触发滚动，那么根据设计原则，按钮肯定是头尾元素之一，所以也不需要animateBack()
                    //所以这个if块是不需要的
                    animateBack();
                }
                //这里没有判断(firstVisibleItem == lastPosition && state == SCROLL_STATE_FLING)的情况，
                //但是如果列表中的单个item如果很长的话还是要判断的，只不过代码又要多几行
                //但是可以取巧一下，在触发滑动的时候拖动执行一下animateHide()或者animateBack()——本例中的话就写在那个点击事件里就可以了）
                //BTW，如果列表的滑动纯是靠手滑动列表，而没有类似于点击一个按钮滚到某个位置的话，只要第一个if就够了…

            }
            lastPosition = firstVisibleItem;
        }
    };

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        Rect rect = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        // 状态栏高度
        statusBarHeight = rect.top;
        View v = getWindow().findViewById(Window.ID_ANDROID_CONTENT);
        int contentTop = v.getTop();
    }

    private class JSONTask extends AsyncTask<Integer, String, List<Listviewover>> {

        String str = "", str1 = "", str2 = "", str3 = "";

        @Override
        protected List<Listviewover> doInBackground(Integer... params) {

            try {
                URL url = new URL("http://40.84.62.67:80/init");
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
                    jsonObject.put("howmany", params[0]);
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

                String finalJson = buffer.toString();

                JSONObject parentObject = new JSONObject(finalJson);

                biaozhi = parentObject.getString("Max");//接受到Max为end时,表明已经是最后一页了,要隐藏下面的button

                JSONArray parentArray = parentObject.getJSONArray("Servers");

                for (int i = 0; i < parentArray.length(); i++) {
                    JSONObject finalObject = parentArray.getJSONObject(i);
                    String title = finalObject.getString("Title");
                    String name = finalObject.getString("Name");
                    String phototouxiang = finalObject.getString("Touxiangphoto");
                    String time = finalObject.getString("Time");
                    time +="_";
                    title += "_";
                    name += "_";
                    phototouxiang += "_";
                    str += title;
                    str1 += name;
                    str2 += phototouxiang;
                    str3 += time;
                }

                sArray = str.split("_");
                sArray1 = str1.split("_");
                String[] sArray2 = str2.split("_");
                sArray3 = str3.split("_");

                if (params[1] == 2) {
                    amData.clear();
                    numtorefresh = 1;
                }

                for (int j = 0; j < sArray.length; j++) {
                    Bitmap bitmap = null;
                    try {
                        byte[] bitmapArray;
                        bitmapArray = Base64.decode(sArray2[j], Base64.DEFAULT);
                        bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Listviewover a = new Listviewover(sArray[j], sArray1[j], bitmap);
                    amData.add(a);
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return amData;
        }

        @Override
        protected void onPostExecute(List<Listviewover> data) {
            if (data != null) {
                listView.removeFooterView(moreView);
                LayoutInflater inflater = getLayoutInflater();
                //Collections.reverse(data);倒置list
                adapter = new MyAdapter(inflater, data);
                listView.addFooterView(moreView);//添加底部view
                listView.setAdapter(adapter);
                if (biaozhi.equals("end")) {
                    listView.removeFooterView(moreView);
                    tx.setVisibility(View.VISIBLE);//提醒可见
                } else {
                    bt.setVisibility(View.VISIBLE);
                    tx.setVisibility(View.GONE);
                }
                imageView.setImageResource(R.drawable.main_title);
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        // 计算最后可见条目的索引
        lastVisibleIndex = visibleItemCount;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        // 滑到底部后自动加载，判断listview已经停止滚动并且最后可视的条目等于adapter的条目
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL && lastVisibleIndex == adapter.getCount() && !biaozhi.equals("end")) {
            numtorefresh++;
            new JSONTask().execute(numtorefresh, 1);//继续加载数据
            //adapter.notifyDataSetChanged();// 通知listView刷新数据
        }

    }


    private void init() {
        imageView = (ImageView) findViewById(R.id.main_title_imageview);
        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        menuButton = (ImageView) findViewById(R.id.main_title_button_right);
        leftButton = findViewById(R.id.fab);
        listView = (ListView) findViewById(R.id.lie);
        upview = findViewById(R.id.include);
        bt = (TextView) moreView.findViewById(R.id.bt_load);
        tx = (TextView) moreView.findViewById(R.id.tx);
    }
}


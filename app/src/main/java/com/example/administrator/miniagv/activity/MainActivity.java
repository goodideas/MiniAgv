package com.example.administrator.miniagv.activity;


import android.content.Intent;
import android.graphics.Color;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import com.example.administrator.miniagv.R;
import com.example.administrator.miniagv.entity.AgvBean;
import com.example.administrator.miniagv.utils.AgvAdapter;
import com.example.administrator.miniagv.utils.Constant;
import com.example.administrator.miniagv.utils.OnReceiveListen;
import com.example.administrator.miniagv.utils.SingleUdp;
import com.example.administrator.miniagv.utils.ToastUtil;
import com.example.administrator.miniagv.utils.Util;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    private static final String IP = "192.168.0.119";
    private static final int PORT = 9987;
    private Button btnConnectAgv;
    private Button btnSearchAgv;
    private ListView lvAgv;
    private List<AgvBean> list = new ArrayList<>();
    private AgvAdapter agvAdapter;
    private int c = 0;
    private int lastSelect = -1;
    private boolean isSelect = false;
    private int selected = -1;
    private Toolbar toolbar;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private long exitTime = 0;
    private LinearLayout.LayoutParams params = new
            LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

    private SingleUdp singleUdp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();


        btnConnectAgv.setOnClickListener(this);
        btnSearchAgv.setOnClickListener(this);
        agvAdapter = new AgvAdapter(this, list);
        lvAgv.setAdapter(agvAdapter);

        View emptyView = LayoutInflater.from(this).inflate(R.layout.programmed_list_empty_layout,null );


        params.gravity = Gravity.CENTER;
                ((ViewGroup) lvAgv.getParent()).addView(emptyView, params);
        lvAgv.setEmptyView(emptyView);

        lvAgv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                if (lastSelect == position) {
                    isSelect = !isSelect;
                    agvAdapter.setSelected(position, isSelect);
                    agvAdapter.notifyDataSetChanged();
                } else {
                    isSelect = true;
                    agvAdapter.setSelected(position, true);
                    agvAdapter.notifyDataSetChanged();
                }

                ToastUtil.customToast(MainActivity.this, "setOnItemClickListener=" + String.valueOf(position));
                selected = isSelect ? position : -1;
                lastSelect = position;
            }
        });

        lvAgv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                list.remove(position);
                agvAdapter.notifyDataSetChanged();
                return true;
            }
        });


        mDrawerToggle = new ActionBarDrawerToggle(MainActivity.this,mDrawerLayout,toolbar,
                R.string.app_name,R.string.app_name){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                drawerView.setClickable(true);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                drawerView.setClickable(false);
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

//        singleUdp = SingleUdp.getUdpInstance();
//        singleUdp.setUdpIp(IP);
//        singleUdp.setUdpRemotePort(PORT);
//        singleUdp.start();
//        singleUdp.setOnReceiveListen(new OnReceiveListen() {
//            @Override
//            public void onReceiveData(byte[] data,int len) {
//                String mString  = Util.bytes2HexString(data,len);
//                Log.e(TAG,"data="+mString+" "+Util.checkData(mString));
//            }
//        });


    }

    private void init() {
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar)findViewById(R.id.toolBar);
        btnConnectAgv = (Button) findViewById(R.id.btnConnectAgv);
        btnSearchAgv = (Button) findViewById(R.id.btnSearchAgv);
        lvAgv = (ListView) findViewById(R.id.lvAgv);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawerLayout);

        toolbar.setTitle(R.string.app_name);
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null){
            getSupportActionBar().setHomeButtonEnabled(true);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnConnectAgv:
                if(selected == -1){
                    ToastUtil.customToast(this,"没有选择！！！");
                }else{
                    ToastUtil.customToast(this, "选择了" + String.valueOf(selected));
                    agvAdapter.setSelected(selected, false);
                    agvAdapter.notifyDataSetChanged();

                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this,UnlockAgvActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Constant.KEY_MAIN_TO_UNLOCK,(AgvBean)agvAdapter.getItem(selected));
                    intent.putExtras(bundle);
                    startActivity(intent);
                    selected = -1;
                    isSelect = false;
                }

                break;
            case R.id.btnSearchAgv:
                AgvBean agvBean = new AgvBean();
                agvBean.setGavId(String.valueOf(c++));
                list.add(agvBean);
                agvAdapter.notifyDataSetChanged();
                lvAgv.smoothScrollToPosition(list.size());
                Log.e(TAG, "listSize=" + list.size() + " c=" + c);

//                singleUdp.send("123456789".getBytes());

                // TODO: 2016/7/8 发送数据，等待进度框，AgvBean->数据库。显示list 
                
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {

            if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                mDrawerLayout.closeDrawer(GravityCompat.START);
            } else if (System.currentTimeMillis() - exitTime > 2000) {
//                Toast.makeText(getApplicationContext(), "再按一次退出",
//                        Toast.LENGTH_SHORT).show();
                ToastUtil.customToast(getApplicationContext(), "再按一次退出");
                exitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(singleUdp!=null){
            singleUdp.stop();
        }
    }
}

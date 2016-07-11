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
import com.example.administrator.miniagv.utils.BroadcastUdp;
import com.example.administrator.miniagv.utils.Constant;
import com.example.administrator.miniagv.utils.OnReceiveListen;
import com.example.administrator.miniagv.utils.SingleUdp;
import com.example.administrator.miniagv.utils.SpHelper;
import com.example.administrator.miniagv.utils.ToastUtil;
import com.example.administrator.miniagv.utils.Util;
import com.example.administrator.miniagv.utils.WaitDialog;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    private Button btnConnectAgv;
    private Button btnSearchAgv;
    private ListView lvAgv;
    private List<AgvBean> list = new ArrayList<>();
    private AgvAdapter agvAdapter;
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
    private BroadcastUdp broadcastUdp;
    private SpHelper spHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();


        btnConnectAgv.setOnClickListener(this);
        btnSearchAgv.setOnClickListener(this);
        agvAdapter = new AgvAdapter(this, list);
        lvAgv.setAdapter(agvAdapter);

        View emptyView = LayoutInflater.from(this).inflate(R.layout.programmed_list_empty_layout, null);


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


        mDrawerToggle = new ActionBarDrawerToggle(MainActivity.this, mDrawerLayout, toolbar,
                R.string.app_name, R.string.app_name) {
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


    }

    private void init() {
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolBar);
        btnConnectAgv = (Button) findViewById(R.id.btnConnectAgv);
        btnSearchAgv = (Button) findViewById(R.id.btnSearchAgv);
        lvAgv = (ListView) findViewById(R.id.lvAgv);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

        toolbar.setTitle(R.string.app_name);
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
        }
        spHelper = new SpHelper(MainActivity.this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnConnectAgv:
                if (selected == -1) {
                    ToastUtil.customToast(this, "没有选择！！！");
                } else {
                    ToastUtil.customToast(this, "选择了" + String.valueOf(selected));
                    agvAdapter.setSelected(selected, false);
                    agvAdapter.notifyDataSetChanged();
                    final AgvBean agvBean = (AgvBean) agvAdapter.getItem(selected);
                    spHelper.saveSpAgvId(agvBean.getGavId());
                    spHelper.saveSpAgvIp(agvBean.getGavIp());
                    spHelper.saveSpAgvMac(agvBean.getGavMac());

                    singleUdp = SingleUdp.getUdpInstance();
                    singleUdp.setUdpIp(agvBean.getGavIp());
                    singleUdp.setUdpRemotePort(Constant.REMOTE_PORT);
                    singleUdp.start();
                    singleUdp.send(Util.HexString2Bytes(Constant.SEND_DATA_SHAKE.replace(" ", "")));
                    WaitDialog.showDialog(MainActivity.this,"正在连接",2000,null);
                    singleUdp.setOnReceiveListen(new OnReceiveListen() {
                        @Override
                        public void onReceiveData(byte[] data, int len, String ip) {
                            String mString = Util.bytes2HexString(data, len);
                            if (Util.checkData(mString)) {
                                String cmd = mString.substring(Constant.DATA_CMD_START, Constant.DATA_CMD_END);
                                if (Constant.CMD_SHAKE_RESPOND.equalsIgnoreCase(cmd)) {
                                    WaitDialog.immediatelyDismiss();
                                    Intent intent = new Intent();
                                    intent.setClass(MainActivity.this, UnlockAgvActivity.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable(Constant.KEY_MAIN_TO_UNLOCK, agvBean);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                    selected = -1;
                                    isSelect = false;
                                }
                            }

                        }
                    });


                }

                break;
            case R.id.btnSearchAgv:
//                AgvBean agvBean = new AgvBean();
//                agvBean.setGavId(String.valueOf(c++));
//                list.add(agvBean);
//                agvAdapter.notifyDataSetChanged();
//                lvAgv.smoothScrollToPosition(list.size());
//                Log.e(TAG, "listSize=" + list.size() + " c=" + c);

//                singleUdp.send("123456789".getBytes());

                if (broadcastUdp == null) {
                    broadcastUdp = new BroadcastUdp();
                }

                broadcastUdp.stop();
                broadcastUdp.init();
                broadcastUdp.send(Util.HexString2Bytes(Constant.SEND_DATA_SEARCH.replace(" ", "")));
                broadcastUdp.setReceiveListen(new OnReceiveListen() {
                    @Override
                    public void onReceiveData(byte[] data, int len, String remoteIp) {
                        String da = Util.bytes2HexString(data, len);
                        Log.e(TAG, "da = " + da);
                        analysisData(da, remoteIp);
                    }
                });
                WaitDialog.immediatelyDismiss();
                WaitDialog.showDialog(MainActivity.this, "正在搜索。。。", 5000, broadcastUdp);

                break;
        }
    }

    private void analysisData(String data, String ip) {
        if (Util.checkData(data)) {
            String cmd = data.substring(Constant.DATA_CMD_START, Constant.DATA_CMD_END);

            if (Constant.CMD_SEARCH_RESPOND.equalsIgnoreCase(cmd)) {
                String agvMac = data.substring(Constant.DATA_MAC_START, Constant.DATA_MAC_END);
                int dataLength = Integer.parseInt(data.substring(Constant.DATA_CONTENT_LENGTH_START_0, Constant.DATA_CONTENT_LENGTH_END_0), 16) +
                        Integer.parseInt(data.substring(Constant.DATA_CONTENT_LENGTH_START_1, Constant.DATA_CONTENT_LENGTH_END_1), 16) * 256;
                String agvId = data.substring(Constant.DATA_CONTENT_START, Constant.DATA_CONTENT_END(dataLength));
                final AgvBean agvBean = new AgvBean();
                agvBean.setGavId(agvId);
                agvBean.setGavIp(ip);
                agvBean.setGavMac(agvMac);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        list.add(agvBean);
                        agvAdapter.notifyDataSetChanged();
                        lvAgv.smoothScrollToPosition(list.size());
                    }
                });

            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {

            if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                mDrawerLayout.closeDrawer(GravityCompat.START);
            } else if (System.currentTimeMillis() - exitTime > 2000) {
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
    }
}

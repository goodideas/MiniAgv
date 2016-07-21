package com.example.administrator.miniagv.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.administrator.miniagv.R;
import com.example.administrator.miniagv.entity.AgvBean;
import com.example.administrator.miniagv.utils.Constant;
import com.example.administrator.miniagv.utils.OnReceiveListen;
import com.example.administrator.miniagv.utils.SingleUdp;
import com.example.administrator.miniagv.utils.SpHelper;
import com.example.administrator.miniagv.utils.ToastUtil;
import com.example.administrator.miniagv.utils.Util;
import com.example.administrator.miniagv.utils.WaitDialog;

public class UnlockAgvActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "UnlockAgvActivity";
    private Button btnUnlockAgv;
    private AgvBean agvBean;
    private SingleUdp singleUdp;

    private Toolbar toolbar;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private long exitTime = 0;
    private Button btnAGVList;
    private SpHelper spHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unlock_agv);
        btnUnlockAgv = (Button)findViewById(R.id.btnUnlockAgv);
        toolbar = (Toolbar) findViewById(R.id.toolBar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        btnAGVList = (Button)findViewById(R.id.btnAGVList);
        toolbar.setTitle(R.string.app_name);
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
        }
        spHelper = new SpHelper(UnlockAgvActivity.this);
        agvBean = new AgvBean();
        agvBean.setGavIp(spHelper.getSpAgvIp());
        agvBean.setGavMac(spHelper.getSpAgvMac());
        agvBean.setGavId(spHelper.getSpAgvId());

        singleUdp = SingleUdp.getUdpInstance();
        singleUdp.setUdpIp(spHelper.getSpAgvIp());
        singleUdp.setUdpRemotePort(Constant.REMOTE_PORT);
        singleUdp.start();
        singleUdp.setOnReceiveListen(new OnReceiveListen() {
            @Override
            public void onReceiveData(byte[] data, int len, @Nullable String remoteIp) {
                String mData = Util.bytes2HexString(data, len);
                Log.e(TAG,"mData="+mData);
                analysisData(mData);
            }
        });

        if (btnUnlockAgv != null) {
            btnUnlockAgv.setOnClickListener(this);
        }


        mDrawerToggle = new ActionBarDrawerToggle(UnlockAgvActivity.this, mDrawerLayout, toolbar,
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
        btnAGVList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                    mDrawerLayout.closeDrawer(GravityCompat.START);
                }
                startActivity(new Intent(UnlockAgvActivity.this,MainActivity.class));
            }
        });
    }

    private void analysisData(String data){
        if(Util.checkData(data)){
            String cmd = data.substring(Constant.DATA_CMD_START,Constant.DATA_CMD_END);
            if(Constant.CMD_UNLOCK_RESPOND.equalsIgnoreCase(cmd)){
                WaitDialog.immediatelyDismiss();
                Intent intent = new Intent();
                intent.setClass(UnlockAgvActivity.this,FunctionMenuActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constant.KEY_MAIN_TO_UNLOCK,agvBean);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        }
    }

    @Override
    protected void onResume() {
        Log.e(TAG,"onResume");
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e(TAG,"onStart");
        agvBean.setGavIp(spHelper.getSpAgvIp());
        agvBean.setGavMac(spHelper.getSpAgvMac());
        agvBean.setGavId(spHelper.getSpAgvId());
        Log.e(TAG, "spHelper.getSpAgvIp()="+spHelper.getSpAgvIp());
        singleUdp = SingleUdp.getUdpInstance();
        singleUdp.setUdpIp(agvBean.getGavIp());
        singleUdp.setUdpRemotePort(Constant.REMOTE_PORT);
        singleUdp.start();
        singleUdp.setOnReceiveListen(new OnReceiveListen() {
            @Override
            public void onReceiveData(byte[] data, int len, @Nullable String remoteIp) {
                String mData = Util.bytes2HexString(data, len);
                Log.e(TAG, "mData=" + mData);
                analysisData(mData);
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnUnlockAgv:
                if(TextUtils.isEmpty(spHelper.getSpAgvIp())){
                    ToastUtil.customToast(this,"当前没有AGV，请搜索AGV");
                }else{
                    singleUdp.send(Util.HexString2Bytes(Constant.SEND_DATA_UNLOCK(spHelper.getSpAgvMac()).replace(" ", "")));
                    WaitDialog.showDialog(UnlockAgvActivity.this, "正在解锁", Constant.UNLOCK_WAIT_DIALOG_MAX_TIME, null);
                }

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

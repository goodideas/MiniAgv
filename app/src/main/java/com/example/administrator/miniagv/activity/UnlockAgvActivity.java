package com.example.administrator.miniagv.activity;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.administrator.miniagv.R;
import com.example.administrator.miniagv.entity.AgvBean;
import com.example.administrator.miniagv.utils.Constant;
import com.example.administrator.miniagv.utils.OnReceiveListen;
import com.example.administrator.miniagv.utils.SingleUdp;
import com.example.administrator.miniagv.utils.Util;

public class UnlockAgvActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "UnlockAgvActivity";
    private Button btnUnlockAgv;
    private AgvBean agvBean;
    private SingleUdp singleUdp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unlock_agv);
        btnUnlockAgv = (Button)findViewById(R.id.btnUnlockAgv);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.setTitle("主页面");

            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setSubtitle("解锁AGV");
        }

        Intent intent = this.getIntent();
        agvBean =(AgvBean)intent.getSerializableExtra(Constant.KEY_MAIN_TO_UNLOCK);
        Log.e(TAG,"agvBeanId = "+agvBean.getGavId());

        singleUdp = SingleUdp.getUdpInstance();
        singleUdp.setUdpIp(agvBean.getGavIp());
        singleUdp.setUdpRemotePort(Constant.REMOTE_PORT);
        singleUdp.setOnReceiveListen(new OnReceiveListen() {
            @Override
            public void onReceiveData(byte[] data, int len, @Nullable String remoteIp) {
                String mData = Util.bytes2HexString(data,len);
                analysisData(mData);
            }
        });
        singleUdp.start();
        if (btnUnlockAgv != null) {
            btnUnlockAgv.setOnClickListener(this);
        }

    }

    private void analysisData(String data){
        if(Util.checkData(data)){
            String cmd = data.substring(Constant.DATA_CMD_START,Constant.DATA_CMD_END);
            if(Constant.CMD_UNLOCK_RESPOND.equalsIgnoreCase(cmd)){
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
                singleUdp.send(Util.HexString2Bytes(Constant.SEND_DATA_UNLOCK.replace(" ","")));
                break;
        }
    }
}

package com.example.administrator.miniagv.activity;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.administrator.miniagv.R;
import com.example.administrator.miniagv.entity.AgvBean;
import com.example.administrator.miniagv.utils.Constant;
import com.example.administrator.miniagv.utils.OnReceiveListen;
import com.example.administrator.miniagv.utils.SingleUdp;
import com.example.administrator.miniagv.utils.ToastUtil;
import com.example.administrator.miniagv.utils.Util;

public class AutoModeActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "AutoModeActivity";

    private Button btnStartTracking,btnStopTracking,btnLoc,btnGetAgvData,btnStop;

    private TextView tvLeftWheelSpeed,tvDistance,tvRightWheelSpeed;

    private SingleUdp singleUdp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_mode);
        btnStartTracking = (Button)findViewById(R.id.btnStartTracking);
        btnStopTracking = (Button)findViewById(R.id.btnStopTracking);
        btnLoc = (Button)findViewById(R.id.btnLoc);
        btnGetAgvData = (Button)findViewById(R.id.btnGetAgvData);
        btnStop = (Button)findViewById(R.id.btnStop);

        tvLeftWheelSpeed = (TextView)findViewById(R.id.tvLeftWheelSpeed);
        tvDistance = (TextView)findViewById(R.id.tvDistance);
        tvRightWheelSpeed = (TextView)findViewById(R.id.tvRightWheelSpeed);


        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.setTitle("选择功能");
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setSubtitle("自动模式");
        }

        Intent intent = this.getIntent();
        AgvBean agvBean =(AgvBean)intent.getSerializableExtra(Constant.KEY_MAIN_TO_UNLOCK);
        Log.e(TAG, "agvBeanId = " + agvBean.getGavId());


        btnStartTracking.setOnClickListener(this);
        btnStopTracking.setOnClickListener(this);
        btnLoc.setOnClickListener(this);
        btnGetAgvData.setOnClickListener(this);
        btnStop.setOnClickListener(this);
        singleUdp = SingleUdp.getUdpInstance();
        singleUdp.setOnReceiveListen(new OnReceiveListen() {
            @Override
            public void onReceiveData(byte[] data, int len, @Nullable String remoteIp) {
                String mData = Util.bytes2HexString(data,len);
                analysis(mData);
            }
        });
    }

    private void analysis(String data){
        if(Util.checkData(data)){
            String cmd = data.substring(Constant.DATA_CMD_START,Constant.DATA_CMD_END);
            if(Constant.CMD_TRACK_RESPOND.equalsIgnoreCase(cmd)){
                ToastUtil.customToast(AutoModeActivity.this,"开始循迹");
            }else if(Constant.CMD_QUERY_RESPOND.equalsIgnoreCase(cmd)){
                Log.e(TAG,"返回状态数据="+data);
                //16byte
                //agv模式 1byte
                //agv状态 1byte
                //错误状态 1byte
                //左轮速度 2byte
                //右轮速度 2byte
                //距离 1byte
                //RFID卡号 8byte
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
            case R.id.btnStartTracking:
                if(singleUdp!=null){
                    singleUdp.send(Util.HexString2Bytes(Constant.SEND_DATA_START_TRACK.replace(" ", "")));
                }
                break;
            case R.id.btnStopTracking:
                if(singleUdp!=null){
                    singleUdp.send(Util.HexString2Bytes(Constant.SEND_DATA_STOP_TRACK.replace(" ", "")));
                }
                break;
            case R.id.btnLoc:

                //TODO 判断etLoc的内容

                if(singleUdp!=null){
                    singleUdp.send("btnLoc".getBytes());
                }

                break;
            case R.id.btnGetAgvData:
                if(singleUdp!=null){
                    singleUdp.send(Util.HexString2Bytes(Constant.SEND_DATA_QUERY.replace(" ", "")));
                }
                break;

            case R.id.btnStop:
                if(singleUdp!=null){
                    singleUdp.send(Util.HexString2Bytes(Constant.SEND_DATA_LOCK.replace(" ","")));
                }
                break;
        }
    }
}

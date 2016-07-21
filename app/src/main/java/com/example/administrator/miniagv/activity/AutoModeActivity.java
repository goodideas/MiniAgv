package com.example.administrator.miniagv.activity;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.administrator.miniagv.R;
import com.example.administrator.miniagv.entity.AgvBean;
import com.example.administrator.miniagv.utils.Constant;
import com.example.administrator.miniagv.utils.OnReceiveListen;
import com.example.administrator.miniagv.utils.SingleUdp;
import com.example.administrator.miniagv.utils.ToastUtil;
import com.example.administrator.miniagv.utils.Util;

public class AutoModeActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "AutoModeActivity";

    private Button btnStartTracking, btnStopTracking, btnLoc, btnGetAgvData, btnStop;

    private TextView tvLeftWheelSpeed, tvDistance, tvRightWheelSpeed, tvAgvMode, tvAgvStatus, tvErrorStatus, tvRFID;
    private TextView tvAutoRFID,tvAutoLoc,tvAutoAction,tvAutoSpeed;
    private EditText etLoc;

    private RadioGroup radioGroup;
    private RadioButton radioButtonSpeed1;
    private RadioButton radioButtonSpeed2;
    private int rbSpeed = 1;

    private SingleUdp singleUdp;
    private AgvBean agvBean;
//    private OnReceiveListen onReceiveListen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_mode);
        init();

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == radioButtonSpeed1.getId()) {
                    ToastUtil.customToast(AutoModeActivity.this, "速度1");
                    rbSpeed = 1;
                } else if (checkedId == radioButtonSpeed2.getId()) {
                    ToastUtil.customToast(AutoModeActivity.this, "速度2");
                    rbSpeed = 2;
                }
            }
        });
        btnStartTracking.setOnClickListener(this);
        btnStopTracking.setOnClickListener(this);
        btnLoc.setOnClickListener(this);
        btnGetAgvData.setOnClickListener(this);
        btnStop.setOnClickListener(this);
        singleUdp = SingleUdp.getUdpInstance();
        singleUdp.setUdpIp(agvBean.getGavIp());
        singleUdp.setUdpRemotePort(Constant.REMOTE_PORT);
        singleUdp.start();
        singleUdp.setOnReceiveListen(new OnReceiveListen() {
            @Override
            public void onReceiveData(byte[] data, int len, @Nullable String remoteIp) {
                final String mData = Util.bytes2HexString(data, len);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        analysis(mData);
                    }
                });

            }
        });


    }

    private void init() {
        btnStartTracking = (Button) findViewById(R.id.btnStartTracking);
        btnStopTracking = (Button) findViewById(R.id.btnStopTracking);
        btnLoc = (Button) findViewById(R.id.btnLoc);
        btnGetAgvData = (Button) findViewById(R.id.btnGetAgvData);
        btnStop = (Button) findViewById(R.id.btnStop);

        tvLeftWheelSpeed = (TextView) findViewById(R.id.tvLeftWheelSpeed);
        tvDistance = (TextView) findViewById(R.id.tvDistance);
        tvRightWheelSpeed = (TextView) findViewById(R.id.tvRightWheelSpeed);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioButtonSpeed1 = (RadioButton) findViewById(R.id.radioButtonSpeed1);
        radioButtonSpeed2 = (RadioButton) findViewById(R.id.radioButtonSpeed2);

        etLoc = (EditText) findViewById(R.id.etAutoLoc);

        tvLeftWheelSpeed = (TextView) findViewById(R.id.tvLeftWheelSpeed);
        tvDistance = (TextView) findViewById(R.id.tvDistance);
        tvRightWheelSpeed = (TextView) findViewById(R.id.tvRightWheelSpeed);
        tvAgvMode = (TextView) findViewById(R.id.tvAgvMode);
        tvAgvStatus = (TextView) findViewById(R.id.tvAgvStatus);
        tvErrorStatus = (TextView) findViewById(R.id.tvErrorStatus);
        tvRFID = (TextView) findViewById(R.id.tvRFID);

        tvAutoRFID = (TextView)findViewById(R.id.tvAutoRFID);
        tvAutoLoc = (TextView)findViewById(R.id.tvAutoLoc);
        tvAutoAction = (TextView)findViewById(R.id.tvAutoAction);
        tvAutoSpeed = (TextView)findViewById(R.id.tvAutoSpeed);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("自动模式");
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setSubtitle("选择功能");
        }

        Intent intent = this.getIntent();
        agvBean = (AgvBean) intent.getSerializableExtra(Constant.KEY_MAIN_TO_UNLOCK);
        Log.e(TAG, "agvBeanId = " + agvBean.getGavId());
    }

    private void analysis(String data) {
        if (Util.checkData(data)) {
            Log.e(TAG, "数据=" + data);
            String cmd = data.substring(Constant.DATA_CMD_START, Constant.DATA_CMD_END);
            //开始循迹或者停止循迹
            if (Constant.CMD_TRACK_RESPOND.equalsIgnoreCase(cmd)) {
                ToastUtil.customToast(AutoModeActivity.this, "开始/停止循迹");
            //急停
            } else if(Constant.CMD_UNLOCK_RESPOND.equalsIgnoreCase(cmd)){
                ToastUtil.customToast(AutoModeActivity.this, "急停");
                Intent in = new Intent();
                in.putExtra(Constant.INTENT_NAME, Constant.INTENT_VALUE);
                setResult(Constant.AUTO_MODE_TO_FUNCTION_MENU_RESULT_CODE, in);
                AutoModeActivity.this.finish();

            //查询数据
            }else if (Constant.CMD_QUERY_RESPOND.equalsIgnoreCase(cmd)) {

                //18byte
                //agv模式 1byte
                //agv状态 1byte
                //错误状态 1byte
                //左轮速度 2byte
                //右轮速度 2byte
                //光电左 1byte
                //光电右 1byte
                //距离 1byte
                //RFID卡号 8byte
                String agvMode = data.substring(Constant.DATA_CONTENT_START, Constant.DATA_CONTENT_START + 2);
                String agvStatus = data.substring(Constant.DATA_CONTENT_START + 2, Constant.DATA_CONTENT_START + 4);
                String agvErrorStatus = data.substring(Constant.DATA_CONTENT_START + 4, Constant.DATA_CONTENT_START + 6);
                String leftWheelSpeed = data.substring(Constant.DATA_CONTENT_START + 6, Constant.DATA_CONTENT_START + 10);
                String rightWheelSpeed = data.substring(Constant.DATA_CONTENT_START + 10, Constant.DATA_CONTENT_START + 14);

                String distance = data.substring(Constant.DATA_CONTENT_START + 18, Constant.DATA_CONTENT_START + 20);
                String RFID = data.substring(Constant.DATA_CONTENT_START + 20, Constant.DATA_CONTENT_START + 36);

                int modeInt = Integer.parseInt(agvMode, 16);
                String mMode = "";
                switch (modeInt) {
                    case 0:
                        mMode = "停车";
                        break;
                    case 1:
                        mMode = "手动模式";
                        break;
                    case 2:
                        mMode = "循迹模式";
                        break;
                    case 3:
                        mMode = "急停";
                        break;
                }
                tvAgvMode.setText(mMode);
                int statusInt = Integer.parseInt(agvStatus, 16);
                String mStatus = "";
                switch (statusInt) {
                    case 0:
                        mStatus = "停止";
                        break;
                    case 1:
                        mStatus = "前进";
                        break;
                    case 2:
                        mStatus = "左转";
                        break;
                    case 3:
                        mStatus = "右转";
                        break;
                }
                tvAgvStatus.setText(mStatus);

                int errorStatusInt = Integer.parseInt(agvErrorStatus, 16);
                String mErrorStatus = "";
                switch (errorStatusInt){
                    case 0:mErrorStatus = "无";
                        break;
                    case 1:mErrorStatus = "距离过近";
                        break;
                    case 2:mErrorStatus = "脱轨";
                        break;
                }
                tvErrorStatus.setText(mErrorStatus);


                int leftInt = Integer.parseInt(leftWheelSpeed.substring(0, 2), 16) +
                        Integer.parseInt(leftWheelSpeed.substring(2, 4), 16) * 256;
                tvLeftWheelSpeed.setText(String.valueOf(leftInt));

                int rightInt = Integer.parseInt(rightWheelSpeed.substring(0, 2), 16) +
                        Integer.parseInt(rightWheelSpeed.substring(2, 4), 16) * 256;

                tvRightWheelSpeed.setText(String.valueOf(rightInt));

                int disInt = Integer.parseInt(distance, 16);
                tvDistance.setText(String.valueOf(disInt));

                tvRFID.setText(RFID);

            //自动返回数据RFID
            }else if(Constant.CMD_RFID_CAR_RESPOND.equalsIgnoreCase(cmd)){
                //11byte
                //loc 1byte
                //rfid 8byte
                //act 1byte
                //speed 1byte
                String loc  = data.substring(Constant.DATA_CONTENT_START,Constant.DATA_CONTENT_START+2);
                String rfid  = data.substring(Constant.DATA_CONTENT_START+2,Constant.DATA_CONTENT_START+18);
                String action  = data.substring(Constant.DATA_CONTENT_START+18,Constant.DATA_CONTENT_START+20);
                String speed  = data.substring(Constant.DATA_CONTENT_START+20,Constant.DATA_CONTENT_START+22);

                int locInt = Integer.parseInt(loc,16);
                tvAutoLoc.setText(String.valueOf(locInt));

                tvAutoRFID.setText(rfid);
                int actionInt = Integer.parseInt(action, 16);
                String mAction = "";
                switch (actionInt) {
                    case 0:
                        mAction = "停止";
                        break;
                    case 1:
                        mAction = "前进";
                        break;
                    case 2:
                        mAction = "靠左行驶";
                        break;
                    case 3:
                        mAction = "靠右行驶";
                        break;

                }
                tvAutoAction.setText(mAction);

                int speedInt = Integer.parseInt(speed,16);
                tvAutoSpeed.setText(String.valueOf(speedInt));


                //错误状态自动返回
            }else if(Constant.CMD_ERROR_STATU_RESPOND.equalsIgnoreCase(cmd)){
                String autoErrorStatus = data.substring(Constant.DATA_CONTENT_START, Constant.DATA_CONTENT_START + 2);
                int autoErrorStatusInt = Integer.parseInt(autoErrorStatus, 16);
                String mAutoErrorStatus = "";
                switch (autoErrorStatusInt){
                    case 0:mAutoErrorStatus = "无";
                        break;
                    case 1:mAutoErrorStatus = "距离过近";
                        break;
                    case 2:mAutoErrorStatus = "脱轨";
                        break;
                }

                tvErrorStatus.setText(mAutoErrorStatus);
            //停在指定位置
            }else if(Constant.CMD_STOP_LOC_RESPOND.equalsIgnoreCase(cmd)){
                ToastUtil.customToast(AutoModeActivity.this,"停在指定位置");
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
        switch (v.getId()) {
            case R.id.btnStartTracking:
                if (singleUdp != null) {
                    if (rbSpeed == 1) {
                        singleUdp.send(Util.HexString2Bytes(Constant.SEND_DATA_START_TRACK_S1(agvBean.getGavMac()).replace(" ", "")));
                    } else if (rbSpeed == 2) {
                        singleUdp.send(Util.HexString2Bytes(Constant.SEND_DATA_START_TRACK_S2(agvBean.getGavMac()).replace(" ", "")));
                    }
                }
                break;
            case R.id.btnStopTracking:
                if (singleUdp != null) {
                    singleUdp.send(Util.HexString2Bytes(Constant.SEND_DATA_STOP_TRACK(agvBean.getGavMac()).replace(" ", "")));
                }
                break;
            case R.id.btnLoc:

                //TODO 判断etLoc的内容

                if (singleUdp != null) {
//                    14字节
                    String mLoc = etLoc.getText().toString();
                    byte[] stopLoc = Util.HexString2Bytes(Constant.SEND_DATA_STOP_LOC(agvBean.getGavMac()).replace(" ", ""));
                    stopLoc[14] = Byte.parseByte(mLoc);
                    stopLoc[15] = Byte.parseByte(mLoc);
                    singleUdp.send(stopLoc);
                }

                break;
            case R.id.btnGetAgvData:
                if (singleUdp != null) {
                    singleUdp.send(Util.HexString2Bytes(Constant.SEND_DATA_QUERY(agvBean.getGavMac()).replace(" ", "")));
                }
                break;

            case R.id.btnStop:
                if (singleUdp != null) {
                    singleUdp.send(Util.HexString2Bytes(Constant.SEND_DATA_LOCK(agvBean.getGavMac()).replace(" ", "")));
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy()");
//        onReceiveListen = null;
    }
}

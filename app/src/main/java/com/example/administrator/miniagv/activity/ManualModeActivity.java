package com.example.administrator.miniagv.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.administrator.miniagv.R;
import com.example.administrator.miniagv.entity.AgvBean;
import com.example.administrator.miniagv.utils.Constant;
import com.example.administrator.miniagv.utils.OnReceiveListen;
import com.example.administrator.miniagv.utils.SingleUdp;
import com.example.administrator.miniagv.utils.ToastUtil;
import com.example.administrator.miniagv.utils.Util;
import com.example.administrator.miniagv.views.SimpleSpeedSeekBarAdapter;
import com.example.administrator.miniagv.views.SpeedSeekBar;
import com.example.administrator.miniagv.views.SpeedSeekBarListener;
import com.example.administrator.miniagv.views.VerticalSeekBar;

public class ManualModeActivity extends AppCompatActivity {

    private static final String TAG = "ManualModeActivity";

    private SpeedSeekBar seekBarLeft;
    private SpeedSeekBar seekBarRight;
    private SpeedSeekBar speedSeekBarCenter;
    private SingleUdp singleUdp;
    private byte[] sendData;
    private byte leftWheel = 0x00, rightWheel = 0x00;
    private boolean flag = true;
    private TextView tvManualErrorStatus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_mode);
        final Resources resources = getResources();
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("选择功能");
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setSubtitle("手动模式");
        }

        seekBarLeft = (SpeedSeekBar) findViewById(R.id.speedSeekBarLeft);
        seekBarRight = (SpeedSeekBar) findViewById(R.id.speedSeekBarRight);
        speedSeekBarCenter = (SpeedSeekBar) findViewById(R.id.speedSeekBarCenter);
        tvManualErrorStatus = (TextView)findViewById(R.id.tvManualErrorStatus);
        Intent intent = this.getIntent();
        AgvBean agvBean = (AgvBean) intent.getSerializableExtra(Constant.KEY_MAIN_TO_UNLOCK);
        Log.e(TAG, "agvBeanId = " + agvBean.getGavId());

        singleUdp = SingleUdp.getUdpInstance();
        singleUdp.setUdpIp(agvBean.getGavIp());
        singleUdp.setUdpRemotePort(Constant.REMOTE_PORT);
        singleUdp.start();
        singleUdp.setOnReceiveListen(new OnReceiveListen() {
            @Override
            public void onReceiveData(byte[] data, int len, @Nullable String remoteIp) {
                String mData = Util.bytes2HexString(data,len);
                if(Util.checkData(mData)){
                    String cmd = mData.substring(Constant.DATA_CMD_START,Constant.DATA_CMD_END);
                    if(Constant.CMD_MANUAL_RESPOND.equalsIgnoreCase(cmd)){
                        ToastUtil.customToast(ManualModeActivity.this,"操作成功");
                    }else if(Constant.CMD_ERROR_STATU_RESPOND.equalsIgnoreCase(cmd)){
                        String manualErrorStatus = mData.substring(Constant.DATA_CONTENT_START,Constant.DATA_CONTENT_START+2);
                        int manualErrorStatusInt = Integer.parseInt(manualErrorStatus, 16);
                        final String mManualErrorStatus = manualErrorStatusInt == 0 ? "距离过近" : "脱轨";
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tvManualErrorStatus.setText(mManualErrorStatus);

                            }
                        });
                    }
                }
            }
        });

        speedSeekBarCenter.setAdapter(new SimpleSpeedSeekBarAdapter(resources, new int[]{
                R.drawable.btn_star5_selector,
                R.drawable.btn_star4_selector,
                R.drawable.btn_star3_selector,
                R.drawable.btn_star2_selector,
                R.drawable.btn_star1_selector,
                R.drawable.btn_star0_selector
        }));
        seekBarLeft.setAdapter(new SimpleSpeedSeekBarAdapter(resources, new int[]{
                R.drawable.btn_star5_selector,
                R.drawable.btn_star4_selector,
                R.drawable.btn_star3_selector,
                R.drawable.btn_star2_selector,
                R.drawable.btn_star1_selector,
                R.drawable.btn_star0_selector
        }));
        seekBarRight.setAdapter(new SimpleSpeedSeekBarAdapter(resources, new int[]{
                R.drawable.btn_star5_selector,
                R.drawable.btn_star4_selector,
                R.drawable.btn_star3_selector,
                R.drawable.btn_star2_selector,
                R.drawable.btn_star1_selector,
                R.drawable.btn_star0_selector
        }));

        seekBarLeft.setPosition(5);
        seekBarRight.setPosition(5);
        speedSeekBarCenter.setPosition(5);


        speedSeekBarCenter.setListener(new SpeedSeekBarListener() {
            @Override
            public void onPositionSelected(int position) {
                flag = false;
                seekBarLeft.setPosition(position);
                seekBarRight.setPosition(position);
                flag = true;
                //14 15 16 字节分别是 左轮速度、右轮速度、校验位
                sendData = Util.HexString2Bytes(Constant.SEND_DATA_SPEED.replace(" ", ""));
                sendData[14] = Byte.parseByte((5 - position) + "");
                sendData[15] = Byte.parseByte((5 - position) + "");
                String hexData = Util.bytes2HexString(new byte[]{sendData[14], sendData[15]}, 2);
                sendData[16] = Util.CheckCode(hexData);
                singleUdp.send(sendData);
            }
        });
        seekBarLeft.setListener(new SpeedSeekBarListener() {
            @Override
            public void onPositionSelected(int position) {
                leftWheel = Byte.parseByte("" + (5 - position));
                if (flag) {
                    sendData = Util.HexString2Bytes(Constant.SEND_DATA_SPEED.replace(" ", ""));
                    sendData[14] = leftWheel;
                    sendData[15] = rightWheel;
                    String hexData = Util.bytes2HexString(new byte[]{sendData[14], sendData[15]}, 2);
                    sendData[16] = Util.CheckCode(hexData);
                    singleUdp.send(sendData);
                    ToastUtil.customToast(ManualModeActivity.this, "seekBarLeft position=" + (5 - position));
                }
            }
        });

        seekBarRight.setListener(new SpeedSeekBarListener() {
            @Override
            public void onPositionSelected(int position) {
                rightWheel = Byte.parseByte("" + (5 - position));
                if (flag) {
                    sendData = Util.HexString2Bytes(Constant.SEND_DATA_SPEED.replace(" ", ""));
                    sendData[14] = leftWheel;
                    sendData[15] = rightWheel;
                    String hexData = Util.bytes2HexString(new byte[]{sendData[14], sendData[15]}, 2);
                    sendData[16] = Util.CheckCode(hexData);
                    singleUdp.send(sendData);
                    ToastUtil.customToast(ManualModeActivity.this, "seekBarRight position=" + (5 - position));
                }
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


}

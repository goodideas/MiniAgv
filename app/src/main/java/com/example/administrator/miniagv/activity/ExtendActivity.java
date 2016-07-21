package com.example.administrator.miniagv.activity;


import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.method.BaseKeyListener;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.administrator.miniagv.R;
import com.example.administrator.miniagv.entity.AgvBean;
import com.example.administrator.miniagv.utils.Constant;
import com.example.administrator.miniagv.utils.OnReceiveListen;
import com.example.administrator.miniagv.utils.SingleUdp;
import com.example.administrator.miniagv.utils.ToastUtil;
import com.example.administrator.miniagv.utils.Util;
import com.example.administrator.miniagv.views.ColorPicker;

public class ExtendActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener,View.OnClickListener{

    private static final String TAG = "ExtendActivity";
    private ColorPicker colorPicker;
    private EditText etR, etG, etB;
    private EditText tvColorTime, tvBuzzerHz, tvBuzzerTime;
    private android.support.v7.widget.AppCompatSeekBar seekBarColorTime, seekBarBuzzerHz, seekBarBuzzerTime;

    private Button btnColor,btnBuzzer;

    private SingleUdp singleUdp;
    private int colorR,colorG,colorB,colorTime;
    private int buzzerHz,buzzerTime;
    private byte[] colorBytes = new byte[4];
    private byte[] buzzerBytes = new byte[2];
    private byte[] sendColor,sendBuzzer;
    private AgvBean agvBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extend);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("扩展");
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setSubtitle("选择功能");
        }

        colorPicker = (ColorPicker) findViewById(R.id.colorPicker);
        etR = (EditText) findViewById(R.id.etR);
        etG = (EditText) findViewById(R.id.etG);
        etB = (EditText) findViewById(R.id.etB);

        tvColorTime = (EditText) findViewById(R.id.tvColorTime);
        tvBuzzerHz = (EditText) findViewById(R.id.tvBuzzerHz);
        tvBuzzerTime = (EditText) findViewById(R.id.tvBuzzerTime);

        btnColor = (Button)findViewById(R.id.btnColor);
        btnBuzzer = (Button)findViewById(R.id.btnBuzzer);

        seekBarColorTime = (android.support.v7.widget.AppCompatSeekBar) findViewById(R.id.seekBarColorTime);
        seekBarBuzzerHz = (android.support.v7.widget.AppCompatSeekBar) findViewById(R.id.seekBarBuzzerHz);
        seekBarBuzzerTime = (android.support.v7.widget.AppCompatSeekBar) findViewById(R.id.seekBarBuzzerTime);

        Intent intent = this.getIntent();
        agvBean =(AgvBean)intent.getSerializableExtra(Constant.KEY_MAIN_TO_UNLOCK);
        Log.e(TAG, "agvBeanId = " + agvBean.getGavId());
        tvColorTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {

                    if (!TextUtils.isEmpty(tvColorTime.getText().toString())) {
                        CharSequence charSequence = tvColorTime.getText();
                        Spannable spanText = (Spannable) charSequence;
                        Selection.setSelection(spanText, charSequence.length());
                        seekBarColorTime.setProgress(Integer.valueOf(tvColorTime.getText().toString()));
                    } else {
                        seekBarColorTime.setProgress(0);
                    }
                }
            }
        });

        tvBuzzerHz.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){

                    if(!TextUtils.isEmpty(tvBuzzerHz.getText().toString())){
                        CharSequence charSequence = tvBuzzerHz.getText();
                        Spannable spanText = (Spannable) charSequence;
                        Selection.setSelection(spanText, charSequence.length());
                        seekBarBuzzerHz.setProgress(Integer.valueOf(tvBuzzerHz.getText().toString()));
                    }else{
                        seekBarBuzzerHz.setProgress(0);
                    }
                }
            }
        });

        tvBuzzerTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){

                    if(!TextUtils.isEmpty(tvBuzzerTime.getText().toString())){
                        CharSequence charSequence = tvBuzzerTime.getText();
                        Spannable spanText = (Spannable) charSequence;
                        Selection.setSelection(spanText, charSequence.length());
                        seekBarBuzzerTime.setProgress(Integer.valueOf(tvBuzzerTime.getText().toString()));
                    }else{
                        seekBarBuzzerTime.setProgress(0);
                    }
                }
            }
        });


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
                    if(Constant.CMD_LIGHTING_RESPOND.equalsIgnoreCase(cmd)){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.customToast(ExtendActivity.this, "LED设置成功");
                            }
                        });

                    }else if(Constant.CMD_BUZZER_RESPOND.equalsIgnoreCase(cmd)){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.customToast(ExtendActivity.this, "蜂鸣器设置成功");
                            }
                        });

                    }
                }
            }
        });

        seekBarColorTime.setOnSeekBarChangeListener(this);
        seekBarBuzzerHz.setOnSeekBarChangeListener(this);
        seekBarBuzzerTime.setOnSeekBarChangeListener(this);
        btnColor.setOnClickListener(this);
        btnBuzzer.setOnClickListener(this);
        colorPicker.setOnColorChangedListener(new ColorPicker.OnColorChangedListener() {
            @Override
            public void onColorChanged(int color) {
                colorR = Color.red(color);
                colorG = Color.green(color);
                colorB = Color.blue(color);
                etR.setText(String.valueOf(Color.red(color)));
                etG.setText(String.valueOf(Color.green(color)));
                etB.setText(String.valueOf(Color.blue(color)));
            }
        });


    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()) {
            case R.id.seekBarColorTime:
                colorTime = progress;
                tvColorTime.setText(String.valueOf(progress));
                break;
            case R.id.seekBarBuzzerHz:
                buzzerHz = progress >220 ?220:progress;
                tvBuzzerHz.setText(String.valueOf(progress));
                break;
            case R.id.seekBarBuzzerTime:
                buzzerTime = progress;
                tvBuzzerTime.setText(String.valueOf(progress));
                break;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

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
            case R.id.btnBuzzer:
                buzzerBytes[0] = (byte) Integer.parseInt(Integer.toHexString(buzzerHz), 16);
                buzzerBytes[1] = (byte) Integer.parseInt(Integer.toHexString(buzzerTime), 16);
                sendBuzzer = Util.HexString2Bytes(Constant.SEND_DATA_BUZZER(agvBean.getGavMac()).replace(" ", ""));
                System.arraycopy(buzzerBytes,0,sendBuzzer,14,buzzerBytes.length);
                sendBuzzer[16] = Util.CheckCode(Util.bytes2HexString(buzzerBytes,buzzerBytes.length));
                singleUdp.send(sendBuzzer);
                break;
            case R.id.btnColor:

                colorBytes[0] = (byte) Integer.parseInt(Integer.toHexString(colorR), 16);
                colorBytes[1] = (byte) Integer.parseInt(Integer.toHexString(colorG), 16);
                colorBytes[2] = (byte) Integer.parseInt(Integer.toHexString(colorB), 16);
                colorBytes[3] = (byte) Integer.parseInt(Integer.toHexString(colorTime), 16);
                sendColor = Util.HexString2Bytes(Constant.SEND_DATA_COLOR(agvBean.getGavMac()).replace(" ",""));
                System.arraycopy(colorBytes,0,sendColor,14,colorBytes.length);
                sendColor[18] = Util.CheckCode(Util.bytes2HexString(colorBytes,colorBytes.length));
                singleUdp.send(sendColor);

                break;

        }
    }
}

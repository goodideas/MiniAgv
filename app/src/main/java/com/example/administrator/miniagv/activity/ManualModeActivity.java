package com.example.administrator.miniagv.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.SeekBar;

import com.example.administrator.miniagv.R;
import com.example.administrator.miniagv.entity.AgvBean;
import com.example.administrator.miniagv.utils.Constant;
import com.example.administrator.miniagv.utils.ToastUtil;
import com.example.administrator.miniagv.views.SimpleSpeedSeekBarAdapter;
import com.example.administrator.miniagv.views.SpeedSeekBar;
import com.example.administrator.miniagv.views.SpeedSeekBarListener;
import com.example.administrator.miniagv.views.VerticalSeekBar;

public class ManualModeActivity extends AppCompatActivity {

    private static final String TAG = "ManualModeActivity";

    private SpeedSeekBar seekBarLeft;
    private SpeedSeekBar seekBarRight;
    private SpeedSeekBar speedSeekBarCenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_mode);
        final Resources resources = getResources();
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.setTitle("选择功能");
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setSubtitle("手动模式");
        }

        seekBarLeft = (SpeedSeekBar)findViewById(R.id.speedSeekBarLeft);
        seekBarRight = (SpeedSeekBar)findViewById(R.id.speedSeekBarRight);
        speedSeekBarCenter = (SpeedSeekBar)findViewById(R.id.speedSeekBarCenter);

        Intent intent = this.getIntent();
        AgvBean agvBean =(AgvBean)intent.getSerializableExtra(Constant.KEY_MAIN_TO_UNLOCK);
        Log.e(TAG, "agvBeanId = " + agvBean.getGavId());

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
                seekBarLeft.setPosition(position);
                seekBarRight.setPosition(position);
            }
        });
        seekBarLeft.setListener(new SpeedSeekBarListener() {
            @Override
            public void onPositionSelected(int position) {
                ToastUtil.customToast(ManualModeActivity.this,"seekBarLeft position="+(5-position));
            }
        });

        seekBarRight.setListener(new SpeedSeekBarListener() {
            @Override
            public void onPositionSelected(int position) {
                ToastUtil.customToast(ManualModeActivity.this,"seekBarRight position="+(5-position));

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

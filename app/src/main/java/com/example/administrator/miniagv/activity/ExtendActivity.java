package com.example.administrator.miniagv.activity;


import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.administrator.miniagv.R;
import com.example.administrator.miniagv.views.ColorPicker;

public class ExtendActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {

    private ColorPicker colorPicker;
    private EditText etR, etG, etB;
    private TextView tvColorTime, tvBuzzerHz, tvBuzzerTime;
    private android.support.v7.widget.AppCompatSeekBar seekBarColorTime, seekBarBuzzerHz, seekBarBuzzerTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extend);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("选择功能");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        colorPicker = (ColorPicker) findViewById(R.id.colorPicker);
        etR = (EditText) findViewById(R.id.etR);
        etG = (EditText) findViewById(R.id.etG);
        etB = (EditText) findViewById(R.id.etB);

        tvColorTime = (TextView) findViewById(R.id.tvColorTime);
        tvBuzzerHz = (TextView) findViewById(R.id.tvBuzzerHz);
        tvBuzzerTime = (TextView) findViewById(R.id.tvBuzzerTime);

        seekBarColorTime = (android.support.v7.widget.AppCompatSeekBar) findViewById(R.id.seekBarColorTime);
        seekBarBuzzerHz = (android.support.v7.widget.AppCompatSeekBar) findViewById(R.id.seekBarBuzzerHz);
        seekBarBuzzerTime = (android.support.v7.widget.AppCompatSeekBar) findViewById(R.id.seekBarBuzzerTime);

        seekBarColorTime.setOnSeekBarChangeListener(this);
        seekBarBuzzerHz.setOnSeekBarChangeListener(this);
        seekBarBuzzerTime.setOnSeekBarChangeListener(this);

        colorPicker.setOnColorChangedListener(new ColorPicker.OnColorChangedListener() {
            @Override
            public void onColorChanged(int color) {
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
                tvColorTime.setText(String.valueOf(progress));
                break;
            case R.id.seekBarBuzzerHz:
                tvBuzzerHz.setText(String.valueOf(progress));
                break;
            case R.id.seekBarBuzzerTime:
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

}

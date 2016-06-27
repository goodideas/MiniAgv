package com.example.administrator.miniagv.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.SeekBar;

import com.example.administrator.miniagv.R;
import com.example.administrator.miniagv.views.VerticalSeekBar;

public class ManualModeActivity extends AppCompatActivity {

    private static final String TAG = "ManualModeActivity";

    private VerticalSeekBar seekBar1;
    private VerticalSeekBar seekBar2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_mode);

        seekBar1 = (VerticalSeekBar)findViewById(R.id.seekBar1);
        seekBar2 = (VerticalSeekBar)findViewById(R.id.seekBar2);

        seekBar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.e(TAG,"onProgressChanged seekBar1 progress="+progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.e(TAG,"onStopTrackingTouch seekBar1 progress="+seekBar.getProgress());
            }
        });


        seekBar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                Log.e(TAG,"onProgressChanged seekBar2 progress="+progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.e(TAG,"onStopTrackingTouch seekBar2 progress="+seekBar.getProgress());
            }
        });

    }
}

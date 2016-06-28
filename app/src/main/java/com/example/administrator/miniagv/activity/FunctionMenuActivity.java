package com.example.administrator.miniagv.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.administrator.miniagv.R;
import com.example.administrator.miniagv.utils.Util;

public class FunctionMenuActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "FunctionMenuActivity";
    private Button btnManualMode;
    private Button btnRFIDProgrammed;
    private Button btnAutoMode;
    private Button btnExtend;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_function_menu);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("选择功能");
        }
        int screenWidth = Util.getScreenWidth(this);
        int btnMeasure = (int)(screenWidth*0.4);

        btnManualMode = (Button) findViewById(R.id.btnManualMode);
        btnRFIDProgrammed = (Button) findViewById(R.id.btnRFIDProgrammed);
        btnAutoMode = (Button) findViewById(R.id.btnAutoMode);
        btnExtend = (Button) findViewById(R.id.btnExtend);

        btnManualMode.getLayoutParams().width = btnMeasure;
        btnManualMode.getLayoutParams().height = btnMeasure;
        btnRFIDProgrammed.getLayoutParams().width = btnMeasure;
        btnRFIDProgrammed.getLayoutParams().height = btnMeasure;
        btnAutoMode.getLayoutParams().width = btnMeasure;
        btnAutoMode.getLayoutParams().height = btnMeasure;
        if (btnExtend != null) {
            btnExtend.getLayoutParams().width = btnMeasure;
            btnExtend.getLayoutParams().height = btnMeasure;
        }



        btnManualMode.setOnClickListener(this);
        btnRFIDProgrammed.setOnClickListener(this);
        btnAutoMode.setOnClickListener(this);
        btnExtend.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAutoMode:
                startActivity(new Intent(FunctionMenuActivity.this, AutoModeActivity.class));
                break;
            case R.id.btnRFIDProgrammed:
                startActivity(new Intent(FunctionMenuActivity.this, ProgrammedModeActivity.class));
                break;
            case R.id.btnManualMode:
                startActivity(new Intent(FunctionMenuActivity.this, ManualModeActivity.class));
                break;
            case R.id.btnExtend:
                startActivity(new Intent(FunctionMenuActivity.this, ExtendActivity.class));
                break;
        }
    }
}

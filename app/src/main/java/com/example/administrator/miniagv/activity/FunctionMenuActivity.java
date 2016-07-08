package com.example.administrator.miniagv.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.administrator.miniagv.R;
import com.example.administrator.miniagv.entity.AgvBean;
import com.example.administrator.miniagv.utils.Constant;
import com.example.administrator.miniagv.utils.Util;

public class FunctionMenuActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "FunctionMenuActivity";
    private Button btnManualMode;
    private Button btnRFIDProgrammed;
    private Button btnAutoMode;
    private Button btnExtend;
    private AgvBean agvBean;
    private Intent mIntent = new Intent();
    private Bundle mBundle = new Bundle();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_function_menu);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("解锁AGV");
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setSubtitle("选择功能");
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


        Intent intent = this.getIntent();
        agvBean =(AgvBean)intent.getSerializableExtra(Constant.KEY_MAIN_TO_UNLOCK);
        Log.e(TAG,"agvBeanId = "+agvBean.getGavId());
        mBundle.putSerializable(Constant.KEY_MAIN_TO_UNLOCK, agvBean);


        btnManualMode.setOnClickListener(this);
        btnRFIDProgrammed.setOnClickListener(this);
        btnAutoMode.setOnClickListener(this);
        btnExtend.setOnClickListener(this);
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
            case R.id.btnAutoMode:

                mIntent.setClass(FunctionMenuActivity.this,AutoModeActivity.class);
                mIntent.putExtras(mBundle);
                startActivity(mIntent);
                break;
            case R.id.btnRFIDProgrammed:

                mIntent.setClass(FunctionMenuActivity.this,ProgrammedModeActivity.class);
                mIntent.putExtras(mBundle);
                startActivity(mIntent);
                break;
            case R.id.btnManualMode:

                mIntent.setClass(FunctionMenuActivity.this, ManualModeActivity.class);
                mIntent.putExtras(mBundle);
                startActivity(mIntent);
                break;
            case R.id.btnExtend:

                mIntent.setClass(FunctionMenuActivity.this, ExtendActivity.class);
                mIntent.putExtras(mBundle);
                startActivity(mIntent);
                break;
        }
    }
}

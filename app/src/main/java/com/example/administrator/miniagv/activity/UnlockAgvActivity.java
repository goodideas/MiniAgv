package com.example.administrator.miniagv.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.administrator.miniagv.R;
import com.example.administrator.miniagv.entity.AgvBean;
import com.example.administrator.miniagv.utils.Constant;

public class UnlockAgvActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "UnlockAgvActivity";
    private Button btnUnlockAgv;
    private AgvBean agvBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unlock_agv);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.setTitle("主页面");

            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setSubtitle("解锁AGV");
        }

        btnUnlockAgv = (Button)findViewById(R.id.btnUnlockAgv);
        Intent intent = this.getIntent();
        agvBean =(AgvBean)intent.getSerializableExtra(Constant.KEY_MAIN_TO_UNLOCK);
        Log.e(TAG,"agvBeanId = "+agvBean.getGavId());

        if (btnUnlockAgv != null) {
            btnUnlockAgv.setOnClickListener(this);
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
                Intent intent = new Intent();
                intent.setClass(UnlockAgvActivity.this,FunctionMenuActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constant.KEY_MAIN_TO_UNLOCK,agvBean);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
        }
    }
}

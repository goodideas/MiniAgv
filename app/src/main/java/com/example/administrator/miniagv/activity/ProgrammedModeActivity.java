package com.example.administrator.miniagv.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.administrator.miniagv.R;
import com.example.administrator.miniagv.utils.ToastUtil;
import com.example.administrator.miniagv.views.ProgrammedItem;

import java.util.ArrayList;
import java.util.List;

public class ProgrammedModeActivity extends AppCompatActivity {

    private static final String TAG = "ProgrammedModeActivity";
    private Button btnProgrammed;
    private LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    private List<ProgrammedItem> programmedItemList = new ArrayList<>();
    private LinearLayout llProgrammedItem;
    private Button btnAddProgrammedItem,btnSubProgrammedItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_programmed_mode);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.setTitle("选择功能");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        llProgrammedItem = (LinearLayout)findViewById(R.id.llProgrammedItem);
        btnAddProgrammedItem = (Button)findViewById(R.id.btnAddProgrammedItem);
        btnSubProgrammedItem = (Button)findViewById(R.id.btnSubProgrammedItem);
        btnAddProgrammedItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgrammedItem programmedItem = new ProgrammedItem(ProgrammedModeActivity.this);
                programmedItem.setLayoutParams(layoutParams);
                programmedItemList.add(programmedItem);
                llProgrammedItem.addView(programmedItem);
                llProgrammedItem.invalidate();
            }
        });

        btnSubProgrammedItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(programmedItemList.size() == 0){
                    ToastUtil.customToast(ProgrammedModeActivity.this,"列表为空！！！");
                }else{
                    llProgrammedItem.removeView(programmedItemList.get(programmedItemList.size()-1));
                    programmedItemList.remove(programmedItemList.size()-1);
                    llProgrammedItem.invalidate();


                }

            }
        });


        btnProgrammed = (Button)findViewById(R.id.btnProgrammed);

        btnProgrammed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int size = programmedItemList.size();
                ProgrammedItem programmedItem;
                for(int i=0;i<size;i++){
                    programmedItem = programmedItemList.get(i);
                    Log.e(TAG,programmedItem.getEtLoc()+" "+programmedItem.getEtRFID()+" "+programmedItem.getEtContent()+" "+programmedItem.getEtSpeed()+" ");
                    ToastUtil.customToast(ProgrammedModeActivity.this,programmedItem.getEtLoc()+" "+programmedItem.getEtRFID()+" "+programmedItem.getEtContent()+" "+programmedItem.getEtSpeed()+" ");
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

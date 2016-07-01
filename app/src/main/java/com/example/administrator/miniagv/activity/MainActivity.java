package com.example.administrator.miniagv.activity;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;


import com.example.administrator.miniagv.R;
import com.example.administrator.miniagv.entity.AgvBean;
import com.example.administrator.miniagv.utils.AgvAdapter;
import com.example.administrator.miniagv.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    private Button btnConnectAgv;
    private Button btnSearchAgv;
    private ListView lvAgv;
    private List<AgvBean> list = new ArrayList<>();
    private AgvAdapter agvAdapter;
    private int c = 0;
    private int lastSelect = -1;
    private boolean isSelect = false;
    private int selected = -1;
    private LinearLayout.LayoutParams params = new
            LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("MiniAGV");
        }

        btnConnectAgv = (Button) findViewById(R.id.btnConnectAgv);
        btnSearchAgv = (Button) findViewById(R.id.btnSearchAgv);
        lvAgv = (ListView) findViewById(R.id.lvAgv);

        btnConnectAgv.setOnClickListener(this);
        btnSearchAgv.setOnClickListener(this);
        agvAdapter = new AgvAdapter(this, list);
        lvAgv.setAdapter(agvAdapter);

        View emptyView = LayoutInflater.from(this).inflate(R.layout.programmed_list_empty_layout, null);

        params.gravity = Gravity.CENTER;
                ((ViewGroup) lvAgv.getParent()).addView(emptyView,params);
        lvAgv.setEmptyView(emptyView);

        lvAgv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                if (lastSelect == position) {
                    isSelect = !isSelect;
                    agvAdapter.setSelected(position, isSelect);
                    agvAdapter.notifyDataSetChanged();
                } else {
                    isSelect = true;
                    agvAdapter.setSelected(position, true);
                    agvAdapter.notifyDataSetChanged();
                }

                ToastUtil.customToast(MainActivity.this, "setOnItemClickListener=" + String.valueOf(position));
                selected = isSelect ? position : -1;
                lastSelect = position;
            }
        });

        lvAgv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                list.remove(position);
                agvAdapter.notifyDataSetChanged();
                return true;
            }
        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnConnectAgv:
                if(selected == -1){
                    ToastUtil.customToast(this,"没有选择！！！");
                }else{
                    ToastUtil.customToast(this,"选择了"+String.valueOf(selected));
                    agvAdapter.setSelected(selected, false);
                    agvAdapter.notifyDataSetChanged();
                    selected = -1;
                    isSelect = false;
                    startActivity(new Intent(MainActivity.this, UnlockAgvActivity.class));
                }

                break;
            case R.id.btnSearchAgv:
                AgvBean agvBean = new AgvBean();
                agvBean.setGavId(String.valueOf(c++));
                list.add(agvBean);
                agvAdapter.notifyDataSetChanged();
                lvAgv.smoothScrollToPosition(list.size());
                Log.e(TAG, "listSize=" + list.size() + " c=" + c);
                break;
        }
    }
}

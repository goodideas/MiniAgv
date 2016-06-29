package com.example.administrator.miniagv.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.administrator.miniagv.R;
import com.example.administrator.miniagv.utils.ToastUtil;
import com.example.administrator.miniagv.utils.Util;
import com.example.administrator.miniagv.views.ProgrammedItem;

import java.util.ArrayList;
import java.util.List;

public class ProgrammedModeActivity extends AppCompatActivity {

    private static final String TAG = "ProgrammedModeActivity";
    private Button btnProgrammed;
    private LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    private List<ProgrammedItem> programmedItemList = new ArrayList<>();
    private LinearLayout llProgrammedItem;
    private Button btnAddProgrammedItem, btnSubProgrammedItem;
    private int btnAddHeight;
    private int isListSizeEmpty = -1;
    private ScrollView svProgrammedItem;
    private TextView tvCountItem;
    private View emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_programmed_mode);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("选择功能");
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setSubtitle("RFID编程");
        }

        llProgrammedItem = (LinearLayout) findViewById(R.id.llProgrammedItem);
        btnAddProgrammedItem = (Button) findViewById(R.id.btnAddProgrammedItem);
        btnSubProgrammedItem = (Button) findViewById(R.id.btnSubProgrammedItem);
        svProgrammedItem = (ScrollView) findViewById(R.id.svProgrammedItem);
        tvCountItem = (TextView) findViewById(R.id.tvCountItem);

        ViewTreeObserver vto2 = btnAddProgrammedItem.getViewTreeObserver();
        vto2.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                btnAddProgrammedItem.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                btnAddHeight = btnAddProgrammedItem.getHeight();
                Log.e(TAG, "btnAddHeight = " + btnAddHeight);
            }
        });

        emptyView = LayoutInflater.from(this).inflate(R.layout.programmed_list_empty_layout,null);
        if(programmedItemList.size() == 0){
            llProgrammedItem.addView(emptyView);
        }


        btnAddProgrammedItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ProgrammedItem programmedItem = new ProgrammedItem(ProgrammedModeActivity.this);
                programmedItem.setLayoutParams(layoutParams);
                programmedItem.setTvNumber(String.valueOf(programmedItemList.size() + 1));
                programmedItemList.add(programmedItem);
                llProgrammedItem.addView(programmedItem);
                llProgrammedItem.invalidate();
                if (isListSizeEmpty == -1) {
                    llProgrammedItem.removeView(emptyView);
                    animateOpen(btnSubProgrammedItem);
                    isListSizeEmpty = 1;
                }

                tvCountItem.setText(String.valueOf(programmedItemList.size()));
                svProgrammedItem.smoothScrollTo(0, llProgrammedItem.getBottom());

            }
        });


        btnSubProgrammedItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (programmedItemList.size() == 0) {
                    ToastUtil.customToast(ProgrammedModeActivity.this, "列表为空！！！");

                } else {
                    llProgrammedItem.removeView(programmedItemList.get(programmedItemList.size() - 1));
                    programmedItemList.remove(programmedItemList.size() - 1);
                    llProgrammedItem.invalidate();
                    if (programmedItemList.size() == 0) {
                        isListSizeEmpty = -1;
                        animateClose(btnSubProgrammedItem);
                        llProgrammedItem.addView(emptyView);
                    }
                    tvCountItem.setText(String.valueOf(programmedItemList.size()));

                }

            }
        });


        btnProgrammed = (Button) findViewById(R.id.btnProgrammed);
        btnProgrammed.getLayoutParams().width = Util.getScreenWidth(this) / 3;
        btnProgrammed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int size = programmedItemList.size();
                ProgrammedItem programmedItem;
                for (int i = 0; i < size; i++) {
                    programmedItem = programmedItemList.get(i);
                    Log.e(TAG, programmedItem.getEtLoc() + " " + programmedItem.getEtRFID() + " " + programmedItem.getEtContent() + " " + programmedItem.getEtSpeed() + " ");
                    ToastUtil.customToast(ProgrammedModeActivity.this, programmedItem.getEtLoc() + " " + programmedItem.getEtRFID() + " " + programmedItem.getEtContent() + " " + programmedItem.getEtSpeed() + " ");
                }

            }
        });

    }


    private void animateOpen(Button btnSub) {
        btnSub.setVisibility(View.VISIBLE);
        ValueAnimator valueAnimator = createDropAnimator(btnSub, 0, btnAddHeight);

        Log.i("Main", "animateOpen");
        valueAnimator.start();
    }

    private void animateClose(final Button btnSub) {
        Log.i("Main", "animateClose");
        ValueAnimator valueAnimator = createDropAnimator(btnSub, btnAddHeight, 0);
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                btnSub.setVisibility(View.GONE);
            }
        });
        valueAnimator.start();
    }


    private ValueAnimator createDropAnimator(final Button btnSub, int start, int end) {

        ValueAnimator valueAnimator = ValueAnimator.ofInt(start, end);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = btnSub.getLayoutParams();
                layoutParams.height = value;

                if (14 >= (value)) {
                    btnSub.setTextSize(value);
                } else {
                    btnSub.setTextSize(14);
                }
                btnSub.setLayoutParams(layoutParams);
            }
        });
        valueAnimator.setDuration(300);
        return valueAnimator;
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

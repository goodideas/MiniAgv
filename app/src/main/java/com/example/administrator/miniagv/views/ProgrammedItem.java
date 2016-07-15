package com.example.administrator.miniagv.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.administrator.miniagv.R;
import com.example.administrator.miniagv.utils.SpinnerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kevin
 * on 2016/6/28 0028.
 */
public class ProgrammedItem extends LinearLayout {

    private EditText etLoc, etRFID;
    private TextView tvNumber;
    private RadioGroup radioGroup;
    private RadioButton rbSpeed1,rbSpeed2;
    private int rbSpeed = 1;
    private int spinnerSelect = 1;
    private Spinner spinnerProgrammed;
    private SpinnerAdapter spinnerAdapter;
    private List<String> list = new ArrayList<>();

    public ProgrammedItem(Context context) {
        this(context, null);
    }


    public ProgrammedItem(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater.from(context).inflate(R.layout.programmed_item_layout, this, true);

        etLoc = (EditText) findViewById(R.id.etLoc);
        etRFID = (EditText) findViewById(R.id.etRFID);
//        etContent = (EditText) findViewById(R.id.etContent);
//        etSpeed = (EditText) findViewById(R.id.etSpeed);
        spinnerProgrammed = (Spinner)findViewById(R.id.spinnerProgrammed);
        tvNumber = (TextView) findViewById(R.id.tvNumber);
        radioGroup = (RadioGroup)findViewById(R.id.radioGroupProgrammed);
        rbSpeed1 = (RadioButton)findViewById(R.id.rbSpeed1);
        rbSpeed2 = (RadioButton)findViewById(R.id.rbSpeed2);
        list.add("前进");
        spinnerAdapter = new SpinnerAdapter(context,list);
        spinnerProgrammed.setAdapter(spinnerAdapter);
        spinnerProgrammed.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerSelect = position + 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(rbSpeed1.getId() == checkedId){
                    rbSpeed = 1;
                }else if(rbSpeed2.getId() == checkedId){
                    rbSpeed = 2;
                }
            }
        });
    }

    public String getEtLoc() {
        return etLoc.getText().toString();
    }

    public String getEtRFID() {
        return etRFID.getText().toString();
    }

//    public String getEtContent() {
//        return etContent.getText().toString();
//    }

//    public String getEtSpeed() {
//        return etSpeed.getText().toString();
//    }

    public int getRbSpeed(){
        return rbSpeed;
    }

    public int getSpinnerSelect(){
        return spinnerSelect;
    }

    public void setTvNumber(String text){
        tvNumber.setText(text);
    }

}

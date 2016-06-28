package com.example.administrator.miniagv.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.administrator.miniagv.R;

/**
 * Created by Kevin
 * on 2016/6/28 0028.
 */
public class ProgrammedItem extends LinearLayout {

    private EditText etLoc, etRFID, etContent, etSpeed;

    public ProgrammedItem(Context context){
        this(context,null);
    }


    public ProgrammedItem(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater.from(context).inflate(R.layout.programmed_item_layout, this, true);

        etLoc = (EditText) findViewById(R.id.etLoc);
        etRFID = (EditText) findViewById(R.id.etRFID);
        etContent = (EditText) findViewById(R.id.etContent);
        etSpeed = (EditText) findViewById(R.id.etSpeed);

    }

    public String getEtLoc() {
        return etLoc.getText().toString();
    }

    public String getEtRFID() {
        return etRFID.getText().toString();
    }

    public String getEtContent() {
        return etContent.getText().toString();
    }

    public String getEtSpeed() {
        return etSpeed.getText().toString();
    }


}

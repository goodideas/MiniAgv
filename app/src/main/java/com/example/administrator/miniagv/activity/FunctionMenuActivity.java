package com.example.administrator.miniagv.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.administrator.miniagv.R;

public class FunctionMenuActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btnManualMode;
    private Button btnRFIDProgrammed;
    private Button btnAutoMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_function_menu);

        btnManualMode = (Button)findViewById(R.id.btnManualMode);
        btnRFIDProgrammed = (Button)findViewById(R.id.btnRFIDProgrammed);
        btnAutoMode = (Button)findViewById(R.id.btnAutoMode);

        btnManualMode.setOnClickListener(this);
        btnRFIDProgrammed.setOnClickListener(this);
        btnAutoMode.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnAutoMode:
                startActivity(new Intent(FunctionMenuActivity.this,AutoModeActivity.class));
                break;
            case R.id.btnRFIDProgrammed:
                startActivity(new Intent(FunctionMenuActivity.this,ProgrammedModeActivity.class));
                break;
            case R.id.btnManualMode:
                startActivity(new Intent(FunctionMenuActivity.this,ManualModeActivity.class));
                break;
        }
    }
}

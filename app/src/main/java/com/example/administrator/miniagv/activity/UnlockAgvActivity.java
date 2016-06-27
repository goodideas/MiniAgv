package com.example.administrator.miniagv.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.administrator.miniagv.R;

public class UnlockAgvActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btnUnlockAgv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unlock_agv);

        btnUnlockAgv = (Button)findViewById(R.id.btnUnlockAgv);

        if (btnUnlockAgv != null) {
            btnUnlockAgv.setOnClickListener(this);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnUnlockAgv:
                startActivity(new Intent(UnlockAgvActivity.this,FunctionMenuActivity.class));
                break;
        }
    }
}

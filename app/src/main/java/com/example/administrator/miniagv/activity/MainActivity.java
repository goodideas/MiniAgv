package com.example.administrator.miniagv.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.administrator.miniagv.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btnConnectAgv;
    private Button btnSearchAgv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnConnectAgv = (Button)findViewById(R.id.btnConnectAgv);
        btnSearchAgv = (Button)findViewById(R.id.btnSearchAgv);

        btnConnectAgv.setOnClickListener(this);
        btnSearchAgv.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnConnectAgv:
                startActivity(new Intent(MainActivity.this,UnlockAgvActivity.class));
                break;
            case R.id.btnSearchAgv:

                break;
        }
    }
}

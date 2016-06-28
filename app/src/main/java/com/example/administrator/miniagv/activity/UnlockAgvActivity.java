package com.example.administrator.miniagv.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.administrator.miniagv.R;

public class UnlockAgvActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btnUnlockAgv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unlock_agv);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.setTitle("主页面");
            actionBar.setDisplayHomeAsUpEnabled(true);

        }

        btnUnlockAgv = (Button)findViewById(R.id.btnUnlockAgv);

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
                startActivity(new Intent(UnlockAgvActivity.this,FunctionMenuActivity.class));
                break;
        }
    }
}

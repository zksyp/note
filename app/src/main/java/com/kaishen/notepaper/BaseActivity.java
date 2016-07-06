package com.kaishen.notepaper;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by kaishen on 16/6/30.
 */
public abstract class BaseActivity extends AppCompatActivity{

    protected Toolbar mToolBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        afterOnCreate();
        mToolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolBar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);
    }

    protected void afterOnCreate() {
    }

    protected void setToolLeftText(String title){
        TextView tv = (TextView)findViewById(R.id.left_tv);
        tv.setVisibility(View.VISIBLE);
        tv.setText(title);
    }

    protected void setToolLeftBtn(View.OnClickListener listener){
        ImageView iv = (ImageView)findViewById(R.id.left_btn);
        iv.setVisibility(View.VISIBLE);
        iv.setOnClickListener(listener);
    }

    protected void setCountText(String count){
        TextView tv = (TextView)findViewById(R.id.count_tv);
        tv.setVisibility(View.VISIBLE);
        tv.setText(count);
    }
    protected void setToolRightBtn(View.OnClickListener listener){
        ImageView iv = (ImageView)findViewById(R.id.right_btn);
        iv.setVisibility(View.VISIBLE);
        iv.setOnClickListener(listener);
    }

    protected void setSearchBtn(View.OnClickListener listener){
        ImageView iv = (ImageView)findViewById(R.id.search_btn);
        iv.setVisibility(View.VISIBLE);
        iv.setOnClickListener(listener);
    }

    protected void setSelectStateText(String state, View.OnClickListener listener){
        TextView tv = (TextView)findViewById(R.id.select_state_tv);
        tv.setVisibility(View.VISIBLE);
        tv.setText(state);
        tv.setOnClickListener(listener);
    }
}

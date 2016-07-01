package com.kaishen.notepaper;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.Window;

/**
 * Created by kaishen on 16/6/30.
 */
public class BaseActivity extends AppCompatActivity{

    protected Toolbar mToolBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        mToolBar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(mToolBar);
//        ActionBar actionBar = getSupportActionBar();
//        if(actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);
    }

    public void setToolBarTitle(String title)
    {
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) actionBar.setTitle(title);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home)
        {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



}

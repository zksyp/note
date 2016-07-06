package com.kaishen.notepaper;

import android.graphics.drawable.Drawable;
import android.media.TimedText;
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
    protected TextView titleTv;
    protected TextView countTv;
    protected TextView selectStateTv;
    protected ImageView leftBtn;
    protected ImageView rightBtn;
    protected ImageView deleteBtn;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        afterOnCreate();

    }

    protected void initHeaderView(){
        mToolBar = (Toolbar) findViewById(R.id.toolbar);
        titleTv = (TextView)findViewById(R.id.titel_tv);
        countTv = (TextView)findViewById(R.id.count_tv);
        selectStateTv = (TextView)findViewById(R.id.select_state_tv);
        leftBtn = (ImageView) findViewById(R.id.left_btn);
        rightBtn = (ImageView) findViewById(R.id.right_btn);
        deleteBtn = (ImageView) findViewById(R.id.delete_btn);
        resetView();
        setSupportActionBar(mToolBar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);
    }

    protected void resetView() {
        titleTv.setVisibility(View.GONE);
        leftBtn.setVisibility(View.GONE);
        countTv.setVisibility(View.GONE);
        deleteBtn.setVisibility(View.GONE);
        rightBtn.setVisibility(View.GONE);
    }

    protected void afterOnCreate() {
    }

    protected void setTitleText(String title){
        titleTv.setVisibility(View.VISIBLE);
        titleTv.setText(title);
    }

    protected void setToolLeftBtn(View.OnClickListener listener){
        leftBtn.setVisibility(View.VISIBLE);
        leftBtn.setBackgroundResource(R.drawable.topbar_icon_back);
        leftBtn.setOnClickListener(listener);
    }

    protected void setCountText(String count){
        countTv.setVisibility(View.VISIBLE);
        countTv.setText(count);
    }
    protected void setToolRightBtn(String funtion, View.OnClickListener listener){
        rightBtn.setVisibility(View.VISIBLE);
        if("搜索".equals(funtion)){
            rightBtn.setBackgroundResource(R.drawable.ic_search);
        }else if("保存".equals(funtion))
        {
            rightBtn.setBackgroundResource(R.drawable.icon_tick);
        }else
        {
            rightBtn.setBackgroundResource(R.drawable.topbar_icon_delete);
        }
        rightBtn.setOnClickListener(listener);
    }

    protected void setDeleteBtn(View.OnClickListener listener){
        deleteBtn.setVisibility(View.VISIBLE);
        deleteBtn.setOnClickListener(listener);
    }

    protected void setSelectStateText(String state, View.OnClickListener listener){
        selectStateTv.setVisibility(View.VISIBLE);
        selectStateTv.setText(state);
        selectStateTv.setOnClickListener(listener);
    }
}

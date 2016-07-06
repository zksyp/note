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
        titleTv = (TextView)findViewById(R.id.title_tv);
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

    public void resetView() {
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
        leftBtn.setVisibility(View.GONE);
        countTv.setVisibility(View.GONE);

        titleTv.setText(title);
    }

    protected void setToolLeftBtn(String funtion, View.OnClickListener listener){
        leftBtn.setVisibility(View.VISIBLE);
        titleTv.setVisibility(View.GONE);
        countTv.setVisibility(View.GONE);

        if("取消".equals(funtion)) {
            leftBtn.setImageResource(R.drawable.topbar_icon_close);
            leftBtn.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        }
        else
        {
            leftBtn.setImageResource(R.drawable.topbar_icon_back);
            leftBtn.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        }
        leftBtn.setOnClickListener(listener);
    }

    protected void setCountText(String count){
        countTv.setVisibility(View.VISIBLE);
        titleTv.setVisibility(View.GONE);
        countTv.setText(count);
    }

    protected void setSelectStateText(String state, View.OnClickListener listener){
        selectStateTv.setVisibility(View.VISIBLE);
        deleteBtn.setVisibility(View.GONE);
        selectStateTv.setText(state);
        selectStateTv.setOnClickListener(listener);
    }

    protected void setSelectStateText(String state){
        selectStateTv.setText(state);
    }

    protected void setDeleteBtn(View.OnClickListener listener){
        deleteBtn.setVisibility(View.VISIBLE);
        selectStateTv.setVisibility(View.GONE);
        rightBtn.setVisibility(View.GONE);

        deleteBtn.setOnClickListener(listener);
    }

    protected void setToolRightBtn(String funtion, View.OnClickListener listener){
        rightBtn.setVisibility(View.VISIBLE);
        deleteBtn.setVisibility(View.GONE);

        if("搜索".equals(funtion)){
            rightBtn.setImageResource(R.drawable.ic_search);
            rightBtn.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        }else if("保存".equals(funtion))
        {
            rightBtn.setImageResource(R.drawable.icon_tick);
            rightBtn.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        }else
        {
            rightBtn.setImageResource(R.drawable.topbar_icon_delete);
            rightBtn.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        }
        rightBtn.setOnClickListener(listener);
    }

     protected  void commonMode(String title, View.OnClickListener rightListener){
         titleTv.setVisibility(View.VISIBLE);
         rightBtn.setVisibility(View.VISIBLE);

         titleTv.setText(title);
         rightBtn.setImageResource(R.drawable.ic_search);
         rightBtn.setOnClickListener(rightListener);
     }

    protected void selectedMode(View.OnClickListener leftListener, View.OnClickListener selectedListener,
                                View.OnClickListener rightListener)
    {
        leftBtn.setVisibility(View.VISIBLE);
        countTv.setVisibility(View.VISIBLE);
        selectStateTv.setVisibility(View.VISIBLE);
        rightBtn.setVisibility(View.VISIBLE);

        leftBtn.setImageResource(R.drawable.topbar_icon_close);
        leftBtn.setOnClickListener(leftListener);
        selectStateTv.setOnClickListener(selectedListener);
        rightBtn.setImageResource(R.drawable.topbar_icon_delete);
        rightBtn.setOnClickListener(rightListener);

    }

    protected void viewMode(View.OnClickListener leftListener, View.OnClickListener deleteListener){
        leftBtn.setVisibility(View.VISIBLE);
        deleteBtn.setVisibility(View.VISIBLE);

        leftBtn.setImageResource(R.drawable.topbar_icon_back);
        leftBtn.setOnClickListener(leftListener);
        deleteBtn.setOnClickListener(deleteListener);
    }

    protected void editMode(View.OnClickListener leftListner, View.OnClickListener rightListener){
        leftBtn.setVisibility(View.VISIBLE);
        rightBtn.setVisibility(View.VISIBLE);

        leftBtn.setImageResource(R.drawable.topbar_icon_back);
        leftBtn.setOnClickListener(leftListner);
        rightBtn.setImageResource(R.drawable.icon_tick);
        rightBtn.setOnClickListener(rightListener);
    }
}

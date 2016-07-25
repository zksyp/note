package com.kaishen.notepaper.utils;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.kaishen.notepaper.utils.AnimationUtil;


/**
 * Created by kaishen on 16/7/22.
 */
public class ScaleDownShowBehavior extends FloatingActionButton.Behavior {

    public ScaleDownShowBehavior(Context context, AttributeSet attrs) {
        super();
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View directTargetChild, View target, int nestedScrollAxes) {
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }

    private boolean isAnimatingOut = false;

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        if((dyConsumed > 0 || dyUnconsumed > 0) && !isAnimatingOut && child.getVisibility() == View.VISIBLE){ //手指上滑,隐藏FAB
            AnimationUtil.scaleHide(child, listener);
        }else if((dyConsumed < 0 || dyUnconsumed < 0) && child.getVisibility() != View.VISIBLE){
            AnimationUtil.scaleShow(child, null); //手指下滑, 显示FAB
        }
    }

    public static <V extends View> ScaleDownShowBehavior from(V view) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if(!(params instanceof CoordinatorLayout.LayoutParams)){
            throw new IllegalArgumentException("The view is not a child of CoordinatorLayout");
        }
        CoordinatorLayout.Behavior behavior = ((CoordinatorLayout.LayoutParams) params).getBehavior();
        if(!(behavior instanceof  ScaleDownShowBehavior)){
            throw new IllegalArgumentException("This view is not associate with ScaleDownShowBehavior");
        }
        return (ScaleDownShowBehavior) behavior;
    }

    private ViewPropertyAnimatorListener listener = new ViewPropertyAnimatorListener() {
        @Override
        public void onAnimationStart(View view) {
            isAnimatingOut = true;
        }

        @Override
        public void onAnimationEnd(View view) {
            isAnimatingOut = false;
            view.setVisibility(View.GONE);

        }

        @Override
        public void onAnimationCancel(View view) {

            isAnimatingOut = false;
        }
    };
}

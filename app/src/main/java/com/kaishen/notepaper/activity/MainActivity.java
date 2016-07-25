package com.kaishen.notepaper.activity;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.kaishen.notepaper.adapter.ListItemAdapter;
import com.kaishen.notepaper.entry.NoteBean;
import com.kaishen.notepaper.R;
import com.kaishen.notepaper.db.DataSource;
import com.kaishen.notepaper.utils.ScaleDownShowBehavior;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.OvershootInterpolator;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;


public class MainActivity extends BaseActivity {

    private RecyclerView mNoteRv;
    private ListItemAdapter mAdapter;
    private FloatingActionButton mFabBtn;
    private List<NoteBean> noteBeanList;
    private boolean state = false;
    private boolean isAllSelect = false;
    private boolean isShow = true;
    private RecyclerView.OnScrollListener mScrollListener;
    private View.OnTouchListener mTouchListener;
    private Set<String> idSet = new HashSet<>();

    public Set<Integer> positionSet = new HashSet<>();
    public static MainActivity instance;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void afterOnCreate() {
        instance = this;
        setContentView(R.layout.activity_main);
        initHeaderView();
        mNoteRv = (RecyclerView) findViewById(R.id.rv_note);
        mFabBtn = (FloatingActionButton) findViewById(R.id.fabButton);
        loadData();
        setOnScrollListener();
//        setOnTouchListener();
        commonMode("便签", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mNoteRv.setLayoutManager(linearLayoutManager);
        mFabBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("TYPE","NEW");
                intent.setClass(MainActivity.this, NoteEditActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        loadData();
    }

    public void loadData() {
        DataSource dataSource = new DataSource(this);
        dataSource.open();
        noteBeanList = dataSource.getSortNoteList();
        mAdapter = new ListItemAdapter(this, noteBeanList);
//        AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(mAdapter);
//        alphaAdapter.setDuration(1000);
//        alphaAdapter.setInterpolator(new OvershootInterpolator());
//        mNoteRv.setAdapter(alphaAdapter);
        mNoteRv.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new ListItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (state) {
                    addOrRemove(position);

                } else {
                    Intent intent = new Intent();
                    String id = noteBeanList.get(position).getId();
                    intent.putExtra("ID", id);
                    intent.putExtra("TYPE", "EDIT");
                    intent.setClass(MainActivity.this, NoteEditActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {
                mAdapter.setSelect(true);
                mAdapter.notifyDataSetChanged();
                animatorForGone();
                isShow = false;
                resetView();
                state = true;
                selectedMode(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        animatorForVisible();
                        isShow = true;
                        positionSet.clear();
                        resetView();
                        commonMode("便签", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        });
                        state = false;
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setSelectStateText("全选", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (!isAllSelect) {
                                    for (int i = 0; i < noteBeanList.size(); i++) {
                                        positionSet.add(i);
                                    }
                                    setSelectStateText("取消全选");
                                    isAllSelect = true;
                                } else {
                                    positionSet.clear();
                                    isAllSelect = false;
                                    setSelectStateText("全选");

                                }
                            }
                        });
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            }
        });

    }


    public void addOrRemove(int position) {
        if (positionSet.contains(position)) {
            positionSet.remove(position);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mAdapter.resetBackground();
                    mAdapter.notify();
                }
            });
        } else {
            positionSet.add(position);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mAdapter.setBackground();
                    mAdapter.notify();
                }
            });
        }

        if (positionSet.size() == 0) {
            animatorForVisible();
            isShow = true;
            resetView();
            commonMode("便签", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this, SearchActivity.class);
                    startActivity(intent);
                }
            });
            state = false;
        } else {
            setCountText(positionSet.size() + "");
            mAdapter.notifyDataSetChanged();
        }
    }


//    public void setOnTouchListener() {
//        if(mNoteRv == null){
//            return;
//        }
//        if(mTouchListener == null){
//            mTouchListener = new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//                    if (MotionEvent.ACTION_DOWN == event.getAction())
//                    {
//                        animatorForGone();
//                    }
//                    if(MotionEvent.ACTION_MOVE == event.getAction())
//                    {
//                        animatorForGone();
//                    }
//                    if(MotionEvent.ACTION_UP == event.getAction())
//                    {
//                        animatorForVisible();
//                    }
//                    return false;
//                }
//            };
//        }
//        mNoteRv.setOnTouchListener(mTouchListener);
//
//    }

    /**
     * 为RecyclerView设置下拉刷新及floatingActionButton的消失出现
     */
    private void setOnScrollListener() {
        if (mNoteRv == null) {
            return;
        }
        if (mScrollListener == null) {
            mScrollListener = new RecyclerView.OnScrollListener() {

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    int y = dy;
                    if (y != dy && isShow) {
                        animatorForGone();
                    } else if (y == dy && isShow) {
                        animatorForVisible();
                    }
                }
            };
            mNoteRv.addOnScrollListener(mScrollListener);
        }
    }

    /**
     * 为floatingActionBar的出现消失设置动画效果
     */
    private void animatorForGone() {
        Animator anim = AnimatorInflater.loadAnimator(this, R.animator.scale_gone);
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animator) {
                mFabBtn.setVisibility(View.GONE);
            }
        });
        anim.setTarget(mFabBtn);
        anim.start();
    }

    private void animatorForVisible() {
        Animator anim = AnimatorInflater.loadAnimator(this, R.animator.scale_visible);
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animator) {
                mFabBtn.setVisibility(View.VISIBLE);
            }
        });
        anim.setTarget(mFabBtn);
        anim.start();
    }

}

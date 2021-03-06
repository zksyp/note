package com.kaishen.notepaper.activity;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.kaishen.notepaper.adapter.ListItemAdapter;
import com.kaishen.notepaper.entity.NoteBean;
import com.kaishen.notepaper.R;
import com.kaishen.notepaper.db.DataSource;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


public class MainActivity extends BaseActivity {

    private RecyclerView mNoteRv;
    private ListItemAdapter mAdapter;
    private FloatingActionButton mFabBtn;
    private List<NoteBean> noteBeanList;
    private DataSource dataSource = new DataSource(this);
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
        dataSource.open();
        noteBeanList = dataSource.getSortNoteList();
        for(int i = 0; i < noteBeanList.size(); i++){
            noteBeanList.get(i).setChosen(false);
        }
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
            public void onItemLongClick(View view, final int position) {
                mAdapter.setSelect(true);
                mAdapter.notifyDataSetChanged();
                positionSet.clear();
                animatorForGone();
                isShow = false;
                resetView();
                addOrRemove(position);
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
                        if (!isAllSelect) {
                            for (int i = 0; i < noteBeanList.size(); i++) {
                                positionSet.add(i);
                                noteBeanList.get(i).setChosen(true);
                            }
                            mAdapter.notifyDataSetChanged();
                            setCountText(positionSet.size() + "");
                            setSelectStateText("取消全选");
                            isAllSelect = true;
                        } else {
                            for (int i = 0; i < noteBeanList.size(); i++) {
                                positionSet.remove(i);
                                noteBeanList.get(i).setChosen(false);
                            }
                            mAdapter.notifyDataSetChanged();
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
                        }
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this).setTitle("提示")
                                .setMessage("将会删除选中便签").setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }).setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        dataSource.open();
                                        positionSet.clear();
                                        for(int i = noteBeanList.size() - 1; i >= 0; i--){
                                            if(noteBeanList.get(i).isChosen()){
                                                dataSource.deleteNote(noteBeanList.get(i).getId());
                                                Log.e("id","" + i);
                                                noteBeanList.remove(i);
                                            }
                                        }
                                        mAdapter.notifyDataSetChanged();
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
                                    }
                                });
                        dialog.show();

                    }
                });
            }
        });

    }


    public void addOrRemove(final int position) {
        if (positionSet.contains(position)) {
            positionSet.remove(position);
            noteBeanList.get(position).setChosen(false);
            mAdapter.notifyDataSetChanged();
        } else {
            positionSet.add(position);
            noteBeanList.get(position).setChosen(true);
            mAdapter.notifyDataSetChanged();
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
            if(positionSet.size() == noteBeanList.size()){
                isAllSelect = true;
                setSelectStateText("取消全选");
            }else
            {
                isAllSelect = false;
            }
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

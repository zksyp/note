package com.kaishen.notepaper;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class MainActivity extends BaseActivity {

    private RecyclerView mNoteRv;
    private ListItemAdapter mAdapter;
    private FloatingActionButton mFabBtn;
    private List<NoteBean> noteBeanList;
    private boolean state = false;
    private boolean isAllSelect = false;
    private boolean isShow = true;
    private RecyclerView.OnScrollListener mScrollListener;

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
                intent.putExtra("ID", noteBeanList.size() + 1);
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
//        List<NoteBean> test = dataSource.getSearchNoteList("的");
//        for(int i = 0;i < test.size(); i++)
//        {
//            Log.e("searchlist", test.get(i).getTime());
//
//        }
//        Log.e("note", noteBeanList.toString());
        mAdapter = new ListItemAdapter(this, noteBeanList);
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
                    intent.setClass(MainActivity.this, NoteEditActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {
                mAdapter.setChecked(true);
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
        } else {
            positionSet.add(position);
        }

        if (positionSet.size() == 0) {
            animatorForVisible();
            isShow = true;
            resetView();
            commonMode("便签", new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            state = false;
        } else {
            setCountText(positionSet.size() + "");
            mAdapter.notifyDataSetChanged();
        }
    }

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
                        y = dy;
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

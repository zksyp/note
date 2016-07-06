package com.kaishen.notepaper;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

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
        commonMode("便签", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
//        setTitleText("便签");
//        setToolRightBtn("搜索", new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
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
        noteBeanList = dataSource.getNoteList();
        mAdapter = new ListItemAdapter(this, noteBeanList);
        mNoteRv.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new ListItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (state == true) {
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
                resetView();
                state = true;
                selectedMode(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setSelectStateText("全选", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (isAllSelect = false) {
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

}

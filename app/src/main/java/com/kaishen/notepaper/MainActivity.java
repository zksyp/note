package com.kaishen.notepaper;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.List;


public class MainActivity extends BaseActivity implements ListItemAdapter.MyItemClickListener {

    private RecyclerView mNoteRv;
    private ListItemAdapter mAdapter;
    private FloatingActionButton mFabBtn;
    private NoteBean noteInfo;
    private List<NoteBean> noteBeanList;
    private com.kaishen.notepaper.FloatingActionButton mFABtn;


    @Override
    protected void afterOnCreate() {
        setContentView(R.layout.activity_main);
        mNoteRv = (RecyclerView) findViewById(R.id.rv_note);
        mFabBtn = (FloatingActionButton) findViewById(R.id.fabButton);
        loadData();
        setToolLeftText("便签");
        setSearchBtn(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mNoteRv.setLayoutManager(linearLayoutManager);
        mFabBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("ID", noteBeanList.size()+1);
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
//        if(noteBeanList.get(0) != null)
//        {
//            Log.e("noteinfo", noteBeanList.get(0).getNote());
//        }
        mAdapter = new ListItemAdapter(this, noteBeanList);
        mNoteRv.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(View view, int postion) {
        Intent intent = new Intent();
        String id = noteBeanList.get(postion).getId();
        intent.putExtra("ID", id);
        intent.setClass(this, NoteEditActivity.class);
        startActivity(intent);
    }
}

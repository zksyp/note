package com.kaishen.notepaper;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

public class MainActivity extends BaseActivity{

    private RecyclerView mNoteRv;
    private ListItemAdapter mAdapter;
    private FloatingActionButton mFabBtn;
    private NoteBean noteInfo;
    private List<NoteBean> noteBeanList;
    private Button mEditBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mNoteRv = (RecyclerView) findViewById(R.id.rv_note);
        loadData();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mNoteRv.setLayoutManager(linearLayoutManager);
        mEditBtn = (Button) findViewById(R.id.btn_edit);

        mEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("ID", 1 + "");
                intent.setClass(MainActivity.this, NoteEditActivity.class);
                startActivity(intent);
            }
        });

//        mNoteRv.addItemDecoration(new DividerItemDecoration(getActivity(),linearLayoutManager.getOrientation()));

    }

    public void loadData() {
        DataSource dataSource = new DataSource(this);
        dataSource.open();
        noteBeanList = dataSource.getNoteList();
        mAdapter = new ListItemAdapter(this, noteBeanList);
        mNoteRv.setAdapter(mAdapter);
    }
}

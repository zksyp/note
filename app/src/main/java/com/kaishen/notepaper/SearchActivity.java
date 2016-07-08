package com.kaishen.notepaper;

import android.content.Intent;
import android.os.Bundle;
import android.sax.StartElementListener;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by kaishen on 16/7/7.
 */
public class SearchActivity extends BaseActivity {

    private RecyclerView mSearchRv;
    private ListItemAdapter mSearchAdapter;
    private List<NoteBean> searchNoteBeanList;
    DataSource ds = new DataSource(this);
    private EditText mSearchTv;
    private ImageView mClearBtn;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void afterOnCreate() {
        super.afterOnCreate();
        setContentView(R.layout.activity_search);
        initHeaderView();
        mSearchRv = (RecyclerView) findViewById(R.id.rv_note);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mSearchRv.setLayoutManager(linearLayoutManager);
        searchTv.setVisibility(View.VISIBLE);
        searchTv.setBackground(null);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);
        if (searchTv.getText().toString().equals("")) {
            rightBtn.setVisibility(View.GONE);
        } else {
            rightBtn.setVisibility(View.VISIBLE);
            rightBtn.setBackgroundResource(R.drawable.topbar_icon_close);
            rightBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    searchTv.setText("");
                    mSearchRv.setAdapter(null);
                    mSearchAdapter.notifyDataSetChanged();
                }
            });
        }
//        mSearchTv = searchMode();
//        mClearBtn = setToolRightBtn(R.drawable.topbar_icon_close);
//        if(!mSearchTv.getText().toString().equals(""))
//        {
//            mClearBtn.setVisibility(View.VISIBLE);
//            mClearBtn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    mSearchTv.setText("");
//                    mSearchRv.setAdapter(null);
//                    mSearchAdapter.notifyDataSetChanged();
//                }
//            });
//        }else
//        {
//            mClearBtn.setVisibility(View.GONE);
//        }
        loadData();
    }

    public void loadData() {
        ds.open();
//                List<NoteBean> test = ds.getSearchNoteList("度");
//        for (int i = 0; i < test.size(); i++) {
//            Log.e("searchlist", test.get(i).getTime() + "/n" + test.get(i).getNote());
//        }
        searchTv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() > 0){
                    rightBtn.setVisibility(View.VISIBLE);
                    rightBtn.setImageResource(R.drawable.topbar_icon_close);
                    rightBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            searchTv.setText("");
                            rightBtn.setVisibility(View.GONE);
                            mSearchRv.setAdapter(null);
                            mSearchAdapter.notifyDataSetChanged();
                        }
                    });
                    ds.open();
                    searchNoteBeanList = ds.getSearchNoteList(searchTv.getText().toString());
                    mSearchAdapter = new ListItemAdapter(SearchActivity.this, searchNoteBeanList);
                    mSearchRv.setAdapter(mSearchAdapter);
                    mSearchAdapter.setOnItemClickListener(new ListItemAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            Intent intent = new Intent();
                            String id = searchNoteBeanList.get(position).getId();
                            intent.putExtra("ID", id);
                            intent.setClass(SearchActivity.this, NoteEditActivity.class);
                            startActivity(intent);
                        }

                        @Override
                        public void onItemLongClick(View view, int position) {

                        }
                    });
                }

            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
//        if (!searchTv.getText().toString().equals("")) {
//            searchNoteBeanList = ds.getSearchNoteList(searchTv.getText().toString());
//            mSearchAdapter = new ListItemAdapter(this, searchNoteBeanList);
//            mSearchRv.setAdapter(mSearchAdapter);
//            mSearchAdapter.setOnItemClickListener(new ListItemAdapter.OnItemClickListener() {
//                @Override
//                public void onItemClick(View view, int position) {
//                    Intent intent = new Intent();
//                    String id = searchNoteBeanList.get(position).getId();
//                    intent.putExtra("ID", id);
//                    intent.setClass(SearchActivity.this, NoteEditActivity.class);
//                    startActivity(intent);
//                }
//
//                @Override
//                public void onItemLongClick(View view, int position) {
//
//                }
//            });
//        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent setIntent = new Intent();
                setIntent.setClass(SearchActivity.this, MainActivity.class);
                SearchActivity.this.finish();
                startActivity(setIntent);
                break;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            SearchActivity.this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}

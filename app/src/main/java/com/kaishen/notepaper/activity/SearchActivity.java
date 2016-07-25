package com.kaishen.notepaper.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.kaishen.notepaper.adapter.ListItemAdapter;
import com.kaishen.notepaper.entry.NoteBean;
import com.kaishen.notepaper.R;
import com.kaishen.notepaper.db.DataSource;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kaishen on 16/7/7.
 */
public class SearchActivity extends BaseActivity {

    private RecyclerView mSearchRv;
    private ListItemAdapter mSearchAdapter;
    private List<NoteBean> searchNoteBeanList = new ArrayList<>();
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
        mSearchAdapter = new ListItemAdapter(SearchActivity.this, searchNoteBeanList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mSearchRv.setLayoutManager(linearLayoutManager);
        searchTv.setVisibility(View.VISIBLE);
        searchTv.setBackground(null);
        mSearchRv.setAdapter(mSearchAdapter);
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
                if (s.length() > 0) {
                    rightBtn.setVisibility(View.VISIBLE);
                    rightBtn.setImageResource(R.drawable.topbar_icon_close);
                    rightBtn.setScaleType(ImageView.ScaleType.CENTER);
                    rightBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            searchTv.setText("");
                            rightBtn.setVisibility(View.GONE);
//                            searchTv.setInputType(InputType.TYPE_NULL);
                            searchNoteBeanList.clear();
                            mSearchAdapter.notifyDataSetChanged();
                        }
                    });
                    ds.open();
                    searchNoteBeanList.clear();
                    searchNoteBeanList.addAll(ds.getSearchNoteList(searchTv.getText().toString()));
                    mSearchAdapter.notifyDataSetChanged();
                    mSearchAdapter.setOnItemClickListener(new ListItemAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            Intent intent = new Intent();
                            String id = searchNoteBeanList.get(position).getId();
                            intent.putExtra("ID", id);
                            intent.putExtra("TYPE", "EDIT");
                            intent.setClass(SearchActivity.this, NoteEditActivity.class);
                            startActivity(intent);
                        }

                        @Override
                        public void onItemLongClick(View view, int position) {

                        }
                    });
                }
                else if(s.length() == 0){
                    rightBtn.setVisibility(View.GONE);
                    searchNoteBeanList.clear();
//                    searchTv.setInputType(InputType.TYPE_NULL);
                    mSearchAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                SearchActivity.this.finish();
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

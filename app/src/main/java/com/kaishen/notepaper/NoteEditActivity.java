package com.kaishen.notepaper;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.EditText;

/**
 * Created by kaishen on 16/7/1.
 */
public class NoteEditActivity extends BaseActivity{

    private EditText mNoteEt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        mNoteEt = (EditText) findViewById(R.id.et_note);
        loadData();
        getEvent();
    }

    public void getEvent() {

    }

    public void loadData() {
        Intent intent = getIntent();
        DataSource ds = new DataSource(this);
        ds.open();
        String id = intent.getStringExtra("ID");
        NoteBean nb;
        nb = ds.getNoteOfId(id);
        if(nb != null) {
            mNoteEt.setText(nb.getNote());
            mNoteEt.setFocusableInTouchMode(true);
        }
        else {
            mNoteEt.setText("");
            mNoteEt.setFocusable(true);
        }
    }
}

package com.kaishen.notepaper;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by kaishen on 16/7/1.
 */
public class NoteEditActivity extends BaseActivity {

    private EditText mNoteEt;
    private android.widget.Button mSaveBtn;
    private DataSource ds = new DataSource(this);
    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日 HH:mm ");//获取当前系统时间
    private ImageView mLeft;
    private ImageView mRight;


    @Override
    protected void afterOnCreate() {
        setContentView(R.layout.activity_edit);
        mNoteEt = (EditText) findViewById(R.id.et_note);
        setToolLeftBtn(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        setToolRightBtn("保存", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id;
                Intent intent = getIntent();
                String getId = intent.getStringExtra("ID");
                if (getId == null) {
                    id = ds.getCount() + 1 + "";
                } else {
                    id = getId;
                }
                ds.open();
                String content = mNoteEt.getText().toString();
                Date curDate = new Date(System.currentTimeMillis());//获取当前时间
                String time = formatter.format(curDate);
                ds.insertOrUpDateNote(id, content, time);
                Intent setIntent = new Intent();
                intent.setClass(NoteEditActivity.this, MainActivity.class);
                startActivity(setIntent);
            }
        });
        loadData();
        getEvent();
    }

    public void getEvent() {

    }

    public void loadData() {
        Intent intent = getIntent();
        ds.open();
        String id = intent.getStringExtra("ID");
        NoteBean nb;
        nb = ds.getNoteOfId(id);
        if (nb != null) {
            mNoteEt.setText(nb.getNote());
            mNoteEt.setFocusableInTouchMode(true);
        } else {
            mNoteEt.setText("");
            mNoteEt.setFocusable(true);
            mNoteEt.setFocusableInTouchMode(true);
        }
    }
}

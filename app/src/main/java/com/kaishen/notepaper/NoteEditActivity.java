package com.kaishen.notepaper;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.MenuItem;
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
    private DataSource ds = new DataSource(this);
    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日 HH:mm ");//获取当前系统时间
    private String getId;

    @Override
    protected void afterOnCreate() {
        setContentView(R.layout.activity_edit);
        initHeaderView();
        mNoteEt = (EditText) findViewById(R.id.et_note);
        viewMode(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(NoteEditActivity.this).setTitle("提示")
                        .setMessage("将会删除此便签").setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                ds.open();
                                ds.deleteNote(getId);
                                Intent intent = new Intent();
                                intent.setClass(NoteEditActivity.this, MainActivity.class);
                                startActivity(intent);
                            }
                        });
                dialog.show();
            }
        });
        mNoteEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    resetView();
                    editMode(new View.OnClickListener() {
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
                            setIntent.setClass(NoteEditActivity.this, MainActivity.class);
                            NoteEditActivity.this.finish();
                            startActivity(setIntent);
                        }
                    });
                }
            }
        });
        loadData();
    }

    public void loadData() {
        Intent intent = getIntent();
        ds.open();
        getId = intent.getStringExtra("ID");
        NoteBean nb;
        nb = ds.getNoteOfId(getId);
        if (nb != null) {
            mNoteEt.setText(nb.getNote());
            mNoteEt.setFocusableInTouchMode(true);
        } else {
            mNoteEt.setText("本地无文件");
            mNoteEt.setFocusable(true);
            mNoteEt.setFocusableInTouchMode(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent setIntent = new Intent();
                setIntent.setClass(NoteEditActivity.this, MainActivity.class);
                NoteEditActivity.this.finish();
                startActivity(setIntent);
                break;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            NoteEditActivity.this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}

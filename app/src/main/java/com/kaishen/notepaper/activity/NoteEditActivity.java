package com.kaishen.notepaper.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.kaishen.notepaper.entity.NoteBean;
import com.kaishen.notepaper.R;
import com.kaishen.notepaper.db.DataSource;

import java.io.File;
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
    private NoteBean nb;

    @Override
    protected void afterOnCreate() {
        setContentView(R.layout.activity_edit);
        initHeaderView();
        mNoteEt = (EditText) findViewById(R.id.et_note);
        Intent intent = getIntent();
        if ("NEW".equals(intent.getStringExtra("TYPE"))) {
            mNoteEt.setFocusable(true);
            mNoteEt.setFocusableInTouchMode(true);
            mNoteEt.requestFocus();
            editMode(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkSave();
                }
            });
        } else {
            resetView();
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
                                    NoteEditActivity.this.finish();
                                }
                            });
                    dialog.show();
                }
            }, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    shareMsg(NoteEditActivity.this, "分享至", "", nb.getNote(), "");
                }
            });
        }

        mNoteEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    resetView();
                    editMode(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            checkSave();
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
        nb = ds.getNoteOfId(getId);
        if (nb != null) {
            mNoteEt.setText(nb.getNote());
            mNoteEt.setFocusableInTouchMode(true);
        } else {
            mNoteEt.setFocusable(true);
            mNoteEt.setFocusableInTouchMode(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NoteEditActivity.this.finish();
                break;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            checkSave();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public static void shareMsg(Context context, String activityTitle, String msgTitle, String msgText, String imgPath) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        if (imgPath == null || imgPath.equals("")) {
            intent.setType("text/plain");
        } else {
            File f = new File(imgPath);
            if (f != null && f.exists() && f.isFile()) {
                intent.setType("image/*");
                Uri u = Uri.fromFile(f);
                intent.putExtra(Intent.EXTRA_STREAM, u);
            }
        }
        intent.putExtra(Intent.EXTRA_SUBJECT, msgTitle);
        intent.putExtra(Intent.EXTRA_TEXT, msgText);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(Intent.createChooser(intent, activityTitle));
    }

    public void checkSave(){
        if (mNoteEt.getText().toString().equals("")) {
            Intent setIntent = new Intent();
            setIntent.setClass(NoteEditActivity.this, MainActivity.class);
            NoteEditActivity.this.finish();
            startActivity(setIntent);
        } else {
            String id;
            Intent intent = getIntent();
            getId = intent.getStringExtra("ID");
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
            Toast.makeText(NoteEditActivity.this, "便签已保存", Toast.LENGTH_SHORT).show();
            setIntent.setClass(NoteEditActivity.this, MainActivity.class);
            NoteEditActivity.this.finish();
            startActivity(setIntent);
        }
    }

}

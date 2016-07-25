package com.kaishen.notepaper.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.kaishen.notepaper.entry.NoteBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kaishen on 16/6/30.
 */
public class DataSource {

    private SQLiteDatabase mDataBase;
    private DBHelper mDBHelper;
    private String[] allColumnsForNote = {
            DBHelper.COLUMN_ID,
            DBHelper.COLUMN_CONTENT,
            DBHelper.COLUMN_TIME
    };

    //创建DBHelper通过open()得到数据库
    public DataSource(Context context) { mDBHelper = DBHelper.getDbHelper(context);}

    //通过DBHelper实例读写数据库
    public void open() throws SQLException{
        mDataBase = mDBHelper.getWritableDatabase();
    }

    //通过DBHelper实例插入数据
    private void insertNote(String id, String content, String time){
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_ID, id);
        values.put(DBHelper.COLUMN_CONTENT, content);
        values.put(DBHelper.COLUMN_TIME, time);
        mDataBase.insert(DBHelper.TABLE_NAME_NOTE_LIST, null, values);
    }

    //通过DBHelper实例修改数据
    private void upDateNote(String id, String content, String time) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_ID, id);
        values.put(DBHelper.COLUMN_CONTENT, content);
        values.put(DBHelper.COLUMN_TIME, time);
        mDataBase.update(DBHelper.TABLE_NAME_NOTE_LIST, values, DBHelper.COLUMN_ID + "=" + id, null);
    }

    //通过id在数据库中找到数据
    public NoteBean getNoteOfId(String id){
        Cursor cursor = mDataBase.query(DBHelper.TABLE_NAME_NOTE_LIST, allColumnsForNote,
                DBHelper.COLUMN_ID + "=" + id, null, null, null, null);
        cursor.moveToFirst();
        NoteBean note = CursortoNote(cursor);
        cursor.close();
        return note;
    }

    public NoteBean CursortoNote(Cursor cursor) {
        if(cursor != null && cursor.getCount() > 0)
        {
            String id = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_ID));
            String content = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_CONTENT));
            String time = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_TIME));
            NoteBean note = new NoteBean();
            note.setId(id);
            note.setNote(content);
            note.setTime(time);
            return note;
        }
        return null;
    }

    public void insertOrUpDateNote(String id, String content, String time){
        if(getNoteOfId(id) == null){
            insertNote(id, content, time);
        }
        else
        {
            upDateNote(id, content, time);
        }

    }

    //获取以ID排序的数据
    public List<NoteBean> getNoteList(){
        Cursor cursor = mDataBase.query(
                DBHelper.TABLE_NAME_NOTE_LIST, allColumnsForNote, null, null, null, null, null);
        cursor.moveToFirst();
        List<NoteBean> res = new ArrayList<>();
        if (cursor.getCount() == 0) return res;
        do {
            String id = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_ID));
            String content = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_CONTENT));
            String time = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_TIME));
            NoteBean note = new NoteBean();
            note.setId(id);
            note.setNote(content);
            note.setTime(time);
            res.add(note);
        } while (cursor.moveToNext());
        cursor.close();
        return res;
    }

    //通过DBHelper实例删除数据
    public void deleteNote(String id){
        mDataBase.delete(DBHelper.TABLE_NAME_NOTE_LIST, DBHelper.COLUMN_ID + "=" + id, null);
    }

    //获取表中数据的条数总数以便确定id
    public int getCount()
    {
        Cursor cursor = mDataBase.query(
                DBHelper.TABLE_NAME_NOTE_LIST, allColumnsForNote, null, null, null, null, null);
        cursor.moveToFirst();
        return cursor.getCount();
    }

    //按修改时间降序排序获得NoteList
    public List<NoteBean> getSortNoteList(){
        Cursor cursor = mDataBase.query(
                DBHelper.TABLE_NAME_NOTE_LIST, allColumnsForNote, null, null, null, null,
                DBHelper.COLUMN_TIME +" DESC");
        cursor.moveToFirst();
        List<NoteBean> res = new ArrayList<>();
        if (cursor.getCount() == 0) return res;
        do {
            String id = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_ID));
            String content = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_CONTENT));
            String time = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_TIME));
            NoteBean note = new NoteBean();
            note.setId(id);
            note.setNote(content);
            note.setTime(time);
            res.add(note);
        } while (cursor.moveToNext());
        cursor.close();
        return res;
    }

    //以内容相关的模糊查询并按时间降序排序
    public List<NoteBean> getSearchNoteList(String sea){

        Cursor cursor = mDataBase.query(
                DBHelper.TABLE_NAME_NOTE_LIST, allColumnsForNote, DBHelper.COLUMN_CONTENT
                        + " LIKE '%" + sea +"%'", null, null, null, DBHelper.COLUMN_TIME +" DESC");
        cursor.moveToFirst();
        List<NoteBean> res = new ArrayList<>();
        if (cursor.getCount() == 0) return res;
        do {
            String id = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_ID));
            String content = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_CONTENT));
            String time = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_TIME));
            NoteBean note = new NoteBean();
            note.setId(id);
            note.setNote(content);
            note.setTime(time);
            res.add(note);
        } while (cursor.moveToNext());
        cursor.close();
        return res;
    }
}

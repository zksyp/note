package com.kaishen.notepaper.entity;

/**
 * Created by kaishen on 16/6/30.
 */
public class NoteBean {

    private String id;
    private String note;
    private String time;
    private boolean isChosen;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isChosen() {
        return isChosen;
    }

    public void setChosen(boolean chosen) {
        isChosen = chosen;
    }
}

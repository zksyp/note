package com.kaishen.notepaper;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.ColorRes;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.List;
import java.util.Set;

import butterknife.OnItemClick;

/**
 * Created by kaishen on 16/6/30.
 */
public class ListItemAdapter extends RecyclerView.Adapter<ListItemAdapter.ItemViewHolder> {

    private List<NoteBean> noteList;
    private Context mContext;
    private OnItemClickListener onItemClickListener;
    private Set<Integer> positionSet;

    public ListItemAdapter(Context context, List<NoteBean> noteList) {
        mContext = context;
        this.noteList = noteList;
    }

    @Override
    public ListItemAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemViewHolder holder = new ItemViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_layout, parent, false));
        return holder;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public void onBindViewHolder(ListItemAdapter.ItemViewHolder holder, final int position) {
        holder.noteTv.setText(noteList.get(position).getNote());
        holder.timeTv.setText(noteList.get(position).getTime());
        positionSet = MainActivity.instance.positionSet;
        if (onItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(v, position);
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (positionSet.contains(position)) {
                        v.setBackgroundColor(mContext.getResources().getColor(R.color.grey));
                        v.findViewById(R.id.checkbox).setVisibility(View.VISIBLE);
                        v.findViewById(R.id.checkbox).setSelected(true);
                    } else {
                        v.setBackgroundColor(mContext.getResources().getColor(R.color.white));
                        v.findViewById(R.id.checkbox).setVisibility(View.VISIBLE);
                        v.findViewById(R.id.checkbox).setSelected(false);
                    }
                    onItemClickListener.onItemLongClick(v, position);
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView noteTv;
        private TextView timeTv;

        public ItemViewHolder(View itemView) {
            super(itemView);
            noteTv = (TextView) itemView.findViewById(R.id.tv_note);
            timeTv = (TextView) itemView.findViewById(R.id.tv_time);
        }
    }

    public interface OnItemClickListener {

        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }
}



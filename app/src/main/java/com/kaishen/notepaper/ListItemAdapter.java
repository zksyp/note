package com.kaishen.notepaper;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.ColorRes;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
    private boolean mSelect = false;

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

    public void setSelect(boolean select) {
        this.mSelect = select;
    }

    @Override
    public void onBindViewHolder(final ListItemAdapter.ItemViewHolder holder, final int position) {
        holder.noteTv.setText(noteList.get(position).getNote());
        holder.timeTv.setText(noteList.get(position).getTime());

        positionSet = MainActivity.instance.positionSet;


        if (onItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mSelect){
                        if (MainActivity.instance.positionSet.contains(position)) {
                            holder.checkBox.setVisibility(View.GONE);
                            holder.frameLayout.setBackgroundResource(R.drawable.ripple_bg);
                            MainActivity.instance.positionSet.remove(position);
                        } else {
                            holder.checkBox.setVisibility(View.VISIBLE);
                            holder.frameLayout.setBackgroundResource(0);
                            MainActivity.instance.positionSet.add(position);
                        }
                        if(MainActivity.instance.positionSet.size() == 0)
                        {
                            mSelect = false;
                        }
                    }
                    else{
                        onItemClickListener.onItemClick(v, position);
                    }
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(final View v) {
                    onItemClickListener.onItemLongClick(v, position);
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (noteList != null) return noteList.size();
        else return 0;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView noteTv;
        private TextView timeTv;
        private AppCompatCheckBox checkBox;
        private FrameLayout frameLayout;

        public ItemViewHolder(View itemView) {
            super(itemView);
            noteTv = (TextView) itemView.findViewById(R.id.tv_note);
            timeTv = (TextView) itemView.findViewById(R.id.tv_time);
            checkBox = (AppCompatCheckBox) itemView.findViewById(R.id.checkbox);
            frameLayout = (FrameLayout) itemView.findViewById(R.id.item_fl);
        }
    }

    public interface OnItemClickListener {

        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }
}



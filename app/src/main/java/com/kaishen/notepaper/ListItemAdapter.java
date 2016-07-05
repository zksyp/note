package com.kaishen.notepaper;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by kaishen on 16/6/30.
 */
public class ListItemAdapter extends RecyclerView.Adapter<ListItemAdapter.ItemViewHolder>{

    private List<NoteBean> noteList;
    private Context mContext;
    private MyItemClickListener myItemClickListener;

    public ListItemAdapter(Context context, List<NoteBean> noteList)
    {
        mContext = context;
        this.noteList = noteList;
    }

    @Override
    public ListItemAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemViewHolder holder = new ItemViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_layout
                , parent, false), myItemClickListener);
        return holder;
    }

    public void setOnItemClickListener(MyItemClickListener myItemClickListener) {
        this.myItemClickListener = myItemClickListener;
    }

    @Override
    public void onBindViewHolder(ListItemAdapter.ItemViewHolder holder, int position) {
        holder.noteTv.setText(noteList.get(position).getNote());
        holder.timeTv.setText(noteList.get(position).getTime());
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView noteTv;
        private TextView timeTv;

        public ItemViewHolder(View itemView, MyItemClickListener ItemClickListener) {
            super(itemView);
            noteTv = (TextView) itemView.findViewById(R.id.tv_note);
            timeTv = (TextView) itemView.findViewById(R.id.tv_time);
            myItemClickListener = ItemClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            myItemClickListener.onItemClick(v, getLayoutPosition());
        }
    }

    public interface MyItemClickListener {
         void onItemClick(View view,int postion);
    }
}

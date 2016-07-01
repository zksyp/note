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
    private LayoutInflater mInflater;
    private Context mContext;

    public ListItemAdapter(Context context, List<NoteBean> noteList)
    {
        mContext = context;
        this.noteList = noteList;
    }

    @Override
    public ListItemAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemViewHolder holder = new ItemViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_layout
                , parent, false));
        holder.itemView.setOnClickListener((View.OnClickListener) mContext);
        return holder;
    }

    @Override
    public void onBindViewHolder(ListItemAdapter.ItemViewHolder holder, int position) {
//        holder.titleTv.setText(noteList.get(position).getTitle());
        holder.noteTv.setText(noteList.get(position).getNote());
        holder.timeTv.setText(noteList.get(position).getTime());
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }



    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView titleTv;
        private TextView noteTv;
        private TextView timeTv;

        public ItemViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            titleTv = (TextView) itemView.findViewById(R.id.tv_title);
            noteTv = (TextView) itemView.findViewById(R.id.tv_note);
            timeTv = (TextView) itemView.findViewById(R.id.tv_time);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            String id = noteList.get(getLayoutPosition()).getId();
            intent.putExtra("ID", id);
            intent.setClass(mContext, NoteEditActivity.class);
            mContext.startActivity(intent);
        }
    }

}

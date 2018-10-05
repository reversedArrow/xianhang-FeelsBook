package com.sample.xianhang_feelsbook.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sample.xianhang_feelsbook.R;
import com.sample.xianhang_feelsbook.bean.Emotion;
import com.sample.xianhang_feelsbook.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * adapter for emotion list
 */
public class EmotionAdapter extends RecyclerView.Adapter<EmotionAdapter.MyHolder> {

    private List<Emotion> list = new ArrayList<>();

    /**
     * add list
     *
     * @param list
     */
    public void setList(List<Emotion> list) {
        this.list.clear();
        this.list = list;
        notifyDataSetChanged();
    }

    /**
     * get list
     *
     * @return
     */
    public List<Emotion> getList() {
        return list;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_emotion, parent, false));
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        holder.bindData(position, list.get(position));
    }

    @Override
    public int getItemCount() { // get emotions count
        return list.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder{

        private TextView emotion;
        private TextView comment;
        private TextView time;

        public MyHolder(View itemView) {
            super(itemView);

            emotion = itemView.findViewById(R.id.tv_emotion);
            comment = itemView.findViewById(R.id.tv_comment);
            time = itemView.findViewById(R.id.tv_time);
        }

        /**
         * bind a emotion
         *
         * @param position
         * @param data
         */
        public void bindData(final int position, final Emotion data){
            // show emotion info
            emotion.setText(CommonUtils.intToEmotionStr(data.emotion));
            comment.setText(data.comment);
            time.setText(data.time);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) { // click listener
                    if(onItemClickListener != null){
                        onItemClickListener.onItemClick(position, data);
                    }
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) { // long click listener
                    if(onItemClickListener != null){
                        onItemClickListener.onItemLongClick(position, data);
                    }
                    return false;
                }
            });
        }
    }


    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    /**
     * click item interface
     */
    public interface OnItemClickListener{
        void onItemClick(int position, Emotion data);
        void onItemLongClick(int position, Emotion data);
    }
}

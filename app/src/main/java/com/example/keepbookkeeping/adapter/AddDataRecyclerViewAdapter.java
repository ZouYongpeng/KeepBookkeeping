package com.example.keepbookkeeping.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.keepbookkeeping.R;
import com.example.keepbookkeeping.bean.DataTypeBean;

import java.util.List;

/**
 * @author 邹永鹏
 * @date 2019/1/31
 * @description :
 */
public class AddDataRecyclerViewAdapter extends RecyclerView.Adapter<AddDataRecyclerViewAdapter.ViewHolder> {

    private List<DataTypeBean> mDataTypeBeans;

    static class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView mImageView;
        private TextView mTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            mImageView=itemView.findViewById(R.id.add_data_viewPager_recyclerView_item_image);
            mTextView=itemView.findViewById(R.id.add_data_viewPager_recyclerView_item_text);
        }
    }

    public AddDataRecyclerViewAdapter(List<DataTypeBean> dataTypeBeans) {
        mDataTypeBeans = dataTypeBeans;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.add_data_viewpager_recyclerview_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (position<mDataTypeBeans.size()){
            DataTypeBean bean=mDataTypeBeans.get(position);
            holder.mImageView.setImageResource(bean.getImageId());
            holder.mTextView.setText(bean.getName());
        }else {
            holder.mImageView.setImageResource(R.drawable.ic_edit);
            holder.mTextView.setText("编辑");
        }
    }

    @Override
    public int getItemCount() {
        return mDataTypeBeans.size()+1;
    }
}

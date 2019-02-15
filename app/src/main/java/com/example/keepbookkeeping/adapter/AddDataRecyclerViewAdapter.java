package com.example.keepbookkeeping.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.keepbookkeeping.R;
import com.example.keepbookkeeping.activities.AddDataActivity;
import com.example.keepbookkeeping.bean.DataTypeBean;
import com.example.keepbookkeeping.events.ChangeDataTypeEvent;
import com.example.keepbookkeeping.utils.RxBus;
import com.example.keepbookkeeping.utils.ToastUtil;

import java.util.List;

/**
 * @author 邹永鹏
 * @date 2019/1/31
 * @description :AddDataActivity的支出/收入分类RecyclerViewAdapter
 */
public class AddDataRecyclerViewAdapter extends RecyclerView.Adapter<AddDataRecyclerViewAdapter.ViewHolder> {

    private List<DataTypeBean> mDataTypeBeans;

    static class ViewHolder extends RecyclerView.ViewHolder{
        private LinearLayout mLinearLayout;
        private ImageView mImageView;
        private TextView mTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            mLinearLayout=itemView.findViewById(R.id.add_data_viewPager_recyclerView_item);
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
        final Context context=parent.getContext();
        View view= LayoutInflater.from(context).inflate(R.layout.add_data_viewpager_recyclerview_item,parent,false);
        final ViewHolder viewHolder=new ViewHolder(view);
        viewHolder.mLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position=viewHolder.getAdapterPosition();
                if (position<mDataTypeBeans.size()){
                    DataTypeBean bean=mDataTypeBeans.get(position);
                    ToastUtil.success(bean.getName());
                    RxBus.getInstance().post(new ChangeDataTypeEvent(bean));
                }else {
                    //编辑
                    ToastUtil.success("添加");
                }
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (position<mDataTypeBeans.size()){
            DataTypeBean bean=mDataTypeBeans.get(position);
            holder.mImageView.setImageResource(bean.getImageId());
            holder.mTextView.setText(bean.getName());
        }else {
            holder.mImageView.setImageResource(R.drawable.ic_edit);
            holder.mTextView.setText("添加");
        }
    }

    @Override
    public int getItemCount() {
        return mDataTypeBeans.size()+1;
    }
}

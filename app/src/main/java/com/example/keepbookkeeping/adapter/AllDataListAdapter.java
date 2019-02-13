package com.example.keepbookkeeping.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.keepbookkeeping.R;
import com.example.keepbookkeeping.bean.SingleDataBean;

import java.util.List;

/**
 * @author 邹永鹏
 * @date 2019/2/12
 * @description :
 */
public class AllDataListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<SingleDataBean> mSingleDataList;

    static class ContentViewHolder extends RecyclerView.ViewHolder{

        private ImageView mContentImage;
        private TextView mContentIncomeText;
        private TextView mContentIncomeMoney;
        private TextView mContentOutcomeText;
        private TextView mContentOutcomeMoney;

        public ContentViewHolder(View itemView) {
            super(itemView);
            mContentImage=itemView.findViewById(R.id.list_item_image);
            mContentIncomeText=itemView.findViewById(R.id.list_item_income_text);
            mContentIncomeMoney=itemView.findViewById(R.id.list_item_income_money);
            mContentOutcomeText=itemView.findViewById(R.id.list_item_outcome_text);
            mContentOutcomeMoney=itemView.findViewById(R.id.list_item_outcome_money);
        }
    }

    static class TimeViewHolder extends RecyclerView.ViewHolder{

        private TextView mTimeText;

        public TimeViewHolder(View itemView) {
            super(itemView);
            mTimeText=itemView.findViewById(R.id.list_time_text);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mSingleDataList.size()+1;
    }
}

package com.example.keepbookkeeping.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.keepbookkeeping.R;
import com.example.keepbookkeeping.bean.FormApartBean;

import java.util.List;

/**
 * @author 邹永鹏
 * @date 2019/1/30
 * @description :
 */
public class FormApartRecyclerViewAdapter extends RecyclerView.Adapter<FormApartRecyclerViewAdapter.ViewHolder> {

    private List<FormApartBean> mFormApartBeans;
    private float mTotalMoney;

    static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView mTypeText;
        private TextView mMoneytext;
        private TextView mPercentText;

        public ViewHolder(View itemView) {
            super(itemView);
            mTypeText=itemView.findViewById(R.id.form_apart_item_name);
            mMoneytext=itemView.findViewById(R.id.form_apart_item_money);
            mPercentText=itemView.findViewById(R.id.form_apart_item_percent);
        }
    }

    public FormApartRecyclerViewAdapter(List<FormApartBean> formApartBeans,float totalMoney) {
        mFormApartBeans = formApartBeans;
        mTotalMoney=totalMoney;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.form_apart_recycler_view_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FormApartBean bean=mFormApartBeans.get(position);
        holder.mTypeText.setText(bean.getTypeName());
        holder.mMoneytext.setText(String.valueOf(bean.getMoney()));
        bean.setPercent((bean.getMoney()*100)/mTotalMoney);
        holder.mPercentText.setText(String.valueOf(bean.getPercent()));
    }

    @Override
    public int getItemCount() {
        return mFormApartBeans.size();
    }

    public void notifyFormApartBeans(List<FormApartBean> formApartBeans,float totalMoney){
        if (mFormApartBeans!=null){
            mFormApartBeans.clear();
        }
        mFormApartBeans.addAll(formApartBeans);
        mTotalMoney=totalMoney;
        this.notifyDataSetChanged();
    }
}

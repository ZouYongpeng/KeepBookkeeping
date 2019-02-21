package com.example.keepbookkeeping.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.keepbookkeeping.R;
import com.example.keepbookkeeping.bean.FormApartBean;
import com.example.keepbookkeeping.bean.FormTrendBean;

import java.util.List;

/**
 * @author 邹永鹏
 * @date 2019/1/30
 * @description :
 */
public class FormTrendRecyclerViewAdapter extends RecyclerView.Adapter<FormTrendRecyclerViewAdapter.ViewHolder>{

    private List<FormTrendBean> mFormTrendBeans;

    static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView mMonthText;
        private TextView mIncomeText;
        private TextView mOutcomeText;
        private TextView mBalanceText;

        public ViewHolder(View itemView) {
            super(itemView);
            mMonthText=itemView.findViewById(R.id.form_trend_item_month);
            mIncomeText=itemView.findViewById(R.id.form_trend_item_income);
            mOutcomeText=itemView.findViewById(R.id.form_trend_item_outcome);
            mBalanceText=itemView.findViewById(R.id.form_trend_item_Balance);
        }
    }

    public FormTrendRecyclerViewAdapter(List<FormTrendBean> formTrendBeans) {
        mFormTrendBeans = formTrendBeans;
    }

    @NonNull
    @Override
    public FormTrendRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.form_trend_recycler_view_item,parent,false);
        return new FormTrendRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FormTrendRecyclerViewAdapter.ViewHolder holder, int position) {
        FormTrendBean bean=mFormTrendBeans.get(position);
        holder.mMonthText.setText(bean.getMonth());
        holder.mIncomeText.setText(String.valueOf(bean.getIncome()));
        holder.mOutcomeText.setText(String.valueOf(bean.getOutcome()));
        holder.mBalanceText.setText(String.valueOf(bean.getIncome()-bean.getOutcome()));
    }

    @Override
    public int getItemCount() {
        if (mFormTrendBeans!=null && mFormTrendBeans.size()>0){
            return mFormTrendBeans.size();
        }else {
            return 0;
        }
    }

    public void notifyFormTrendBeans(List<FormTrendBean> formTrendBeans){
        if (mFormTrendBeans!=null) {
            mFormTrendBeans.clear();
        }
        if (formTrendBeans!=null){
            mFormTrendBeans.addAll(formTrendBeans);
            this.notifyDataSetChanged();
        }
    }
}

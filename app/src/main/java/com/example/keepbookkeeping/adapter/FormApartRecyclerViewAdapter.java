package com.example.keepbookkeeping.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.keepbookkeeping.R;
import com.example.keepbookkeeping.activities.AddDataActivity;
import com.example.keepbookkeeping.bean.FormApartBean;
import com.example.keepbookkeeping.utils.LogUtil;
import com.example.keepbookkeeping.utils.ToastUtil;

import java.util.List;

/**
 * @author 邹永鹏
 * @date 2019/1/30
 * @description :
 */
public class FormApartRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<FormApartBean> mFormApartBeans;
    private float mTotalMoney;
    private boolean hasData;
    private static final int TYPE_EMPTY=0;
    private static final int TYPE_DATE=1;

    static class DataViewHolder extends RecyclerView.ViewHolder{
        private TextView mTypeText;
        private TextView mMoneyText;
        private TextView mPercentText;

        public DataViewHolder(View itemView) {
            super(itemView);
            mTypeText=itemView.findViewById(R.id.form_apart_item_name);
            mMoneyText=itemView.findViewById(R.id.form_apart_item_money);
            mPercentText=itemView.findViewById(R.id.form_apart_item_percent);
        }
    }

    static class EmptyViewHolder extends RecyclerView.ViewHolder{
        private TextView mAddDataText;
        private TextView mLoginText;
        private TextView mSelectDateText;

        public EmptyViewHolder(View itemView) {
            super(itemView);
            mAddDataText=itemView.findViewById(R.id.list_empty_add_data);
            mLoginText=itemView.findViewById(R.id.list_empty_login);
            mSelectDateText=itemView.findViewById(R.id.list_empty_select_other_date);
        }

    }

    public FormApartRecyclerViewAdapter(Context context, List<FormApartBean> formApartBeans, float totalMoney) {
        mContext=context;
        mFormApartBeans = formApartBeans;
        mTotalMoney=totalMoney;
        if (mFormApartBeans!=null && mFormApartBeans.size()>=1){
            hasData=true;
        }else {
            hasData=false;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (hasData){
            return TYPE_DATE;
        }else {
            return TYPE_EMPTY;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType==TYPE_DATE){
            View view= LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.form_apart_recycler_view_item,parent,false);
            return new DataViewHolder(view);
        }else {
            View view= LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_empty_item,parent,false);
            return new EmptyViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof DataViewHolder){
            FormApartBean bean=mFormApartBeans.get(position);
            ((DataViewHolder)holder).mTypeText.setText(bean.getTypeName());
            ((DataViewHolder)holder).mMoneyText.setText(String.valueOf(bean.getMoney()));
            bean.setPercent((bean.getMoney()*100)/mTotalMoney);
            ((DataViewHolder)holder).mPercentText.setText(String.valueOf(bean.getPercent()));
        }else if (holder instanceof EmptyViewHolder){
            ((EmptyViewHolder) holder).mSelectDateText.setVisibility(View.VISIBLE);
            ((EmptyViewHolder) holder).mAddDataText.setOnClickListener(mEmptyViewListener);
            ((EmptyViewHolder) holder).mLoginText.setOnClickListener(mEmptyViewListener);
            ((EmptyViewHolder) holder).mSelectDateText.setOnClickListener(mEmptyViewListener);
        }

    }

    @Override
    public int getItemCount() {
        if (hasData){
            return mFormApartBeans.size();
        }else {
            return 1;
        }
    }

    public void notifyFormApartBeans(List<FormApartBean> formApartBeans,float totalMoney){
        if (mFormApartBeans!=null){
            mFormApartBeans.clear();
        }
        mFormApartBeans.addAll(formApartBeans);
        mTotalMoney=totalMoney;
        if (mFormApartBeans!=null && mFormApartBeans.size()>=1){
            hasData=true;
        }else {
            hasData=false;
        }
        this.notifyDataSetChanged();
    }

    private View.OnClickListener mEmptyViewListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.list_empty_add_data:
                    AddDataActivity.startAddDataActivity(mContext);
                    break;
                case R.id.list_empty_login:
                    //ToastUtil.success("登陆");
                    break;
                case R.id.list_empty_select_other_date:
                    //ToastUtil.success("选择其他时间");
                    break;
                default:
                    break;
            }
        }
    };

}

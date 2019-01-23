package com.example.keepbookkeeping.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.keepbookkeeping.R;
import com.example.keepbookkeeping.bean.BillApartBean;

import java.util.List;

/**
 * @author 邹永鹏
 * @date 2019/1/23
 * @description :
 */
public class BillApartAdapter extends RecyclerView.Adapter<BillApartAdapter.ViewHolder> {

    private List<BillApartBean> mBillApartBeanList;

    static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView mImage;
        TextView mNameText;
        TextView mDescriptionText;
        TextView mMoneyText;

        public ViewHolder(View itemView) {
            super(itemView);
            mImage=itemView.findViewById(R.id.bill_image);
            mNameText=itemView.findViewById(R.id.bill_name);
            mDescriptionText=itemView.findViewById(R.id.bill_description);
            mMoneyText=itemView.findViewById(R.id.bill_money);
        }
    }

    public BillApartAdapter(List<BillApartBean> billApartBeanList){
        mBillApartBeanList=billApartBeanList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.bill_aparts_item,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BillApartBean bean=mBillApartBeanList.get(position);
        holder.mNameText.setText(bean.getName());
        holder.mDescriptionText.setText(bean.getDescription());
        holder.mMoneyText.setText(String.valueOf(bean.getMoney()));
    }

    @Override
    public int getItemCount() {
        return mBillApartBeanList.size();
    }
}

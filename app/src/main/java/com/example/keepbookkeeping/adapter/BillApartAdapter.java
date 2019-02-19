package com.example.keepbookkeeping.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.keepbookkeeping.R;
import com.example.keepbookkeeping.activities.BillDetailsActivity;
import com.example.keepbookkeeping.bean.BillApartBean;
import com.example.keepbookkeeping.events.ShowAddNewBillDialogEvent;
import com.example.keepbookkeeping.utils.AllDataTableUtil;
import com.example.keepbookkeeping.utils.BillTableUtil;
import com.example.keepbookkeeping.utils.LogUtil;
import com.example.keepbookkeeping.utils.RxBus;
import com.example.keepbookkeeping.utils.ToastUtil;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 邹永鹏
 * @date 2019/1/23
 * @description :
 */
public class BillApartAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<BillApartBean> mBillApartBeanList;
    private int billType=0;

    private static final int TYPE_CONTENT=0;
    private static final int TYPE_EXTRAS=1;
    private static final int TYPE_ADD_DATA=2;

    static class BillViewHolder extends RecyclerView.ViewHolder{
        RelativeLayout mLayout;
        ImageView mImage;
        TextView mNameText;
        TextView mDescriptionText;
        TextView mCurrentMoneyText;
        TextView mSpendMoneyText;

        public BillViewHolder(View itemView) {
            super(itemView);
            mLayout=itemView.findViewById(R.id.bill_apart_item_layout);
            mImage=itemView.findViewById(R.id.bill_image);
            mNameText=itemView.findViewById(R.id.bill_name);
            mDescriptionText=itemView.findViewById(R.id.bill_description);
            mCurrentMoneyText=itemView.findViewById(R.id.bill_current_money);
            mSpendMoneyText=itemView.findViewById(R.id.bill_spend_money);
        }
    }

    static class AddBillViewHolder extends RecyclerView.ViewHolder{

        RelativeLayout mLayout;

        public AddBillViewHolder(View itemView) {
            super(itemView);
            mLayout=itemView.findViewById(R.id.bill_add_item_layout);
        }

    }

    public BillApartAdapter(Context context,List<BillApartBean> billApartBeanList){
        mContext=context;
        mBillApartBeanList=billApartBeanList;
    }

    @Override
    public int getItemViewType(int position) {
        if (position<mBillApartBeanList.size()){
            return TYPE_CONTENT;
        }else if (position==mBillApartBeanList.size()){
            return TYPE_EXTRAS;
        }else {
            return TYPE_ADD_DATA;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType==TYPE_CONTENT || viewType==TYPE_EXTRAS){
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.bill_aparts_item,parent,false);
            return new BillViewHolder(view);
        }else {
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.bill_add_item,parent,false);
            return new AddBillViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof BillViewHolder){
            final BillApartBean bean;
            if (position<mBillApartBeanList.size()){
                bean=mBillApartBeanList.get(position);
                billType=bean.getType();
                float spendMoney=AllDataTableUtil.getMoneyByBillName(bean.getName(),AllDataTableUtil.TYPE_TOTAL);
                float currentMoney= BillTableUtil.getInitialCountByBillName(bean.getName())+spendMoney;
                ((BillViewHolder)holder).mCurrentMoneyText.setText(String.valueOf(currentMoney));
                ((BillViewHolder)holder).mSpendMoneyText.setText("收支："+String.valueOf(spendMoney));
                ((BillViewHolder) holder).mLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle=new Bundle();
                        bundle.putSerializable("bean",bean);
                        BillDetailsActivity.startBillDetailsActivity(mContext,bundle);
                    }
                });
            }else {
                if (billType==BillApartBean.TYPE_BILL_ASSETS){
                    bean=new BillApartBean(-1,billType,R.drawable.ic_bill_shouzhai,"应收账","别人欠我的钱",0,0);
                }else {
                    bean=new BillApartBean(-1,billType,R.drawable.ic_bill_qian,"应还账","我欠别人的钱",0,0);
                }
                ((BillViewHolder) holder).mLayout.setOnClickListener(mLayoutListener);

            }
            ((BillViewHolder)holder).mImage.setImageResource(bean.getImageId());
            ((BillViewHolder)holder).mNameText.setText(bean.getName());
            ((BillViewHolder)holder).mDescriptionText.setText(bean.getDescription());
        }
        else if (holder instanceof AddBillViewHolder){
            ((AddBillViewHolder) holder).mLayout.setOnClickListener(mLayoutListener);
        }
    }

    @Override
    public int getItemCount() {
        return mBillApartBeanList.size()+2;
    }

    public void notifyData(List<BillApartBean> billApartBeanList){
        if (billApartBeanList!=null){
            mBillApartBeanList.clear();
            mBillApartBeanList=billApartBeanList;
            notifyDataSetChanged();
        }
    }

    private View.OnClickListener mLayoutListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId()==R.id.bill_apart_item_layout){
                ToastUtil.success("设置欠账");
            }else if (v.getId()==R.id.bill_add_item_layout){
                ToastUtil.success("添加新账户");
                LogUtil.d("rxbus","发送 ShowAddNewBillDialogEvent");
                RxBus.getInstance().post(new ShowAddNewBillDialogEvent());
            }
        }
    };

}

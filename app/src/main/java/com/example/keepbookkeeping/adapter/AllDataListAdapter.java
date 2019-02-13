package com.example.keepbookkeeping.adapter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.keepbookkeeping.R;
import com.example.keepbookkeeping.bean.SingleDataBean;
import com.example.keepbookkeeping.utils.DataBaseUtil;
import com.example.keepbookkeeping.utils.DateUtil;
import com.example.keepbookkeeping.utils.LogUtil;

import java.util.List;

/**
 * @author 邹永鹏
 * @date 2019/2/12
 * @description :
 */
public class AllDataListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private SQLiteDatabase db;
    private List<SingleDataBean> mSingleDataList;
    private List<String> mDateList;
    private List<String> mYearMonthList;

    private int count;
    private int index;
    private int dateIndex;
    private int monthIndex;
    private int bindIndex;
    private final int TYPE_DATE_ITEM=0;
    private final int TYPE_MONTH_ITEM=1;
    private final int TYPE_CONTENT_ITEM=2;
    private boolean isShowDate=false;
    private boolean isShowMonth=true;
    private String date;

    static class DateViewHolder extends RecyclerView.ViewHolder{

        private ImageView mContentImage;
        private TextView mContentIncomeText;
        private TextView mContentIncomeMoney;
        private TextView mContentOutcomeText;
        private TextView mContentOutcomeMoney;

        public DateViewHolder(View itemView) {
            super(itemView);
            mContentImage=itemView.findViewById(R.id.list_item_image);
            mContentIncomeText=itemView.findViewById(R.id.list_item_income_text);
            mContentIncomeMoney=itemView.findViewById(R.id.list_item_income_money);
            mContentOutcomeText=itemView.findViewById(R.id.list_item_outcome_text);
            mContentOutcomeMoney=itemView.findViewById(R.id.list_item_outcome_money);
        }
    }

    static class MonthViewHolder extends RecyclerView.ViewHolder{

        private TextView mMonthText;

        public MonthViewHolder(View itemView) {
            super(itemView);
            mMonthText=itemView.findViewById(R.id.list_time_text);
        }

    }

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

    public AllDataListAdapter(SQLiteDatabase db) {
        this.db=db;
        index=0;
        dateIndex=0;
        monthIndex=0;
        bindIndex=0;
        mSingleDataList= DataBaseUtil.queryAllDataOrderByDate(db);
        mYearMonthList=DataBaseUtil.getDifferentMonthList(db);
        mDateList=DataBaseUtil.getDifferentDateList(db);
        count=mSingleDataList.size()+mYearMonthList.size()+mDateList.size();
    }

    private void checkData(String date){
        String[] strings=date.split("-");
        //当月
//        if (TextUtils.equals(strings[0],String.valueOf(DateUtil.getCurrentYear())) &&
//                DateUtil.getCurrentMonth()==Integer.parseInt(strings[1])){
//            isShowDate=true;
//            isShowMonth=false;
//        }else {
//            isShowDate=false;
//            isShowMonth=true;
//        }

    }

    @Override
    public int getItemViewType(int position) {
        int type;
        if (isShowDate){
            type=TYPE_DATE_ITEM;
            isShowDate=false;
            isShowMonth=false;
        }else if (isShowMonth){
            type=TYPE_MONTH_ITEM;
            isShowMonth=false;
            isShowDate=true;
        }else {
            type=TYPE_CONTENT_ITEM;
            if (mSingleDataList!=null && index<mSingleDataList.size()-1){
                String currentDate=DateUtil.dateToString(mSingleDataList.get(index).getDate());
                String nextDate=DateUtil.dateToString(mSingleDataList.get(index+1).getDate());
                String[] currentDates = currentDate.split("-");
                String[] nextDates = nextDate.split("-");
                if (TextUtils.equals(currentDate,nextDate)){
                    //同一天
                    isShowDate=false;
                    isShowMonth=false;
                }else if (TextUtils.equals(currentDates[0],nextDates[0]) &&
                        TextUtils.equals(currentDates[1],nextDates[1])){
                    //同一个月
                    isShowDate=true;
                    isShowMonth=false;
                }else {
                    //不同月
                    isShowDate=false;
                    isShowMonth=true;
                }
            }
            index++;
        }
        return type;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context=parent.getContext();
        if (viewType==TYPE_DATE_ITEM){
            View view= LayoutInflater.from(context).inflate(R.layout.list_content_item,parent,false);
            return new DateViewHolder(view);
        }else if (viewType==TYPE_MONTH_ITEM){
            View view= LayoutInflater.from(context).inflate(R.layout.list_time_item,parent,false);
            return new MonthViewHolder(view);
        }else {
            View view= LayoutInflater.from(context).inflate(R.layout.list_content_item,parent,false);
            return new ContentViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof DateViewHolder && dateIndex<mDateList.size()){
            String date=mDateList.get(dateIndex);
            LogUtil.d("AllDataList","显示日期"+date);
            ((DateViewHolder) holder).mContentIncomeText.setText("收入");
            ((DateViewHolder) holder).mContentOutcomeText.setText("支出");
            dateIndex++;
        }else if (holder instanceof MonthViewHolder && monthIndex<mYearMonthList.size()){
            String monthDate=mYearMonthList.get(monthIndex);
            LogUtil.d("AllDataList","显示月份"+monthDate);
            ((MonthViewHolder) holder).mMonthText.setText(monthDate);
            monthIndex++;
        }else if (holder instanceof ContentViewHolder && bindIndex<mSingleDataList.size()){
            SingleDataBean bean=mSingleDataList.get(bindIndex);
            LogUtil.d("AllDataList","显示数据"+bean.toString());
            ((ContentViewHolder) holder).mContentIncomeText.setText("收入");
            ((ContentViewHolder) holder).mContentIncomeMoney.setText(bindIndex*1000+".00");
            ((ContentViewHolder) holder).mContentOutcomeText.setText("支出");
            ((ContentViewHolder) holder).mContentOutcomeMoney.setText(bindIndex*100+".00");
            bindIndex++;
        }
    }

    @Override
    public int getItemCount() {
//        LogUtil.d("AllDataList","getItemCount = "+count);
        return count;
    }
}

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

import java.util.Arrays;
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
    private final int TYPE_END_ITEM=3;
    private boolean isShowDate=false;
    private boolean isShowMonth=true;
    private boolean isStop=false;
    private int[] positionToIndex;
    private int[] positionToType;

    static class DateViewHolder extends RecyclerView.ViewHolder{

        private TextView mDateText;
        private TextView mDateIncomeText;
        private TextView mDateIncomeMoney;
        private TextView mDateOutcomeText;
        private TextView mDateOutcomeMoney;

        public DateViewHolder(View itemView) {
            super(itemView);
            mDateText=itemView.findViewById(R.id.list_date_item_text);
            mDateIncomeText=itemView.findViewById(R.id.list_date_item_income_text);
            mDateIncomeMoney=itemView.findViewById(R.id.list_date_item_income_money);
            mDateOutcomeText=itemView.findViewById(R.id.list_date_item_outcome_text);
            mDateOutcomeMoney=itemView.findViewById(R.id.list_date_item_outcome_money);
        }
    }

    static class MonthViewHolder extends RecyclerView.ViewHolder{

        private TextView mMonthText;

        public MonthViewHolder(View itemView) {
            super(itemView);
            mMonthText=itemView.findViewById(R.id.list_time_text);
        }

    }

    static class EndViewHolder extends RecyclerView.ViewHolder{

        public EndViewHolder(View itemView) {
            super(itemView);
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

    public AllDataListAdapter(SQLiteDatabase database) {
        this.db=database;
        index=0;
        dateIndex=0;
        monthIndex=0;
        bindIndex=0;
        mSingleDataList= DataBaseUtil.queryAllDataOrderByDate(db);
        mYearMonthList=DataBaseUtil.getDifferentMonthList(db);
        mDateList=DataBaseUtil.getDifferentDateList(db);
        count=mSingleDataList.size()+mYearMonthList.size()+mDateList.size()+1;
        positionToIndex=new int[count];
        positionToType=new int[count];
        Arrays.fill(positionToIndex,-1);
        Arrays.fill(positionToType,-1);
    }

    @Override
    public int getItemViewType(int position) {
        if (positionToType[position]==-1){
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
                            TextUtils.equals(currentDates[1],nextDates[1]) &&
                            !TextUtils.equals(currentDates[2],nextDates[2])){
                        //同一个月
                        isShowDate=true;
                        isShowMonth=false;
                    }else {
                        //不同月
                        isShowDate=false;
                        isShowMonth=true;
                    }
                    index++;
                }else if (index==mSingleDataList.size()-1){
                    index++;
                }else {
                    type=TYPE_END_ITEM;
                }
            }
            positionToType[position]=type;
        }
        return positionToType[position];
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context=parent.getContext();
        if (viewType==TYPE_DATE_ITEM){
            View view= LayoutInflater.from(context).inflate(R.layout.list_date_item,parent,false);
            return new DateViewHolder(view);
        }else if (viewType==TYPE_MONTH_ITEM){
            View view= LayoutInflater.from(context).inflate(R.layout.list_month_item,parent,false);
            return new MonthViewHolder(view);
        }else if (viewType==TYPE_CONTENT_ITEM){
            View view= LayoutInflater.from(context).inflate(R.layout.list_content_item,parent,false);
            return new ContentViewHolder(view);
        }else {
            View view= LayoutInflater.from(context).inflate(R.layout.list_end_item,parent,false);
            return new EndViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof DateViewHolder){
            if (positionToIndex[position]==-1){
                positionToIndex[position]=dateIndex++;
            }
            LogUtil.d("AllDataList","positionToIndex[ "+position+" ] = "+positionToIndex[position]);
            String date=mDateList.get(positionToIndex[position]);
            LogUtil.d("AllDataList","显示日期"+date);
            ((DateViewHolder) holder).mDateText.setText(DateUtil.getDayOfDate(date));
            ((DateViewHolder) holder).mDateIncomeText.setText("收入");
            ((DateViewHolder) holder).mDateIncomeMoney.setText("");
            ((DateViewHolder) holder).mDateOutcomeText.setText("支出");
            ((DateViewHolder) holder).mDateOutcomeMoney.setText("");
        }else if (holder instanceof MonthViewHolder){
            if (positionToIndex[position]==-1){
                positionToIndex[position]=monthIndex++;
            }
            LogUtil.d("AllDataList","positionToIndex[ "+position+" ] = "+positionToIndex[position]);
            String monthDate=mYearMonthList.get(positionToIndex[position]);
            LogUtil.d("AllDataList","显示月份"+monthDate);
            ((MonthViewHolder) holder).mMonthText.setText(monthDate);
        }else if (holder instanceof ContentViewHolder){
            if (positionToIndex[position]==-1 && bindIndex<mSingleDataList.size()){
                positionToIndex[position]=bindIndex++;
            }
            LogUtil.d("AllDataList","positionToIndex[ "+position+" ] = "+positionToIndex[position]);
            SingleDataBean bean=mSingleDataList.get(positionToIndex[position]);
            LogUtil.d("AllDataList","显示数据"+bean.toString());
            if (bean.getType()==SingleDataBean.TYPE_INCOME_DATA){
                ((ContentViewHolder) holder).mContentIncomeText.setText("收入");
                ((ContentViewHolder) holder).mContentIncomeMoney.setText(bean.getMoney()+"");
                ((ContentViewHolder) holder).mContentOutcomeText.setText("");
                ((ContentViewHolder) holder).mContentOutcomeMoney.setText("");
            }else {
                ((ContentViewHolder) holder).mContentIncomeText.setText("");
                ((ContentViewHolder) holder).mContentIncomeMoney.setText("");
                ((ContentViewHolder) holder).mContentOutcomeText.setText("支出");
                ((ContentViewHolder) holder).mContentOutcomeMoney.setText(bean.getMoney()+"");
            }
        }else {
            LogUtil.d("AllDataList","EndViewHolder");
        }
    }

    @Override
    public int getItemCount() {
        return count;
    }
}

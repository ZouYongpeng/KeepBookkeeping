package com.example.keepbookkeeping.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.keepbookkeeping.R;
import com.example.keepbookkeeping.activities.AddDataActivity;
import com.example.keepbookkeeping.activities.LoginActivity;
import com.example.keepbookkeeping.bean.BmobBean.User;
import com.example.keepbookkeeping.bean.SingleDataBean;
import com.example.keepbookkeeping.bill.BillFragment;
import com.example.keepbookkeeping.events.ChangeFragmentTypeEvent;
import com.example.keepbookkeeping.events.NotifyBillListEvent;
import com.example.keepbookkeeping.events.NotifyFormListEvent;
import com.example.keepbookkeeping.utils.AllDataTableUtil;
import com.example.keepbookkeeping.utils.BillTableUtil;
import com.example.keepbookkeeping.utils.DateUtil;
import com.example.keepbookkeeping.utils.DataTypeTableUtil;
import com.example.keepbookkeeping.utils.LogUtil;
import com.example.keepbookkeeping.utils.RxBus;
import com.example.keepbookkeeping.utils.ToastUtil;
import com.example.keepbookkeeping.utils.UserUtil;

import java.util.Arrays;
import java.util.List;

/**
 * @author 邹永鹏
 * @date 2019/2/12
 * @description :
 */
public class AllDataListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;

    private List<SingleDataBean> mSingleDataList;
    private List<String> mDateList;
    private List<String> mYearMonthList;
    private boolean[] isClickList;

    private int count;
    private int index;
    private int dateIndex;
    private int monthIndex;
    private int bindIndex;
    private final int TYPE_DATE_ITEM=0;
    private final int TYPE_MONTH_ITEM=1;
    private final int TYPE_CONTENT_ITEM=2;
    private final int TYPE_END_ITEM=3;
    private final int TYPE_EMPTY_ITEM=4;
    private boolean isShowDate;
    private boolean isShowMonth;
    private int[] positionToIndex;
    private int[] positionToType;

    private String mValue;
    private int mDataType;
    public static final int TYPE_ALL_DATA=0;
    public static final int TYPE_QUERY_BILL_NAME=1;
    public static final int TYPE_SEARCH_DATA_BY_WORD=2;

    private String currentUserId;


    public static class DateViewHolder extends RecyclerView.ViewHolder{

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

        private String date;
        public String getDate() {
            return date;
        }
        public void setDate(String date) {
            this.date = date;
        }
    }

    public static class MonthViewHolder extends RecyclerView.ViewHolder{

        private TextView mMonthText;

        public MonthViewHolder(View itemView) {
            super(itemView);
            mMonthText=itemView.findViewById(R.id.list_time_text);
        }

        private String date;
        public String getDate() {
            return date;
        }
        public void setDate(String date) {
            this.date = date;
        }
    }

    public static class ContentViewHolder extends RecyclerView.ViewHolder{

        private ImageView mContentImage;
        private TextView mContentIncomeText;
        private TextView mContentIncomeMoney;
        private TextView mContentOutcomeText;
        private TextView mContentOutcomeMoney;
        private TextView mIncomeBillText;
        private TextView mIncomeDesrciiption;
        private TextView mOutcomeBillText;
        private TextView mOutcomeDesrciiption;

        private RelativeLayout mLayout;
        private ImageView mDeleteImage;
        private ImageView mEditImage;

        private AnimationSet mAnimationSet;

        public ContentViewHolder(View itemView) {
            super(itemView);
            mContentImage=itemView.findViewById(R.id.list_item_image);
            mContentIncomeText=itemView.findViewById(R.id.list_item_income_text);
            mContentIncomeMoney=itemView.findViewById(R.id.list_item_income_money);
            mContentOutcomeText=itemView.findViewById(R.id.list_item_outcome_text);
            mContentOutcomeMoney=itemView.findViewById(R.id.list_item_outcome_money);
            mLayout=itemView.findViewById(R.id.list_content_item);
            mDeleteImage=itemView.findViewById(R.id.list_item_delete);
            mEditImage=itemView.findViewById(R.id.list_item_edit);
            mIncomeBillText=itemView.findViewById(R.id.list_item_bill_right);
            mIncomeDesrciiption=itemView.findViewById(R.id.list_item_description_right);
            mOutcomeBillText=itemView.findViewById(R.id.list_item_bill_left);
            mOutcomeDesrciiption=itemView.findViewById(R.id.list_item_description_left);

            mAnimationSet=new AnimationSet(true);

            Animation alphaAnim=new AlphaAnimation(0.0f,1.0f);
            Animation rotateAnim=new RotateAnimation(90,360,
                    Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);

            mAnimationSet.addAnimation(alphaAnim);
            mAnimationSet.addAnimation(rotateAnim);
            mAnimationSet.setDuration(500);
            mAnimationSet.setFillAfter(true);

        }

        private String date;
        public String getDate() {
            return date;
        }
        public void setDate(String date) {
            this.date = date;
        }
    }

    public static class EndViewHolder extends RecyclerView.ViewHolder{

        public EndViewHolder(View itemView) {
            super(itemView);
        }

    }

    public static class EmptyViewHolder extends RecyclerView.ViewHolder{

        private TextView mAddDataText;
        private TextView mLoginText;

        public EmptyViewHolder(View itemView) {
            super(itemView);
            mAddDataText=itemView.findViewById(R.id.list_empty_add_data);
            mLoginText=itemView.findViewById(R.id.list_empty_login);
        }
    }

    public AllDataListAdapter(Context context) {
        mContext=context;
        initAdapter();
        mDataType=TYPE_ALL_DATA;
    }

    public AllDataListAdapter(Context context,int dataType,String value) {
        mContext=context;
        mDataType=dataType;
        mValue=value;
        initAdapter();
    }

    private void initAdapter(){
        isShowDate=false;
        isShowMonth=true;
        index=0;
        dateIndex=0;
        monthIndex=0;
        bindIndex=0;
        User user=UserUtil.getInstance().getCurrentUser();
        if (user!=null){
            currentUserId=user.getObjectId();
        }else {
            currentUserId="local";
        }
        if (mDataType==TYPE_ALL_DATA){
            mSingleDataList= AllDataTableUtil.queryAllDataOrderByDate();
            mYearMonthList= AllDataTableUtil.getAllDifferentMonthList();
            mDateList= AllDataTableUtil.getAllDifferentDateList();
        }else if (mDataType==TYPE_QUERY_BILL_NAME){
            mSingleDataList= AllDataTableUtil.queryDataOrderByDateByBillName(mValue);
            mYearMonthList= AllDataTableUtil.getDifferentMonthListInBillName(mValue);
            mDateList= AllDataTableUtil.getDifferentDateListInBillName(mValue);
        }else {
            mSingleDataList= AllDataTableUtil.queryDataByWord(mValue);
            mYearMonthList= AllDataTableUtil.getDifferentMonthListInSearchWord(mValue);
            mDateList= AllDataTableUtil.getDifferentDateListInSearchWorld(mValue);
        }
        count=mSingleDataList.size()+mYearMonthList.size()+mDateList.size()+1;
        positionToIndex=new int[count];
        positionToType=new int[count];
        isClickList=new boolean[count];
        Arrays.fill(positionToIndex,-1);
        Arrays.fill(positionToType,-1);
        Arrays.fill(isClickList,false);
    }

    @Override
    public int getItemViewType(int position) {
        if (count==1){
            return TYPE_EMPTY_ITEM;
        }
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
        }else if (viewType==TYPE_END_ITEM){
            View view= LayoutInflater.from(context).inflate(R.layout.list_end_item,parent,false);
            return new EndViewHolder(view);
        }else {
            View view= LayoutInflater.from(context).inflate(R.layout.list_empty_item,parent,false);
            return new EmptyViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof DateViewHolder){
            if (positionToIndex[position]==-1){
                positionToIndex[position]=dateIndex++;
            }
            LogUtil.d("AllDataList","positionToIndex[ "+position+" ] = "+positionToIndex[position]);
            String date=mDateList.get(positionToIndex[position]);
            LogUtil.d("AllDataList","显示日期"+date);
            ((DateViewHolder) holder).setDate(date);
            ((DateViewHolder) holder).mDateText.setText(DateUtil.getDayOfDate(date));
            ((DateViewHolder) holder).mDateIncomeText.setText("收入");
            ((DateViewHolder) holder).mDateIncomeMoney.setText(String.valueOf(AllDataTableUtil.getTotalIncomeMoney(date)));
            ((DateViewHolder) holder).mDateOutcomeText.setText("支出");
            ((DateViewHolder) holder).mDateOutcomeMoney.setText(String.valueOf(AllDataTableUtil.getTotalOutcomeMoney(date)));
        }else if (holder instanceof MonthViewHolder){
            if (positionToIndex[position]==-1){
                positionToIndex[position]=monthIndex++;
            }
            LogUtil.d("AllDataList","positionToIndex[ "+position+" ] = "+positionToIndex[position]);
            String monthDate=mYearMonthList.get(positionToIndex[position]);
            LogUtil.d("AllDataList","显示月份"+monthDate);
            ((MonthViewHolder) holder).setDate(monthDate);
            ((MonthViewHolder) holder).mMonthText.setText(monthDate);
        }else if (holder instanceof ContentViewHolder){
            if (positionToIndex[position]==-1 && bindIndex<mSingleDataList.size()){
                positionToIndex[position]=bindIndex++;
            }
            LogUtil.d("AllDataList","positionToIndex[ "+position+" ] = "+positionToIndex[position]);
            final SingleDataBean bean=mSingleDataList.get(positionToIndex[position]);
            LogUtil.d("AllDataList","显示数据"+bean.getObjectId()+" , "+bean.toString());
            ((ContentViewHolder) holder).setDate(DateUtil.dateToString(bean.getDate()));
            ((ContentViewHolder) holder).mContentImage.setImageResource(DataTypeTableUtil.getImageId(bean.getTypeName()));
            if (bean.getType()==SingleDataBean.TYPE_INCOME_DATA){
                ((ContentViewHolder) holder).mContentIncomeText.setText("收入");
                ((ContentViewHolder) holder).mContentIncomeMoney.setText(bean.getMoney()+"");
                ((ContentViewHolder) holder).mContentOutcomeText.setText("");
                ((ContentViewHolder) holder).mContentOutcomeMoney.setText("");
                if (mDataType==TYPE_SEARCH_DATA_BY_WORD){
                    ((ContentViewHolder) holder).mIncomeBillText.setVisibility(View.VISIBLE);
                    ((ContentViewHolder) holder).mIncomeDesrciiption.setVisibility(View.VISIBLE);
                    ((ContentViewHolder) holder).mOutcomeBillText.setVisibility(View.INVISIBLE);
                    ((ContentViewHolder) holder).mOutcomeDesrciiption.setVisibility(View.INVISIBLE);
                    ((ContentViewHolder) holder).mOutcomeBillText.setText(bean.getBillName());
                    ((ContentViewHolder) holder).mOutcomeDesrciiption.setText(bean.getDescription());
                }else {
                    ((ContentViewHolder) holder).mIncomeBillText.setVisibility(View.GONE);
                    ((ContentViewHolder) holder).mIncomeDesrciiption.setVisibility(View.GONE);
                    ((ContentViewHolder) holder).mOutcomeBillText.setVisibility(View.GONE);
                    ((ContentViewHolder) holder).mOutcomeDesrciiption.setVisibility(View.GONE);
                }
            }else {
                ((ContentViewHolder) holder).mContentIncomeText.setText("");
                ((ContentViewHolder) holder).mContentIncomeMoney.setText("");
                ((ContentViewHolder) holder).mContentOutcomeText.setText("支出");
                ((ContentViewHolder) holder).mContentOutcomeMoney.setText(bean.getMoney()+"");
                if (mDataType==TYPE_SEARCH_DATA_BY_WORD){
                    ((ContentViewHolder) holder).mIncomeBillText.setVisibility(View.INVISIBLE);
                    ((ContentViewHolder) holder).mIncomeDesrciiption.setVisibility(View.INVISIBLE);
                    ((ContentViewHolder) holder).mOutcomeBillText.setVisibility(View.VISIBLE);
                    ((ContentViewHolder) holder).mOutcomeDesrciiption.setVisibility(View.VISIBLE);
                    ((ContentViewHolder) holder).mIncomeBillText.setText(bean.getBillName());
                    ((ContentViewHolder) holder).mIncomeDesrciiption.setText(bean.getDescription());
                }else {
                    ((ContentViewHolder) holder).mIncomeBillText.setVisibility(View.GONE);
                    ((ContentViewHolder) holder).mIncomeDesrciiption.setVisibility(View.GONE);
                    ((ContentViewHolder) holder).mOutcomeBillText.setVisibility(View.GONE);
                    ((ContentViewHolder) holder).mOutcomeDesrciiption.setVisibility(View.GONE);
                }
            }
            if (isClickList[position]){
                ((ContentViewHolder) holder).mDeleteImage.startAnimation(((ContentViewHolder) holder).mAnimationSet);
                ((ContentViewHolder) holder).mDeleteImage.setVisibility(View.VISIBLE);
                ((ContentViewHolder) holder).mEditImage.startAnimation(((ContentViewHolder) holder).mAnimationSet);
                ((ContentViewHolder) holder).mEditImage.setVisibility(View.VISIBLE);
            }else {
                ((ContentViewHolder) holder).mDeleteImage.setVisibility(View.GONE);
                ((ContentViewHolder) holder).mEditImage.setVisibility(View.GONE);
            }
            ((ContentViewHolder) holder).mLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isClickList[position]){
                        isClickList[position]=false;
                    }else {
                        Arrays.fill(isClickList,false);
                        isClickList[position]=true;
                    }
                    notifyDataSetChanged();
                }
            });
            ((ContentViewHolder) holder).mDeleteImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AllDataTableUtil.deleteDataById(bean.getId());
                    isClickList[position]=false;
                    initAdapter();
                    notifyDataSetChanged();
                    int type= BillTableUtil.getTypeByBillName(bean.getBillName());
                    if (type!=-1){
                        RxBus.getInstance().post(new NotifyBillListEvent(type));
                    }
                    RxBus.getInstance().post(new NotifyFormListEvent(0));
                    if (!TextUtils.equals(UserUtil.getInstance().getCurrentUserId(),"local")){
                        Log.d("SingleDataBean", "delete: "+bean.getObjectId());
                    }
                }
            });
            ((ContentViewHolder) holder).mEditImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isClickList[position]=false;
                    //ToastUtil.success("编辑"+bean.getId());
                    Bundle bundle=new Bundle();
                    bundle.putSerializable("bean",bean);
                    AddDataActivity.startAddDataActivity(mContext,bundle);
                }
            });
        }else if (holder instanceof EmptyViewHolder){
            ((EmptyViewHolder) holder).mAddDataText.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    AddDataActivity.startAddDataActivity(mContext);
                }
            });
            User currentUser=UserUtil.getInstance().getCurrentUser();
            if (currentUser!=null){
                ((EmptyViewHolder) holder).mLoginText.setText("同步数据");
                ((EmptyViewHolder) holder).mLoginText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        UserUtil.getInstance().uploadSingleDataList(mSingleDataList,UserUtil.getInstance().getCurrentUserId());
                        notifyDataInSearchAllData("");
                        RxBus.getInstance().post(new NotifyBillListEvent(1));
                        RxBus.getInstance().post(new NotifyBillListEvent(0));
                        RxBus.getInstance().post(new NotifyFormListEvent(1));
                        RxBus.getInstance().post(new NotifyFormListEvent(0));
                    }
                });
            }else {
                ((EmptyViewHolder) holder).mLoginText.setText("登陆以同步数据");
                ((EmptyViewHolder) holder).mLoginText.setOnClickListener(mEmptyListener);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mSingleDataList.size()+mYearMonthList.size()+mDateList.size()+1;
    }

    private View.OnClickListener mEmptyListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.list_empty_add_data:
                    AddDataActivity.startAddDataActivity(mContext);
                    break;
                case R.id.list_empty_login:
                    LoginActivity.startLoginActivity(mContext);
                    break;
                default:
                    break;
            }
        }
    };

    public void notifyDataInSearchAllData(String word){
        if (!TextUtils.isEmpty(word)){
            mDataType=TYPE_SEARCH_DATA_BY_WORD;
            mValue=word;
            initAdapter();
            notifyDataSetChanged();
        }else {
            mDataType=TYPE_ALL_DATA;
            initAdapter();
            notifyDataSetChanged();
        }
    }

    public List<SingleDataBean> getSingleDataList(){
        return mSingleDataList;
    }

}

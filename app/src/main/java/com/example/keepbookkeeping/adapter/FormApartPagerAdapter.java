package com.example.keepbookkeeping.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.keepbookkeeping.R;
import com.example.keepbookkeeping.bean.FormApartBean;
import com.example.keepbookkeeping.bean.SingleDataBean;
import com.example.keepbookkeeping.utils.AllDataTableUtil;
import com.example.keepbookkeeping.utils.ColorUtil;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.interfaces.datasets.IPieDataSet;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 邹永鹏
 * @date 2019/1/29
 * @description :
 */
public class FormApartPagerAdapter extends PagerAdapter {

    private Context mContext;
    private ViewPager mViewPager;
    private String mQueryDate;
    private List<FormApartBean> mIncomeList;
    private float mIncomeMoney;
    private List<FormApartBean> mOutcomeList;
    private float mOutcomeMoney;

    public static final int TYPE_INCOME=0;
    public static final int TYPE_OUTCOME=1;
    public static final int TAG_INCOME_TEXT=2;
    public static final int TAG_INCOME_IMAGE=3;
    public static final int TAG_OUTCOME_TEXT=4;
    public static final int TAG_OUTCOME_IMAGE=5;

    public FormApartPagerAdapter(Context context, ViewPager viewPager, String queryDate) {
        mContext = context;
        mViewPager=viewPager;
        mQueryDate = queryDate;
        initDataList();
    }

    private void initDataList(){
        if (!TextUtils.isEmpty(mQueryDate)){
            mIncomeList= AllDataTableUtil.getFormApartListByDateAndType("%"+mQueryDate+"%",1);
            mIncomeMoney=AllDataTableUtil.getSumMoneyByDate("%"+mQueryDate+"%",AllDataTableUtil.TYPE_INCOME);
            mOutcomeList= AllDataTableUtil.getFormApartListByDateAndType("%"+mQueryDate+"%",0);
            mOutcomeMoney=AllDataTableUtil.getSumMoneyByDate("%"+mQueryDate+"%",AllDataTableUtil.TYPE_OUTCOME);
        }
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view=View.inflate(mContext,R.layout.form_apart_piepic_item,null);

        PieChart pieChart=view.findViewById(R.id.form_apart_pie_chart);
        TextView typeTextView=view.findViewById(R.id.form_apart_name);
        TextView moneyTextView=view.findViewById(R.id.form_apart_money);

        if (position==0){
            typeTextView.setText("月总收入");
            moneyTextView.setTag(TAG_INCOME_TEXT);
            moneyTextView.setText(mIncomeMoney+"元");
            pieChart.setTag(TAG_INCOME_IMAGE);
            setPieChartDate(pieChart,mIncomeList);
        }else if (position==1){
            typeTextView.setText("月总支出");
            moneyTextView.setText(mOutcomeMoney+"元");
            moneyTextView.setTag(TAG_OUTCOME_TEXT);
            pieChart.setTag(TAG_OUTCOME_IMAGE);
            setPieChartDate(pieChart,mOutcomeList);
        }
        container.addView(view);
        return view;
    }

    private void setPieChartDate(PieChart pieChart,List<FormApartBean> list){
        List<PieEntry> entryList = new ArrayList<>();
        for (FormApartBean bean:list){
            entryList.add(new PieEntry(bean.getMoney(),bean.getTypeName()));
        }
        PieDataSet dataSet=new PieDataSet(entryList,"");

        dataSet.setColors(ColorUtil.getPieChartColorList());

        PieData pieData=new PieData(dataSet);
        pieData.setDrawValues(true);

        pieChart.setData(pieData);
        pieChart.setTouchEnabled(false);

        Description description=new Description();
        description.setText("");
        pieChart.setDescription(description);
        //百分比显示
        pieChart.setUsePercentValues(true);
        //孔半径
        pieChart.setHoleRadius(50);
        //不显示中间的透明小圆
        pieChart.setTransparentCircleRadius(0);

        pieChart.invalidate();
    }

    public void notifyData(String queryDate,int type){
        if (!TextUtils.isEmpty(queryDate)){
            mQueryDate=queryDate;
            initDataList();
            if (type==TYPE_INCOME && mViewPager!=null){
                TextView textView=(TextView) mViewPager.findViewWithTag(TAG_INCOME_TEXT);
                if (textView!=null){
                    textView.setText(mIncomeMoney+"元");
                }
                PieChart pieChart=(PieChart) mViewPager.findViewWithTag(TAG_INCOME_IMAGE);
                if (pieChart!=null){
                    setPieChartDate(pieChart,mIncomeList);
                }
            }else if (type==TYPE_OUTCOME && mViewPager!=null){
                TextView textView=(TextView) mViewPager.findViewWithTag(TAG_OUTCOME_TEXT);
                if (textView!=null){
                    textView.setText(mOutcomeMoney+"元");
                }
                PieChart pieChart=(PieChart) mViewPager.findViewWithTag(TAG_OUTCOME_IMAGE);
                if (pieChart!=null){
                    setPieChartDate(pieChart,mOutcomeList);
                }
            }
        }
    }

    /**
     * 解决数据不刷新的问题，不过滑动太快速生硬,所以不采用
     */
//    @Override
//    public int getItemPosition(Object object) {
//        return POSITION_NONE;
//    }

    public List<FormApartBean> getIncomeList() {
        return mIncomeList;
    }

    public float getIncomeMoney() {
        return mIncomeMoney;
    }

    public List<FormApartBean> getOutcomeList() {
        return mOutcomeList;
    }

    public float getOutcomeMoney() {
        return mOutcomeMoney;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (position==0){
            return "月总收入";
        }else {
            return "月总支出";
        }
    }


}

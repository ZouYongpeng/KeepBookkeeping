package com.example.keepbookkeeping.utils;

import com.example.keepbookkeeping.R;
import com.example.keepbookkeeping.bean.DataTypeBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 邹永鹏
 * @date 2019/1/31
 * @description :
 */
public class GetDataTypeUtil {

    public static List<DataTypeBean> getIncomeDataTypeBeanList(){
        List<DataTypeBean> list=new ArrayList<>();
        list.add(new DataTypeBean(R.drawable.ic_income_gongzi,"工资"));
        list.add(new DataTypeBean(R.drawable.ic_income_shenghuofei,"生活费"));
        list.add(new DataTypeBean(R.drawable.ic_income_hongbao,"红包"));
        list.add(new DataTypeBean(R.drawable.ic_income_linghuaqian,"零花钱"));
        list.add(new DataTypeBean(R.drawable.ic_income_jianzhi,"兼职"));
        list.add(new DataTypeBean(R.drawable.ic_income_touzi,"投资"));
        list.add(new DataTypeBean(R.drawable.ic_income_jiangjin,"奖金"));
        list.add(new DataTypeBean(R.drawable.ic_income_baoxiao,"报销"));
        list.add(new DataTypeBean(R.drawable.ic_income_xianjin,"现金"));
        list.add(new DataTypeBean(R.drawable.ic_income_tuikuan,"退款"));
        list.add(new DataTypeBean(R.drawable.ic_income_zhifubao,"支付宝"));
        list.add(new DataTypeBean(R.drawable.ic_income_default,"一般"));

        return list;
    }

    public static List<DataTypeBean> getOutcomeDataTypeBeanList(){
        List<DataTypeBean> list=new ArrayList<>();

        list.add(new DataTypeBean(R.drawable.ic_outcome_default,"一般"));
        list.add(new DataTypeBean(R.drawable.ic_outcome_eat,"餐饮"));
        list.add(new DataTypeBean(R.drawable.ic_outcome_car,"交通"));
        list.add(new DataTypeBean(R.drawable.ic_outcome_drink,"饮料"));
        list.add(new DataTypeBean(R.drawable.ic_outcome_fruit,"水果"));
        list.add(new DataTypeBean(R.drawable.ic_outcome_snacks,"零食"));
        list.add(new DataTypeBean(R.drawable.ic_outcome_vegetables,"买菜"));
        list.add(new DataTypeBean(R.drawable.ic_outcome_clothes,"衣服"));
        list.add(new DataTypeBean(R.drawable.ic_outcome_daily_necessities,"日用品"));
        list.add(new DataTypeBean(R.drawable.ic_outcome_tellphone_cost,"话费"));
        list.add(new DataTypeBean(R.drawable.ic_outcome_cosmetics,"护肤"));
        list.add(new DataTypeBean(R.drawable.ic_outcome_rent,"房租"));
        list.add(new DataTypeBean(R.drawable.ic_outcome_movie,"电影"));
        list.add(new DataTypeBean(R.drawable.ic_outcome_taobao,"淘宝"));
        list.add(new DataTypeBean(R.drawable.ic_outcome_red_packet,"红包"));
        list.add(new DataTypeBean(R.drawable.ic_outcome_drugs,"药品"));

        return list;
    }
}

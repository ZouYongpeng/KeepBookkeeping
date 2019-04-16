package com.example.keepbookkeeping.utils;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.keepbookkeeping.R;
import com.example.keepbookkeeping.bean.BillApartBean;
import com.example.keepbookkeeping.db.KBKAllDataBaseHelper;
import com.example.keepbookkeeping.events.NotifyBillListEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 邹永鹏
 * @date 2019/2/16
 * @description :
 */
public class BillTableUtil {

    public static final int TYPE_ASSETS=0;
    public static final int TYPE_DEBT=1;
    public static final int TYPE_ALL=2;


    public static final String INSERT_DATA_INTO_BILL
            = "insert into AllBill(type,imageId,name,description,initial_count,canChange)" +
            " values( ? , ? , ? , ? , ?  , ? )";

    public static final String QUERY_ALL_BILL="select * from AllBill";

    public static final String QUERY_ALL_ASSETS_BILL="select * from AllBill where type = 0";

    public static final String QUERY_ALL_DEBT_BILL="select * from AllBill where type = 1";

    public static final String QUERY_ALL_BILL_NAME="select name from AllBill";

    public static final String QUERY_TYPE_BY_BILL_NAME_="select type from AllBill where name = ?";

    public static final String QUERY_INITIAL_COUNT_BY_BILL_NAME_="select initial_count from AllBill where name = ?";

    public static final String QUERY_IMAGE_ID_BY_BILL_NAME="select imageId from AllBill where name = ?";

    public static final String QUERY_BILL_NAME_BY_IMAGE_ID="select name from AllBill where imageId = ?";

    public static final String QUERY_CANNOT_CHANGE_BILL_NAME_="select name from AllBill where canChange = 0";

    public static final String SELECT_NAME_BY_TYPE="select name from AllBill where type = ? ";

    public static final String QUERY_CHANGE_BY_NAME="select canChange from AllBill where name = ?";

    public static void initBill(SQLiteDatabase db){
        db.execSQL(INSERT_DATA_INTO_BILL,new String[]{"0",String.valueOf(R.drawable.ic_bill_xianjin),"现金","现金余额","0","0"});
        db.execSQL(INSERT_DATA_INTO_BILL,new String[]{"0",String.valueOf(R.drawable.ic_bill_yinhangka),"储值卡","储值卡余额","0","0"});
        db.execSQL(INSERT_DATA_INTO_BILL,new String[]{"0",String.valueOf(R.drawable.ic_income_wechat),"微信","微信钱包","0","0"});
        db.execSQL(INSERT_DATA_INTO_BILL,new String[]{"0",String.valueOf(R.drawable.ic_income_zhifubao),"支付宝","支付宝余额","0","0"});
//        db.execSQL(INSERT_DATA_INTO_BILL,new String[]{"0",String.valueOf(R.drawable.ic_bill_shouzhai),"应收账","别人欠我的钱"});

        db.execSQL(INSERT_DATA_INTO_BILL,new String[]{"1",String.valueOf(R.drawable.ic_bill_xinyongka),"信用卡","未设置信用卡额度","0","0"});
        db.execSQL(INSERT_DATA_INTO_BILL,new String[]{"1",String.valueOf(R.drawable.ic_bill_huabei),"蚂蚁花呗","未设置花呗额度","0","0"});
        db.execSQL(INSERT_DATA_INTO_BILL,new String[]{"1",String.valueOf(R.drawable.ic_bill_baitiao),"京东白条","未设置白条额度","0","0"});
//        db.execSQL(INSERT_DATA_INTO_BILL,new String[]{"1",String.valueOf(R.drawable.ic_bill_qian),"应还账","我欠别人的钱"});
    }

    /**
     * 获取db中所有资产类型的账户数据
     * @return
     */
    public static List<BillApartBean> getAllBillAssetsList(){
        return getAllBillAssetsList(TYPE_ALL);
    }

    public static List<BillApartBean> getAllBillAssetsList(int queryType){
        List<BillApartBean> list=new ArrayList<>();
        String sql;
        if (queryType==TYPE_ALL){
            sql=QUERY_ALL_BILL;
        }else if (queryType==TYPE_ASSETS){
            sql=QUERY_ALL_ASSETS_BILL;
        }else {
            sql=QUERY_ALL_DEBT_BILL;
        }
        SQLiteDatabase db= KBKAllDataBaseHelper.getInstance().getWritableDatabase();
        Cursor cursor=db.rawQuery(sql,null);
        if (cursor.moveToFirst()){
            do {
                int id=cursor.getInt(cursor.getColumnIndex("id"));
                int type=cursor.getInt(cursor.getColumnIndex("type"));
                int imageId=cursor.getInt(cursor.getColumnIndex("imageId"));
                String name=cursor.getString(cursor.getColumnIndex("name"));
                String description=cursor.getString(cursor.getColumnIndex("description"));
                float initialCount=cursor.getFloat(cursor.getColumnIndex("initial_count"));
                int canChange=cursor.getInt(cursor.getColumnIndex("canChange"));
                list.add(new BillApartBean(id,type,imageId,name,description,initialCount,canChange));

            }while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public static String[] getBillName(int type) {
        if (type==TYPE_ASSETS){
            return getBillName(SELECT_NAME_BY_TYPE,new String[]{"0"});
        }else if (type==TYPE_DEBT){
            return getBillName(SELECT_NAME_BY_TYPE,new String[]{"1"});
        }else {
            return getBillName(QUERY_ALL_BILL_NAME,null);
        }
    }

    public static String[] getBillName(String sql,String[] selectionArgs) {
        List<String> list=new ArrayList<>();
        SQLiteDatabase db= KBKAllDataBaseHelper.getInstance().getWritableDatabase();
        Cursor cursor=db.rawQuery(sql,selectionArgs);
        if (cursor.moveToFirst()){
            do {
                list.add(cursor.getString(cursor.getColumnIndex("name")));

            }while (cursor.moveToNext());
        }
        cursor.close();
        String[] strings = new String[list.size()];
        list.toArray(strings);
        return strings;
    }

    /**
     * 根据消费的账户名（billName）获取BillTable的type
     * @param billName
     * @return
     */
    public static int getTypeByBillName(String billName){
        int type=-1;
        SQLiteDatabase db= KBKAllDataBaseHelper.getInstance().getWritableDatabase();
        Cursor cursor=db.rawQuery(QUERY_TYPE_BY_BILL_NAME_,new String[]{billName});
        if (cursor.moveToFirst()){
            type=cursor.getInt(cursor.getColumnIndex("type"));
        }
        cursor.close();
        return type;
    }

    public static float getInitialCountByBillName(String billName){
        float count=0;
        SQLiteDatabase db= KBKAllDataBaseHelper.getInstance().getWritableDatabase();
        Cursor cursor=db.rawQuery(QUERY_INITIAL_COUNT_BY_BILL_NAME_,new String[]{billName});
        if (cursor.moveToFirst()){
            count=cursor.getInt(cursor.getColumnIndex("initial_count"));
        }
        cursor.close();
        return count;
    }

    public static String[] getCannotChangeBillName(){
        List<String> list=new ArrayList<>();
        SQLiteDatabase db= KBKAllDataBaseHelper.getInstance().getWritableDatabase();
        Cursor cursor=db.rawQuery(QUERY_CANNOT_CHANGE_BILL_NAME_,null);
        if (cursor.moveToFirst()){
            do {
                list.add(cursor.getString(cursor.getColumnIndex("name")));

            }while (cursor.moveToNext());
        }
        cursor.close();
        String[] strings = new String[list.size()];
        list.toArray(strings);
        return strings;
    }

    public static int getImageIdByBillName(String billName){
        int imageId=0;
        SQLiteDatabase db= KBKAllDataBaseHelper.getInstance().getWritableDatabase();
        Cursor cursor=db.rawQuery(QUERY_IMAGE_ID_BY_BILL_NAME,new String[]{billName});
        if (cursor.moveToFirst()){
            imageId=cursor.getInt(cursor.getColumnIndex("imageId"));
        }
        cursor.close();
        return imageId;
    }

    public static String getBillNameByImageId(int imageId){
        String billName="";
        SQLiteDatabase db= KBKAllDataBaseHelper.getInstance().getWritableDatabase();
        Cursor cursor=db.rawQuery(QUERY_BILL_NAME_BY_IMAGE_ID,new String[]{String.valueOf(imageId)});
        if (cursor.moveToFirst()){
            billName=cursor.getString(cursor.getColumnIndex("name"));
        }
        cursor.close();
        return billName;
    }

    public static boolean billNameIsExist(String billName){
        boolean isExist=false;
        SQLiteDatabase db= KBKAllDataBaseHelper.getInstance().getWritableDatabase();
        Cursor cursor=db.rawQuery(QUERY_IMAGE_ID_BY_BILL_NAME,new String[]{billName});
        if (cursor.moveToFirst()){
            isExist=true;
        }
        cursor.close();
        return isExist;
    }

    public static void insertBillData(BillApartBean bean){
        SQLiteDatabase db= KBKAllDataBaseHelper.getInstance().getWritableDatabase();
        db.execSQL(INSERT_DATA_INTO_BILL, new String[]{
                String.valueOf(bean.getType()),
                String.valueOf(bean.getImageId()),
                bean.getName(),
                bean.getDescription(),
                String.valueOf(bean.getInitialCount()),
                "1"});
    }

    public static final String QUERY_SUM_INITIAL_COUNT_BY_TYPE="select sum(initial_count) as sum_count from AllBill where type = ? ";
    public static float getSumInitialMoney(int type){
        float sumInitialMoney=0;
        Cursor cursor;
        if (type==TYPE_ASSETS){
            cursor=KBKAllDataBaseHelper.getInstance().getWritableDatabase().rawQuery(QUERY_SUM_INITIAL_COUNT_BY_TYPE,new String[]{"0"});

        }else {
            cursor=KBKAllDataBaseHelper.getInstance().getWritableDatabase().rawQuery(QUERY_SUM_INITIAL_COUNT_BY_TYPE,new String[]{"1"});
        }
        if (cursor.moveToFirst()){
            do {
                sumInitialMoney+=cursor.getFloat(cursor.getColumnIndex("sum_count"));

//                LogUtil.d("db",cursor.getInt(cursor.getColumnIndex("type"))+" - "+
//                        cursor.getFloat(cursor.getColumnIndex("money"))+" - "+
//                        cursor.getString(cursor.getColumnIndex("bill_name")));
            }while (cursor.moveToNext());
        }
        return sumInitialMoney;
    }

    public static boolean getCanChangeByBillName(String billName){
        boolean res=false;
        Cursor cursor=KBKAllDataBaseHelper.getInstance().getWritableDatabase().rawQuery(QUERY_CHANGE_BY_NAME,new String[]{billName});
        if (cursor.moveToFirst()){
            res=(cursor.getInt(cursor.getColumnIndex("canChange")) == 0 ? false : true);
        }
        return res;
    }
    public static final String UPDATE_CANNOT_CHANGE_DATA_BY_ID="update AllBill set description = ? , initial_count = ? where id = ? ";

    public static void updateCannotChangeDataById(int id,String initialCount,String description){
        LogUtil.d("BillDetails","id = "+id+" , initialCount = "+initialCount+" , description = "+description);
        String[] strings=new String[]{description,initialCount,String.valueOf(id)};
        KBKAllDataBaseHelper.getInstance().getWritableDatabase().execSQL(UPDATE_CANNOT_CHANGE_DATA_BY_ID,strings);
    }

    public static final String UPDATE_DATA_BY_ID="UPDATE AllBill " +
            "SET type = ? " +
            ", imageId = ? " +
            ", name = ? " +
            ", description = ? " +
            ", initial_count = ? " +
            "where id = ?";

    public static void updateCanChangeDataById(BillApartBean bean,String oldBillName){
        LogUtil.d("BillDetails",bean.toString());
        String[] strings=new String[]{
                String.valueOf(bean.getType()),
                String.valueOf(bean.getImageId()),
                bean.getName(),
                bean.getDescription(),
                String.valueOf(bean.getInitialCount()),
                String.valueOf(bean.getId())
        };
        KBKAllDataBaseHelper.getInstance().getWritableDatabase().execSQL(UPDATE_DATA_BY_ID,strings);
        /**
         * 还要改变AllData表中的数据
         */
        AllDataTableUtil.updateBillName(bean.getName(),oldBillName);
    }

}
